package husacct.analyse.domain.famix;

import java.util.TreeSet;

abstract class FamixDecompositionEntity extends FamixEntity {

    public boolean external = false;
    public String belongsToPackage;
    public TreeSet<String> children = new TreeSet<>();
}
