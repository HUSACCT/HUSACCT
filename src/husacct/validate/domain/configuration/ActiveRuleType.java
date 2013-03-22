package husacct.validate.domain.configuration;

import java.util.ArrayList;
import java.util.List;

public class ActiveRuleType implements Cloneable {

    private final String ruleType;
    private List<ActiveViolationType> violationTypes;

    public ActiveRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public ActiveRuleType(String ruleType, List<ActiveViolationType> activeViolationTypes) {
        this.ruleType = ruleType;
        this.violationTypes = activeViolationTypes;
    }

    public String getRuleType() {
        return ruleType;
    }

    public List<ActiveViolationType> getViolationTypes() {
        if (violationTypes == null) {
            violationTypes = new ArrayList<ActiveViolationType>();
        }
        return violationTypes;
    }

    void setViolationTypes(List<ActiveViolationType> violationTypes) {
        this.violationTypes = violationTypes;
    }
}