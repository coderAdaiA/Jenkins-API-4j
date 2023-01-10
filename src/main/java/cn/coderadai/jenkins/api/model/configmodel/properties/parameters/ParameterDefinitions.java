package cn.coderadai.jenkins.api.model.configmodel.properties.parameters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author: coderAdai
 * @date 2022/07/26 15:53
 * @description: TODO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterDefinitions implements Serializable {

    @XmlElement(name = "hudson.model.TextParameterDefinition")
    private List<TextParameterDefinition> textParameters;

    @XmlElement(name = "hudson.model.BooleanParameterDefinition")
    private List<BooleanParameterDefinition> booleanParameters;
}
