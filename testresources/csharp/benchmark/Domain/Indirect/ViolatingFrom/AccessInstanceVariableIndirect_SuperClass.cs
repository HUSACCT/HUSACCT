using Domain.Indirect;

namespace Domain.Indirect.ViolatingFrom
{
    public class AccessInstanceVariableIndirect_SuperClass : BaseIndirect
    {
        public String Method()
        {
            return subDao.VariableOnSuperClass;
        }
    }
}