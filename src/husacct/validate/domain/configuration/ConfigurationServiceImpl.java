package husacct.validate.domain.configuration;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.validate.domain.exception.SeverityChangedException;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public final class ConfigurationServiceImpl extends Observable {

	private final SeverityConfigRepository severityConfig;
	private final SeverityPerTypeRepository severityPerTypeRepository;
	private final ViolationRepository violationRepository;
	private final RuleTypesFactory ruletypeFactory;
	private final ViolationHistoryRepository violationHistoryRepository;
	private final ActiveViolationTypesRepository activeViolationTypesRepository;
	// TreeMap dependenciesOnFromTo has as first key pathFrom, as second key pathTo, and as value a list of dependencies.
	// Note: To find inner classes to, make use of tailMap() instead of get(). tailMap() may return a Map of values!
	private TreeMap<String, TreeMap<String, ArrayList<DependencyDTO>>> dependenciesOnFromTo; 
	
	private Logger logger = Logger.getLogger(ConfigurationServiceImpl.class);


	public ConfigurationServiceImpl() {
		this.severityConfig = new SeverityConfigRepository();
		this.violationRepository = new ViolationRepository();
		this.severityPerTypeRepository = new SeverityPerTypeRepository(this.ruletypeFactory = new RuleTypesFactory(this), this);
		this.activeViolationTypesRepository = new ActiveViolationTypesRepository(this.ruletypeFactory);
		this.severityPerTypeRepository.initializeDefaultSeverities();
		this.violationHistoryRepository = new ViolationHistoryRepository();
		this.dependenciesOnFromTo = new TreeMap<String, TreeMap<String, ArrayList<DependencyDTO>>>();
	}

	public void clearViolations() {
		violationRepository.clear();
	}

	public int getSeverityValue(Severity severity) {
		return severityConfig.getSeverityValue(severity);
	}

	public List<Severity> getAllSeverities() {
		return severityConfig.getAllSeverities();
	}

	/**
	 * @throws SeverityChangedException
	 */
	public void setSeverities(List<Severity> severities) {
		severityConfig.setSeverities(severities);
		notifyServiceListeners();
	}

	public Severity getSeverityByName(String severityName) {
		return severityConfig.getSeverityByName(severityName);
	}

	public SimpleEntry<Calendar, List<Violation>> getAllViolations() {
		return violationRepository.getAllViolations();
	}

	public void addViolations(List<Violation> violations) {
		violationRepository.addViolation(violations);
		setChanged();
		notifyObservers();
		notifyServiceListeners();
	}
	
	public void filterAndSortAllViolations(){
		violationRepository.filterAndSortAllViolations();
	}

	public HashMap<String, HashMap<String, Severity>> getAllSeveritiesPerTypesPerProgrammingLanguages() {
		return severityPerTypeRepository.getSeveritiesPerTypePerProgrammingLanguage();
	}

	public void setSeveritiesPerTypesPerProgrammingLanguages(HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages) {
		severityPerTypeRepository.setSeverityMap(severitiesPerTypesPerProgrammingLanguages);
		notifyServiceListeners();
	}

	public void setSeveritiesPerTypesPerProgrammingLanguages(String language, HashMap<String, Severity> severitiesPerTypesPerProgrammingLanguages) {
		severityPerTypeRepository.setSeverityMap(language, severitiesPerTypesPerProgrammingLanguages);
		notifyServiceListeners();
	}

	public Severity getSeverityFromKey(String language, String key) {
		return severityPerTypeRepository.getSeverity(language, key);
	}

	public void restoreAllKeysToDefaultSeverities(String language) {
		severityPerTypeRepository.restoreAllKeysToDefaultSeverities(language);
		setChanged();
		notifyObservers();
		notifyServiceListeners();
	}

	public void restoreKeyToDefaultSeverity(String language, String key) {
		severityPerTypeRepository.restoreKeyToDefaultSeverity(language, key);
		setChanged();
		notifyObservers();
		notifyServiceListeners();
	}

	public void restoreSeveritiesToDefault() {
		severityConfig.restoreToDefault();
		notifyServiceListeners();
	}

	public RuleTypesFactory getRuleTypesFactory() {
		return ruletypeFactory;
	}

	public List<ViolationHistory> getViolationHistory() {
		return violationHistoryRepository.getViolationHistory();
	}

	public void setViolationHistory(List<ViolationHistory> list) {
		violationHistoryRepository.setViolationHistories(list);
	}

	public Map<String, List<ActiveRuleType>> getActiveViolationTypes() {
		return activeViolationTypesRepository.getActiveViolationTypes();
	}

	public void setActiveViolationTypes(String programmingLanguage, List<ActiveRuleType> activeViolationTypes) {
		activeViolationTypesRepository.setActiveViolationTypes(programmingLanguage, activeViolationTypes);
	}

	public void setActiveViolationTypes(Map<String, List<ActiveRuleType>> activeViolationTypes) {
		activeViolationTypesRepository.setActiveViolationTypes(activeViolationTypes);
	}

	public void createHistoryPoint(String description) {
		SimpleEntry<Calendar, List<Violation>> violationsResult = getAllViolations();
		Calendar date = violationsResult.getKey();
		List<Violation> violations = violationsResult.getValue();

		ViolationHistory violationHistory = new ViolationHistory(violations, getAllSeverities(), date, description);
		violationHistoryRepository.addViolationHistory(violationHistory);
	}

	public void removeViolationHistory(Calendar date) {
		violationHistoryRepository.removeViolationHistory(date);
	}

	public ViolationHistory getViolationHistoryByDate(Calendar date) {
		return violationHistoryRepository.getViolationHistoryByDate(date);
	}

	public List<ViolationHistory> getViolationHistories() {
		return violationHistoryRepository.getViolationHistory();
	}

	public boolean isViolationEnabled(String programmingLanguage, String ruleTypeKey, String violationTypeKey) {
		if (activeViolationTypesRepository != null) {
			getActiveViolationTypes();
			return activeViolationTypesRepository.isEnabled(programmingLanguage, ruleTypeKey, violationTypeKey);
		} else {
			return true;
		}
	}

	public void attachViolationHistoryRepositoryObserver(Observer observer) {
		violationHistoryRepository.addObserver(observer);
	}

	private void notifyServiceListeners() {
		ServiceProvider.getInstance().getValidateService().notifyServiceListeners();
	}
	
    // Fill HashMap dependenciesOnFromTo 
	public void initializeDependencyHashMap(){
		this.dependenciesOnFromTo = new TreeMap<String, TreeMap<String, ArrayList<DependencyDTO>>>();
		DependencyDTO[] dependencies = ServiceProvider.getInstance().getAnalyseService(). getAllDependencies();
		TreeMap<String, ArrayList<DependencyDTO>> toMap;
		try{
	        for(DependencyDTO dependency : dependencies) {
            	String uniqueNameFrom = dependency.from;
            	String uniqueNameTo = dependency.to;
            	if(dependenciesOnFromTo.containsKey(uniqueNameFrom)){
            		toMap = dependenciesOnFromTo.get(uniqueNameFrom);
            		if(toMap.containsKey(uniqueNameTo)){
            			// Check if there is a dependency with the same Type and LineNr in the ArrayList
            			ArrayList<DependencyDTO> matchingDependencies = toMap.get(uniqueNameTo);
            			boolean found = false;
            			for(DependencyDTO matchingDependency : matchingDependencies){
	            			if(matchingDependency.type == dependency.type){
	            				if(matchingDependency.lineNumber == dependency.lineNumber){
	            					// Do nothing, dependency already exists
	            					found = true;
	            					break;
	            				}
            				}
            			}
	            		if(!found){
	            			// The dependency does not exist yet under the from-key and to-key, so add it.
	            			matchingDependencies.add(dependency);
            			}
        			}
            		else{
            			// No toMap exists for the to-key, so create it.
            			ArrayList<DependencyDTO> newList = new ArrayList<DependencyDTO>();
            			newList.add(dependency);
            			toMap.put(uniqueNameTo, newList);
            		}
            	}
            	else{
            		// No map exists for the from-key, so add it.
        			ArrayList<DependencyDTO> newList = new ArrayList<DependencyDTO>();
        			newList.add(dependency);
            		toMap = new TreeMap<String, ArrayList<DependencyDTO>>();
            		toMap.put(uniqueNameTo, newList);            		
	            	dependenciesOnFromTo.put(uniqueNameFrom, toMap);
            	}
	        }
	        
	        
		} catch(Exception e) {
	        this.logger.warn("Exception may result in incomplete dependency list. Exception:  " + e);
	        //e.printStackTrace();
		}

	}
	
	public ArrayList<DependencyDTO> getDependenciesFromTo(String classPathFrom, String classPathTo){
		ArrayList<DependencyDTO> returnValue = new ArrayList<DependencyDTO>();
		// Select all dependencies within TreeMap dependenciesOnFromTo whose pathFrom equals classPathFrom
		TreeMap<String, ArrayList<DependencyDTO>> fromMap = dependenciesOnFromTo.get(classPathFrom);
		// Select all dependencies within fromMap whose pathTo starts with classPathTo
		if(fromMap != null ){	
			ArrayList<DependencyDTO> dependencyList = fromMap.get(classPathTo);
			if(dependencyList != null){
				returnValue.addAll(dependencyList);
			}
		}	
		
		/* Old trial:	
		// Select all dependencies within TreeMap dependenciesOnFromTo whose pathFrom starts with classPathFrom
		SortedMap<String, TreeMap<String, ArrayList<DependencyDTO>>> fromMaps = dependenciesOnFromTo.tailMap(classPathFrom);
		Set<String> pathFromKeys = fromMaps.keySet();
		for(String pathFrom : pathFromKeys){
			TreeMap<String, ArrayList<DependencyDTO>> fromMap = fromMaps.get(pathFrom);
			// Select all dependencies within fromMap whose pathTo starts with classPathTo
			SortedMap<String, ArrayList<DependencyDTO>> potentialToMaps = fromMap.tailMap(classPathTo);
			Set<String> pathToKeys = potentialToMaps.keySet(); 
			for(String pathTo : pathToKeys){
				ArrayList<DependencyDTO> dependencyList = potentialToMaps.get(pathTo);
				returnValue.addAll(dependencyList);
			}
			*/
		return returnValue;
	}
}