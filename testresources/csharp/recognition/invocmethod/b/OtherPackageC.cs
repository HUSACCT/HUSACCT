using invocmethod.a;

namespace invocmethod.b
{
    class OtherPackageC
    {
        public OtherPackageC()
        {
            new invocmethod.a.TheType().TheMethod();
        }
    }
}
