package cn.coderadai.jenkins.api.util;

import cn.coderadai.jenkins.api.exception.JenkinsClientException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class JenkinsAPIUtils {

    private static final ClassLoader classLoader = JenkinsAPIUtils.class.getClassLoader();

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isEmptyCollection(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static String getFileContent(String fileName) {
        try (InputStream resourceStream = classLoader.getResourceAsStream(fileName)) {
            if (Objects.isNull(resourceStream)) {
                throw new JenkinsClientException(String.format("File not found! Path: [%s]", fileName));
            }

            return new BufferedReader(new InputStreamReader(resourceStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new JenkinsClientException(e);
        }
    }

    public static <T> T tryExecute(Supplier<T> supplier) {
        if (Objects.isNull(supplier)) {
            return null;
        }

        try {
            return supplier.get();
        } catch (Exception e) {
            throw new JenkinsClientException(e);
        }
    }

    public static void tryExecute(Runnable runnable) {
        if (Objects.isNull(runnable)) {
            return;
        }

        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
