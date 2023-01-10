package cn.coderadai.jenkins.api.parameter.jobParameter;

import cn.coderadai.jenkins.api.util.JenkinsAPIUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public class BooleanParameter extends BaseParameter implements IParameter {
    private static final String XML_PATH = "JenkinsXmlTemplates/Parameters/BooleanParameterDefinition.xml";

    private static final String DEFAULT_VALUE_TAG = "${defaultValue}";

    @NonNull
    @Builder.Default
    private Boolean defaultValue = false;

    @Override
    public String toXmlString() {
        String content = JenkinsAPIUtils.getFileContent(XML_PATH);
        content = this.replaceBaseParameters(content);
        content = content.replace(DEFAULT_VALUE_TAG, this.getDefaultValue().toString());
        return content;
    }
}
