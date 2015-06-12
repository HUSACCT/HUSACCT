package husacct.define.presentation.utils;

import husacct.common.OSDetector;
import husacct.common.Resource;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class ReportToHTML {

	private static int ID = 1;
	private ModuleDomainService moduleService;
	private AppliedRuleDomainService ruleService;

	public ReportToHTML(){
		moduleService = new ModuleDomainService();
		ruleService = new AppliedRuleDomainService();

	}

	public void createReport() throws URISyntaxException{
		File htmlTemplateFile = new File(Resource.get(Resource.TEMPLATE_REPORT).toURI());
		String htmlString = "";
		try {
			htmlString = FileUtils.readFileToString(htmlTemplateFile);
		} catch (IOException e) {
			System.out.println("Reading failed. (Define -> Presentation: ReportToHTML.java)");
			e.printStackTrace();
		}
		String summary = getSummary();
		String content = getContent(moduleService.getRootModules());
		htmlString = htmlString.replace("%SUMMARY%", summary);
		htmlString = htmlString.replace("%CONTENT%", content);
		File file = new File(OSDetector.getAppFolder() + File.separator + "report.html");	
		try {
			FileUtils.writeStringToFile(file, htmlString);
			Desktop.getDesktop().browse(file.toURI());
		} catch (IOException e) {
			System.out.println("Writing failed. (Define -> Presentation: ReportToHTML.java)");
			e.printStackTrace();
		}		
	}

	/*
	 * For making a list with totals
	 */
	private String getSummary(){
		String summary = ""; 
		int modules = countModules(moduleService.getRootModules());
		int appliedRules = ruleService.getAllMainRules().length;
		int softwareUnits = countSoftwareUnits(moduleService.getRootModules());
		summary += makeli(modules+" Modules;");
		summary += makeli(appliedRules+" Applied Rules ("+ruleService.getAllEnabledMainRules().length+" enabled);");
		summary += makeli(softwareUnits+" Software Units.");
		return summary;
	}
	// These are all the root modules.
	private int countModules(ModuleStrategy[] modules){
		int counter = 0;
		for(int i = 0; i < modules.length; i++){
			counter += countModules(modules[i]);
		}
		return counter;
	}
	// Recursively search all modules
	private int countModules(ModuleStrategy module){
		int counter = 1;
		if(module.hasSubModules()){
			for(ModuleStrategy submodule : module.getSubModules()){
				counter += countModules(submodule);
			}
		}
		return counter;
	}
	// Put all software units per root module and further in one map.
	private int countSoftwareUnits(ModuleStrategy[] modules){
		int counter = 0;
		for(ModuleStrategy module : modules){
			counter += module.countSoftwareUnits();
		}
		return counter;
	}
	private String makeli(String content){
		return String.format("<li>%s</li>", content);
	}

	/*
	 * For making the content, a jQuery tree sorted on Modules
	 */
	private String getContent(ModuleStrategy[] rootModules){
		StringBuilder content = new StringBuilder();
		for(int i = 0; i < rootModules.length; i++){
			String moduleName = rootModules[i].getName();
			HashMap<String, String> softwareUnits = getSoftwareUnits(rootModules[i]);
			HashMap<String, Boolean> appliedRules = getAppliedRules(rootModules[i].getId());
			content.append(makeTableRow(moduleName, softwareUnits, appliedRules));
			int parentId = ID;
			ID++;
			if(rootModules[i].hasSubModules()){
				content.append(makeChildRow(parentId, rootModules[i].getSubModules()));
			}
		}
		return content.toString();
	}
	private HashMap<String, String> getSoftwareUnits(ModuleStrategy module){ 
		return module.getSoftwareUnitNames();
	}

	private HashMap<String, Boolean> getAppliedRules(long moduleId){
		HashMap<String, Boolean> appliedRules = new HashMap<String, Boolean>();
		for(AppliedRuleStrategy rule : ruleService.getAllMainRules()){
			if(rule.getModuleFrom().getId() == moduleId){
				appliedRules.put(rule.getRuleTypeKey(), rule.isEnabled());
			}			
		}
		return appliedRules;
	}
	
	private String makeTableRow(String moduleName, HashMap<String,String> softwareUnits, HashMap<String,Boolean> appliedRules){
		StringBuilder 	row 	= new StringBuilder();
		String 			su 		= makeSUList(softwareUnits); 
		String 			rules 	= makeRulesList(appliedRules); 
		
		row.append("<tr data-tt-id=\""+ID+"\">");
		row.append(makeTableData(moduleName));	  // Module
		row.append(makeTableData(su));			 // Software Units
		row.append(makeTableData(rules)); 		// Applied Rules
		row.append("</tr>");
		return row.toString();
	}
	private String makeTableData(String content){
		return String.format("<td>%s</td>", content);
	}
	private String makeChildRow(int parentId, ArrayList<ModuleStrategy> subModules){
		StringBuilder row = new StringBuilder();
		for(ModuleStrategy sub : subModules){
			HashMap<String, String> 	softwareUnits	= getSoftwareUnits(sub);
			HashMap<String, Boolean> 	appliedRules 	= getAppliedRules(sub.getId());
			String 						su 				= makeSUList(softwareUnits); 
			String 						rules 			= makeRulesList(appliedRules);
			
			row.append("<tr data-tt-id=\""+ID+"\" data-tt-parent-id=\""+parentId+"\">");
			row.append(makeTableData(sub.getName())); // Module
			row.append(makeTableData(su));			 // Software Units
			row.append(makeTableData(rules)); 		// Applied Rules
			row.append("</tr>");
			int thisId = ID;
			ID++;
			if(sub.hasSubModules()){
				row.append(makeChildRow(thisId, sub.getSubModules()));
			}
		}
		
		return row.toString();
	}
	
	private String makeSUList(HashMap<String, String> softwareUnits){
		StringBuilder list = new StringBuilder();
		Iterator<Map.Entry<String, String>> i = softwareUnits.entrySet().iterator(); 
		while(i.hasNext()){
		    String key = i.next().getKey();
		    list.append(key+"  ("+softwareUnits.get(key)+")");
		    if(i.hasNext()){
		    	list.append("<br/>");
		    }
		}
		return list.toString();
	}
	
	private String makeRulesList(HashMap<String, Boolean> appliedRules){
		StringBuilder list = new StringBuilder();
		Iterator<Map.Entry<String, Boolean>> i = appliedRules.entrySet().iterator(); 
		while(i.hasNext()){
		    String key = i.next().getKey();
		    if(appliedRules.get(key)){
		    	list.append("<span>"+key+"</span>");
		    }else{
		    	list.append("<span style=\"color: #C7C7C7; \">"+key+"</span>");
		    }
		    
		    if(i.hasNext()){
		    	list.append("<br/>");
		    }
		}
		return list.toString();
	}

}
