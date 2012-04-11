package husacct.analyse.abstraction.mappers.javamapper.famixObjectGenerators;

import husacct.analyse.domain.famix.FamixObject;

import org.antlr.runtime.tree.CommonTree;

public abstract class JavaGenerator {
	public abstract FamixObject generateFamix(CommonTree Tree);
}
