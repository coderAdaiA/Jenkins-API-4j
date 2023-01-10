package cn.coderadai.jenkins.api.util;

/**
 * @author: coderAdai
 * @date 2022/07/27 20:10
 * @description: TODO
 */
public class URLStringBuilder {
    private String urlString;

    public URLStringBuilder(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("urlString can not be null or empty.");
        }

        this.urlString = url;
    }

    public URLStringBuilder appendIfNotEmpty(String path) {
        if (path == null || path.isEmpty()) {
            return this;
        }

        String resolvePath = this.trimPath(path);
        if (this.urlString.endsWith("/")) {
            this.urlString = String.format("%s%s", this.urlString, resolvePath);
        } else {
            this.urlString = String.format("%s/%s", this.urlString, resolvePath);
        }

        return this;
    }

    private String trimPath(String str) {
        return str.replaceAll("//+", "/");
    }

    public String build() {
        return this.urlString;
    }
}
