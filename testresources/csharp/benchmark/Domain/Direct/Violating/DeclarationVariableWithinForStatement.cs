using Technology.Direct.Dao;
using Domain.Direct;

namespace Domain.Direct.Violating
{
    public class DeclarationVariableWithinForStatement : Base
    {
        public String GetProfileInformation()
        {
    		foreach  (ProfileDAO pdao in profileDAOs) {
    			string p = pdao.toString();
    		}
            return "";
        }
    }
}