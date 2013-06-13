namespace Domain.Indirect.AlowedFrom
{
    public class AccessInstanceVariableIndirect_SuperClass : Domain.Indirect.BaseIndirect
    {
        public String Method()
        {
            return subDao.VariableOnSuperClass;
        }
    }
}