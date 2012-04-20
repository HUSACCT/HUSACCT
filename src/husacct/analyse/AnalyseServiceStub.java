package husacct.analyse;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AnalyseServiceStub implements IAnalyseService{

	private HashMap<String, ArrayList<Object>> analysed;
	public AnalyseServiceStub(){

		ArrayList<AnalysedModuleDTO> foursquareSub = new ArrayList<AnalysedModuleDTO>();
		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Account", "Account", "class"));
		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Friends", "Friends", "class"));
		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Map", "Map", "class"));
		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.History", "History", "class"));

		ArrayList<AnalysedModuleDTO> latitudeSub = new ArrayList<AnalysedModuleDTO>();
		latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Account", "Account", "class"));
		latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Friends", "Friends", "class"));
		latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Map", "Map", "class"));		

		ArrayList<AnalysedModuleDTO> locationbasedSub = new ArrayList<AnalysedModuleDTO>();
		locationbasedSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare", "foursquare", "package", foursquareSub));
		locationbasedSub.add(new AnalysedModuleDTO("domain.locationbased.latitude", "latitude", "package", latitudeSub));

		ArrayList<AnalysedModuleDTO> domainSub = new ArrayList<AnalysedModuleDTO>();
		domainSub.add(new AnalysedModuleDTO("domain.locationbased", "locationbased", "package", locationbasedSub));
		domainSub.add(new AnalysedModuleDTO("domain.locationbased.Account", "Account", "class"));
		domainSub.add(new AnalysedModuleDTO("domain.locationbased.Friends", "Friends", "class"));
		domainSub.add(new AnalysedModuleDTO("domain.locationbased.Map", "Map", "class"));
		domainSub.add(new AnalysedModuleDTO("domain.locationbased.History", "History", "class"));

		ArrayList<AnalysedModuleDTO> foursquare1Sub = new ArrayList<AnalysedModuleDTO>();
		foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.AccountDAO", "AccountDAO", "class"));
		foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", "FriendsDAO", "class"));
		foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.IMap", "IMap", "class"));
		foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", "HistoryDAO", "class"));

		ArrayList<AnalysedModuleDTO> locationbased1Sub = new ArrayList<AnalysedModuleDTO>();
		locationbased1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare", "foursquare", "package", foursquare1Sub));

		ArrayList<AnalysedModuleDTO> socialmediaSub = new ArrayList<AnalysedModuleDTO>();
		socialmediaSub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased", "locationbased", "package", locationbased1Sub));

		ArrayList<AnalysedModuleDTO> infrastructureSub = new ArrayList<AnalysedModuleDTO>();
		infrastructureSub.add(new AnalysedModuleDTO("infrastructure.socialmedia", "socialmedia", "package", socialmediaSub));

		ArrayList<AnalysedModuleDTO> analysedSub = new ArrayList<AnalysedModuleDTO>();
		analysedSub.add(new AnalysedModuleDTO("domain", "domain", "package", domainSub));
		analysedSub.add(new AnalysedModuleDTO("infrastructure", "infrastructure", "package", infrastructureSub));

		AnalysedModuleDTO analysedModules = new AnalysedModuleDTO("", "root", "package", analysedSub);
		analysed = new HashMap<String, ArrayList<Object>>();

		List<AnalysedModuleDTO> rootElement = new ArrayList<AnalysedModuleDTO>();
		rootElement.add(analysedModules);
		GenerateHashmap(rootElement);


		addDependency(new DependencyDTO("domain.locationbased.foursquare.History", "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", "Extends", 10));
		addDependency(new DependencyDTO("domain.locationbased.latitude.Account", "infrastructure.socialmedia.locationbased.latitude.AccountDAO", "InvocConstructor", 11));
		addDependency(new DependencyDTO("domain.locationbased.latitude.Friends", "infrastructure.socialmedia.locationbased.latitude.FriendsDAO", "Extends", 10));
		addDependency(new DependencyDTO("domain.locationbased.foursquare.Map", "infrastructure.socialmedia.locationbased.foursquare.IMap", "Extends", 10));
		addDependency(new DependencyDTO("domain.locationbased.foursquare.Account", "infrastructure.socialmedia.locationbased.foursquare.AccountDAO", "InvocConstructor", 10));
		addDependency(new DependencyDTO("domain.locationbased.foursquare.Friends", "infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", "Extends", 10));
		addDependency(new DependencyDTO("domain.locationbased.latitude.Map", "infrastructure.socialmedia.locationbased.latitude.IMap", "Implements", 10));

	}

	private void GenerateHashmap(List<AnalysedModuleDTO> subModules) {
		if(subModules != null){
			for(AnalysedModuleDTO currentModule : subModules){
				ArrayList<Object> objecten = new ArrayList<Object>();
				ArrayList<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();
				objecten.add(currentModule);
				objecten.add(dependencies);

				analysed.put(currentModule.uniqueName, objecten);
				GenerateHashmap(currentModule.subModules);
			}
		}
	}

	private void addDependency(DependencyDTO dependency){
		ArrayList<Object> getElement = analysed.get(dependency.from);

		ArrayList<DependencyDTO> dependencies = (ArrayList<DependencyDTO>) getElement.get(1);
		dependencies.add(dependency);


	}



	@Override
	public void analyseApplication() {
		// TODO Auto-generated method stub

	}

	@Override
	public DependencyDTO[] getDependency(String from, String to) {

		ArrayList<DependencyDTO> allDependencies = new ArrayList<DependencyDTO>();

		for(String s : analysed.keySet()){
			if(s.indexOf(from) == -1){
				continue;
			}

			ArrayList<Object> currentElement = analysed.get(s);
			for(DependencyDTO dependency: (ArrayList<DependencyDTO>) currentElement.get(1)){
				if(dependency.to.indexOf(to) != -1){
					allDependencies.add(dependency);
				}
			}
		}


		if(allDependencies.size() != 0){
			DependencyDTO[] dependencyDTO = new DependencyDTO[allDependencies.size()];

			int iterator = 0;
			for(DependencyDTO d : allDependencies){
				dependencyDTO[iterator] = d;
				iterator++;
			}

			return dependencyDTO;


		}

		return new DependencyDTO[0];
	}

	@Override
	public DependencyDTO[] getDependency(String from) {
	
		ArrayList<DependencyDTO> allDependencies = new ArrayList<DependencyDTO>();

		for(String s : analysed.keySet()){
			if(s.indexOf(from) == -1){
				continue;
			}
			
			ArrayList<Object> currentElement = analysed.get(s);
			for(DependencyDTO dependency: (ArrayList<DependencyDTO>) currentElement.get(1)){
				allDependencies.add(dependency);
			}
		}
		
		DependencyDTO[] matchDependency = new DependencyDTO[allDependencies.size()];
		int iterator = 0;
		for(DependencyDTO d : allDependencies){
			matchDependency[iterator] = d;
			iterator++;
		}
		
		return matchDependency;
	}

	@Override
	public String[] getAvailableLanguages() {
		String[] languages = {"Java", "C#"};
		return languages;
	}

	@Override
	public AnalysedModuleDTO[] getRootModules() {
		AnalysedModuleDTO rootElement = (AnalysedModuleDTO) analysed.get("").get(0);

		AnalysedModuleDTO[] returnModules = new AnalysedModuleDTO[rootElement.subModules.size()];

		int iterator = 0;
		for(AnalysedModuleDTO module : rootElement.subModules){
			returnModules[iterator] = module;
//			module.subModules = new ArrayList<AnalysedModuleDTO>();
			iterator++;
		}

		return returnModules;
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from) {
		AnalysedModuleDTO getElement = (AnalysedModuleDTO) analysed.get(from).get(0);

		AnalysedModuleDTO[] modules = new AnalysedModuleDTO[getElement.subModules.size()];

		int iterator = 0;
		for(AnalysedModuleDTO module : getElement.subModules){
			modules[iterator] = module;
			iterator++;
		}

		return modules;
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
		int currentDepth = 0;


		if(depth == 1){
			return this.getChildModulesInModule(from);
		}

		AnalysedModuleDTO getElement = (AnalysedModuleDTO) analysed.get(from).get(0);

		AnalysedModuleDTO[] modules = new AnalysedModuleDTO[getElement.subModules.size()];

		int iterator = 0;
		for(AnalysedModuleDTO module : getElement.subModules){
			modules[iterator] = module;
			iterator++;
		}

		currentDepth = 1;
		AnalysedModuleDTO[] rightDepthModules = modules;

		while(currentDepth != depth){


			rightDepthModules = NextDepth(rightDepthModules);
			currentDepth++;
		}

//		for(AnalysedModuleDTO m : rightDepthModules){
//			m.subModules = new ArrayList<AnalysedModuleDTO>();
//		}
		return modules;
	}

	@Override
	public AnalysedModuleDTO getParentModuleForModule(String child) {
		if(child.indexOf(".") == -1){
			return new AnalysedModuleDTO("", "", "");
		}

		String[] pathSplitted = child.split("\\.");
		String parentPath = pathSplitted[0];
		for(int iterator = 1; iterator < pathSplitted.length-1; iterator++){
			if(iterator != pathSplitted.length-1){		
				parentPath += "." + pathSplitted[iterator];
			}
		}

		return (AnalysedModuleDTO) analysed.get(parentPath).get(0);
	}


	private AnalysedModuleDTO[] NextDepth(AnalysedModuleDTO[] modules){
		List<AnalysedModuleDTO> test = new ArrayList<AnalysedModuleDTO>();
		for(AnalysedModuleDTO module : modules){		
			if(module.subModules != null){
				for (AnalysedModuleDTO submodule : module.subModules) {
					test.add(submodule);
				}
			}
		}

		AnalysedModuleDTO[] depthModules = new AnalysedModuleDTO[test.size()];
		int iterator = 0;
		for(AnalysedModuleDTO save : test){
			depthModules[iterator] = save;
			iterator++;
		}

		return depthModules;
	}
}