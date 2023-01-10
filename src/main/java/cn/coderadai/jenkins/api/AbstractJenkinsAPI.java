package cn.coderadai.jenkins.api;

import cn.coderadai.jenkins.api.parameter.buildParameter.BuildParameter;
import cn.coderadai.jenkins.api.util.JenkinsAPIUtils;
import cn.coderadai.jenkins.api.util.JobPropertyUtils;
import cn.coderadai.jenkins.api.util.URLStringBuilder;
import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.features.JobsApi;
import com.cdancy.jenkins.rest.features.QueueApi;
import com.cdancy.jenkins.rest.features.SystemApi;
import com.offbytwo.jenkins.JenkinsServer;
import okhttp3.Request;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author: coderAdai
 * @date 2022/06/09 18:34
 * @description: TODO
 */
public abstract class AbstractJenkinsAPI implements AutoCloseable {

    protected static final String SET_NEXT_BUILD_NUMBER_TOOL_JOB_NAME = "JENKINS_API_SET_NEXT_BUILD_NUMBER";
    protected static final String SET_NEXT_BUILD_NUMBER_TOOL_PARAMETER_JOB_FULL_NAME = "JOB_FULL_NAME";
    protected static final String SET_NEXT_BUILD_NUMBER_TOOL_PARAMETER_NUMBER = "NEXT_BUILD_NUMBER";
    private final String headerAuthorization;
    protected String endPoint;
    protected String userName;
    protected String password;
    protected String userNameAndToken;
    private String builtInAgentLabel = "built-in";
    private JenkinsClient client;
    private JenkinsServer server;

    protected AbstractJenkinsAPI(String endPoint, String userName, String password) {
        this(endPoint, userName, password, null);
    }

    protected AbstractJenkinsAPI(String endPoint, String userNameAndToken) {
        this(endPoint, null, null, userNameAndToken);
    }

    protected AbstractJenkinsAPI(String endPoint) {
        this(endPoint, null, null, null);
    }

    private AbstractJenkinsAPI(String endPoint, String userName, String password, String userNameAndToken) {
        if (endPoint == null || endPoint.isEmpty()) {
            throw new IllegalArgumentException("The endpoint can not be null or empty!");
        }

        this.endPoint = endPoint;
        this.userName = userName;
        this.password = password;
        this.userNameAndToken = userNameAndToken;

        if (StringUtils.hasText(userName) && StringUtils.hasText(password)) {
            this.client = JenkinsClient.builder().endPoint(endPoint).credentials(String.format("%s:%s", userName, password)).build();
            this.server = new JenkinsServer(URI.create(endPoint), userName, password);
        } else if (StringUtils.hasText(userNameAndToken)) {
            this.client = JenkinsClient.builder().endPoint(endPoint).apiToken(userNameAndToken).build();
            this.server = new JenkinsServer(URI.create(endPoint), userNameAndToken.split(":")[0], userNameAndToken.split(":")[1]);
        } else {
            this.client = JenkinsClient.builder().endPoint(endPoint).build();
            this.server = new JenkinsServer(URI.create(endPoint));
        }

        this.headerAuthorization = this.computeAuthorization(userName, password, userNameAndToken);
    }

    public JenkinsServer getServer() {
        return server;
    }

    private String computeAuthorization(String userName, String password, String userNameAndToken) {
        if (userName != null && !userName.isEmpty() && password != null && !password.isEmpty()) {
            return Base64.encodeBase64String((userName + ":" + password).getBytes(StandardCharsets.UTF_8));
        } else if (userNameAndToken != null && !userNameAndToken.isEmpty()) {
            return Base64.encodeBase64String(userNameAndToken.getBytes(StandardCharsets.UTF_8));
        }
        return null;
    }

    protected Request makeGetRequest(String folderName, String jobName, String urlSuffix) {
        String url = new URLStringBuilder(this.endPoint)
                .appendIfNotEmpty(folderName)
                .appendIfNotEmpty("job")
                .appendIfNotEmpty(jobName)
                .appendIfNotEmpty(urlSuffix)
                .build();

        Request.Builder reqBuilder = new Request.Builder().url(url);
        if (this.headerAuthorization != null) {
            reqBuilder.header("Authorization", this.headerAuthorization);
        }
        return reqBuilder.build();
    }

    protected void setBuiltInAgentLabel(String label) {
        this.builtInAgentLabel = label;
    }

    protected SystemApi getSystemApi() {
        return this.client.api().systemApi();
    }

    protected JobsApi getJobsApi() {
        return this.client.api().jobsApi();
    }

    protected QueueApi getQueueApi() {
        return this.client.api().queueApi();
    }

    protected Boolean internalIsJobExists(String folderName, String jobName) {
        String description = getJobsApi().description(folderName, jobName);
        return !Objects.isNull(description);
    }

    protected Integer internalBuildWithParameter(String folderName, String jobName, BuildParameter buildParameter) {
        return getJobsApi().buildWithParameters(folderName, jobName, buildParameter.getProperties()).value();
    }

    protected void internalSetNextBuildNumberAsync(String folderName, String jobName, Integer buildNumber) {
        this.createInternalJobIfNotExists();

        String jobFullName = JenkinsAPIUtils.isNullOrEmpty(folderName) ? jobName : String.format("%s/%s", folderName, jobName);

        BuildParameter buildParameter = BuildParameter.builder().parameter(SET_NEXT_BUILD_NUMBER_TOOL_PARAMETER_JOB_FULL_NAME, jobFullName).parameter(SET_NEXT_BUILD_NUMBER_TOOL_PARAMETER_NUMBER, buildNumber.toString()).build();
        this.internalBuildWithParameter("", SET_NEXT_BUILD_NUMBER_TOOL_JOB_NAME, buildParameter);
    }

    private void createInternalJobIfNotExists() {
        if (!internalIsJobExists("", SET_NEXT_BUILD_NUMBER_TOOL_JOB_NAME)) {
            String templateXml = JobPropertyUtils.getInternalSetNextBuildNumberJobXml();
            String xml = templateXml.replace("${AgentLabel}", this.builtInAgentLabel);
            JenkinsAPIUtils.tryExecute(() -> getJobsApi().create("", SET_NEXT_BUILD_NUMBER_TOOL_JOB_NAME, xml));
        }
    }

    @Override
    public void close() throws Exception {
        if (!Objects.isNull(this.client)) {
            this.client.close();
            this.client = null;
        }
        if (!Objects.isNull(this.server)) {
            this.server.close();
            this.server = null;
        }
    }
}
