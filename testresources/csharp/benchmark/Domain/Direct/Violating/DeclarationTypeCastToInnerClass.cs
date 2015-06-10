namespace Domain.Direct.Violating

{

	using Technology.Direct.Dao;

	public class DeclarationTypeCastToInnerClass
	{
		public void GetProfileInformation()
		{
            object o = (CallInstanceOuterClassDAO.CallInstanceInnerClassDAO)new object();
		}
	}
}
