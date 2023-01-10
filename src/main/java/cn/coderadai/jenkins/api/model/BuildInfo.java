package cn.coderadai.jenkins.api.model;

import cn.coderadai.jenkins.api.util.CopyUtils;
import cn.coderadai.jenkins.api.util.JenkinsAPIUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class BuildInfo {
    private String displayName;
    private String description;
    private int number;
    private String url;
    private boolean building;

    private long timestamp;
    private Date startTime;

    private long duration;
    private long estimatedDuration;

    private boolean keepLog;
    private int queueId;
    private BuildResult result;

    private String builtOn;

    public static BuildInfo copyFrom(com.cdancy.jenkins.rest.domain.job.BuildInfo info) {
        BuildInfo copy = CopyUtils.copy(info, BuildInfo.class);
        if (!Objects.isNull(info) && !Objects.isNull(copy)) {
            if (copy.getTimestamp() > 0) {
                copy.setStartTime(new Date(copy.getTimestamp()));
            }

            String infoResultString = info.result();
            if (!JenkinsAPIUtils.isNullOrEmpty(infoResultString)) {
                copy.setResult(BuildResult.parse(infoResultString));
            }
        }
        return copy;
    }

    protected static List<BuildInfo> copyFrom(List<com.cdancy.jenkins.rest.domain.job.BuildInfo> infos) {
        if (JenkinsAPIUtils.isEmptyCollection(infos)) {
            return new ArrayList<>();
        }
        return infos.stream().map(BuildInfo::copyFrom).collect(Collectors.toList());
    }
}
