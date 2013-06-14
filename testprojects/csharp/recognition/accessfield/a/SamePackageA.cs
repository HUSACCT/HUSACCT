namespace accessfield.a
{
    class SamePackageA
    {
        private TheOwner owner = new TheOwner();

        public SamePackageA()
        {
            string s = owner.theString;
        }
    }
}
