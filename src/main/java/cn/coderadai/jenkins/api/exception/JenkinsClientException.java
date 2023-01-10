package cn.coderadai.jenkins.api.exception;

/**
 * @author: coderAdai
 * @date 2022/06/09 16:43
 * @description: TODO
 */
public class JenkinsClientException extends RuntimeException {
    public JenkinsClientException(String message) {
        super(message);
    }

    public JenkinsClientException(Throwable cause) {
        super(cause);
    }

    public JenkinsClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
