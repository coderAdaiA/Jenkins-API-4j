package cn.coderadai.jenkins.api.model;

import cn.coderadai.jenkins.api.util.JenkinsAPIUtils;
import cn.coderadai.jenkins.api.util.JobPropertyUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class JenkinsPipelineJob extends JenkinsJob {
    private static final String XML_PATH = "JenkinsXmlTemplates/PipelineJobConfig.xml";

    @NonNull
    @Builder.Default
    private String pipelineGitUrl = "";

    @NonNull
    @Builder.Default
    private String pipelineGitCertId = "";

    @NonNull
    @Builder.Default
    private String pipelineBranch = "";

    @NonNull
    @Builder.Default
    private String pipelineScriptPath = "";

    @NonNull
    @Builder.Default
    private Boolean lightweight = true;

    public String toXmlString() {
        String content = JenkinsAPIUtils.getFileContent(XML_PATH);

        if (this.getDescription() != null) {
            content = content.replace("${description}", this.getDescription());
        }

        content = content.replace("${logDaysToKeep}", this.getLogDaysToKeep().toString());
        content = content.replace("${logNumToKeep}", this.getLogNumToKeep().toString());
        content = content.replace("${artifactDaysToKeep}", this.getArtifactDaysToKeep().toString());
        content = content.replace("${artifactNumToKeep}", this.getArtifactNumToKeep().toString());

        content = content.replace("${disableConcurrentBuildsJobProperty}",
                JobPropertyUtils.getDisableConcurrentBuildsXml(this.getIsConcurrentBuild()));

        content = content.replace("${authorizationMatrixProperty}",
                JobPropertyUtils.getAuthorizationMatrixPropertyXml(this.getAuthorizationPermissions()));

        content = content.replace("${parameterDefinitions}", this.convertParameters().toString());

        String cpsScmFlowDefinitionXml = JobPropertyUtils.getPipelineCpsScmFlowDefinitionXml(this.getPipelineGitUrl(),
                this.getPipelineGitCertId(), this.getPipelineBranch(), this.getPipelineScriptPath(), this.getLightweight());
        content = content.replace("${pipelineCpsScmFlowDefinition}", cpsScmFlowDefinitionXml);

        return content;
    }
}
