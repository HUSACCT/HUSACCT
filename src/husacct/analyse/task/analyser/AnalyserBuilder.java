package husacct.analyse.task.analyser;

import husacct.analyse.task.analyser.csharp.CSharpAnalyser;
import husacct.analyse.task.analyser.java.JavaAnalyser;

class AnalyserBuilder{
	
	public AbstractAnalyser getAnalyser(String language){
		AbstractAnalyser applicationAnalyser;
		if(language.equals(new JavaAnalyser().getProgrammingLanguage())){
			applicationAnalyser = new JavaAnalyser();
		}
		else if(language.equals(new CSharpAnalyser().getProgrammingLanguage())){
			applicationAnalyser = new CSharpAnalyser();
		}
		else{
			applicationAnalyser = null;
			//TODO Erik Create Exception
		}
		return applicationAnalyser;
	}
}
