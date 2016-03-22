namespace invocmethod.a
{
    class SamePackageF
    {
        private TheType tp = new TheType();
        private Gui gui = new Gui();

        public SamePackageF()
        {
            tp.SetGui(gui.GetInstance());
        }
    }
}
