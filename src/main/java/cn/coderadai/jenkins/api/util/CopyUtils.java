package cn.coderadai.jenkins.api.util;

import cn.coderadai.jenkins.api.exception.JenkinsClientException;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public final class CopyUtils {

    private CopyUtils() {
    }

    public static <T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }

        try {
            T obj = clazz.newInstance();
            BeanUtils.copyProperties(source, obj);
            return obj;
        } catch (Exception e) {
            throw new JenkinsClientException("copy object error", e);
        }
    }

    public static <T, K> List<T> copyList(List<K> sourceList, Class<T> clazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return null;
        }
        return sourceList.stream().map(input -> copy(input, clazz)).collect(Collectors.toList());
    }
}
