namespace invocmethod.a
{
    class SamePackageH
    {
        public SamePackageH()
        {
            new TheType().SetGui(new Gui().GetInstance());
        }
    }
}
