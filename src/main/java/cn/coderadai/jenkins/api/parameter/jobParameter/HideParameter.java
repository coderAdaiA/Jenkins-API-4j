package cn.coderadai.jenkins.api.parameter.jobParameter;

import cn.coderadai.jenkins.api.util.JenkinsAPIUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author: coderAdai
 * @date 2022/07/07 11:50
 * @description: TODO
 */
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public class HideParameter extends BaseParameter implements IParameter {
    private static final String XML_PATH = "JenkinsXmlTemplates/Parameters/WHideParameterDefinition.xml";

    private static final String DEFAULT_VALUE_TAG = "${defaultValue}";

    @NonNull
    @Builder.Default
    private String defaultValue = "";

    @Override
    public String toXmlString() {
        String fileContent = JenkinsAPIUtils.getFileContent(XML_PATH);
        fileContent = this.replaceBaseParameters(fileContent);
        fileContent = fileContent.replace(DEFAULT_VALUE_TAG, this.getDefaultValue());
        return fileContent;
    }
}
