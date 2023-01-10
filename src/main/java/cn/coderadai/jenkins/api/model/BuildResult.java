package cn.coderadai.jenkins.api.model;

import java.util.Arrays;

/**
 * @author: coderAdai
 * @date 2022/06/10 18:52
 * @description: TODO
 */
public enum BuildResult {

    SUCCESS("SUCCESS"),
    UNSTABLE("UNSTABLE"),
    FAILURE("FAILURE"),
    NOT_BUILT("NOT_BUILT"),
    ABORTED("ABORTED");

    private final String value;

    BuildResult(String val) {
        this.value = val;
    }

    public static BuildResult parse(String buildResultString) {
        return Arrays.stream(BuildResult.values()).filter(r -> r.value.equalsIgnoreCase(buildResultString))
                .findFirst()
                .orElse(null);
    }
}
