using System;
using System.Text;

namespace AXP
{
    /// <summary>
    /// 定义数据封包接口类型
    /// </summary>
    public interface IParcelable
    {
        Int32 ReadFromParcel(Parcel parcel);

        Int32 WriteToParcel(Parcel parcel);
    }

    /// <summary>
    /// 定义数据封包类型
    /// </summary>
    public class Parcel : IDisposable
    {
        System.IO.Stream mStream;
        System.IO.BinaryReader mReader;
        System.IO.BinaryWriter mWriter;
        Boolean mIsLittleEndian = true;

        public Parcel()
        {
            mStream = new System.IO.MemoryStream();
            mReader = new System.IO.BinaryReader(mStream);
            mWriter = new System.IO.BinaryWriter(mStream);

            mIsLittleEndian = true;
            mWriter.Write(mIsLittleEndian);
            Int32 len = 0;
            mWriter.Write(len);
        }

        public Parcel(System.IO.MemoryStream stream)
        {
            mStream = stream;
            mReader = new System.IO.BinaryReader(mStream);
            mWriter = new System.IO.BinaryWriter(mStream);
            Reset();
        }

        public void Dispose()
        {
            if (mReader != null) {
                mReader.Close();
                mReader.Dispose();
                mReader = null;
            }

            if (mWriter != null) {
                mWriter.Close();
                mWriter.Dispose();
                mWriter = null;
            }

            if (mStream != null) {
                mStream.Close();
                mStream.Dispose();
                mStream = null;
            }
        }

        internal Int32 UpdateHeaderLength()
        {
            Int32 len = (Int32)mStream.Position - 5;
            mStream.Position = 1;
            mWriter.Write(len);
            mStream.Position = len + 5;

            mStream.SetLength(mStream.Position);
            return AResult.AS_OK;
        }

        public Int32 Reset()
        {
            if ((mStream == null) || (mReader == null))
                return AResult.AE_FAIL;

            mStream.Position = 0;
            if ((mStream.Length - mStream.Position) < (sizeof(Boolean) + sizeof(Int32)))
                return AResult.AE_FAIL;

            mIsLittleEndian = mReader.ReadBoolean();
            mReader.ReadInt32();

            return AResult.AS_OK;
        }

        public Int32 Reset(Byte[] buffer)
        {

            if (mReader != null) {
                mReader.Close();
                mReader.Dispose();
                mReader = null;
            }

            if (mWriter != null) {
                mWriter.Close();
                mWriter.Dispose();
                mWriter = null;
            }

            if (mStream != null) {
                mStream.Close();
                mStream.Dispose();
                mStream = null;
            }

            mStream = new System.IO.MemoryStream(buffer);
            mReader = new System.IO.BinaryReader(mStream);
            mWriter = new System.IO.BinaryWriter(mStream);

            mStream.Position = 0;
            if ((mStream.Length - mStream.Position) < (sizeof(Boolean) + sizeof(Int32)))
                return AResult.AE_FAIL;

            mIsLittleEndian = mReader.ReadBoolean();
            mReader.ReadInt32();

            return AResult.AS_OK;
        }

        public Int64 GetPosition()
        {
            if (mStream == null)
                return -1;

            return mStream.Position;
        }

        public void Seek(Int64 position)
        {
            if (mStream == null)
                return;

            mStream.Position = position;
        }

        public Int64 GetLength()
        {
            if (mStream == null)
                return -1;

            return mStream.Length;
        }

        public Byte[] GetPayload()
        {
            if (mStream == null)
                return null;
            else {
                System.IO.MemoryStream stream = (System.IO.MemoryStream)mStream;
                Byte[] buffer = new Byte[stream.Length];
                Array.Copy(stream.ToArray(), buffer, stream.Length);

                return buffer;
            }
        }

        #region 读取

        public Int32 Read(ref Byte[] value, Int64 length)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < length))
                return AResult.AE_FAIL;

            value = mReader.ReadBytes((Int32)length);

            return AResult.AS_OK;
        }

        public Int32 ReadBoolean(ref Boolean value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Boolean)) + 1))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            value = mReader.ReadBoolean();

            return AResult.AS_OK;
        }

        public Int32 ReadByte(ref SByte value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(SByte) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            value = mReader.ReadSByte();

            return AResult.AS_OK;
        }

        public Int32 ReadInt8(ref SByte value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(SByte) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            value = mReader.ReadSByte();

            return AResult.AS_OK;
        }

        public Int32 ReadUInt8(ref Byte value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Byte) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            value = mReader.ReadByte();

            return AResult.AS_OK;
        }

        public Int32 ReadInt16(ref Int16 value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Int16) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            value = mReader.ReadInt16();
            if (!mIsLittleEndian) {
                UInt16 val = (UInt16)value;
                val = ByteOrder.Swap16(val);
                value = (Int16)val;
            }

            return AResult.AS_OK;
        }

        public Int32 ReadUInt16(ref UInt16 value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(UInt16) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            value = mReader.ReadUInt16();
            if (!mIsLittleEndian)
                value = ByteOrder.Swap16(value);

            return AResult.AS_OK;
        }

        public Int32 ReadInt32(ref Int32 value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Int32) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            value = mReader.ReadInt32();
            if (!mIsLittleEndian) {
                UInt32 val = (UInt32)value;
                val = ByteOrder.Swap32(val);
                value = (Int32)val;
            }

            return AResult.AS_OK;
        }

        public Int32 ReadUInt32(ref UInt32 value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(UInt32) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            value = mReader.ReadUInt32();
            if (!mIsLittleEndian)
                value = ByteOrder.Swap32(value);

            return AResult.AS_OK;
        }

        public Int32 ReadInt64(ref Int64 value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Int64) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            value = mReader.ReadInt64();
            if (!mIsLittleEndian) {
                UInt64 val = (UInt64)value;
                val = ByteOrder.Swap64(val);
                value = (Int64)val;
            }

            return AResult.AS_OK;
        }

        public Int32 ReadUInt64(ref UInt64 value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(UInt64) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            value = mReader.ReadUInt64();
            if (!mIsLittleEndian)
                value = ByteOrder.Swap64(value);

            return AResult.AS_OK;
        }

        public Int32 ReadFloat(ref Single value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Single) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            if (mIsLittleEndian)
                value = mReader.ReadSingle();
            else {
                UInt32 val = mReader.ReadUInt32();
                val = ByteOrder.Swap32(val);
                Byte[] b = BitConverter.GetBytes(val);
                value = BitConverter.ToSingle(b, 0);
            }

            return AResult.AS_OK;
        }

        public Int32 ReadDouble(ref Double value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Double) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            if (mIsLittleEndian)
                value = mReader.ReadDouble();
            else {
                UInt64 val = mReader.ReadUInt64();
                val = ByteOrder.Swap64(val);
                Byte[] b = BitConverter.GetBytes(val);
                value = BitConverter.ToDouble(b, 0);
            }

            return AResult.AS_OK;
        }

        public Int32 ReadNullableBoolean(ref Boolean? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Boolean)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue)
                value = mReader.ReadBoolean();
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadNullableByte(ref SByte? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(SByte)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue)
                value = mReader.ReadSByte();
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadNullableInt8(ref SByte? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(SByte)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue)
                value = mReader.ReadSByte();
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadNullableUInt8(ref Byte? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Byte)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue)
                value = mReader.ReadByte();
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadNullableInt16(ref Int16? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Int16)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue) {
                value = mReader.ReadInt16();
                if (!mIsLittleEndian) {
                    UInt16 val = (UInt16)value;
                    val = ByteOrder.Swap16(val);
                    value = (Int16)val;
                }
            }
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadNullableUInt16(ref UInt16? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(UInt16)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue) {
                value = mReader.ReadUInt16();
                if (!mIsLittleEndian) {
                    UInt16 val = (UInt16)value;
                    val = ByteOrder.Swap16(val);
                    value = (UInt16)val;
                }
            }
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadNullableInt32(ref Int32? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Int32)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue) {
                value = mReader.ReadUInt16();
                if (!mIsLittleEndian) {
                    UInt32 val = (UInt32)value;
                    val = ByteOrder.Swap32(val);
                    value = (Int32)val;
                }
            }
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadNullableUInt32(ref UInt32? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(UInt32)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue) {
                value = mReader.ReadUInt16();
                if (!mIsLittleEndian) {
                    UInt32 val = (UInt32)value;
                    val = ByteOrder.Swap32(val);
                    value = (UInt32)val;
                }
            }
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadNullableInt64(ref Int64? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Int64)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue) {
                value = mReader.ReadUInt16();
                if (!mIsLittleEndian) {
                    UInt64 val = (UInt64)value;
                    val = ByteOrder.Swap64(val);
                    value = (Int64)val;
                }
            }
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadNullableUInt64(ref UInt64? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(UInt64)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue) {
                value = mReader.ReadUInt16();
                if (!mIsLittleEndian) {
                    UInt64 val = (UInt64)value;
                    val = ByteOrder.Swap64(val);
                    value = (UInt64)val;
                }
            }
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadNullableFloat(ref Single? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Single)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue) {
                if (mIsLittleEndian)
                    value = mReader.ReadSingle();
                else {
                    UInt32 val = mReader.ReadUInt32();
                    val = ByteOrder.Swap32(val);
                    Byte[] b = BitConverter.GetBytes(val);
                    value = BitConverter.ToSingle(b, 0);
                }
            }
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadNullableDouble(ref Double? value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Single)) + 2))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Boolean hasValue = mReader.ReadBoolean();
            if (hasValue) {
                if (mIsLittleEndian)
                    value = mReader.ReadDouble();
                else {
                    UInt64 val = mReader.ReadUInt64();
                    val = ByteOrder.Swap64(val);
                    Byte[] b = BitConverter.GetBytes(val);
                    value = BitConverter.ToDouble(b, 0);
                }
            }
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadString(ref String value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Int32) + 1)))
                return AResult.AE_FAIL;

            mReader.ReadSByte();
            Int32 len = mReader.ReadInt32();
            if (len > 0) {
                if ((mStream.Length - mStream.Position) < len)
                    return AResult.AE_FAIL;

                Byte[] data = mReader.ReadBytes(len);
                value = Encoding.UTF8.GetString(data);
            }
            else
                value = null;

            return AResult.AS_OK;
        }

        public Int32 ReadByteArray(ref Byte[] value)
        {
            if ((mStream == null)
                || (mReader == null)
                || ((mStream.Length - mStream.Position) < (sizeof(Int32) + 1)))
                return AResult.AE_FAIL;

            Int32 len = mReader.ReadInt32();
            if (len > 0) {
                if ((mStream.Length - mStream.Position) < len)
                    return AResult.AE_FAIL;

                value = mReader.ReadBytes(len);
            }
            else
                value = null;

            return AResult.AS_OK;
        }

        #endregion

        #region 写入

        public Int32 Write(Byte[] value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteBoolean(Boolean value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'z';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteByte(SByte value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'c';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteInt8(SByte value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'b';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteInt16(Int16 value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'r';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteInt32(Int32 value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'i';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteInt64(Int64 value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'g';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteUInt8(Byte value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'e';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteUInt16(UInt16 value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'n';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteUInt32(UInt32 value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'u';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteUInt64(UInt64 value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'m';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteFloat(Single value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'f';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteDouble(Double value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'d';
            mWriter.Write(typeName);
            mWriter.Write(value);

            return UpdateHeaderLength();
        }

        public Int32 WriteNullBoolean(Boolean? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'Z';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteNullableByte(SByte? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'C';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteNullableInt8(SByte? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'B';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteNullableInt16(Int16? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'R';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteNullableInt32(Int32? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'I';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteNullableInt64(Int64? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'G';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteNullableUInt8(Byte? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'E';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteNullableUInt16(UInt16? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'N';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteNullableUInt32(UInt32? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'U';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteNullableUInt64(UInt64? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'M';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteNullableFloat(Single? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'F';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteNullableDouble(Double? value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'D';
            mWriter.Write(typeName);

            if ((value == null) || (!value.HasValue)) {
                mWriter.Write(false);
            }
            else {
                mWriter.Write(true);
                mWriter.Write(value.Value);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteString(String value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'S';
            mWriter.Write(typeName);

            if (value == null) {
                Int32 len = 0;
                mWriter.Write(len);
            }
            else {
                Byte[] data = Encoding.UTF8.GetBytes(value);
                if (data == null)
                    return AResult.AE_FAIL;

                mWriter.Write(data.Length);
                mWriter.Write(data);
            }

            return UpdateHeaderLength();
        }

        public Int32 WriteByteArray(Byte[] value)
        {
            if ((mWriter == null) || (mStream == null))
                return AResult.AE_FAIL;

            SByte typeName = (SByte)'A';
            mWriter.Write(typeName);

            if (value == null) {
                Int32 len = 0;
                mWriter.Write(len);
            }
            else {
                mWriter.Write(value.Length);
                mWriter.Write(value);
            }

            return UpdateHeaderLength();
        }

        #endregion
    }
}