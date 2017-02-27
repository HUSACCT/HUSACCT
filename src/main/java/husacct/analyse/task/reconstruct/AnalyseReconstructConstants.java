package husacct.analyse.task.reconstruct;

public class AnalyseReconstructConstants {
	
	public static class Algorithm{
		//TKey = Translation Key. Form common/resources/local/...
		// Practical approaches
		public static final String Layers_HUSACCT_SelectedModule = "Layers_HUSACCT_SelectedModule";
		public static final String Component_HUSACCT_SelectedModule = "Component_HUSACCT_SelectedModule";
		public static final String Externals_Recognition = "Externals_Recognition";
		public static final String CombinedAndIterative_HUSACCT_SelectedModule = "CombinedAndIterative_HUSACCT_SelectedModule";
		
		// Research approaches
		public static final String Layers_Scanniello_Original = "Layers_Scanniello_Original";
		public static final String Layers_Scanniello_Improved = "Layers_Scanniello_Improved";
		
		public static final String Layers_Goldstein_Root_Original = "Layers_Goldstein_Root_Original";
		public static final String Layers_Goldstein_HUSACCT_SelectedModule = "Layers_Goldstein_HUSACCT_SelectedModule";
		
		public static final String Layers_HUSACCT_SAEroCon2016 = "Layers_HUSACCT_SAEroCon2016";

		public static final String Gateways_HUSACCT_Root = "Gateways_HUSACCT_Root";
		public static final String Adapter_Recognition = "Adapter_Recognition";
	}
	
	public static class RelationTypes{
		public static final String allDependencies = "AllDependencies";
		public static final String umlLinks = "UmlLinks";
		public static final String accessCallReferenceDependencies = "AccessCallReferenceDependencies";
	}
	
	public static class Granularities{
		//public static final String PackagesInRootOnlyClasses = "PackagesInRoot(WithClassesOnly)";
		public static final String PackagesAndClasses = "PackagesAndClasses";
		public static final String Classes = "Classes";
		public static final String Packages = "Packages";
	}
	
	public static class AlgorithmParameter{
		public static final String Threshold = "Threshold";
		public static final String RelationType = "RelationType";
		public static final String Granularity = "Granularity";
	}
}
