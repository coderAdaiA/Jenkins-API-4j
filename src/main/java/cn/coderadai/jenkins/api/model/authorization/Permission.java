package cn.coderadai.jenkins.api.model.authorization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Permission {
    @Builder.Default
    private String name = "";

    @Builder.Default
    private PermissionType type = PermissionType.READ;

    @Builder.Default
    private PermissionStyle style = PermissionStyle.USER;
}
