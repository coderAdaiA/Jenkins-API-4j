package cn.coderadai.jenkins.api.model.configmodel.properties.parameters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * @author: coderAdai
 * @date 2022/07/26 15:56
 * @description: TODO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class TextParameterDefinition implements Serializable {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "defaultValue")
    private String defaultValue;
}
