package cn.coderadai.jenkins.api.model.configmodel.scm;

import cn.coderadai.jenkins.api.model.configmodel.scm.branch.Branches;
import cn.coderadai.jenkins.api.model.configmodel.scm.userremote.UserRemoteConfigs;
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
public class Scm implements Serializable {
    private String configVersion;
    private UserRemoteConfigs userRemoteConfigs;
    private Branches branches;
}
