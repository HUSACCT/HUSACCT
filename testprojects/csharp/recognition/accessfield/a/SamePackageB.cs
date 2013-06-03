namespace accessfield.a
{
    class SamePackageB
    {
        private TheOwner owner = new TheOwner();

        public SamePackageB()
        {
            owner.theString = "test";
        }
    }
}
