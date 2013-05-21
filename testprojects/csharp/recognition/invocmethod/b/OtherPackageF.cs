using invocmethod.a;

namespace invocmethod.b
{
    class OtherPackageF
    {
        private invocmethod.a.TheType tp = new invocmethod.a.TheType();

        public OtherPackageF()
        {
            tp.TheMethod();
        }
    }
}
