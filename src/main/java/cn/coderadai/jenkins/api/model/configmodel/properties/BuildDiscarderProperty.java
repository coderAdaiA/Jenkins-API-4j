package cn.coderadai.jenkins.api.model.configmodel.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BuildDiscarderProperty implements Serializable {
    private TasksLogRotatorStrategy tasksLogRotatorStrategy;
}
