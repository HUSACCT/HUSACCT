using Domain.Indirect;

namespace Domain.Indirect.ViolatingFrom
{
    public class AccessInstanceVariableIndirect_SuperSuperClass : BaseIndirect
    {
        public String Method()
        {
            return subSubDao.VariableOnSuperClass;
        }
    }
}