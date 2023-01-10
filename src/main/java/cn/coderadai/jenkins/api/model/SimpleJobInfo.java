package cn.coderadai.jenkins.api.model;

import cn.coderadai.jenkins.api.util.CopyUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SimpleJobInfo {
    private String name;
    private String url;
    private String color;

    public static List<SimpleJobInfo> copyFrom(List<com.cdancy.jenkins.rest.domain.job.Job> jobs) {
        List<SimpleJobInfo> simpleJobInfos = CopyUtils.copyList(jobs, SimpleJobInfo.class);
        if (Objects.isNull(simpleJobInfos)) {
            return new ArrayList<>();
        }
        return simpleJobInfos;
    }
}
