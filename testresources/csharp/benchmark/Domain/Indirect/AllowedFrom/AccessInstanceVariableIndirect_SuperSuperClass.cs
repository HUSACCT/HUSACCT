using Domain.Indirect;

namespace Domain.Indirect.AllowedFrom
{
    public class AccessInstanceVariableIndirect_SuperSuperClass : BaseIndirect
    {
        public String Method()
        {
            return subSubDao.VariableOnSuperClass;
        }
    }
}