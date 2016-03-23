using Technology.Direct.Dao;

namespace Domain.Direct.Violating
{
	public class CallFromInnerClass
	{
		// create an array
        private const int SIZE = 7;
        private int[] arrayOfInts = new int[SIZE];

		public CallFromInnerClass()
		{
			// fill the array with ascending integer values
			for (int i = 0; i < SIZE; i++)
			{
				arrayOfInts[i] = i;
			}
		}

        // inner class implements the Iterator pattern
		public class CallingInnerClass : Domain.Direct.Base
		{
			private readonly CallFromInnerClass _enclosing;
			private int next = 0;
			CallInstanceOuterClassDAO.CallInstanceInnerClassDAO innerClassDao;


			// start stepping through the array from the beginning
			public virtual bool HasNext()
			{
				return true;
			}

			public virtual int GetNext()
			{
				// record a value of an even index of the array
				int retValue = this._enclosing.arrayOfInts[this.next];
				//get the next even element
				this.next += 2;
				return retValue;
			}

			internal CallingInnerClass(CallFromInnerClass _enclosing)
			{
				this._enclosing = _enclosing;
                
			}
			
	    	public int CallMethodInstanceInnerClass() {
	    		int b;
	    		b = innerClassDao.getNext();

			int c = innerDao.getText();
	    		return c;
	    	}

		}
	}
}
