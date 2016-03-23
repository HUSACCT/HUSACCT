using accessfield.a;

namespace accessfield.b
{
    class OtherPackageB
    {
        private TheOwner owner = new TheOwner();

        public OtherPackageB()
        {
            owner.theString = "test";
        }
    }
}
