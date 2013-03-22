package husacct.validate.domain.factory.ruletype;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.define.IDefineService;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.exception.RuleInstantionException;
import husacct.validate.domain.exception.RuleTypeNotFoundException;
import husacct.validate.domain.exception.SeverityNotFoundException;
import husacct.validate.domain.factory.violationtype.AbstractViolationType;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internal_transfer_objects.CategoryKeyClassDTO;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class RuleTypesFactory {

    private Logger logger = Logger.getLogger(RuleTypesFactory.class);
    private final IDefineService defineService = ServiceProvider.getInstance().getDefineService();
    private final ConfigurationServiceImpl configuration;
    private AbstractViolationType violationtypefactory;
    private HashMap<String, CategoryKeyClassDTO> allRuleTypes;
    private HashMap<String, CategoryKeyClassDTO> mainRuleTypes;

    public RuleTypesFactory(ConfigurationServiceImpl configuration) {
        this.configuration = configuration;

        RuleTypesGenerator ruletypegenerator = new RuleTypesGenerator();
        this.allRuleTypes = ruletypegenerator.generateAllRules();
        this.mainRuleTypes = ruletypegenerator.generateRules(RuleTypes.mainRuleTypes);
    }

    public HashMap<String, List<RuleType>> getRuleTypes(String programmingLanguage) {
        List<RuleType> ruleTypes = generateRuleTypes(programmingLanguage);
        return extractCategoriesFromRuleType(ruleTypes);
    }

    private List<RuleType> generateRuleTypes(String language) {
        setViolationTypeFactory(language);

        List<RuleType> rules = new ArrayList<RuleType>();

        for (Entry<String, CategoryKeyClassDTO> set : allRuleTypes.entrySet()) {
            try {
                Class<RuleType> ruletypeClass = set.getValue().getRuleClass();
                String categoryKey = set.getValue().getCategoryKey();
                if (ruletypeClass != null) {
                    List<ViolationType> violationlist = Collections.emptyList();
                    if (violationtypefactory != null) {
                        violationlist = violationtypefactory.createViolationTypesByRule(set.getKey());
                    }

                    RuleType rule = generateRuleObject(ruletypeClass, set.getKey(), categoryKey, violationlist);
                    rules.add(rule);

                }
            } catch (RuleInstantionException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return rules;
    }

    private HashMap<String, List<RuleType>> extractCategoriesFromRuleType(List<RuleType> ruletypes) {
        HashMap<String, List<RuleType>> returnMap = new HashMap<String, List<RuleType>>();

        for (RuleType ruletype : ruletypes) {
            final String categoryKey = ruletype.getCategoryKey();
            List<RuleType> categoryRules = returnMap.get(categoryKey);
            if (categoryRules != null) {
                categoryRules.add(ruletype);
            } else {
                List<RuleType> ruleList = new ArrayList<RuleType>();
                ruleList.add(ruletype);
                returnMap.put(categoryKey, ruleList);
            }
        }
        return returnMap;
    }

    public List<RuleType> getRuleTypes() {
        ApplicationDTO application = defineService.getApplicationDetails();
        if (application != null) {
            if (application.programmingLanguage == null || application.programmingLanguage.equals("")) {
                return generateDefaultRuleTypes();
            } else {
                return generateDefaultRuleTypes(application.programmingLanguage);
            }
        }
        return Collections.emptyList();
    }

    //Return all the default instances of Rule
    private List<RuleType> generateDefaultRuleTypes() {
        List<RuleType> rules = new ArrayList<RuleType>();
        setViolationTypeFactory();
        for (Entry<String, CategoryKeyClassDTO> set : mainRuleTypes.entrySet()) {
            try {
                Class<RuleType> ruletypeClass = set.getValue().getRuleClass();
                String categoryKey = set.getValue().getCategoryKey();
                if (ruletypeClass != null) {
                    RuleType rule = generateRuleObject(ruletypeClass, set.getKey(), categoryKey, new ArrayList<ViolationType>());
                    rules.add(rule);
                }
            } catch (RuleInstantionException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return rules;
    }

    //Depending on the language give instance of Rule + violationtypes
    private List<RuleType> generateDefaultRuleTypes(String language) {
        setViolationTypeFactory(language);

        List<RuleType> rules = new ArrayList<RuleType>();

        for (Entry<String, CategoryKeyClassDTO> set : mainRuleTypes.entrySet()) {
            try {
                Class<RuleType> ruletypeClass = set.getValue().getRuleClass();
                String categoryKey = set.getValue().getCategoryKey();
                if (ruletypeClass != null) {
                    List<ViolationType> violationlist = Collections.emptyList();
                    if (violationtypefactory != null) {
                        violationlist = violationtypefactory.createViolationTypesByRule(set.getKey());
                    }

                    RuleType rule = generateRuleObject(ruletypeClass, set.getKey(), categoryKey, violationlist);
                    rules.add(rule);

                }
            } catch (RuleInstantionException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return rules;
    }

    private void setViolationTypeFactory(String language) {
        this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(language, configuration);
        if (violationtypefactory == null) {
            logger.debug("Warning language does not exists: " + language);
        }
    }

    public RuleType generateRuleType(String ruleKey) throws RuleInstantionException {
        setViolationTypeFactory();

        CategoryKeyClassDTO categoryKeyClass = allRuleTypes.get(ruleKey);
        if (categoryKeyClass != null) {
            Class<RuleType> ruletypeClass = categoryKeyClass.getRuleClass();
            String categoryKey = categoryKeyClass.getCategoryKey();
            if (ruletypeClass != null) {
                List<ViolationType> violationtypes = Collections.emptyList();
                if (violationtypefactory != null) {
                    violationtypes = violationtypefactory.createViolationTypesByRule(ruleKey);
                }
                return generateRuleObject(ruletypeClass, ruleKey, categoryKey, violationtypes);
            }
        } else {
            logger.warn(String.format("Key: %s does not exists", ruleKey));
        }
        throw new RuleTypeNotFoundException(ruleKey);
    }

    private void setViolationTypeFactory() {
        this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(configuration);
        if (violationtypefactory == null) {
            logger.debug("Warning no language specified in define component");
        }
    }

    private RuleType generateRuleObject(Class<RuleType> ruleClass, String key, String categoryKey, List<ViolationType> violationtypes) throws RuleInstantionException {
        try {
            RuleType rootRule = (RuleType) ruleClass.getConstructor(String.class, String.class, List.class, Severity.class).newInstance(key, categoryKey, violationtypes, createSeverity(key));
            List<RuleType> exceptionRuletypes = new ArrayList<RuleType>();
            for (RuleTypes ruletype : rootRule.getExceptionRuleKeys()) {
                final RuleType generatedRuleType = generateRuleTypeWithoutExceptionRules(ruletype.toString());
                if (generatedRuleType != null) {
                    exceptionRuletypes.add(generatedRuleType);
                }
            }
            rootRule.setExceptionrules(exceptionRuletypes);
            return rootRule;
        } catch (IllegalArgumentException e) {
            ExceptionOccured(e);
        } catch (SecurityException e) {
            ExceptionOccured(e);
        } catch (InstantiationException e) {
            ExceptionOccured(e);
        } catch (IllegalAccessException e) {
            ExceptionOccured(e);
        } catch (InvocationTargetException e) {
            ExceptionOccured(e);
        } catch (NoSuchMethodException e) {
            ExceptionOccured(e);
        }
        throw new RuleInstantionException(key);
    }

    private RuleType generateRuleTypeWithoutExceptionRules(String ruleKey) throws RuleInstantionException {
        CategoryKeyClassDTO categoryKeyClass = allRuleTypes.get(ruleKey);
        if (categoryKeyClass != null) {
            Class<RuleType> ruletypeClass = categoryKeyClass.getRuleClass();
            String categoryKey = categoryKeyClass.getCategoryKey();
            if (ruletypeClass != null) {
                List<ViolationType> violationtypes = Collections.emptyList();
                if (violationtypefactory != null) {
                    violationtypes = violationtypefactory.createViolationTypesByRule(ruleKey);
                }
                return generateRuleObjectWithoutExceptionRules(ruletypeClass, ruleKey, categoryKey, violationtypes);
            }
        } else {
            logger.warn(String.format("Key: %s does not exists", ruleKey));
        }
        throw new RuleInstantionException(ruleKey);
    }

    private RuleType generateRuleObjectWithoutExceptionRules(Class<RuleType> ruleClass, String key, String categoryKey, List<ViolationType> violationtypes) throws RuleInstantionException {
        try {
            return (RuleType) ruleClass.getConstructor(String.class, String.class, List.class, Severity.class).newInstance(key, categoryKey, violationtypes, createSeverity(key));
        } catch (IllegalArgumentException e) {
            ExceptionOccured(e);
        } catch (SecurityException e) {
            ExceptionOccured(e);
        } catch (InstantiationException e) {
            ExceptionOccured(e);
        } catch (IllegalAccessException e) {
            ExceptionOccured(e);
        } catch (InvocationTargetException e) {
            ExceptionOccured(e);
        } catch (NoSuchMethodException e) {
            ExceptionOccured(e);
        }
        throw new RuleInstantionException(key);
    }

    private void ExceptionOccured(Exception e) {
        logger.error(e.getMessage(), e);
    }

    private Severity createSeverity(String ruleTypeKey) {
        try {
            return configuration.getSeverityFromKey(defineService.getApplicationDetails().programmingLanguage, ruleTypeKey);
        } catch (SeverityNotFoundException e) {
            DefaultSeverities defaultSeverity = getCategoryKeyClassDTO(ruleTypeKey);
            if (defaultSeverity != null) {
                return configuration.getSeverityByName(defaultSeverity.toString());
            }
        }
        return null;
    }

    private DefaultSeverities getCategoryKeyClassDTO(String ruleTypeKey) {
        for (CategoryKeyClassDTO ruleType : allRuleTypes.values()) {
            if (ruleType.getRuleClass().getSimpleName().toLowerCase().replace("rule", "").equals(ruleTypeKey.toLowerCase())) {
                return ruleType.getDefaultSeverity();
            }
        }
        return null;
    }
}