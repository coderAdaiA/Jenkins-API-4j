package cn.coderadai.jenkins.api.model;

import cn.coderadai.jenkins.api.util.CopyUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class SimpleBuildInfo {
    private String url;
    private int number;
    private boolean building;

    protected static SimpleBuildInfo copyFrom(com.cdancy.jenkins.rest.domain.job.BuildInfo info) {
        return CopyUtils.copy(info, SimpleBuildInfo.class);
    }

    protected static List<SimpleBuildInfo> copyFrom(List<com.cdancy.jenkins.rest.domain.job.BuildInfo> infos) {
        if (Objects.isNull(infos)) {
            return new ArrayList<>();
        }
        return infos.stream().map(SimpleBuildInfo::copyFrom).collect(Collectors.toList());
    }
}
