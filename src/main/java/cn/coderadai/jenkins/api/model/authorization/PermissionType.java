package cn.coderadai.jenkins.api.model.authorization;

/**
 * @author: coderAdai
 * @date 2022/05/16 10:09
 * @description: TODO
 */
public enum PermissionType {
    /**
     * Read
     */
    READ("hudson.model.Item.Read"),

    /**
     * Configure
     */
    CONFIGURE("hudson.model.Item.Configure"),

    /**
     * Delete
     */
    DELETE("hudson.model.Item.Delete"),

    /**
     * Build
     */
    BUILD("hudson.model.Item.Build"),

    /**
     * Cancel
     */
    CANCEL("hudson.model.Item.Cancel"),

    /**
     * Workspace
     */
    WORKSPACE("hudson.model.Item.Workspace"),

    /**
     * Discover
     */
    DISCOVER("hudson.model.Item.Discover"),

    /**
     * Move
     */
    MOVE("hudson.model.Item.Move"),

    /**
     * Replay run
     */
    RUN_REPLAY("hudson.model.Run.Replay"),

    /**
     * Update run
     */
    RUN_UPDATE("hudson.model.Run.Update"),

    /**
     * SCM tag
     */
    SCM_TAG("hudson.scm.SCM.Tag"),

    /**
     * View credentials
     */
    CREDENTIALS_VIEW("com.cloudbees.plugins.credentials.CredentialsProvider.View"),

    /**
     * Create credentials
     */
    CREDENTIALS_CREATE("com.cloudbees.plugins.credentials.CredentialsProvider.Create"),

    /**
     * Update credentials
     */
    CREDENTIALS_UPDATE("com.cloudbees.plugins.credentials.CredentialsProvider.Update"),

    /**
     * Delete credentials
     */
    CREDENTIALS_DELETE("com.cloudbees.plugins.credentials.CredentialsProvider.Delete"),

    /**
     * ManageDomains credentials
     */
    CREDENTIALS_MANAGE_DOMAINS("com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains");

    private final String value;

    PermissionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
