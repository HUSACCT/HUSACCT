namespace Technology.Direct.Dao
{
	public class CallInstanceOuterClassDAO
	{
		// create an array
        private const int SIZE = 7;
        private int[] arrayOfInts = new int[SIZE];

		public CallInstanceOuterClassDAO()
		{
			// fill the array with ascending integer values
			for (int i = 0; i < SIZE; i++)
			{
				arrayOfInts[i] = i;
			}
		}

		public virtual void PrintEven()
		{
			// print out values of even indices of the array
			CallInstanceOuterClassDAO.CallInstanceInnerClassDAO iterator = new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO
				(this);
			while (iterator.HasNext())
			{
				System.Console.Out.WriteLine(iterator.GetNext() + " ");
			}
		}

        // inner class implements the Iterator pattern
		public class CallInstanceInnerClassDAO
		{
			private readonly CallInstanceOuterClassDAO _enclosing;
			private int next = 0;

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

			internal CallInstanceInnerClassDAO(CallInstanceOuterClassDAO _enclosing)
			{
				this._enclosing = _enclosing;
                
			}
		}
	}
}
