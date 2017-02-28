package AXP;

public class BitBuffer implements IParcelable
{
    protected boolean mIsByteHigh;
    protected int mPosition;
    protected int mStartOffset;
    protected int mBitLength;
    protected byte[] mBitBuffer;

    public short GetUInt8(int length)
    {
        if ((mBitBuffer == null) || (length < 1) || (length > 8) || ((mPosition + length) > mBitLength)) {
            if ((mPosition + length) > mBitLength)
                mPosition = mBitLength;

            return 0;
        }

        short value = 0;
        if (mIsByteHigh) {
            for (int i = length - 1, j = 0; i >= 0; --i, ++j) {
                value = (short) (value | (mBitBuffer[mPosition + i] << j));
            }
        }
        else {
            for (int i = 0; i < length; i++) {
                value = (short) (value | (mBitBuffer[mPosition + i] << i));
            }
        }

        mPosition += length;

        return value;
    }

    public int GetLength()
    {
        return (mBitLength - mPosition);
    }

    public int GetPosition()
    {
        return mPosition;
    }

    public void SetPosition(int newPosition)
    {
        mPosition = newPosition;
    }

    public int ReadFromParcel(Parcel parcel)
    {
        if (parcel == null)
            return AResult.AE_FAIL;

        try {
            mIsByteHigh = parcel.ReadBoolean();
            mPosition = parcel.ReadInt32();
            mStartOffset = parcel.ReadInt32();
            mBitBuffer = parcel.ReadByteArray();
            if (mBitBuffer != null)
                mBitLength = mBitBuffer.length;
            else
                mBitLength = 0;
        }
        catch (Exception e) {
            // TODO: handle exception
            return AResult.AE_FAIL;
        }

        return AResult.AS_OK;
    }

    public int WriteToParcel(Parcel parcel)
    {
        if (parcel == null)
            return AResult.AE_FAIL;

        if (AResult.AFAILED(parcel.WriteBoolean(mIsByteHigh)))
            return AResult.AE_FAIL;

        if (AResult.AFAILED(parcel.WriteInt32(mPosition)))
            return AResult.AE_FAIL;

        if (AResult.AFAILED(parcel.WriteInt32(mStartOffset)))
            return AResult.AE_FAIL;

        byte[] buffer = null;
        if (mBitBuffer != null) {
            buffer = new byte[mBitBuffer.length];
            if (buffer != null) {
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte) mBitBuffer[i];
                }
            }
        }

        if (AResult.AFAILED(parcel.WriteByteArray(buffer)))
            return AResult.AE_FAIL;

        return AResult.AS_OK;
    }

    public String ToString()
    {
        String str = null;
        int position = mPosition;

        while (mPosition < mBitLength) {
            if (str == null) {
                int i = (int) (mBitLength % 8);
                if (i == 0)
                    str = String.format("0x%02X", GetUInt8(8));
                else
                    str = String.format("0x%02X", GetUInt8(i));
            }
            else {
                str = String.format("%s0x%02X", str, GetUInt8(8));
            }

            if (str == null)
                return null;
        }

        mPosition = position;

        return str;
    }
}