package cn.coderadai.jenkins.api.parameter.jobParameter;


public class Parameters {
    private final StringBuilder xmlString;

    public Parameters() {
        this.xmlString = new StringBuilder();
    }

    public static ParametersBuilder builder() {
        return new ParametersBuilder();
    }

    public Parameters addParameter(IParameter parameter) {
        this.xmlString.append(parameter.toXmlString()).append("\n");
        return this;
    }

    public String toString() {
        return this.xmlString.toString();
    }

    public static class ParametersBuilder {
        private final Parameters parameters;

        public ParametersBuilder() {
            this.parameters = new Parameters();
        }

        public ParametersBuilder addParameter(IParameter parameter) {
            this.parameters.xmlString.append(parameter.toXmlString()).append("\n");
            return this;
        }

        public String build() {
            return this.parameters.xmlString.toString();
        }
    }
}
