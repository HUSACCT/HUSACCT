package husacct.define.domain.module;

import java.util.Comparator;

public class ModuleComparator implements Comparator<Object> {
	
	@Override
	public int compare(Object emp1, Object emp2) {
		int compareReturn = 0;
		if(emp1 instanceof Layer && emp2 instanceof Layer) {
			int level1 = ((Layer)emp1).getHierarchicalLevel();
			int level2 = ((Layer)emp2).getHierarchicalLevel();
			compareReturn = this.compareInts(level1, level2);
		} else if(emp1 instanceof Layer) {
			compareReturn = -1;
		} else if(emp2 instanceof Layer) {
			compareReturn = 1;
		}  else if(((Module) emp1).getparent() instanceof Component&&
				((Module) emp1).getparent() instanceof Component) {
		compareReturn=	compareComponents(emp1,emp2);
		}else {
			long moduleid1 = ((Module)emp1).getId();
			long moduleid2 = ((Module)emp2).getId();
			compareReturn = this.compareLongs(moduleid1, moduleid2);
		} 
		return compareReturn;
    }
	
	

	private int compareInts(int int1, int int2) {
		if(int1 > int2) {
            return 1;
		} else if(int1 < int2) {
        	return -1;
        } else {
            return 0;
        }
	}
	
	private int compareLongs(long long1, long long2) {
		if(long1 > long2) {
            return 1;
		} else if(long1 < long2) {
        	return -1;
        } else {
            return 0;
        }
	}
	
	private int compareComponents(Object emp1, Object emp2) {
		long long1 = ((Module)emp1).getId();
		long long2 = ((Module)emp1).getId();
		if(emp1 instanceof Facade || emp2 instanceof Facade)
		{
			return 1;
		}else if(long1 > long2) {
            return 1;
		} else if(long1 < long2) {
        	return -1;
        } else {
            return 0;
        }
		
		
		
		
		

	}
}