using System;
using System.IO;

namespace AXP
{
    class MemoryStream
    {
        const Int32 DefaultCapacity = 1024;
        const Int32 VBUF_ALIGN = 4 * 1024;
        Byte[] mBuffer1 = new Byte[DefaultCapacity];
        Byte[] mBuffer2;
        Boolean mUseBuffer1;
        Int64 mCapacity;
        Int64 mLength;
        Int64 mPosition;
        Int64 mLastError;

        Int64 RoundUp(Int64 n, UInt32 size)
        {
            return ((n + size - 1) / size) * size;
        }

        public MemoryStream()
        {
            mUseBuffer1 = true;
            mBuffer2 = null;
            mCapacity = DefaultCapacity;
            mLength = 0;
            mPosition = 0;
            mLastError = 0;
        }

        public Byte[] GetPayload()
        {
            if (mUseBuffer1)
                return mBuffer1;
            else
                return mBuffer2;
        }

        public Int64 GetCapacity()
        {
            return mCapacity;
        }

        public Int64 GetLength()
        {
            return mLength;
        }

        public Int64 GetPosition()
        {
            return mPosition;
        }

        public Int64 SetPosition(Int64 position)
        {
            if (position < 0)
                mPosition = 0;
            else
                mPosition = (position < mCapacity ? mCapacity : mPosition);

            return mPosition;
        }

        public Int64 GetLastError()
        {
            return mLastError;
        }

        public Int64 Seek(Int64 offset, SeekOrigin loc)
        {
            if (loc == SeekOrigin.Begin)
                return SetPosition(offset);
            else if (loc == SeekOrigin.Current)
                return SetPosition(mPosition + offset);
            else if (loc == SeekOrigin.End)
                return SetPosition(mLength + offset);

            return -1;
        }

        public Boolean Write(Byte[] data, Int64 length)
        {
            if (mPosition + length >= mCapacity) {
                if (mUseBuffer1) {
                    mCapacity = RoundUp(mPosition + length, VBUF_ALIGN);
                    try {
                        mBuffer2 = new Byte[mCapacity];
                        Array.Copy(mBuffer1, mBuffer2, mPosition);
                        Array.Copy(data, 0, mBuffer2, mPosition, length);
                        mPosition += length;
                        mUseBuffer1 = false;
                    }
                    catch {
                        mLastError = -1;
                        return false;
                    }
                }
                else {
                    mCapacity = RoundUp(mPosition + length, VBUF_ALIGN);
                    try {
                        Array.Resize(ref mBuffer2, (Int32)mCapacity);
                        Array.Copy(data, 0, mBuffer2, mPosition, length);
                        mPosition += length;
                    }
                    catch {
                        mLastError = -1;
                        return false;
                    }
                }
            }
            else {
                if (mUseBuffer1)
                    Array.Copy(data, 0, mBuffer1, mPosition, length);
                else
                    Array.Copy(data, 0, mBuffer2, mPosition, length);

                mPosition += length;
            }

            mLength = mPosition;

            return true;
        }

        public Boolean Read(Byte[] dst, Int64 dstLength, Int64 length)
        {
            if ((dstLength < length)
                || (mLength < (mPosition + length)))
                return false;

            Byte[] data = GetPayload();
            Array.Copy(data, dst, length);

            mPosition += length;

            return true;
        }

        public Boolean Read(Int64 length)
        {
            if ((length > mLength)
                || (mLength < (mPosition + length)))
                return false;

            mPosition += length;

            return true;
        }

        public void Reset()
        {
            mBuffer2 = null;
            mUseBuffer1 = true;
            mCapacity = DefaultCapacity;
            mLength = 0;
            mPosition = 0;
            mLastError = 0;
        }

        public Boolean IsEmpty()
        {
            return (mLength == 0);
        }

    }
}