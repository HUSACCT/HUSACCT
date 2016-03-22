using accessfield.a;

namespace accessfield.b
{
    class OtherPackageA
    {
        private TheOwner owner = new TheOwner();

        public OtherPackageA()
        {
            string s = owner.theString;
        }
    }
}
