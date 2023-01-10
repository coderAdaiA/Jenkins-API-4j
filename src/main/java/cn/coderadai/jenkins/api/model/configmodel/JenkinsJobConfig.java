package cn.coderadai.jenkins.api.model.configmodel;

import cn.coderadai.jenkins.api.model.configmodel.builders.Builders;
import cn.coderadai.jenkins.api.model.configmodel.properties.Properties;
import cn.coderadai.jenkins.api.model.configmodel.scm.Scm;
import cn.coderadai.jenkins.api.model.configmodel.triggers.Triggers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class JenkinsJobConfig implements Serializable {
    private String description;
    private String keepDependencies;
    private String assignedNode;
    private String disabled;
    private String concurrentBuild;
    private String customWorkspace;
    private Properties properties;
    private Scm scm;
    private Triggers triggers;
    private Builders builders;
}
