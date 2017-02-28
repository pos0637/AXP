using System;

namespace AXP
{
    public class BitBuffer : IParcelable
    {
        protected Boolean mIsByteHigh;
        protected Int32 mPosition;
        protected Int32 mStartOffset;
        protected Int32 mBitLength;
        protected Byte[] mBitBuffer;

        public Byte GetUInt8(
            Int32 length)
        {
            if ((mBitBuffer == null)
                || (length < 1)
                || (length > 8)
                || ((mPosition + length) > mBitLength)) {
                if ((mPosition + length) > mBitLength)
                    mPosition = mBitLength;

                return 0;
            }

            Byte value = 0;
            if (mIsByteHigh) {
                for (Int32 i = length - 1, j = 0; i >= 0; --i, ++j) {
                    value = (Byte)(value | (mBitBuffer[mPosition + i] << j));
                }
            }
            else {
                for (Int32 i = 0; i < length; i++) {
                    value = (Byte)(value | (mBitBuffer[mPosition + i] << i));
                }
            }

            mPosition += length;

            return value;
        }

        public Int32 GetLength()
        {
            return (mBitLength - mPosition);
        }

        public Int32 GetPosition()
        {
            return mPosition;
        }

        public void SetPosition(Int32 newPosition)
        {
            mPosition = newPosition;
        }

        public virtual Int32 ReadFromParcel(
            Parcel parcel)
        {
            if (parcel == null)
                return AResult.AE_FAIL;

            if (AResult.AFAILED(parcel.ReadBoolean(ref mIsByteHigh)))
                return AResult.AE_FAIL;

            if (AResult.AFAILED(parcel.ReadInt32(ref mPosition)))
                return AResult.AE_FAIL;

            if (AResult.AFAILED(parcel.ReadInt32(ref mStartOffset)))
                return AResult.AE_FAIL;

            if (AResult.AFAILED(parcel.ReadByteArray(ref mBitBuffer)))
                return AResult.AE_FAIL;

            if (mBitBuffer != null)
                mBitLength = mBitBuffer.Length;
            else
                mBitLength = 0;

            return AResult.AS_OK;
        }

        public virtual Int32 WriteToParcel(
            Parcel parcel)
        {
            if (parcel == null)
                return AResult.AE_FAIL;

            if (AResult.AFAILED(parcel.WriteBoolean(mIsByteHigh)))
                return AResult.AE_FAIL;

            if (AResult.AFAILED(parcel.WriteInt32(mPosition)))
                return AResult.AE_FAIL;

            if (AResult.AFAILED(parcel.WriteInt32(mStartOffset)))
                return AResult.AE_FAIL;

            if (AResult.AFAILED(parcel.WriteByteArray(mBitBuffer)))
                return AResult.AE_FAIL;

            return AResult.AS_OK;
        }

        public override String ToString()
        {
            String str = null;
            Int32 position = mPosition;

            while (mPosition < mBitLength) {
                if (str == null) {
                    Int32 i = (Int32)(mBitLength % 8);
                    if (i == 0)
                        str = String.Format("0x{0:X2}", GetUInt8(8));
                    else
                        str = String.Format("0x{0:X2}", GetUInt8(i));
                }
                else {
                    str = String.Format("{0} 0x{1:X2}", str, GetUInt8(8));
                }

                if (str == null)
                    return null;
            }

            mPosition = position;

            return str;
        }
    }
}