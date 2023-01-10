package cn.coderadai.jenkins.api.model;

import cn.coderadai.jenkins.api.util.CopyUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class JobInfo {
    private String name;
    private String displayName;
    private String description;
    private String url;
    private String color;
    private boolean buildable;
    private boolean inQueue;
    private boolean keepDependencies;
    private int nextBuildNumber;
    private boolean concurrentBuild;
    private List<SimpleBuildInfo> builds;
    private SimpleBuildInfo firstBuild;
    private SimpleBuildInfo lastBuild;
    private SimpleBuildInfo lastCompleteBuild;
    private SimpleBuildInfo lastFailedBuild;
    private SimpleBuildInfo lastStableBuild;
    private SimpleBuildInfo lastSuccessfulBuild;
    private SimpleBuildInfo lastUnstableBuild;
    private SimpleBuildInfo lastUnsuccessfulBuild;

    public static JobInfo copyFrom(com.cdancy.jenkins.rest.domain.job.JobInfo info) {
        JobInfo result = CopyUtils.copy(info, JobInfo.class);
        if (Objects.isNull(result)) {
            return null;
        }
        result.setBuilds(SimpleBuildInfo.copyFrom(info.builds()));
        result.setFirstBuild(SimpleBuildInfo.copyFrom(info.firstBuild()));
        result.setLastBuild(SimpleBuildInfo.copyFrom(info.lastBuild()));
        result.setLastCompleteBuild(SimpleBuildInfo.copyFrom(info.lastCompleteBuild()));
        result.setLastFailedBuild(SimpleBuildInfo.copyFrom(info.lastFailedBuild()));
        result.setLastStableBuild(SimpleBuildInfo.copyFrom(info.lastStableBuild()));
        result.setLastSuccessfulBuild(SimpleBuildInfo.copyFrom(info.lastSuccessfulBuild()));
        result.setLastUnstableBuild(SimpleBuildInfo.copyFrom(info.lastUnstableBuild()));
        result.setLastUnsuccessfulBuild(SimpleBuildInfo.copyFrom(info.lastUnsuccessfulBuild()));
        return result;
    }
}
