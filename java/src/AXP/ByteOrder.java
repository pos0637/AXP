package AXP;

public final class ByteOrder
{
    public static final short SWAP_16(short s)
    {
        return (short) ((s << 8) | (s >>> 8));
    }

    public static final int SWAP_32(int i)
    {
        return ((i << 24) | (i >>> 24) | ((i & 0x00ff0000) >>> 8) | (i & 0x0000ff00) << 8);
    }

    public static final long SWAP_64(long l)
    {
        return ((l << 56) | (l >>> 56) | ((l & 0x00ff000000000000L) >>> 40) | ((l & 0x0000ff0000000000L) >>> 24) | ((l & 0x000000ff00000000L) >>> 8) | ((l & 0x00000000ff000000L) << 8) | ((l & 0x0000000000ff0000L) << 24) | ((l & 0x000000000000ff00L) << 40));
    }
}