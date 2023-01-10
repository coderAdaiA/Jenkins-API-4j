package cn.coderadai.jenkins.api.parameter.jobParameter;

import cn.coderadai.jenkins.api.util.JenkinsAPIUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public class ChoiceParameter extends BaseParameter implements IParameter {
    private static final String XML_PATH = "JenkinsXmlTemplates/Parameters/ChoiceParameterDefinition.xml";

    private static final String CHOICES_TAG = "${choices}";

    @NonNull
    @Builder.Default
    protected List<String> choices = new ArrayList<>();

    private String getChoicesXml() {
        return this.getChoices().stream()
                .map(choice -> String.format("<string>%s</string>\n", choice))
                .collect(Collectors.joining());
    }

    @Override
    public String toXmlString() {
        String content = JenkinsAPIUtils.getFileContent(XML_PATH);
        content = this.replaceBaseParameters(content);
        content = content.replace(CHOICES_TAG, this.getChoicesXml());
        return content;
    }
}
