using invocmethod.a;

namespace invocmethod.b
{
    class OtherPackageD
    {
        private TheType tp = new TheType();

        public OtherPackageD()
        {
            tp.TheMethod();
        }
    }
}
