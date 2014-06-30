namespace invocmethod.a
{
    class SamePackageC
    {
        private TheType tp = new TheType();

        public SamePackageC()
        {
            string s = tp.ToString();
        }
    }
}
