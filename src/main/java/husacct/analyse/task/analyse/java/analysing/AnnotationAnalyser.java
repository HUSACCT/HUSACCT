package husacct.analyse.task.analyse.java.analysing;

import java.util.EnumSet;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.task.analyse.java.parsing.JavaParser.AnnotationContext;

class AnnotationAnalyser {

    private String name;
    private String uniqueName;
    private int lineNumber;
    private IModelCreationService modelService = new FamixCreationServiceImpl();

    public AnnotationAnalyser(AnnotationContext annotation, String belongsToClass) {
        if ((annotation != null) && (belongsToClass != null) && !belongsToClass.equals("")) {
        	if (annotation.annotationName() != null) {
	        	name = annotation.annotationName().getText();
	        	lineNumber = annotation.getStart().getLine();
		    	if (!name.equals("") && !isSkippable(name)) { 
			        this.uniqueName = belongsToClass + "." + this.name; 
			        modelService.createAnnotation(belongsToClass, this.name, this.name, this.uniqueName, this.lineNumber, "");
		    	}
	    	}
        }
    }

    private boolean isSkippable(String type) {
		for(SkippedJavaAnnotationTypes skippedType : EnumSet.allOf(SkippedJavaAnnotationTypes.class)){
			if(skippedType.toString().equals(type)){
				return true;
			}
		}
		return false;    }
    
    enum SkippedJavaAnnotationTypes {
    	
    	Override ("Override"),
    	Author ("Author"),
    	SuppressWarnings ("SuppressWarnings"),
    	Deprecated ("Deprecated"),
    	SafeVarargs ("SafeVarargs"),
    	FunctionalInterface ("FunctionalInterface"),
    	Interned ("Interned"),
    	NonNull ("NonNull"),
    	Readonly ("Readonly");
     	
    	private String type;
    	
    	private SkippedJavaAnnotationTypes(String type){
    		this.type = type;
    	}
    	
    	@Override
    	public String toString(){
    		return type;
    	}
    }	

}
