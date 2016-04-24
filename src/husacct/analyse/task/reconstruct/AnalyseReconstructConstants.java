package husacct.analyse.task.reconstruct;

public class AnalyseReconstructConstants {
	
	public static class Algorithm{
		//TKey = Translation Key. Form common/resources/local/...
		public static final String Layers_Scanniello_Original = "Layers_Scanniello_Original";
		public static final String Layers_Scanniello_Improved = "Layers_Scanniello_Improved";
		
		public static final String Layers_Goldstein_Multiple_Improved = "Layers_Goldstein_Multiple_Improved";
		public static final String Layers_Goldstein_Original = "Layers_Goldstein_Original";
		public static final String Layers_Goldstein_Root_Improved = "Layers_Goldstein_Root_Improved";
		
		public static final String Component_HUSACCT_SelectedModule = "Component_HUSACCT_SelectedModule";
		
		public static final String Gateways_HUSACCT_Root = "Gateways_HUSACCT_Root";
		public static final String Adapter_Recognition = "Adapter_Recognition";

		public static final String Externals_Recognition = "Externals_Recognition";
	}
	
	public static class ApproachesTable{
		public static final String ApproachesConstants = "ApproachesConstants";
		public static final String PanelAllApproaches = "AllApproaches";
		public static final String PanelDistinctApproaches = "DistinctApproaches";
		
	}
	
	public static class RelationTypes{
		public static final String allDependencies = "AllDependencies";
		public static final String umlLinks = "UmlLinks";
		public static final String accessCallReferenceDependencies = "AccessCallReferenceDependencies";
	}
}
