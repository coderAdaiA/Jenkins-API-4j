package cn.coderadai.jenkins.api.parameter.jobParameter;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
class BaseParameter {
    private static final String NAME_TAG = "${name}";

    private static final String DESCRIPTION_TAG = "${description}";

    @NonNull
    @Builder.Default
    private String name = "";

    @NonNull
    @Builder.Default
    private String description = "";

    public String replaceBaseParameters(String fileContent) {
        return fileContent.replace(NAME_TAG, this.getName())
                .replace(DESCRIPTION_TAG, this.getDescription());
    }
}
