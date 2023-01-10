package cn.coderadai.jenkins.api.model.configmodel.properties;

import cn.coderadai.jenkins.api.model.configmodel.properties.parameters.ParametersDefinitionProperty;
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
public class Properties implements Serializable {
    @XmlElement(name = "hudson.security.AuthorizationMatrixProperty")
    private AuthorizationMatrixProperty authorizationMatrixProperty;

    @XmlElement(name = "jenkins.model.BuildDiscarderProperty")
    private BuildDiscarderProperty buildDiscarderProperty;

    @XmlElement(name = "hudson.model.ParametersDefinitionProperty")
    private ParametersDefinitionProperty parametersDefinitionProperty;
}

