using System;

namespace AXP
{
    public static class ByteOrder
    {
        public static UInt16 Swap16(UInt16 s)
        {
            return (UInt16)((((s) & 0xff) << 8) | (((s) >> 8) & 0xff));
        }

        public static UInt32 Swap32(UInt32 l)
        {
            return (UInt32)(((l) >> 24) |
                (((l) & 0x00ff0000) >> 8) |
                (((l) & 0x0000ff00) << 8) |
                ((l) << 24));
        }

        public static UInt64 Swap64(UInt64 ll)
        {
            return (UInt64)(((ll) >> 56) |
                (((ll) & 0x00ff000000000000) >> 40) |
                (((ll) & 0x0000ff0000000000) >> 24) |
                (((ll) & 0x000000ff00000000) >> 8) |
                (((ll) & 0x00000000ff000000) << 8) |
                (((ll) & 0x0000000000ff0000) << 24) |
                (((ll) & 0x000000000000ff00) << 40) |
                (((ll) << 56)));
        }
    }
} // namespace AXP 