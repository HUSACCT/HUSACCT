package husacct.analyse.task.analyser;

import husacct.analyse.task.analyser.clojure.ClojureAnalyser;
import husacct.analyse.task.analyser.csharp.CSharpAnalyser;
import husacct.analyse.task.analyser.java.JavaAnalyser;

class AnalyserBuilder {
	enum language {
		Java, CSharp, Clojure
	}

    public AbstractAnalyser getAnalyser(String language) {
    	switch (language)
    	{
    case "Java":
    	return new JavaAnalyser();
    case "CSharp":
    	return new CSharpAnalyser();
    case "Clojure":
    	return new ClojureAnalyser();
    	}
    	return null;
    	}
    }