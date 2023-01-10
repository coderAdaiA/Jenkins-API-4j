package cn.coderadai.jenkins.api.model;

import cn.coderadai.jenkins.api.util.JenkinsAPIUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * @author: coderAdai
 * @date 2022/07/06 18:22
 * @description: TODO
 */
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode
public class JenkinsFolder {
    private static final String XML_PATH = "JenkinsXmlTemplates/FolderConfig.xml";

    private static final String FOLDER_LIBRARIES_XML_PATH = "JenkinsXmlTemplates/FolderLibraries.xml";

    @NonNull
    @Builder.Default
    private String name = "";

    @NonNull
    @Builder.Default
    private String parentFolderName = "";

    @NonNull
    @Builder.Default
    private String description = "";

    @NonNull
    @Builder.Default
    private String libraryName = "";

    @NonNull
    @Builder.Default
    private String libraryGitUrl = "";

    @NonNull
    @Builder.Default
    private String libraryGitCertId = "";

    @NonNull
    @Builder.Default
    private String libraryDefaultVersion = "";

    @NonNull
    @Builder.Default
    private Integer libraryCacheRefreshTimeMinutes = 0;

    public String toXmlString() {
        String content = JenkinsAPIUtils.getFileContent(XML_PATH);
        content = content.replace("${displayName}", this.getName());
        content = content.replace("${description}", this.getDescription());
        content = content.replace("${folderLibraries}", getLibraryPropertyXmlString());
        return content;
    }

    private String getLibraryPropertyXmlString() {
        if (!IsOpenLibrary()) {
            return "";
        }

        String content = JenkinsAPIUtils.getFileContent(FOLDER_LIBRARIES_XML_PATH);
        content = content.replace("${libraryName}", this.getLibraryName());
        content = content.replace("${libraryId}", UUID.randomUUID().toString());
        content = content.replace("${libraryGitUrl}", this.getLibraryGitUrl());
        content = content.replace("${libraryGitCredentialsId}", this.getLibraryGitCertId());
        content = content.replace("${libraryDefaultVersion}", this.getLibraryDefaultVersion());
        content = content.replace("${libraryCacheRefreshTimeMinutes}", this.getLibraryCacheRefreshTimeMinutes().toString());
        return content;
    }

    private Boolean IsOpenLibrary() {
        return !this.getLibraryName().isEmpty() && !this.getLibraryGitUrl().isEmpty();
    }
}
