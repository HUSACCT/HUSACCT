package domain.direct.allowed;

public class CallFromOuterClass {
    // create an array
    private final static int SIZE = 15;
    private int[] arrayOfInts = new int[SIZE];
    
    public CallFromOuterClass() {
        // fill the array with ascending integer values
        for (int i = 0; i < SIZE; i++) {
            arrayOfInts[i] = i;
        }
    }
    
    // inner class implements the Iterator pattern
    public class CallingInnerClass extends domain.direct.Base{
        // start stepping through the array from the beginning
        private int next = 0;
        
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
    		return b;
    	}

    }
}