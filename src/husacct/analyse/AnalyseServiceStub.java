package husacct.analyse;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.services.IServiceListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public class AnalyseServiceStub implements IAnalyseService {

    private HashMap<String, ArrayList<Object>> analysed;

    public AnalyseServiceStub() {
        generateModule();
        //printHashmap(analysed.get("").get(0));
    }

    private void GenerateHashmap(List<AnalysedModuleDTO> subModules) {
        if (subModules != null) {
            for (AnalysedModuleDTO currentModule : subModules) {
                ArrayList<Object> objecten = new ArrayList<Object>();
                ArrayList<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();
                objecten.add(currentModule);
                objecten.add(dependencies);

                analysed.put(currentModule.uniqueName, objecten);
                GenerateHashmap(currentModule.subModules);
            }
        }
    }

    private void addDependency(DependencyDTO dependency) {
        ArrayList<Object> getElement = analysed.get(dependency.from);
        ArrayList<DependencyDTO> dependencies = (ArrayList<DependencyDTO>) getElement.get(1);
        dependencies.add(dependency);
    }

    @Override
    public void analyseApplication(String[] paths, String programmingLanguage) {
    }

    @Override
    public void analyseApplication(ProjectDTO project) {
    }

    @Override
    public DependencyDTO[] getDependencies(String from, String to) {

        ArrayList<DependencyDTO> allDependencies = new ArrayList<DependencyDTO>();

        for (String s : analysed.keySet()) {
            if (s.indexOf(from) == -1) {
                continue;
            }

            ArrayList<Object> currentElement = analysed.get(s);
            for (DependencyDTO dependency : (ArrayList<DependencyDTO>) currentElement.get(1)) {
                if (dependency.to.indexOf(to) != -1) {
                    allDependencies.add(dependency);
                }
            }
        }


        if (allDependencies.size() != 0) {
            DependencyDTO[] dependencyDTO = new DependencyDTO[allDependencies.size()];

            int iterator = 0;
            for (DependencyDTO d : allDependencies) {
                dependencyDTO[iterator] = d;
                iterator++;
            }

            return dependencyDTO;


        }

        return new DependencyDTO[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public DependencyDTO[] getDependenciesFrom(String from) {

        ArrayList<DependencyDTO> allDependencies = new ArrayList<DependencyDTO>();

        for (String s : analysed.keySet()) {
            if (s.indexOf(from) == -1) {
                continue;
            }

            ArrayList<Object> currentElement = analysed.get(s);
            for (DependencyDTO dependency : (ArrayList<DependencyDTO>) currentElement.get(1)) {
                allDependencies.add(dependency);
            }
        }

        DependencyDTO[] matchDependency = new DependencyDTO[allDependencies.size()];
        int iterator = 0;
        for (DependencyDTO d : allDependencies) {
            matchDependency[iterator] = d;
            iterator++;
        }

        return matchDependency;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DependencyDTO[] getDependenciesTo(String to) {
        ArrayList<DependencyDTO> allDependencies = new ArrayList<DependencyDTO>();

        for (String key : analysed.keySet()) {
            ArrayList<Object> teest = analysed.get(key);

            for (DependencyDTO o : (ArrayList<DependencyDTO>) teest.get(1)) {

                if (o.to.indexOf(to) != -1) {
                    allDependencies.add(o);
                }
            }
        }

        if (allDependencies.size() <= 0) {
            return new DependencyDTO[0];
        }

        DependencyDTO[] dependencies = new DependencyDTO[allDependencies.size()];

        int iterator = 0;
        for (DependencyDTO d : allDependencies) {
            dependencies[iterator] = d;
            iterator++;
        }


        return dependencies;
    }

    public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter) {
        DependencyDTO[] dependencies = getDependencies(from, to);
        ArrayList<DependencyDTO> filtered = new ArrayList<DependencyDTO>();

        for (DependencyDTO dependency : dependencies) {
            for (String s : dependencyFilter) {
                if (dependency.type.equals(s)) {
                    filtered.add(dependency);
                }
            }
        }

        if (filtered.size() == 0) {
            return new DependencyDTO[0];
        }

        DependencyDTO[] filter = new DependencyDTO[filtered.size()];
        int iterator = 0;
        for (DependencyDTO d : filtered) {
            filter[iterator] = d;
            iterator++;
        }


        return filter;
    }

    public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter) {
        DependencyDTO[] dependencies = getDependenciesFrom(from);
        ArrayList<DependencyDTO> filtered = new ArrayList<DependencyDTO>();

        for (DependencyDTO dependency : dependencies) {
            for (String s : dependencyFilter) {
                if (dependency.type.equals(s)) {
                    filtered.add(dependency);
                }
            }
        }

        if (filtered.size() == 0) {
            return new DependencyDTO[0];
        }

        DependencyDTO[] filter = new DependencyDTO[filtered.size()];
        int iterator = 0;
        for (DependencyDTO d : filtered) {
            filter[iterator] = d;
            iterator++;
        }


        return filter;
    }

    public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter) {
        DependencyDTO[] dependencies = getDependenciesTo(to);
        ArrayList<DependencyDTO> filtered = new ArrayList<DependencyDTO>();

        for (DependencyDTO dependency : dependencies) {
            for (String s : dependencyFilter) {
                if (dependency.type.equals(s)) {
                    filtered.add(dependency);
                }
            }
        }

        if (filtered.size() == 0) {
            return new DependencyDTO[0];
        }

        DependencyDTO[] filter = new DependencyDTO[filtered.size()];
        int iterator = 0;
        for (DependencyDTO d : filtered) {
            filter[iterator] = d;
            iterator++;
        }


        return filter;
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
        for (AnalysedModuleDTO module : rootElement.subModules) {



            returnModules[iterator] = module;
            module.subModules = new ArrayList<AnalysedModuleDTO>();
            iterator++;
        }

        return returnModules;
    }

    @Override
    public AnalysedModuleDTO[] getChildModulesInModule(String from) {
        generateModule();

        if (analysed.get(from) == null) {
            return new AnalysedModuleDTO[0];
        }

        AnalysedModuleDTO getElement = (AnalysedModuleDTO) analysed.get(from).get(0);

        if (getElement.subModules == null) {
            return new AnalysedModuleDTO[0];
        }

        AnalysedModuleDTO[] modules = new AnalysedModuleDTO[getElement.subModules.size()];

        int iterator = 0;
        for (AnalysedModuleDTO module : getElement.subModules) {
            modules[iterator] = module;
            iterator++;
        }

        return modules;
    }

    @Override
    public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
        generateModule();
        int currentDepth = 0;

        if (depth == 0) {
            AnalysedModuleDTO[] modules = this.getChildModulesInModule(from);
            return modules;
        }

        if (depth == 1) {
            AnalysedModuleDTO[] modules = this.getChildModulesInModule(from);
            for (AnalysedModuleDTO module : modules) {
                module.subModules = new ArrayList<AnalysedModuleDTO>();
            }
            return modules;
        }

        if (analysed.get(from) == null) {
            return new AnalysedModuleDTO[0];
        }

        AnalysedModuleDTO getElement = (AnalysedModuleDTO) analysed.get(from).get(0);

        if (getElement.subModules == null) {
            return new AnalysedModuleDTO[0];
        }

        AnalysedModuleDTO[] modules = new AnalysedModuleDTO[getElement.subModules.size()];

        int iterator = 0;
        for (AnalysedModuleDTO module : getElement.subModules) {
            modules[iterator] = module;
            iterator++;
        }

        currentDepth = 1;
        AnalysedModuleDTO[] rightDepthModules = modules;

        while (currentDepth <= depth) {


            rightDepthModules = NextDepth(rightDepthModules);
            currentDepth++;
        }

        for (AnalysedModuleDTO m : rightDepthModules) {
            m.subModules = new ArrayList<AnalysedModuleDTO>();
        }
        return modules;
    }

    @Override
    public AnalysedModuleDTO getParentModuleForModule(String child) {
        generateModule();
        if (analysed.get(child) == null) {
            return new AnalysedModuleDTO("", "", "", "");
        }


        if (child.indexOf(".") == -1) {
            return new AnalysedModuleDTO("", "", "", "");
        }

        String[] pathSplitted = child.split("\\.");
        String parentPath = pathSplitted[0];
        for (int iterator = 1; iterator < pathSplitted.length - 1; iterator++) {
            if (iterator != pathSplitted.length - 1) {
                parentPath += "." + pathSplitted[iterator];
            }
        }

        AnalysedModuleDTO parentModule = (AnalysedModuleDTO) analysed.get(parentPath).get(0);
        for (AnalysedModuleDTO m : parentModule.subModules) {
            m.subModules = new ArrayList<AnalysedModuleDTO>();
        }

        return parentModule;
    }

    private AnalysedModuleDTO[] NextDepth(AnalysedModuleDTO[] modules) {
        List<AnalysedModuleDTO> test = new ArrayList<AnalysedModuleDTO>();
        for (AnalysedModuleDTO module : modules) {
            if (module.subModules != null) {
                for (AnalysedModuleDTO submodule : module.subModules) {
                    test.add(submodule);
                }
            }
        }

        AnalysedModuleDTO[] depthModules = new AnalysedModuleDTO[test.size()];
        int iterator = 0;
        for (AnalysedModuleDTO save : test) {
            depthModules[iterator] = save;
            iterator++;
        }

        return depthModules;
    }

    public void generateModule() {
        ArrayList<AnalysedModuleDTO> foursquareSub = new ArrayList<AnalysedModuleDTO>();
        foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Account", "Account", "class", "public"));
        foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Friends", "Friends", "class", "public"));
        foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Map", "Map", "class", "public"));
        foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.History", "History", "class", "public"));

        ArrayList<AnalysedModuleDTO> latitudeSub = new ArrayList<AnalysedModuleDTO>();
        latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Account", "Account", "class", "public"));
        latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Friends", "Friends", "class", "public"));
        latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Map", "Map", "class", "public"));

        ArrayList<AnalysedModuleDTO> locationbasedSub = new ArrayList<AnalysedModuleDTO>();
        locationbasedSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare", "foursquare", "package", "", foursquareSub));
        locationbasedSub.add(new AnalysedModuleDTO("domain.locationbased.latitude", "latitude", "package", "", latitudeSub));


        ArrayList<AnalysedModuleDTO> domainSub = new ArrayList<AnalysedModuleDTO>();
        domainSub.add(new AnalysedModuleDTO("domain.locationbased", "locationbased", "package", "", locationbasedSub));


        ArrayList<AnalysedModuleDTO> foursquare1Sub = new ArrayList<AnalysedModuleDTO>();
        foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.AccountDAO", "AccountDAO", "class", "public"));
        foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", "FriendsDAO", "abstract", "public"));
        foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.IMap", "IMap", "interface", "public"));
        foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", "HistoryDAO", "class", "public"));

        ArrayList<AnalysedModuleDTO> latitude1Sub = new ArrayList<AnalysedModuleDTO>();
        latitude1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.latitude.AccountDAO", "AccountDAO", "class", "public"));
        latitude1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.latitude.FriendsDAO", "FriendsDAO", "abstract", "public"));
        latitude1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.latitude.IMap", "IMap", "interface", "public"));

        ArrayList<AnalysedModuleDTO> locationbased1Sub = new ArrayList<AnalysedModuleDTO>();
        locationbased1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare", "foursquare", "package", "", foursquare1Sub));
        locationbased1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.latitude", "latitude", "package", "", latitude1Sub));

        ArrayList<AnalysedModuleDTO> socialmediaSub = new ArrayList<AnalysedModuleDTO>();
        socialmediaSub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased", "locationbased", "package", "", locationbased1Sub));

        ArrayList<AnalysedModuleDTO> infrastructureSub = new ArrayList<AnalysedModuleDTO>();
        infrastructureSub.add(new AnalysedModuleDTO("infrastructure.socialmedia", "socialmedia", "package", "", socialmediaSub));

        ArrayList<AnalysedModuleDTO> analysedSub = new ArrayList<AnalysedModuleDTO>();
        analysedSub.add(new AnalysedModuleDTO("domain", "domain", "package", "", domainSub));
        analysedSub.add(new AnalysedModuleDTO("infrastructure", "infrastructure", "package", "", infrastructureSub));

        AnalysedModuleDTO analysedModules = new AnalysedModuleDTO("", "root", "package", "", analysedSub);
        analysed = new HashMap<String, ArrayList<Object>>();

        ArrayList<AnalysedModuleDTO> rootElement = new ArrayList<AnalysedModuleDTO>();
        rootElement.add(analysedModules);
        GenerateHashmap(rootElement);


        addDependency(new DependencyDTO("domain.locationbased.foursquare.History", "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", "ExtendsAbstract", 10));
        addDependency(new DependencyDTO("domain.locationbased.latitude.Account", "infrastructure.socialmedia.locationbased.latitude.AccountDAO", "InvocConstructor", 11));
        addDependency(new DependencyDTO("domain.locationbased.latitude.Friends", "infrastructure.socialmedia.locationbased.latitude.FriendsDAO", "ExtendsConcrete", 10));
        addDependency(new DependencyDTO("domain.locationbased.foursquare.Map", "infrastructure.socialmedia.locationbased.foursquare.IMap", "ExtendsAbstract", 10));
        addDependency(new DependencyDTO("domain.locationbased.foursquare.Account", "infrastructure.socialmedia.locationbased.foursquare.AccountDAO", "InvocConstructor", 10));
        addDependency(new DependencyDTO("domain.locationbased.foursquare.Friends", "infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", "ExtendsAbstract", 10));
        addDependency(new DependencyDTO("domain.locationbased.latitude.Map", "infrastructure.socialmedia.locationbased.latitude.IMap", "Implements", 10));

    }

    @SuppressWarnings("unused")
    private void printHashmap(Object rootElement) {
        AnalysedModuleDTO element = (AnalysedModuleDTO) rootElement;

        int space = 100;
        int uniquelength = space - element.uniqueName.length();
        int total = (int) Math.ceil(uniquelength / 8);

        String tab = "";
        for (int i = 0; i < total; i++) {
            tab += "\t";
        }

        String info = element.uniqueName + tab + element.type + "\t" + element.visibility;

        System.out.println(info);

        ArrayList<AnalysedModuleDTO> subElementen = (ArrayList<AnalysedModuleDTO>) element.subModules;

        for (AnalysedModuleDTO d : subElementen) {
            printHashmap(d);
        }
    }

    @Override
    public boolean isAnalysed() {
        return false;
    }

    @Override
    public JInternalFrame getJInternalFrame() {
        return null;
    }

    @Override
    public AnalysedModuleDTO getModuleForUniqueName(String uniquename) {
        return null;
    }

    @Override
    public void addServiceListener(IServiceListener listener) {
    }

    @Override
    public DependencyDTO[] getAllDependencies() {
        return this.getDependencies("", "");
    }

    @Override
    public void notifyServiceListeners() {
    }

    @Override
    public void exportDependencies(String fullPath) {
    }
    
    @Override
	public String[] getExternalSystems(){
		return new String[0];
	}

	@Override
	public Element getWorkspaceData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadWorkspaceData(Element root) {
		// TODO Auto-generated method stub
		
	}
}