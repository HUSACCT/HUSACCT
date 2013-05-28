namespace invocmethod.a
{
    class SamePackageG
    {
        private TheType tp = new TheType();

        public SamePackageG()
        {
            tp.SetGui(new Gui().GetInstance());
        }
    }
}
