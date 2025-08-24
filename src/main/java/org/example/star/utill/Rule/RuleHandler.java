package org.example.star.utill.Rule;

import org.example.star.constants.Queries;
import org.example.star.model.rule.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RuleHandler {
    public RuleHandler() {
    }

    public static boolean ruleISValid(Rule rule) {
        Queries query = Queries.getQueryTemplates().get(rule.getQueryName());
        return (query != null) && ruleQueryArgumentsIsValid(query.getPossibleArgs(), rule.getArguments(), query.getArgsOrder());
    }

    private static boolean ruleQueryArgumentsIsValid(Map<String, List<String>> possibleArgs, List<String> currentArgs, List<String> argsOrder) {
        if (possibleArgs.size() != currentArgs.size()) {
            return false;
        }
        for (int i = 0; i < possibleArgs.size(); i++) {
            if (!possibleArgs.get(argsOrder.get(i)).contains(currentArgs.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static Map<String, Object> buildSqlParameters(Rule rule){
        Queries queryTemplate = Queries.getQueryTemplates().get(rule.getQueryName());
        Map<String,Object> params = new HashMap<>();
        for (int i = 0; i < queryTemplate.getPossibleArgs().size(); i++) {
            String paramKey = queryTemplate.getArgsOrder().get(i);
            params.put(paramKey,rule.getArguments().get(i));
        }
        return params;
    }

    public static String getSqlQuery(Rule rule) {
        return Queries.getQueryTemplates().get(rule.getQueryName()).getQuery();
    }

}
