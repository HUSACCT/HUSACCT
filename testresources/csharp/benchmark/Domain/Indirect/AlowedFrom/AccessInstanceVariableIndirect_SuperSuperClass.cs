namespace Domain.Indirect.AllowedFrom
{
    public class AccessInstanceVariableIndirect_SuperSuperClass : Domain.Indirect.BaseIndirect
    {
        public String Method()
        {
            return subSubDao.VariableOnSuperClass;
        }
    }
}