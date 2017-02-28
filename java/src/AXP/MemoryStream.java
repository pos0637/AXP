package AXP;

import java.util.Arrays;

public class MemoryStream
{
	private byte mBuffer [];
	private long mCapacity;
	private long mLength;
	private long mPosition;
	private int mLastError;
	private final static int DEFUALT_LENGTH = 1024;
	private final static int VBUF_ALIGN = 4 * 1024;

	static long RoundUp(long n, long size)
	{
	    return ((n + size - 1) / size) * size;
	}

	public MemoryStream()
	{
		mBuffer = new byte[DEFUALT_LENGTH];
		mCapacity = DEFUALT_LENGTH;
		Reset();
	}

	public void Reset()
	{
		mLength = 0;
		mPosition = 0;
		mLastError = 0;
	}

	public boolean IsEmpty()
	{
		return mLength == 0;
	}

	public long SetPosition(long position)
	{
	    if (position < 0)
	        mPosition = 0;
	    else
	        mPosition = position < mCapacity ? position : mCapacity;

		return mPosition;
	}

	public long GetPosition()
	{
		return mPosition;
	}

	public long GetCapacity()
	{
		return mCapacity;
	}

	public long GetLength()
	{
		return mLength;
	}

	public int GetLastError()
	{
		return mLastError;
	}

	public byte[] GetPayload()
	{
		return mBuffer;
	}

	protected boolean EnsureCapacity(long length)
	{
        if (length >= mCapacity) {
        	mCapacity = RoundUp(length, VBUF_ALIGN);
        	mBuffer = Arrays.copyOf(mBuffer, (int)mCapacity);
        	if (mBuffer == null)
        		return false;
        }

        return true;
	}

	public boolean WriteByte(int b)
	{
        if (!EnsureCapacity(mPosition + 1))
        	return false;

        mBuffer[(int)mPosition] = (byte) b;
        mPosition += 1;
        mLength = mPosition;

        return true;
	}

	public boolean Write(byte b[])
	{
        if ((b == null) || (b.length < 1))
            return false;

        return Write(b, 0, b.length);
	}

	public boolean Write(byte b[], int off, int len)
	{
        if ((b == null) || (off < 0 || len < 0 || len > b.length - off))
            return false;

        if (len == 0)
        	return true;

        if (!EnsureCapacity(mPosition + len))
        	return false;

        System.arraycopy(b, off, mBuffer, (int)mPosition, len);
        mPosition += len;
        mLength = mPosition;

        return true;
	}

	public int Read()
	{
		if ((mLength < mPosition + 1) || (mPosition >= mCapacity))
			return -1;

		return mBuffer[(int)(mPosition++)] & 0xff;
	}

	public boolean Read(byte b[], int length)
	{
        if ((b == null)
        	|| (b.length < 1)
        	|| (length < 1)
        	|| (b.length < length)
        	|| (mLength < mPosition + length))
            return false;

        System.arraycopy(mBuffer, (int)mPosition, b, 0, length);
		mPosition += b.length;

		return true;
	}

	public int Read(int length)
	{
		if ((mLength < mPosition + length) || (mPosition >= mCapacity))
			return -1;

		mPosition += length;

		return 0;
	}
}