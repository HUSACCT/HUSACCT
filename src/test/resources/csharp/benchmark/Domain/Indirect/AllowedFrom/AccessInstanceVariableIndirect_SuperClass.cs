using Domain.Indirect;

namespace Domain.Indirect.AllowedFrom
{
    public class AccessInstanceVariableIndirect_SuperClass : BaseIndirect
    {
        public String Method()
        {
            return subDao.VariableOnSuperClass;
        }
    }
}