package husacct.define.presentation.utils;

import husacct.define.domain.services.stateservice.StateService;

import java.util.ArrayList;



public class DragAndDropHelper {

	
	public static Object[] interpretObjects(String data){
		
		
		return filterUnigName(data);
		
	}
	
	
	private static Object[] filterUnigName(String result) {
		String[] vars = result.split("\\s+");
		ArrayList<String> names = new ArrayList<>();
		ArrayList<String> types = new ArrayList<>();
		
		for (int i =0;i<vars.length;i++) {
			if (i%2==0) {
				names.add(vars[i].trim());
			} else {
				types.add(vars[i].trim());
			}
		}
		if (StateService.instance().getAnalyzedSoftWareUnit(names.get(0))==null) {
			return new Object[]{types,names};
		}else{
		
		return new Object[]{names,types};
		}
	}
	
}
