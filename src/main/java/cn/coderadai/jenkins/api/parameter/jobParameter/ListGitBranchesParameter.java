package cn.coderadai.jenkins.api.parameter.jobParameter;

import cn.coderadai.jenkins.api.util.JenkinsAPIUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * @author: coderAdai
 * @date 2022/06/28 16:56
 * @description: TODO
 */
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public class ListGitBranchesParameter extends BaseParameter implements IParameter {

    private static final String XML_PATH = "JenkinsXmlTemplates/Parameters/ListGitBranchesParameterDefinition.xml";

    private static final String UUID_TAG = "${uuid}";

    private static final String REMOTE_URL_TAG = "${remoteURL}";

    private static final String CREDENTIALS_ID_TAG = "${credentialsId}";

    private static final String DEFAULT_VALUE_TAG = "${defaultValue}";

    private static final String QUICK_FILTER_ENABLED_TAG = "${quickFilterEnabled}";

    private static final String LIST_SIZE_TAG = "${listSize}";


    @NonNull
    @Builder.Default
    private String remoteUrl = "";

    @NonNull
    @Builder.Default
    private String credentialsId = "";

    @NonNull
    @Builder.Default
    private String defaultValue = "";

    @NonNull
    @Builder.Default
    private Boolean quickFilterEnabled = true;

    @NonNull
    @Builder.Default
    private Integer listSize = 5;

    @Override
    public String toXmlString() {
        String fileContent = JenkinsAPIUtils.getFileContent(XML_PATH);
        fileContent = this.replaceBaseParameters(fileContent);
        fileContent = fileContent.replace(UUID_TAG, UUID.randomUUID().toString())
                .replace(REMOTE_URL_TAG, this.getRemoteUrl())
                .replace(CREDENTIALS_ID_TAG, this.getCredentialsId())
                .replace(DEFAULT_VALUE_TAG, this.getDefaultValue())
                .replace(QUICK_FILTER_ENABLED_TAG, this.getQuickFilterEnabled().toString())
                .replace(LIST_SIZE_TAG, this.getListSize().toString());
        return fileContent;
    }
}
