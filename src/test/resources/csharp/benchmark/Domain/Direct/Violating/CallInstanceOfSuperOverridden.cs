using Technology.Direct.Dao;
using System.Console;

namespace Domain.Direct.Violating

{
    public class CallInstanceOfSuperOverridden: HistoryDAO
	{
		public void printMethod() {
			base.printMethod();
       		Console.WriteLine("Printed in Subclass.");
    		}

	}
}
