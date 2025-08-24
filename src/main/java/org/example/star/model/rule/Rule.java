package org.example.star.model.rule;

import java.util.List;

public class Rule {
    private final String queryName;
    private final List<String> arguments;
    private final boolean negate;

    public Rule(String queryName, List<String> arguments, boolean negate) {
        this.queryName = queryName;
        this.arguments = arguments;
        this.negate = negate;
    }

    public String getQueryName() {
        return queryName;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public boolean isNegate() {
        return negate;
    }
}
