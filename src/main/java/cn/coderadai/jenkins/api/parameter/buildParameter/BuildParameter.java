package cn.coderadai.jenkins.api.parameter.buildParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildParameter {
    protected final Map<String, List<String>> properties;

    public BuildParameter() {
        this.properties = new HashMap<>();
    }

    public static ParameterBuilder builder() {
        return new ParameterBuilder();
    }

    public Map<String, List<String>> getProperties() {
        return new HashMap<>(this.properties);
    }

    public static class ParameterBuilder {
        private final BuildParameter parameter;

        public ParameterBuilder() {
            this.parameter = new BuildParameter();
        }

        public ParameterBuilder parameter(String key, String... values) {
            this.parameter.properties.put(key, Arrays.stream(values).collect(Collectors.toList()));
            return this;
        }

        public BuildParameter build() {
            return this.parameter;
        }
    }
}

