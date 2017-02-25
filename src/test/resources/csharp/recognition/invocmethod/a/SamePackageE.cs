namespace invocmethod.a
{
    class SamePackageE
    {
        private TheType tp = new TheType();
        private Gui gui = new Gui();

        public SamePackageE()
        {
            tp.SetGui(gui);
        }
    }
}
