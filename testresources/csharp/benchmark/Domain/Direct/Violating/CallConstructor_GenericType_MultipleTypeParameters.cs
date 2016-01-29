using System.Collections.Generic.Dictionary;

namespace Domain.Direct.Violating
{
	public class CallConstructor_GenericType_MultipleTypeParameters
	{
		public CallConstructor_GenericType_MultipleTypeParameters()
		{
            dictionary = Dictionary<ProfileDAO, UserDAO>();
		}
	}
}
