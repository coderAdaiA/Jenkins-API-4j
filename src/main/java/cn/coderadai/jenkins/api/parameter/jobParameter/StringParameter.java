package cn.coderadai.jenkins.api.parameter.jobParameter;

import cn.coderadai.jenkins.api.util.JenkinsAPIUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public class StringParameter extends BaseParameter implements IParameter {
    private static final String XML_PATH = "JenkinsXmlTemplates/Parameters/StringParameterDefinition.xml";

    private static final String TRIM_TAG = "${trim}";

    private static final String DEFAULT_VALUE_TAG = "${defaultValue}";

    @NonNull
    @Builder.Default
    private String defaultValue = "";

    @NonNull
    @Builder.Default
    private Boolean isTrim = false;

    @Override
    public String toXmlString() {
        String fileContent = JenkinsAPIUtils.getFileContent(XML_PATH);
        fileContent = this.replaceBaseParameters(fileContent);
        fileContent = fileContent.replace(DEFAULT_VALUE_TAG, this.getDefaultValue())
                .replace(TRIM_TAG, this.getIsTrim().toString());
        return fileContent;
    }
}
