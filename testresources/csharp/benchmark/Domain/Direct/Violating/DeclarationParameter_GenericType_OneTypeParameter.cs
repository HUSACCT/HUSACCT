using System.Collections.Generic;
using Technology.Direct.Dao;

namespace Domain.Direct.Violating
{
	public class DeclarationParameter_GenericType_OneTypeParameter
	{
		public String testUsageOfInstanceVariableDefinedByGenericTypeOneParameterType(List<ProfileDAO> pDao)
		{
			return pDao.get(0).toString();
		}
	}
}
