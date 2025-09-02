package org.example.star.model.rule;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Rule {
    private String queryName;
    private List<String> arguments;
    private boolean negate;


    public Rule(String queryName, List<String> arguments,boolean negate) {
        this.queryName = queryName;
        this.arguments = arguments;
        this.negate = negate;
    }

    public Rule( ){}

    @JsonProperty("query")
    public String getQueryName() {
        return queryName;
    }
    @JsonProperty("arguments")
    public List<String> getArguments() {
        return arguments;
    }
    @JsonProperty("negate")
    public boolean isNegate() {
        return negate;
    }
    @JsonProperty("query")
    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }
    @JsonProperty("arguments")
    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }
    @JsonProperty("negate")
    public void setNegate(boolean negate) {
        this.negate = negate;
    }
}
