package domain.direct.violating;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class CallFromInnerClass {
    // create an array
    private final static int SIZE = 15;
    private int[] arrayOfInts = new int[SIZE];
    
    public CallFromInnerClass() {
        // fill the array with ascending integer values
        for (int i = 0; i < SIZE; i++) {
            arrayOfInts[i] = i;
        }
    }
    
    // inner class implements the Iterator pattern
    public class CallingInnerClass extends domain.direct.Base {
        // start stepping through the array from the beginning
        private int next = 0;

        private CallInstanceOuterClassDAO.CallInstanceInnerClassDAO innerDao; // Declaration of inner class, while only the outer class is imported

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
        
    	public int CallMethodInstanceInnerClass() {
    		int b;
    		b = innerDao.getNext();
    		//AccessFromInnerClass
    		String t = innerDao.text;
    		return b;
    		
    	}

    }
}