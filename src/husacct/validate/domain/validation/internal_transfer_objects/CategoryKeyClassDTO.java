package husacct.validate.domain.validation.internal_transfer_objects;

import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.ruletype.RuleType;

public class CategoryKeyClassDTO {

    private final String categoryKey;
    private final Class<RuleType> ruleClass;
    private final DefaultSeverities defaultSeverity;

    public CategoryKeyClassDTO(String categoryKey, Class<RuleType> ruleClass, DefaultSeverities defaultSeverity) {
        this.categoryKey = categoryKey;
        this.ruleClass = ruleClass;
        this.defaultSeverity = defaultSeverity;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public Class<RuleType> getRuleClass() {
        return ruleClass;
    }

    public DefaultSeverities getDefaultSeverity() {
        return defaultSeverity;
    }
}