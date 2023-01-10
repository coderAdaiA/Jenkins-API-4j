package cn.coderadai.jenkins.api.model;

import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@NoArgsConstructor
public class BuildLog implements Iterator<String> {
    private List<String> list;
    private int index;

    public BuildLog(String text) {
        this.list = Arrays.asList(text.split("\\r?\\n", -1));
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return this.index < this.list.size();
    }

    @Override
    public String next() {
        try {
            return this.list.get(this.index);
        } finally {
            this.index++;
        }
    }

    public void forEach(Function<String, Boolean> function) {
        Objects.requireNonNull(function);
        while (hasNext()) {
            Boolean nextOne = function.apply(next());
            if (!nextOne) {
                break;
            }
        }
    }

    public void resetIndex() {
        this.index = 0;
    }

    public String getLog() {
        return String.join("\n", this.list);
    }
}
