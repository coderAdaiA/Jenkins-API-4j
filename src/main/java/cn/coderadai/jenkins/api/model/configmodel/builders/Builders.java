package cn.coderadai.jenkins.api.model.configmodel.builders;

import cn.coderadai.jenkins.api.model.configmodel.tasks.BatchFile;
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
public class Builders implements Serializable {
    @XmlElement(name = "hudson.tasks.BatchFile")
    private BatchFile batchFile;
}
