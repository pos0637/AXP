package AXP;

import java.io.*;
import java.math.BigInteger;

public class Parcel
{
    private MemoryStream mStream;
    private boolean mIsLittleEndian;

    private int UpdateParcelHeaderLength()
    {
        byte[] buffer = mStream.GetPayload();
        int len = (int) mStream.GetPosition() - 5;

        buffer[1] = (byte) (len >>> 0);
        buffer[2] = (byte) (len >>> 8);
        buffer[3] = (byte) (len >>> 16);
        buffer[4] = (byte) (len >>> 24);

        return AResult.AS_OK;
    }

    public Parcel()
    {
        mStream = new MemoryStream();
        mIsLittleEndian = true;
        if (!mStream.WriteByte(mIsLittleEndian ? 1 : 0))
            return;

        if (!mStream.WriteByte(0))
            return;

        if (!mStream.WriteByte(0))
            return;

        if (!mStream.WriteByte(0))
            return;

        if (!mStream.WriteByte(0))
            return;

        UpdateParcelHeaderLength();
    }

    public Parcel(MemoryStream stream)
    {
        if (stream == null)
            throw new NullPointerException();

        mStream = stream;
        Reset();
    }

    public int Reset()
    {
        mStream.SetPosition(0);

        try {
            int ch = mStream.Read();
            if (ch < 0)
            	return AResult.AE_FAIL;

            mIsLittleEndian = (ch != 0);
            mStream.Read(4);
        }
        catch (Exception e) {
            return AResult.AE_FAIL;
        }

        return AResult.AS_OK;
    }

    public int Reset(byte[] buffer)
    {
        if ((buffer == null) || (buffer.length < 1))
            return AResult.AE_FAIL;

        return Reset(buffer, 0, buffer.length);
    }

    public int Reset(byte[] buffer, int off, int len)
    {
        mStream = new MemoryStream();
        if (!mStream.Write(buffer, 0, len))
            return AResult.AE_FAIL;

        return Reset();
    }

    public void Seek(long pos)
    {
        mStream.SetPosition(pos);
    }

    public long GetPosition()
    {
        return mStream.GetPosition();
    }

    public long GetLength()
    {
        return mStream.GetLength();
    }

    public byte[] GetPayload()
    {
        return mStream.GetPayload();
    }

    public int Write(byte[] value)
    {
        if (!mStream.Write(value))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteBoolean(boolean value)
    {
        char typeName = 'z';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte(value ? 1 : 0))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableBoolean(Boolean value)
    {
        char typeName = 'Z';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte(value.booleanValue() ? 1 : 0))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteByte(byte value)
    {
        char typeName = 'c';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte(value))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableByte(Byte value)
    {
        char typeName = 'C';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte(value.byteValue()))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteInt8(byte value)
    {
        char typeName = 'b';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte(value))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteUInt8(short value)
    {
        char typeName = 'e';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte(value))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableInt8(Byte value)
    {
        char typeName = 'B';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte(value.byteValue()))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableUInt8(Short value)
    {
        char typeName = 'E';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte(value.byteValue()))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteInt16(short value)
    {
        char typeName = 'r';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((value >>> 0) & 0xFF))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((value >>> 8) & 0xFF))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteUInt16(int value)
    {
        char typeName = 'n';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((value >>> 0) & 0xFF))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((value >>> 8) & 0xFF))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableInt16(Short value)
    {
        char typeName = 'R';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((value >>> 0) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((value >>> 8) & 0xFF))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableUInt16(Integer value)
    {
        char typeName = 'N';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            short temp = value.shortValue();
            if (!mStream.WriteByte((temp >>> 0) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((temp >>> 8) & 0xFF))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteInt32(int value)
    {
        char typeName = 'i';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((value >>> 0) & 0xFF))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((value >>> 8) & 0xFF))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((value >>> 16) & 0xFF))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((value >>> 24) & 0xFF))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteUInt32(long value)
    {
        char typeName = 'u';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        int temp = (int)value;

        if (!mStream.WriteByte((temp >>> 0) & 0xFF))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((temp >>> 8) & 0xFF))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((temp >>> 16) & 0xFF))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((temp >>> 24) & 0xFF))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableInt32(Integer value)
    {
        char typeName = 'I';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            int temp = value.intValue();

            if (!mStream.WriteByte((temp >>> 0) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((temp >>> 8) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((temp >>> 16) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((temp >>> 24) & 0xFF))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableUInt32(Long value)
    {
        char typeName = 'U';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            int temp = value.intValue();

            if (!mStream.WriteByte((temp >>> 0) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((temp >>> 8) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((temp >>> 16) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((temp >>> 24) & 0xFF))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteInt64(long value)
    {
        char typeName = 'g';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        byte[] writeBuffer = new byte[8];

        writeBuffer[0] = (byte) (value >>> 0);
        writeBuffer[1] = (byte) (value >>> 8);
        writeBuffer[2] = (byte) (value >>> 16);
        writeBuffer[3] = (byte) (value >>> 24);
        writeBuffer[4] = (byte) (value >>> 32);
        writeBuffer[5] = (byte) (value >>> 40);
        writeBuffer[6] = (byte) (value >>> 48);
        writeBuffer[7] = (byte) (value >>> 56);

        if (!mStream.Write(writeBuffer))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteUInt64(BigInteger value)
    {
        if (value == null)
            return AResult.AE_FAIL;

        char typeName = 'm';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        long temp = value.longValue();
        byte[] writeBuffer = new byte[8];
        writeBuffer[0] = (byte) (temp >>> 0);
        writeBuffer[1] = (byte) (temp >>> 8);
        writeBuffer[2] = (byte) (temp >>> 16);
        writeBuffer[3] = (byte) (temp >>> 24);
        writeBuffer[4] = (byte) (temp >>> 32);
        writeBuffer[5] = (byte) (temp >>> 40);
        writeBuffer[6] = (byte) (temp >>> 48);
        writeBuffer[7] = (byte) (temp >>> 56);

        if (!mStream.Write(writeBuffer))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableInt64(Long value)
    {
        char typeName = 'G';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            long temp = value.longValue();
            byte[] writeBuffer = new byte[8];
            writeBuffer[0] = (byte) (temp >>> 0);
            writeBuffer[1] = (byte) (temp >>> 8);
            writeBuffer[2] = (byte) (temp >>> 16);
            writeBuffer[3] = (byte) (temp >>> 24);
            writeBuffer[4] = (byte) (temp >>> 32);
            writeBuffer[5] = (byte) (temp >>> 40);
            writeBuffer[6] = (byte) (temp >>> 48);
            writeBuffer[7] = (byte) (temp >>> 56);

            if (!mStream.Write(writeBuffer))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableUInt64(BigInteger value)
    {
        char typeName = 'M';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            long temp = value.longValue();
            byte[] writeBuffer = new byte[8];
            writeBuffer[0] = (byte) (temp >>> 0);
            writeBuffer[1] = (byte) (temp >>> 8);
            writeBuffer[2] = (byte) (temp >>> 16);
            writeBuffer[3] = (byte) (temp >>> 24);
            writeBuffer[4] = (byte) (temp >>> 32);
            writeBuffer[5] = (byte) (temp >>> 40);
            writeBuffer[6] = (byte) (temp >>> 48);
            writeBuffer[7] = (byte) (temp >>> 56);

            if (!mStream.Write(writeBuffer))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteFloat(float value)
    {
        char typeName = 'f';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        int temp = Float.floatToIntBits(value);
        if (!mStream.WriteByte((temp >>> 0) & 0xFF))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((temp >>> 8) & 0xFF))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((temp >>> 16) & 0xFF))
            return AResult.AE_FAIL;

        if (!mStream.WriteByte((temp >>> 24) & 0xFF))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableFloat(Float value)
    {
        char typeName = 'F';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            int temp = Float.floatToIntBits(value);
            if (!mStream.WriteByte((temp >>> 0) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((temp >>> 8) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((temp >>> 16) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((temp >>> 24) & 0xFF))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteDouble(double value)
    {
        char typeName = 'd';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        long temp = Double.doubleToLongBits(value);
        byte[] writeBuffer = new byte[8];
        writeBuffer[0] = (byte) (temp >>> 0);
        writeBuffer[1] = (byte) (temp >>> 8);
        writeBuffer[2] = (byte) (temp >>> 16);
        writeBuffer[3] = (byte) (temp >>> 24);
        writeBuffer[4] = (byte) (temp >>> 32);
        writeBuffer[5] = (byte) (temp >>> 40);
        writeBuffer[6] = (byte) (temp >>> 48);
        writeBuffer[7] = (byte) (temp >>> 56);

        if (!mStream.Write(writeBuffer))
            return AResult.AE_FAIL;

        return UpdateParcelHeaderLength();
    }

    public int WriteNullableDouble(Double value)
    {
        char typeName = 'D';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte(1))
                return AResult.AE_FAIL;

            long temp = Double.doubleToLongBits(value);
            byte[] writeBuffer = new byte[8];
            writeBuffer[0] = (byte) (temp >>> 0);
            writeBuffer[1] = (byte) (temp >>> 8);
            writeBuffer[2] = (byte) (temp >>> 16);
            writeBuffer[3] = (byte) (temp >>> 24);
            writeBuffer[4] = (byte) (temp >>> 32);
            writeBuffer[5] = (byte) (temp >>> 40);
            writeBuffer[6] = (byte) (temp >>> 48);
            writeBuffer[7] = (byte) (temp >>> 56);

            if (!mStream.Write(writeBuffer))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteByteArray(byte[] value)
    {
        char typeName = 'A';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if ((value == null) || (value.length < 1)) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            if (!mStream.WriteByte((value.length >>> 0) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((value.length >>> 8) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((value.length >>> 16) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte((value.length >>> 24) & 0xFF))
                return AResult.AE_FAIL;

            if (!mStream.Write(value))
                return AResult.AE_FAIL;
        }

        return UpdateParcelHeaderLength();
    }

    public int WriteString(String value)
    {
        char typeName = 'S';
        if (!mStream.WriteByte(typeName))
            return AResult.AE_FAIL;

        if (value == null) {
            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;

            if (!mStream.WriteByte(0))
                return AResult.AE_FAIL;
        }
        else {
            byte[] byteBuffer;
            try {
                byteBuffer = value.getBytes("UTF-8");
            }
            catch (Exception e) {
                return AResult.AE_FAIL;
            }

            if (byteBuffer == null) {
                if (!mStream.WriteByte(0))
                    return AResult.AE_FAIL;

                if (!mStream.WriteByte(0))
                    return AResult.AE_FAIL;

                if (!mStream.WriteByte(0))
                    return AResult.AE_FAIL;

                if (!mStream.WriteByte(0))
                    return AResult.AE_FAIL;
            }
            else {
                if (!mStream.WriteByte((byteBuffer.length >>> 0) & 0xFF))
                    return AResult.AE_FAIL;

                if (!mStream.WriteByte((byteBuffer.length >>> 8) & 0xFF))
                    return AResult.AE_FAIL;

                if (!mStream.WriteByte((byteBuffer.length >>> 16) & 0xFF))
                    return AResult.AE_FAIL;

                if (!mStream.WriteByte((byteBuffer.length >>> 24) & 0xFF))
                    return AResult.AE_FAIL;

                if (!mStream.Write(byteBuffer))
                    return AResult.AE_FAIL;
            }
        }

        return UpdateParcelHeaderLength();
    }

    public byte[] Read(int length) throws Exception
    {
        if (length <= 0)
        	throw new IOException();;

        byte[] bytes = new byte[length];
        if (!mStream.Read(bytes, length))
            throw new IOException();

        return bytes;
    }


    public boolean ReadBoolean() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int ch = mStream.Read();
        if (ch < 0)
            throw new IOException();

        return ch != 0;
    }

    public Boolean ReadNullableBoolean() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
            int ch = mStream.Read();
            if (ch < 0)
                throw new IOException();

            return ch != 0;
        }
        else
            return null;
    }

    public byte ReadByte() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int ch = mStream.Read();
        if (ch < 0)
            throw new IOException();

        return (byte) ch;
    }

    public Byte ReadNullableByte() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
            int ch = mStream.Read();
            if (ch < 0)
                throw new IOException();

            return (byte)ch;
        }
        else
            return null;
    }

    public byte ReadInt8() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int ch = mStream.Read();
        if (ch < 0)
            throw new IOException();

        return (byte) ch;
    }

    public short ReadUInt8() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int ch = mStream.Read();
        if (ch < 0)
            throw new IOException();

        return (short) ch;
    }

    public Byte ReadNullableInt8() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
            int ch = mStream.Read();
            if (ch < 0)
                throw new IOException();

            return (byte)ch;
        }
        else
            return null;
    }

    public Short ReadNullableUInt8() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
            int ch = mStream.Read();
            if (ch < 0)
                throw new IOException();

            return (short)ch;
        }
        else
            return null;
    }

    public short ReadInt16() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int ch1 = mStream.Read();
        int ch2 = mStream.Read();
        if ((ch1 | ch2) < 0)
            throw new IOException();

        if (mIsLittleEndian)
            return (short) ((ch2 << 8) + (ch1 << 0));
        else
            return (short) ((ch1 << 8) + (ch2 << 0));
    }

    public int ReadUInt16() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int ch1 = mStream.Read();
        int ch2 = mStream.Read();
        if ((ch1 | ch2) < 0)
            throw new IOException();

        if (mIsLittleEndian)
            return (int) ((ch2 << 8) + (ch1 << 0));
        else
            return (int) ((ch1 << 8) + (ch2 << 0));
    }

    public Short ReadNullableInt16() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
            int ch1 = mStream.Read();
            int ch2 = mStream.Read();
            if ((ch1 | ch2) < 0)
                throw new IOException();

            if (mIsLittleEndian)
                return (short) ((ch2 << 8) + (ch1 << 0));
            else
                return (short) ((ch1 << 8) + (ch2 << 0));
        }
        else
            return null;
    }

    public Integer ReadNullableUInt16() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
            int ch1 = mStream.Read();
            int ch2 = mStream.Read();
            if ((ch1 | ch2) < 0)
                throw new IOException();

            if (mIsLittleEndian)
                return (int) ((ch2 << 8) + (ch1 << 0));
            else
                return (int) ((ch1 << 8) + (ch2 << 0));
        }
        else
            return null;
    }

    public int ReadInt32() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int ch1 = mStream.Read();
        int ch2 = mStream.Read();
        int ch3 = mStream.Read();
        int ch4 = mStream.Read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new IOException();

        if (mIsLittleEndian)
            return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
        else
            return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public long ReadUInt32() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int ch1 = mStream.Read();
        int ch2 = mStream.Read();
        int ch3 = mStream.Read();
        int ch4 = mStream.Read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new IOException();

        if (mIsLittleEndian)
            return (long)((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
        else
            return (long)((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public Integer ReadNullableInt32() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
            int ch1 = mStream.Read();
            int ch2 = mStream.Read();
            int ch3 = mStream.Read();
            int ch4 = mStream.Read();
            if ((ch1 | ch2 | ch3 | ch4) < 0)
                throw new IOException();

            if (mIsLittleEndian)
                return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
            else
                return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
        }
        else
            return null;
    }

    public Long ReadNullableUInt32() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
            int ch1 = mStream.Read();
            int ch2 = mStream.Read();
            int ch3 = mStream.Read();
            int ch4 = mStream.Read();
            if ((ch1 | ch2 | ch3 | ch4) < 0)
                throw new IOException();

            if (mIsLittleEndian)
                return (long)((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
            else
                return (long)((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
        }
        else
            return null;
    }

    public long ReadInt64() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        byte[] readBuffer = new byte[8];
        if (!mStream.Read(readBuffer, readBuffer.length))
            throw new IOException();

        if (mIsLittleEndian) {
            return (((long) readBuffer[7] << 56) + ((long) (readBuffer[6] & 255) << 48) + ((long) (readBuffer[5] & 255) << 40) + ((long) (readBuffer[4] & 255) << 32) + ((long) (readBuffer[3] & 255) << 24) + ((readBuffer[2] & 255) << 16) + ((readBuffer[1] & 255) << 8) + ((readBuffer[0] & 255) << 0));
        }
        else {
            return (((long) readBuffer[0] << 56) + ((long) (readBuffer[1] & 255) << 48) + ((long) (readBuffer[2] & 255) << 40) + ((long) (readBuffer[3] & 255) << 32) + ((long) (readBuffer[4] & 255) << 24) + ((readBuffer[5] & 255) << 16) + ((readBuffer[6] & 255) << 8) + ((readBuffer[7] & 255) << 0));
        }
    }

    public BigInteger ReadUInt64() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        byte[] readBuffer = new byte[8];
        if (!mStream.Read(readBuffer, readBuffer.length))
            throw new IOException();

        long temp;
        if (mIsLittleEndian) {
            temp = (((long) readBuffer[7] << 56) + ((long) (readBuffer[6] & 255) << 48) + ((long) (readBuffer[5] & 255) << 40) + ((long) (readBuffer[4] & 255) << 32) + ((long) (readBuffer[3] & 255) << 24) + ((readBuffer[2] & 255) << 16) + ((readBuffer[1] & 255) << 8) + ((readBuffer[0] & 255) << 0));
        }
        else {
            temp = (((long) readBuffer[0] << 56) + ((long) (readBuffer[1] & 255) << 48) + ((long) (readBuffer[2] & 255) << 40) + ((long) (readBuffer[3] & 255) << 32) + ((long) (readBuffer[4] & 255) << 24) + ((readBuffer[5] & 255) << 16) + ((readBuffer[6] & 255) << 8) + ((readBuffer[7] & 255) << 0));
        }

        return new BigInteger(Long.toHexString(temp), 16);
    }

    public Long ReadNullableInt64() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
            byte[] readBuffer = new byte[8];
            if (!mStream.Read(readBuffer, readBuffer.length))
                throw new IOException();

            if (mIsLittleEndian) {
                return (((long) readBuffer[7] << 56) + ((long) (readBuffer[6] & 255) << 48) + ((long) (readBuffer[5] & 255) << 40) + ((long) (readBuffer[4] & 255) << 32) + ((long) (readBuffer[3] & 255) << 24) + ((readBuffer[2] & 255) << 16) + ((readBuffer[1] & 255) << 8) + ((readBuffer[0] & 255) << 0));
            }
            else {
                return (((long) readBuffer[0] << 56) + ((long) (readBuffer[1] & 255) << 48) + ((long) (readBuffer[2] & 255) << 40) + ((long) (readBuffer[3] & 255) << 32) + ((long) (readBuffer[4] & 255) << 24) + ((readBuffer[5] & 255) << 16) + ((readBuffer[6] & 255) << 8) + ((readBuffer[7] & 255) << 0));
            }
        }
        else
            return null;
    }

    public BigInteger ReadNullableUInt64() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
             byte[] readBuffer = new byte[8];
            if (!mStream.Read(readBuffer, readBuffer.length))
                throw new IOException();

            long temp;
            if (mIsLittleEndian) {
                temp = (((long) readBuffer[7] << 56) + ((long) (readBuffer[6] & 255) << 48) + ((long) (readBuffer[5] & 255) << 40) + ((long) (readBuffer[4] & 255) << 32) + ((long) (readBuffer[3] & 255) << 24) + ((readBuffer[2] & 255) << 16) + ((readBuffer[1] & 255) << 8) + ((readBuffer[0] & 255) << 0));
            }
            else {
                temp = (((long) readBuffer[0] << 56) + ((long) (readBuffer[1] & 255) << 48) + ((long) (readBuffer[2] & 255) << 40) + ((long) (readBuffer[3] & 255) << 32) + ((long) (readBuffer[4] & 255) << 24) + ((readBuffer[5] & 255) << 16) + ((readBuffer[6] & 255) << 8) + ((readBuffer[7] & 255) << 0));
            }

            return new BigInteger(Long.toHexString(temp), 16);
        }
        else
            return null;
    }

    public float ReadFloat() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int ch1 = mStream.Read();
        int ch2 = mStream.Read();
        int ch3 = mStream.Read();
        int ch4 = mStream.Read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new IOException();

        int temp;
        if (mIsLittleEndian)
            temp = ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
        else
            temp = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));

        return Float.intBitsToFloat(temp);
    }

    public Float ReadNullableFloat() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
            int ch1 = mStream.Read();
            int ch2 = mStream.Read();
            int ch3 = mStream.Read();
            int ch4 = mStream.Read();
            if ((ch1 | ch2 | ch3 | ch4) < 0)
                throw new IOException();

            int temp;
            if (mIsLittleEndian)
                temp = ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
            else
                temp = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));

            return Float.intBitsToFloat(temp);
        }
        else
            return null;
    }

    public double ReadDouble() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        byte[] readBuffer = new byte[8];
        if (!mStream.Read(readBuffer, readBuffer.length))
            throw new IOException();

        long temp;
        if (mIsLittleEndian) {
            temp = (((long) readBuffer[7] << 56) + ((long) (readBuffer[6] & 255) << 48) + ((long) (readBuffer[5] & 255) << 40) + ((long) (readBuffer[4] & 255) << 32) + ((long) (readBuffer[3] & 255) << 24) + ((readBuffer[2] & 255) << 16) + ((readBuffer[1] & 255) << 8) + ((readBuffer[0] & 255) << 0));
        }
        else {
            temp = (((long) readBuffer[0] << 56) + ((long) (readBuffer[1] & 255) << 48) + ((long) (readBuffer[2] & 255) << 40) + ((long) (readBuffer[3] & 255) << 32) + ((long) (readBuffer[4] & 255) << 24) + ((readBuffer[5] & 255) << 16) + ((readBuffer[6] & 255) << 8) + ((readBuffer[7] & 255) << 0));
        }

        return Double.longBitsToDouble(temp);
    }

    public Double ReadNullableDouble() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        int hasValue = mStream.Read();
        if (hasValue < 0)
            throw new IOException();

        if (hasValue != 0) {
            byte[] readBuffer = new byte[8];
            if (!mStream.Read(readBuffer, readBuffer.length))
                throw new IOException();

            long temp;
            if (mIsLittleEndian) {
                temp = (((long) readBuffer[7] << 56) + ((long) (readBuffer[6] & 255) << 48) + ((long) (readBuffer[5] & 255) << 40) + ((long) (readBuffer[4] & 255) << 32) + ((long) (readBuffer[3] & 255) << 24) + ((readBuffer[2] & 255) << 16) + ((readBuffer[1] & 255) << 8) + ((readBuffer[0] & 255) << 0));
            }
            else {
                temp = (((long) readBuffer[0] << 56) + ((long) (readBuffer[1] & 255) << 48) + ((long) (readBuffer[2] & 255) << 40) + ((long) (readBuffer[3] & 255) << 32) + ((long) (readBuffer[4] & 255) << 24) + ((readBuffer[5] & 255) << 16) + ((readBuffer[6] & 255) << 8) + ((readBuffer[7] & 255) << 0));
            }

            return Double.longBitsToDouble(temp);
        }
        else
            return null;
    }

    public byte[] ReadByteArray() throws Exception
    {
        int typeName = mStream.Read();
        if (typeName < 0)
            throw new IOException();

        byte[] bytes = null;
        int ch1 = mStream.Read();
        int ch2 = mStream.Read();
        int ch3 = mStream.Read();
        int ch4 = mStream.Read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new IOException();

        int length;
        if (mIsLittleEndian)
            length = ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
        else
            length = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));

        if (length > 0) {
            bytes = new byte[length];
            if (!mStream.Read(bytes, length))
                throw new IOException();
        }

        return bytes;
    }

    public String ReadString() throws Exception
    {
        byte[] value = ReadByteArray();
        if ((value == null) || (value.length < 1))
            return null;

        return new String(value, "UTF-8");
    }

}
