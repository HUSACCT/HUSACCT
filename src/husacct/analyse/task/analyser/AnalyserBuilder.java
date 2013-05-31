package husacct.analyse.task.analyser;

import husacct.analyse.task.analyser.csharp.CSharpAnalyser;
import husacct.analyse.task.analyser.java.JavaAnalyser;

class AnalyserBuilder {

    public AbstractAnalyser getAnalyser(String language) {
        if (language.equals(new JavaAnalyser().getProgrammingLanguage())) {
            return new JavaAnalyser();
        } else if (language.equals(new CSharpAnalyser().getProgrammingLanguage())) {
            return new CSharpAnalyser();
        } else {
            return null;
        }
    }
}
