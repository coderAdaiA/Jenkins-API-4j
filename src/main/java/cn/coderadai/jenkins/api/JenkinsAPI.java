package cn.coderadai.jenkins.api;

import cn.coderadai.jenkins.api.exception.JenkinsClientException;
import cn.coderadai.jenkins.api.model.*;
import cn.coderadai.jenkins.api.model.configmodel.JenkinsJobConfig;
import cn.coderadai.jenkins.api.parameter.buildParameter.BuildParameter;
import cn.coderadai.jenkins.api.util.JenkinsAPIUtils;
import cn.coderadai.jenkins.api.util.JobPropertyUtils;
import cn.coderadai.jenkins.api.util.XmlUtils;
import com.cdancy.jenkins.rest.domain.job.JobList;
import com.cdancy.jenkins.rest.domain.job.ProgressiveText;
import com.cdancy.jenkins.rest.domain.queue.QueueItem;
import com.cdancy.jenkins.rest.domain.system.SystemInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.LockSupport;

public final class JenkinsAPI extends AbstractJenkinsAPI implements AutoCloseable {

    /**
     * init jenkins api client with username and password
     *
     * @param endPoint jenkins server endpoint
     * @param username username
     * @param password password
     */
    public JenkinsAPI(String endPoint, String username, String password) {

        super(endPoint, username, password);
    }

    /**
     * init jenkins api client with username and token
     *
     * @param endPoint         jenkins server endpoint
     * @param userNameAndToken jenkins username and api token, ex: "${useName}:${token}"
     */
    public JenkinsAPI(String endPoint, String userNameAndToken) {
        super(endPoint, userNameAndToken);
    }

    /**
     * init jenkins client without auth
     *
     * @param endPoint jenkins server endpoint
     */
    public JenkinsAPI(String endPoint) {
        super(endPoint);
    }

    /**
     * create pipeline job
     *
     * @param job JenkinsPipelineModel
     * @return result
     */
    public Boolean createPipelineJob(JenkinsPipelineJob job) {
        return getJobsApi().create(job.getFolderName(), job.getName(), job.toXmlString()).value();
    }

    /**
     * set next build number(async)
     *
     * @param jobName     job name
     * @param buildNumber build number
     */
    public void setNextBuildNumberAsync(String jobName, Integer buildNumber) {
        setNextBuildNumberAsync("", jobName, buildNumber);
    }

    /**
     * set next build number(async)
     *
     * @param folderName  folder name
     * @param jobName     job name
     * @param buildNumber build number
     */
    public void setNextBuildNumberAsync(String folderName, String jobName, Integer buildNumber) {
        if (buildNumber <= 0) {
            throw new JenkinsClientException("`buildNumber` must be positive integer!");
        }

        if (!isJobExists(folderName, jobName)) {
            throw new JenkinsClientException("The job not found!");
        }

        JenkinsAPIUtils.tryExecute(() -> this.internalSetNextBuildNumberAsync(folderName, jobName, buildNumber));
    }

    /**
     * get last build number
     *
     * @param jobName job name
     * @return builder number
     */
    public Integer getLastBuildNumber(String jobName) {
        return getLastBuildNumber("", jobName);
    }

    /**
     * get last build number
     *
     * @param folderName folder name
     * @param jobName    job name
     * @return builder number
     */
    public Integer getLastBuildNumber(String folderName, String jobName) {
        return JenkinsAPIUtils.tryExecute(() -> getJobsApi().lastBuildNumber(folderName, jobName));
    }

    /**
     * stop job
     *
     * @param jobName     job name
     * @param buildNumber build number
     * @return result
     */
    public Boolean stopBuildingJob(String jobName, int buildNumber) {
        return stopBuildingJob("", jobName, buildNumber);
    }

    /**
     * stop job
     *
     * @param folderName  folder name
     * @param jobName     job name
     * @param buildNumber build number
     * @return result
     */
    public Boolean stopBuildingJob(String folderName, String jobName, int buildNumber) {
        return JenkinsAPIUtils.tryExecute(() -> getJobsApi().stop(folderName, jobName, buildNumber).value());
    }

    /**
     * get job url
     *
     * @param jobName job name
     * @return job url
     */
    public String getJobUrl(String jobName) {
        return getJobUrl("", jobName);
    }

    /**
     * get job url
     *
     * @param folderName folder name
     * @param jobName    job name
     * @return job url
     */
    public String getJobUrl(String folderName, String jobName) {
        return JenkinsAPIUtils.tryExecute(() -> getJobsApi().jobInfo(folderName, jobName).url());
    }

    /**
     * folder exists
     *
     * @param folderName folder name
     * @return result
     */
    public Boolean isFolderExists(String folderName) {
        return this.isFolderExists("", folderName);
    }

    /**
     * folder exists
     *
     * @param parentFolderName parent folder name
     * @param folderName       folder name
     * @return result
     */
    public Boolean isFolderExists(String parentFolderName, String folderName) {
        String config = this.getJobsApi().config(parentFolderName, folderName);
        return config != null && config.contains("com.cloudbees.hudson.plugins.folder.Folder");
    }

    /**
     * create jenkins folder, not view!
     *
     * @param folderName folder name
     * @return result
     */
    public Boolean createFolder(String folderName) {
        return this.createFolder("", folderName);
    }

    /**
     * create jenkins folder, not view!
     *
     * @param parentFolderName parent folder name
     * @param folderName       folder name
     * @return result
     */
    public Boolean createFolder(String parentFolderName, String folderName) {
        JenkinsFolder jenkinsFolder = JenkinsFolder.builder()
                .parentFolderName(parentFolderName)
                .name(folderName)
                .build();
        return this.createFolder(jenkinsFolder);
    }

    /**
     * create jenkins folder, not view!
     *
     * @param folder jenkins folder model
     * @return result
     */
    public Boolean createFolder(JenkinsFolder folder) {
        return getJobsApi().create(folder.getParentFolderName(), folder.getName(), folder.toXmlString()).value();
    }

    /**
     * delete jenkins folder, not view!
     *
     * @param folderName folder name
     * @return result
     */
    public Boolean deleteFolder(String folderName) {
        return this.deleteFolder("", folderName);
    }

    /**
     * delete jenkins folder, not view!
     *
     * @param parentFolderName parent folder name
     * @param folderName       folder name
     * @return result
     */
    public Boolean deleteFolder(String parentFolderName, String folderName) {
        return getJobsApi().delete(parentFolderName, folderName).value();
    }

    /**
     * rename folder
     *
     * @param folderName    folder name
     * @param newFolderName new folder name
     */
    public Boolean renameFolder(String folderName, String newFolderName) {
        return this.renameFolder("", folderName, newFolderName);
    }

    /**
     * rename folder
     *
     * @param parentFolderName parent folder name
     * @param folderName       folder name
     * @param newFolderName    new folder name
     */
    public Boolean renameFolder(String parentFolderName, String folderName, String newFolderName) {
        return getJobsApi().rename(parentFolderName, folderName, newFolderName);
    }

    /**
     * job exists
     *
     * @param jobName job name
     * @return result
     */
    public Boolean isJobExists(String jobName) {
        return isJobExists("", jobName);
    }

    /**
     * job exists
     *
     * @param folderName folder name
     * @param jobName    job name
     * @return result
     */
    public Boolean isJobExists(String folderName, String jobName) {
        return JenkinsAPIUtils.tryExecute(() -> this.internalIsJobExists(folderName, jobName));
    }

    /**
     * delete job
     *
     * @param jobName job name
     * @return result
     */
    public Boolean deleteJob(String jobName) {
        return deleteJob("", jobName);
    }

    /**
     * delete job
     *
     * @param folderName folder name
     * @param jobName    job name
     * @return result
     */
    public Boolean deleteJob(String folderName, String jobName) {
        return getJobsApi().delete(folderName, jobName).value();
    }

    /**
     * rename job name
     *
     * @param jobName    original job name
     * @param newJobName new job name
     */
    public Boolean renameJob(String jobName, String newJobName) {
        return renameJob("", jobName, newJobName);
    }

    /**
     * rename job name
     *
     * @param folderName folder name
     * @param jobName    job name
     * @param newJobName new job name
     */
    public Boolean renameJob(String folderName, String jobName, String newJobName) {
        return JenkinsAPIUtils.tryExecute(() -> getJobsApi().rename(folderName, jobName, newJobName));
    }

    /**
     * build with parameter
     *
     * @param jobName        job name
     * @param buildParameter BuildParameter
     * @return jenkins build queue id
     */
    public Integer buildWithParameter(String jobName, BuildParameter buildParameter) {
        return buildWithParameter("", jobName, buildParameter);
    }

    /**
     * build with parameter
     *
     * @param folderName     folder name
     * @param jobName        job name
     * @param buildParameter BuildParameter
     * @return buildNumber
     */
    public Integer buildWithParameter(String folderName, String jobName, BuildParameter buildParameter) {
        Integer queueId = JenkinsAPIUtils.tryExecute(() -> this.internalBuildWithParameter(folderName, jobName, buildParameter));
        if (queueId != null) {
            return waitForBuildNumber(queueId);
        }
        return null;
    }

    /**
     * get job config(from config.xml file)
     *
     * @param jobName job name
     * @return jenkins job config object
     */
    public JenkinsJobConfig getJobConfig(String jobName) {
        return this.getJobConfig("", jobName);
    }

    /**
     * get job config(from config.xml file)
     *
     * @param folderName folder name
     * @param jobName    job name
     * @return jenkins job config object
     */
    public JenkinsJobConfig getJobConfig(String folderName, String jobName) {
        String configXml = getJobsApi().config(folderName, jobName);
        return XmlUtils.convertXml2Object(configXml, JenkinsJobConfig.class);
    }

    /**
     * get build log
     *
     * @param jobName     job name
     * @param buildNumber build number
     * @return BuildLog
     */
    public BuildLog getBuildLog(String jobName, Integer buildNumber) {
        return getBuildLog("", jobName, buildNumber);
    }

    /**
     * get build log
     *
     * @param folderName  folder name
     * @param jobName     job name
     * @param buildNumber build number
     * @return BuildLog
     */
    public BuildLog getBuildLog(String folderName, String jobName, Integer buildNumber) {
        String buildLogString = getBuildLogString(folderName, jobName, buildNumber);
        if (buildLogString.isEmpty()) {
            return new BuildLog("");
        }
        return new BuildLog(buildLogString);
    }

    /**
     * get build log string
     *
     * @param jobName     job name
     * @param buildNumber build number
     * @return log string
     */
    public String getBuildLogString(String jobName, Integer buildNumber) {
        return getBuildLogString("", jobName, buildNumber);
    }

    /**
     * get build log string
     *
     * @param folderName  folder name
     * @param jobName     job name
     * @param buildNumber build number
     * @return log string
     */
    public String getBuildLogString(String folderName, String jobName, Integer buildNumber) {
        ProgressiveText progressiveText = getJobsApi().progressiveText(folderName, jobName, buildNumber, 0);
        if (progressiveText == null) {
            return "";
        }
        return progressiveText.text();
    }

    /**
     * get job list
     *
     * @return job list
     */
    public List<SimpleJobInfo> getJobList() {
        return getJobList("");
    }

    /**
     * get job list
     *
     * @param folderName folder name
     * @return job list
     */
    public List<SimpleJobInfo> getJobList(String folderName) {
        try {
            JobList jobList = getJobsApi().jobList(folderName);
            if (jobList == null || jobList.jobs().isEmpty()) {
                return new ArrayList<>();
            }
            return SimpleJobInfo.copyFrom(jobList.jobs());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * get job info(include simple build info)
     *
     * @param jobName job name
     * @return job info
     */
    public JobInfo getJobInfo(String jobName) {
        return getJobInfo("", jobName);
    }

    /**
     * get job info(include simple build info)
     *
     * @param folderName folder name
     * @param jobName    job name
     * @return job info
     */
    public JobInfo getJobInfo(String folderName, String jobName) {
        com.cdancy.jenkins.rest.domain.job.JobInfo jobInfo = getJobsApi().jobInfo(folderName, jobName);
        return JobInfo.copyFrom(jobInfo);
    }

    /**
     * get full build info
     *
     * @param jobName     job name
     * @param buildNumber build number
     * @return full build info
     */
    public BuildInfo getBuildInfo(String jobName, Integer buildNumber) {
        return getBuildInfo("", jobName, buildNumber);
    }

    /**
     * get full build info
     *
     * @param folderName  folder name
     * @param jobName     job name
     * @param buildNumber build number
     * @return full build info
     */
    public BuildInfo getBuildInfo(String folderName, String jobName, Integer buildNumber) {
        return BuildInfo.copyFrom(getJobsApi().buildInfo(folderName, jobName, buildNumber));
    }

    /**
     * get last build time
     *
     * @param jobName job name
     * @return last build time
     */
    public Date getLastBuildTime(String jobName) {
        return getLastBuildTime("", jobName);
    }

    /**
     * get last build time
     *
     * @param jobName job name
     * @return Date
     */
    public Date getLastBuildTime(String folderName, String jobName) {
        String buildTimestamp = getJobsApi().lastBuildTimestamp(folderName, jobName);
        if (Objects.isNull(buildTimestamp) || buildTimestamp.isEmpty()) {
            return null;
        }

        try {
            return SimpleDateFormat.getInstance().parse(buildTimestamp);
        } catch (ParseException e) {
            throw new JenkinsClientException("Parse build timestamp error", e);
        }
    }

    /**
     * @param jobName job name
     * @return result
     */
    public Boolean enableJob(String jobName) {
        return this.enableJob("", jobName);
    }

    /**
     * @param folderName folder name
     * @param jobName    job name
     * @return result
     */
    public Boolean enableJob(String folderName, String jobName) {
        return this.getJobsApi().enable(folderName, jobName);
    }

    /**
     * @param jobName job name
     * @return result
     */
    public Boolean disableJob(String jobName) {
        return this.disableJob("", jobName);
    }

    /**
     * @param folderName folder name
     * @param jobName    job name
     * @return result
     */
    public Boolean disableJob(String folderName, String jobName) {
        return this.getJobsApi().disable(folderName, jobName);
    }

    /**
     * @param jobName job name
     * @return assigned node
     */
    public String getAssignedNode(String jobName) {
        return this.getAssignedNode("", jobName);
    }

    /**
     * @param folderName folder name
     * @param jobName    job name
     * @return assigned node
     */
    public String getAssignedNode(String folderName, String jobName) {
        JenkinsJobConfig config = this.getJobConfig(folderName, jobName);
        if (config != null) {
            return config.getAssignedNode();
        }
        return null;
    }

    /**
     * get injected env vars
     *
     * @param jobName     job name
     * @param buildNumber build number
     * @return result map
     */
    public Map<String, String> getInjectedEnvVars(String jobName, int buildNumber) {
        return this.getInjectedEnvVars(null, jobName, buildNumber);
    }

    /**
     * get injected env vars
     *
     * @param folderName  folder name
     * @param jobName     job name
     * @param buildNumber build number
     * @return result map
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getInjectedEnvVars(String folderName, String jobName, int buildNumber) {
        try {
            OkHttpClient client = new OkHttpClient();

            String urlSuffix = String.format("%d/injectedEnvVars/api/json", buildNumber);
            Request request = this.makeGetRequest(folderName, jobName, urlSuffix);

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    try (ResponseBody body = response.body()) {
                        if (body != null) {
                            return new ObjectMapper().readValue(body.string(), Map.class);
                        }
                    }
                }
            }

            return null;
        } catch (Exception e) {
            throw new JenkinsClientException("Get build injected env vars failed.", e);
        }
    }

    /**
     * copy job
     *
     * @param jobName               job name
     * @param originalFolderName    original folder name
     * @param destinationFolderName destination folder name
     */
    public void copyJob(String jobName, String originalFolderName, String destinationFolderName) {
        String jobConfig = this.getJobsApi().config(originalFolderName, jobName);
        this.getJobsApi().create(destinationFolderName, jobName, jobConfig);
    }

    /**
     * move job
     *
     * @param jobName               job name
     * @param originalFolderName    original folder name
     * @param destinationFolderName destination folder name
     */
    public String moveJob(String jobName, String originalFolderName, String destinationFolderName) {

        String moveScript = String.format("jenkins = Jenkins.instance\n" +
                "jenkins.items.grep { it instanceof com.cloudbees.hudson.plugins.folder.Folder }.each { folder ->\n" +
                "    if(folder.getName().equals(\"%s\")) {\n" +
                "        folder.getItems().grep { it.name == \"%s\" }.each { job ->\n" +
                "            Items.move(job, ${targetFolder})\n" +
                "        }\n" +
                "    }\n" +
                "}", originalFolderName, jobName);

        if (destinationFolderName == null || destinationFolderName.equals("")) {
            moveScript = moveScript.replace("${targetFolder}", "jenkins");
        } else {
            moveScript = moveScript.replace("${targetFolder}", String.format("jenkins.getItemByFullName(\"%s\")", destinationFolderName));
        }
        try {
            return this.runScript(moveScript);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * update job config
     *
     * @param jobName     job name
     * @param gitUrl      git url
     * @param gitCertId   git cert id
     * @param gitBranch   git branch
     * @param scriptPath  script path
     * @param lightweight lightweight
     * @return operation result
     */
    public boolean updatePipelineJob(String jobName, String gitUrl, String gitCertId, String gitBranch,
                                     String scriptPath, Boolean lightweight) {
        return this.updatePipelineJob("", jobName, gitUrl, gitCertId, gitBranch, scriptPath, lightweight);
    }

    /**
     * update job config
     *
     * @param folderName  folder name
     * @param jobName     job name
     * @param gitUrl      git url
     * @param gitCertId   git cert id
     * @param gitBranch   git branch
     * @param scriptPath  script path
     * @param lightweight lightweight
     * @return operation result
     */
    public boolean updatePipelineJob(String folderName, String jobName, String gitUrl, String gitCertId, String gitBranch,
                                     String scriptPath, Boolean lightweight) {
        String jobConfig = this.getJobsApi().config(folderName, jobName);
        if (jobConfig == null || jobConfig.isEmpty()) {
            return false;
        }

        String startTag = "<definition class=\"org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition\"";
        String endTag = "</definition>";
        int startIndex = jobConfig.indexOf(startTag);
        int endIndex = jobConfig.indexOf(endTag);

        if (startIndex > 0 && endIndex > 0) {
            endIndex += endTag.length();
            String toBeReplaced = jobConfig.substring(startIndex, endIndex);

            String cpsScmFlowDefinitionXml = JobPropertyUtils.getPipelineCpsScmFlowDefinitionXml(gitUrl, gitCertId,
                    gitBranch, scriptPath, lightweight);

            jobConfig = jobConfig.replace(toBeReplaced, cpsScmFlowDefinitionXml);
            return this.getJobsApi().config(folderName, jobName, jobConfig);
        }

        return false;
    }

    /**
     * get jenkins system info
     *
     * @return jenkins sys info
     */
    public SystemInfo getSystemInfo() {
        return this.getSystemApi().systemInfo();
    }


    /**
     * Runs the provided groovy script on the server and returns the result.
     * <p>
     * This is similar to running groovy scripts using the script console.
     * <p>
     * In the instance where your script causes an exception, the server still
     * returns a 200 status, so detecting errors is very challenging. It is
     * recommended to use heuristics to check your return string for stack
     * traces by detecting strings like "groovy.lang.(something)Exception".
     *
     * @param script The script to run.
     * @return results The results of the run of the script.
     * @throws IOException in case of an error.
     */
    public String runScript(String script) throws IOException {
        return getServer().runScript(script);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @NotNull
    private Integer waitForBuildNumber(Integer queueId) {
        QueueItem queueItem = getQueueApi().queueItem(queueId);

        while (queueItem.executable() == null) {
            LockSupport.parkNanos(50 * 1000 * 1000L);
            queueItem = getQueueApi().queueItem(queueId);
        }
        return queueItem.executable().number();
    }
}
