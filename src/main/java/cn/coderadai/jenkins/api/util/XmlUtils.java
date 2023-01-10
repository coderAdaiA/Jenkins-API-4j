package cn.coderadai.jenkins.api.util;

import cn.coderadai.jenkins.api.exception.JenkinsClientException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public final class XmlUtils {

    private XmlUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertXml2Object(String xml, Class<T> clazz) {
        try {
            Unmarshaller unmarshal = JAXBContext.newInstance(clazz).createUnmarshaller();
            StringReader sr = new StringReader(xml);
            T xmlObject = (T) unmarshal.unmarshal(sr);
            sr.close();
            return xmlObject;
        } catch (Exception e) {
            throw new JenkinsClientException("Convert xml to object error", e);
        }
    }
}
