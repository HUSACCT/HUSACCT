package husacct.analyse.task.analyse.java.analysing;

import husacct.analyse.task.analyse.java.parsing.JavaParser.FormalParameterContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.FormalParameterListContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.TypeTypeContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.VariableModifierContext;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ParameterAnalyser extends JavaGenerator {

    private String belongsToMethod; //this includes the signature of the method (uniqueName)
    private String belongsToClass;
    private String declareName = "";
    private String declareType = "";
	private int lineNumber = 0;
    private String uniqueName;
    private String paramTypesInSignature = ""; // Builds up to e.g. (String,int). Used to identify methods with the same name, but different parameters. 

    private boolean nameFound = false;
    private boolean declareTypeFound = false;
    private ArrayList<ArrayList<Object>> parameterQueue; // The parameters need to be stored, until signature has been build up completely.
    private Logger logger = Logger.getLogger(ParameterAnalyser.class);
    
    public String generateParameterObjects(FormalParameterListContext parametersList, String belongsToMethod, String belongsToClass) {
        this.parameterQueue = new ArrayList<>();
        this.belongsToMethod = belongsToMethod;
        this.belongsToClass = belongsToClass;
        /* Test helper
       	if (this.belongsToClass.equals("domain.direct.violating.DeclarationParameter_GenericType_OneTypeParameter")){
    		//if (belongsToMethod.contains("performExternalScript")) {
    				boolean breakpoint1 = true;
    		//}
    	} */

        try{
	        if (parametersList.formalParameter() != null) {
	    		for (FormalParameterContext parameter : parametersList.formalParameter()) {
	    			if (parameter.variableModifier() != null) {
	    				dispatchAnnotation(parameter.variableModifier());
	    			}
	    			if (parameter.variableDeclaratorId() != null && parameter.variableDeclaratorId().Identifier() != null) {
	    				setName(parameter.variableDeclaratorId().Identifier());
	    			}
	    			if (parameter.typeType() != null) {
	    				setTypeAndAddToQue(parameter.typeType());
	    			}
	    		}
	    	}
	    	if (parametersList.lastFormalParameter() != null) {
	    		if (parametersList.lastFormalParameter().variableModifier() != null) {
	    			dispatchAnnotation(parametersList.lastFormalParameter().variableModifier());
	    		}
	    		if (parametersList.lastFormalParameter().variableDeclaratorId() != null && 
	    				parametersList.lastFormalParameter().variableDeclaratorId().Identifier() != null) {
	    			setName(parametersList.lastFormalParameter().variableDeclaratorId().Identifier());
	    		}
	    		if (parametersList.lastFormalParameter().typeType() != null) {
	    			setTypeAndAddToQue(parametersList.lastFormalParameter().typeType());
	    		}
	    	}
	        
	        if (parametersList != null) {
			    writeParametersToDomain();
			}
    	}
    	catch (Exception e) {
    		logger.warn(" Exception while processing: " + belongsToClass + e.getMessage());
    	}
        return paramTypesInSignature;
    }

    private void dispatchAnnotation(List<VariableModifierContext> modifierList) {
		for (VariableModifierContext modifier : modifierList) {
			if (modifier.annotation() != null) {
				new AnnotationAnalyser(modifier.annotation(), belongsToClass);
			}
		}
    }
    
    private void setName(TerminalNode identifier) {
		this.declareName = identifier.getText();
        if ((this.declareName != null)&& (!this.declareName.trim().equals(""))) {
            this.nameFound = true;
        }
    	
    }
    
    private void setTypeAndAddToQue(TypeTypeContext typeType) {
        this.declareType = determineTypeOfTypeType(typeType, belongsToClass);
        this.lineNumber = typeType.start.getLine();
       	if(!this.declareType.trim().equals("")) {
       		this.declareTypeFound = true;
            this.paramTypesInSignature += !this.paramTypesInSignature.equals("") ? "," : "";
            this.paramTypesInSignature += this.declareType;
       	}
        if (this.nameFound && this.declareTypeFound) {
            this.addToQueue();
        }
        clearParameterValues();
    }
    
    private void clearParameterValues() {
    	declareName = "";
        declareType = "";
        nameFound = false;
        declareTypeFound = false;
    }
    
    private void addToQueue() {
        ArrayList<Object> myParam = new ArrayList<>();
        myParam.add(this.declareType);
        myParam.add(this.declareName);
        myParam.add(this.lineNumber);
        parameterQueue.add(myParam);
        this.declareType = null;
        this.declareName = null;
    }

	private void writeParametersToDomain() {
        for (ArrayList<Object> currentParameter : parameterQueue) {
            String type = (String) currentParameter.get(0);
            String name = (String) currentParameter.get(1);
            int lineNr =  (int) currentParameter.get(2);
            this.uniqueName = this.belongsToClass + "." + this.belongsToMethod + "(" + this.paramTypesInSignature + ")." + name;
            String belongsToMethodToPassThrough = this.belongsToClass + "." + this.belongsToMethod + "(" + this.paramTypesInSignature + ")";
            if (SkippedJavaTypes.isSkippable(type)) {
                modelService.createParameterOnly(name, uniqueName, type, belongsToClass, lineNr, belongsToMethodToPassThrough);
            } else {
                modelService.createParameter(name, uniqueName, type, belongsToClass, lineNr, belongsToMethodToPassThrough);
            }
        }
    }
}
