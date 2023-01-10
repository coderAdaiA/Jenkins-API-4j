package cn.coderadai.jenkins.api.test;

import cn.coderadai.jenkins.api.JenkinsAPI;
import cn.coderadai.jenkins.api.model.JenkinsPipelineJob;
import cn.coderadai.jenkins.api.model.SimpleJobInfo;
import cn.coderadai.jenkins.api.parameter.buildParameter.BuildParameter;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: coderAdai
 * @date 2022/06/10 09:56
 * @description: TODO
 */
public class JenkinsAPITest {

    private static final String JENKINS_ENDPOINT = "https://onebuild-jci.beisen-inc.com/";
    private static final String JENKINS_USERNAME_TOKEN = "onebuild:11f29a631b53abad5df9df0eb51cca0399";

    @Test
    public void testJenkinsProvider() {
        LocalDateTime start = LocalDateTime.now();
        String folderName = "Test_Folder";
        String jobName = "JobName";

        try (JenkinsAPI apiClient = new JenkinsAPI(JENKINS_ENDPOINT)) {
            if (!apiClient.isFolderExists(folderName)) {
                apiClient.createFolder(folderName);
            }

            JenkinsPipelineJob pipelineJob = JenkinsPipelineJob.builder()
                    .folderName(folderName)
                    .name(jobName)
                    .description("desc")
                    .isConcurrentBuild(true)
                    .logNumToKeep(15)
                    .lightweight(true)
                    .build();
            apiClient.createPipelineJob(pipelineJob);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.printf("%xs%n", Duration.between(start, LocalDateTime.now()).getSeconds());
    }

    @Test
    public void testMoveJob() {
        try (JenkinsAPI apiClient = new JenkinsAPI(JENKINS_ENDPOINT, JENKINS_USERNAME_TOKEN)) {
            String result = apiClient.moveJob("Assessment.VideoCloud", "MSBuild_BI", "MSBuild_Attendance");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuildJob() {
        try (JenkinsAPI apiClient = new JenkinsAPI(JENKINS_ENDPOINT, JENKINS_USERNAME_TOKEN)) {
            List<SimpleJobInfo> jobList = apiClient.getJobList();
            System.out.println(jobList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
