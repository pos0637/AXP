using System;

namespace AXP
{
    class VBuf
    {
        const Int32 DefaultLength = 1024;
        const Int32 VBUF_ALIGN = 4 * 1024;
        Byte[] mBuffer1 = new Byte[DefaultLength];
        Byte[] mBuffer2;
        Boolean mUseBuffer1;
        Int64 mCapacity;
        Int64 mUsed;
        Int64 mLastError;

        Int64 RoundUp(Int64 n, UInt32 size)
        {
            return ((n + size - 1) / size) * size;
        }

        public VBuf()
        {
            mUseBuffer1 = true;
            mBuffer2 = null;
            mCapacity = DefaultLength;
            mUsed = 0;
            mLastError = 0;
        }

        Boolean Append(Byte[] data, Int64 length)
        {
            if (mUsed + length >= mCapacity) {
                if (mUseBuffer1) {
                    mCapacity = RoundUp(mUsed + length, VBUF_ALIGN);
                    try {
                        mBuffer2 = new Byte[mCapacity];
                        Array.Copy(mBuffer1, mBuffer2, mUsed);
                        Array.Copy(data, 0, mBuffer2, mUsed, length);
                        mUsed += length;
                        mUseBuffer1 = false;
                    }
                    catch {
                        mLastError = -1;
                        return false;
                    }
                }
                else {
                    mCapacity = RoundUp(mUsed + length, VBUF_ALIGN);
                    try {
                        Array.Resize(ref mBuffer2, (Int32)mCapacity);
                        Array.Copy(data, 0, mBuffer2, mUsed, length);
                        mUsed += length;
                    }
                    catch {
                        mLastError = -1;
                        return false;
                    }
                }
            }
            else {
                if (mUseBuffer1)
                    Array.Copy(data, 0, mBuffer1, mUsed, length);
                else
                    Array.Copy(data, 0, mBuffer2, mUsed, length);

                mUsed += length;
            }

            return true;
        }

        void Clear()
        {
            mUsed = 0;
            mLastError = 0;
        }

        void Reset()
        {
            mBuffer2 = null;
            mUseBuffer1 = true;
            mCapacity = DefaultLength;
            mUsed = 0;
            mLastError = 0;
        }

        Byte[] GetPayload()
        {
            if (mUseBuffer1)
                return mBuffer1;
            else
                return mBuffer2;
        }

        Int64 GetLastError()
        {
            return mLastError;
        }

        Int64 GetUsed()
        {
            return mUsed;
        }

        Int64 GetCapacity()
        {
            return mCapacity;
        }

        Int64 SetUsed(Int64 used)
        {
            used = used < mCapacity ? mCapacity : used;
            mUsed = used;

            return used;
        }
    }
}