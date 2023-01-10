package cn.coderadai.jenkins.api.model;

import cn.coderadai.jenkins.api.model.authorization.Permission;
import cn.coderadai.jenkins.api.parameter.jobParameter.IParameter;
import cn.coderadai.jenkins.api.parameter.jobParameter.Parameters;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: coderAdai
 * @date 2022/06/09 18:02
 * @description: TODO
 */
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode
public class JenkinsJob {

    @Builder.Default
    private String folderName = "";

    @Builder.Default
    private String name = "";

    @Builder.Default
    private String description = "";

    @Builder.Default
    private Integer logDaysToKeep = -1;

    @Builder.Default
    private Integer logNumToKeep = 25;

    @Builder.Default
    private Integer artifactDaysToKeep = -1;

    @Builder.Default
    private Integer artifactNumToKeep = -1;

    @Builder.Default
    private Boolean isConcurrentBuild = true;

    @Builder.Default
    private List<Permission> authorizationPermissions = new ArrayList<>();

    @Builder.Default
    private List<IParameter> buildParameters = new ArrayList<>();

    public Parameters convertParameters() {
        Parameters parameters = new Parameters();
        if (this.buildParameters != null) {
            this.buildParameters.forEach(parameters::addParameter);
        }
        return parameters;
    }
}
