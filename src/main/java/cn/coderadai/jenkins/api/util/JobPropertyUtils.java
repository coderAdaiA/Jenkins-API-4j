package cn.coderadai.jenkins.api.util;

import cn.coderadai.jenkins.api.model.authorization.Permission;

import java.util.List;

public final class JobPropertyUtils {

    private static final String DISABLE_CONCURRENT_BUILDS_JOB_PROPERTY_XML_PATH = "JenkinsXmlTemplates/DisableConcurrentBuildsJobProperty.xml";

    private static final String AUTHORIZATION_MATRIX_PROPERTY_XML_PATH = "JenkinsXmlTemplates/AuthorizationMatrixProperty.xml";

    private static final String INTERNAL_SET_NEXT_BUILD_NUMBER_JOB_XML_PATH = "JenkinsXmlTemplates/BuiltIn/SetNextBuildNumberToolJob.xml";

    private static final String PIPELINE_CPS_SCM_FLOW_DEFINITION_XML_PATH = "JenkinsXmlTemplates/PipelineCpsScmFlowDefinition.xml";


    public static String getInternalSetNextBuildNumberJobXml() {
        return JenkinsAPIUtils.getFileContent(INTERNAL_SET_NEXT_BUILD_NUMBER_JOB_XML_PATH);
    }

    public static String getDisableConcurrentBuildsXml(Boolean concurrentBuilds) {
        if (concurrentBuilds == null || concurrentBuilds) {
            return "";
        }
        return JenkinsAPIUtils.getFileContent(DISABLE_CONCURRENT_BUILDS_JOB_PROPERTY_XML_PATH);
    }

    public static String getAuthorizationMatrixPropertyXml(List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return "";
        }

        String OPENING_TAG = "<permission>";
        String CLOSING_TAG = "</permission>";

        StringBuilder stringBuilder = new StringBuilder();
        for (Permission permission : permissions) {
            String name = permission.getName();
            if (name == null || name.isEmpty()) {
                continue;
            }

            String style = permission.getStyle().getValue();
            String type = permission.getType().getValue();

            stringBuilder.append(OPENING_TAG);
            stringBuilder.append(String.format("%s%s:%s", style, type, name));
            stringBuilder.append(CLOSING_TAG);
            stringBuilder.append("\n");
        }

        String PERMISSIONS_TAG = "${permissions}";
        String xml = JenkinsAPIUtils.getFileContent(AUTHORIZATION_MATRIX_PROPERTY_XML_PATH);
        return xml.replace(PERMISSIONS_TAG, stringBuilder.toString());
    }

    public static String getPipelineCpsScmFlowDefinitionXml(String gitUrl, String gitCertId, String gitBranch,
                                                            String scriptPath, Boolean lightweight) {
        String content = JenkinsAPIUtils.getFileContent(PIPELINE_CPS_SCM_FLOW_DEFINITION_XML_PATH);
        content = content.replace("${pipelineGitUrl}", gitUrl);
        content = content.replace("${pipelineGitCertID}", gitCertId);
        content = content.replace("${pipelineBranch}", gitBranch);
        content = content.replace("${pipelineScriptPath}", scriptPath);
        content = content.replace("${lightweight}", lightweight.toString());
        return content;
    }
}
