package cn.coderadai.jenkins.api.model.configmodel.properties;

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
@XmlRootElement(name = "strategy")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TasksLogRotatorStrategy implements Serializable {
    private String daysToKeep;
    private String numToKeep;
    private String artifactDaysToKeep;
    private String artifactNumToKeep;
}
