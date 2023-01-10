package cn.coderadai.jenkins.api.model.configmodel.scm.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Branches implements Serializable {
    @XmlElement(name = "hudson.plugins.git.BranchSpec")
    private GitBranchSpec gitBranchSpec;
}
