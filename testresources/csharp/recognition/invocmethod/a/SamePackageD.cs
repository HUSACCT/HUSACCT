namespace invocmethod.a
{
    class SamePackageD
    {
        private TheType tp = new TheType();

        public SamePackageD() 
        {
            tp.SetGui(new Gui());
        }
    }
}
