using System;

namespace AXP
{
    public static class AResult
    {
        public const Int32 AS_OK = 0;
        public const Int32 AE_FAIL = -1;
        public const Int32 AE_INVALID_OPERATION = -2;
        public const Int32 AE_DOES_NOT_EXISTS = -3;
        public const Int32 AE_ALREADY_EXISTS = -4;
        public const Int32 AE_OUTOFMEMORY = -5;
        public const Int32 AE_INVALIDARG = -6;
        public const Int32 AE_NOTIMPL = -7;
        public const Int32 AE_EXHAUSTED_RESOURCE = -8;
        public const Int32 AE_BUSY = -9;
        public const Int32 AE_TIMEOUT = -10;
        public const Int32 AE_NOT_SUPPORTED = -11;
        public const Int32 AE_BUFFERTOOSMALL = -12;
        public const Int32 AE_READFAULT = -13;
        public const Int32 AE_WRITEFAULT = -14;
        public const Int32 AE_NOT_FOUND = -15;
        public const Int32 AE_ALREADY_INIT = -16;
        public const Int32 AE_OUTOFALIGNMENT = -17;
        public const Int32 AE_IO_ERROR = -18;
        public const Int32 AE_TERMINATED = -19;
        public const Int32 AE_NOT_ENOUGH_USER_STACK = -100;

        public static Boolean ASUCCEEDED(Int32 value)
        {
            return (0 == (value));
        }

        public static Boolean AFAILED(Int32 value)
        {
            return (0 != (value));
        }
    };
}