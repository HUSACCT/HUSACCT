package technology.direct.dao;

public class CallInstanceOuterClassDAO {
    // create an array
    private final static int SIZE = 15;
    private int[] arrayOfInts = new int[SIZE];
    
    public CallInstanceOuterClassDAO() {
        // fill the array with ascending integer values
        for (int i = 0; i < SIZE; i++) {
            arrayOfInts[i] = i;
        }
    }
    
    public void printEven() {
        // print out values of even indices of the array
        CallInstanceInnerClassDAO iterator = this.new CallInstanceInnerClassDAO();
        while (iterator.hasNext()) {
            System.out.println(iterator.getNext() + " ");
        }
    }
    
    public void callConstructorInnerClass() {
    	CallInstanceOuterClassDAO.CallInstanceInnerClassDAO i = new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO();
    }

    public void callDefaultConstructorInnerClass() {
    	CallInstanceOuterClassDAO.CallInstanceInnerClassDAO i = new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO();
    }
    
    // inner class implements the Iterator pattern
    public class CallInstanceInnerClassDAO {
        // start stepping through the array from the beginning
        private int next = 0;
        private TestConstructorCallOfInnerClass otherInnerClass;
        public String text;
        
        public CallInstanceInnerClassDAO() {
        	this.text = "s";
		}
        public CallInstanceInnerClassDAO(String s) {
        	this.text = s;
        }
        public CallInstanceInnerClassDAO(TestConstructorCallOfInnerClass otherInner) {
        	this.otherInnerClass = otherInner;
        }
        
		public boolean hasNext() {
            return true ;
        }
        
        public int getNext() {
            // record a value of an even index of the array
            int retValue = arrayOfInts[next];
            //get the next even element
            next += 2;
            return retValue;
        }
        
    }
    
    public enum InnerEnumeration {
    	ONE,TWO,THREE,FOUR;
    }
    
    public interface CallInstanceInnerInterfaceDAO {
    	public void InterfaceMethod();
    }

    public static class StaticNestedClass { 
    	CallInstanceOuterClassDAO outer; 
    	String var;
    	
    	public StaticNestedClass() {
    		this.var = "s";
    	}

    	public StaticNestedClass(String var) {
    		this.var = var;
    	}
 
		public CallInstanceOuterClassDAO getOuter() { 
			return outer; 
		} 
	} 
    
    public class TestConstructorCallOfInnerClass {
    	CallInstanceOuterClassDAO.CallInstanceInnerClassDAO i = new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO(this);
    }

}