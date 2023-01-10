package cn.coderadai.jenkins.api.model.authorization;

/**
 * @author: coderAdai
 * @date 2022/08/16 11:09
 * @description: TODO
 */
public enum PermissionStyle {
    /**
     * old style
     */
    OLD(""),

    /**
     * user style
     */
    USER("USER:"),

    /**
     * group style
     */
    GROUP("GROUP:");

    private final String value;

    PermissionStyle(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
