package AXP;

public final class AResult
{
    public static final int AS_OK = 0;
    public static final int AE_FAIL = -1;
    public static final int AE_INVALID_OPERATION = -2;
    public static final int AE_DOES_NOT_EXISTS = -3;
    public static final int AE_ALREADY_EXISTS = -4;
    public static final int AE_OUTOFMEMORY = -5;
    public static final int AE_INVALIDARG = -6;
    public static final int AE_NOTIMPL = -7;
    public static final int AE_EXHAUSTED_RESOURCE = -8;
    public static final int AE_BUSY = -9;
    public static final int AE_TIMEOUT = -10;
    public static final int AE_NOT_SUPPORTED = -11;
    public static final int AE_BUFFERTOOSMALL = -12;
    public static final int AE_READFAULT = -13;
    public static final int AE_WRITEFAULT = -14;
    public static final int AE_NOT_FOUND = -15;
    public static final int AE_ALREADY_INIT = -16;
    public static final int AE_OUTOFALIGNMENT = -17;
    public static final int AE_IO_ERROR = -18;
    public static final int AE_TERMINATED = -19;
    public static final int AE_NOT_ENOUGH_USER_STACK = -100;

    public static boolean ASUCCEEDED(int value)
    {
        return (0 == (value));
    }

    public static boolean AFAILED(int value)
    {
        return (0 != (value));
    }
}