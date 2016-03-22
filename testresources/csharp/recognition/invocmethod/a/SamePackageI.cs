namespace invocmethod.a
{
    class SamePackageI
    {
        private TheType tp = new TheType();

        public SamePackageI()
        {
            tp.getGui().GetInstance().ToString();
        }
    }
}
