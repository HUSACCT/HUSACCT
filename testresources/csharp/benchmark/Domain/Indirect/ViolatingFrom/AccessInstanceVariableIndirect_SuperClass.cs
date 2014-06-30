namespace Domain.Indirect.ViolatingFrom
{
    public class AccessInstanceVariableIndirect_SuperClass : Domain.Indirect.BaseIndirect
    {
        public String Method()
        {
            return subDao.VariableOnSuperClass;
        }
    }
}