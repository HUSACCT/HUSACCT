namespace Domain.Direct.Violating
{
	using Domain.Direct;
	using System.Collections.Generic;
	using Technology.Direct.Dao;

	public class DeclarationReturnType_GenericType_OneTypeParameter : Base
	{
		public List<ProfileDAO> testUsageOfInstanceVariableDefinedByGenericTypeOneParameterType()
		{
		return profileDAOs;
		}
	}
}
