package cn.coderadai.jenkins.api.model.configmodel.triggers;

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
public class Triggers implements Serializable {
    @XmlElement(name = "hudson.triggers.TimerTrigger")
    private TimerTrigger timerTrigger;

    @XmlElement(name = "hudson.triggers.SCMTrigger")
    private SCMTrigger scmTrigger;
}
