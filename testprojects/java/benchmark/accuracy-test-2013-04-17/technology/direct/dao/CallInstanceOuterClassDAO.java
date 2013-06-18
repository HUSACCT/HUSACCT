<<<<<<< HEAD
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
    
    // inner class implements the Iterator pattern
    public class CallInstanceInnerClassDAO {
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
    }
=======
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
    
    // inner class implements the Iterator pattern
    public class CallInstanceInnerClassDAO {
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
    }
>>>>>>> 7b87626b99107db400440335a38d325b9ac19cd3
}