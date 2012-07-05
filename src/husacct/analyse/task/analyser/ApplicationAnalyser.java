package husacct.analyse.task.analyser;

import husacct.ServiceProvider;
import husacct.analyse.task.analyser.csharp.CSharpAnalyser;
import husacct.analyse.task.analyser.java.JavaAnalyser;
import husacct.common.dto.ApplicationDTO;
import husacct.define.IDefineService;

import java.util.List;

public class ApplicationAnalyser {
	
	private AnalyserBuilder builder;
	
	public ApplicationAnalyser(){
		this.builder = new AnalyserBuilder();
	}
	
	@Deprecated /*Will soon be removed due to an interface-change of the Analyse Component*/
	public void analyseApplication() {				
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService definitionService = provider.getDefineService();
		ApplicationDTO appDto = definitionService.getApplicationDetails();
		String language = appDto.programmingLanguage;
		String[] paths = appDto.paths;
		analyseApplication(paths, language);
	}
	
	public void analyseApplication(String[] paths, String programmingLanguage) {				
		AbstractAnalyser analyser = builder.getAnalyser(programmingLanguage);
		SourceFileFinder sourceFileFinder = new SourceFileFinder();
		String sourceFileExtension = getExtensionForLanguage(programmingLanguage);
		for(String workspacePath: paths){
			try{
				List<MetaFile> fileData = sourceFileFinder.getFileInfoFromProject(workspacePath, sourceFileExtension);
				for(MetaFile fileInfo: fileData){
					analyser.generateModelFromSource(fileInfo.getPath());
				}
			}
			catch(Exception e){
				//TODO Generate Custom Exception
			}
		}
		analyser.connectDependencies();
	}
	
	public boolean isZip(String path){
		return path.endsWith(".zip");
	}

	public String[] getAvailableLanguages() {
		String[] availableLanguages = new String[]{
			new JavaAnalyser().getProgrammingLanguage(),
			new CSharpAnalyser().getProgrammingLanguage()
		};
		return availableLanguages;
	}
	
	private String getExtensionForLanguage(String language){
		AbstractAnalyser analyser = builder.getAnalyser(language);
		return analyser.getFileExtension();
	}
	
}