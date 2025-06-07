using System;
using System.Threading;
using UnityEngine;

public class DataInputStream
{
	public myReader r;

	private const int INTERVAL = 5;

        private const int MAXTIME = 500;

        private static readonly AutoResetEvent operationEvent = new AutoResetEvent(false);

	public static DataInputStream istemp;

	private static int status;

	private static string filenametemp;

	public DataInputStream(string filename)
	{
		TextAsset textAsset = (TextAsset)Resources.Load(filename, typeof(TextAsset));
		r = new myReader(ArrayCast.cast(textAsset.bytes));
	}

	public DataInputStream(sbyte[] data)
	{
		r = new myReader(data);
	}

        public static void update()
        {
                if (status == 2)
                {
                        status = 1;
                        istemp = __getResourceAsStream(filenametemp);
                        status = 0;
                        operationEvent.Set();
                }
        }

	public static DataInputStream getResourceAsStream(string filename)
	{
		return __getResourceAsStream(filename);
	}

	private static DataInputStream _getResourceAsStream(string filename)
	{
                if (status != 0)
                {
                        operationEvent.WaitOne(INTERVAL * MAXTIME);
                        if (status != 0)
                        {
                                Debug.LogError("CANNOT GET INPUTSTREAM " + filename + " WHEN GETTING " + filenametemp);
                                return null;
                        }
                }
                istemp = null;
                filenametemp = filename;
                operationEvent.Reset();
                status = 2;
                if (!operationEvent.WaitOne(INTERVAL * MAXTIME))
                {
                        Debug.LogError("TOO LONG FOR CREATE INPUTSTREAM " + filename);
                        status = 0;
                        return null;
                }
                return istemp;
        }

	private static DataInputStream __getResourceAsStream(string filename)
	{
		try
		{
			return new DataInputStream(filename);
		}
		catch (Exception)
		{
			return null;
		}
	}

	public short readShort()
	{
		return r.readShort();
	}

	public int readInt()
	{
		return r.readInt();
	}

	public int read()
	{
		return r.readUnsignedByte();
	}

	public void read(ref sbyte[] data)
	{
		r.read(ref data);
	}

	public void close()
	{
		r.Close();
	}

	public void Close()
	{
		r.Close();
	}

	public string readUTF()
	{
		return r.readUTF();
	}

	public sbyte readByte()
	{
		return r.readByte();
	}

	public long readLong()
	{
		return r.readLong();
	}

	public bool readBoolean()
	{
		return r.readBoolean();
	}

	public int readUnsignedByte()
	{
		return (byte)r.readByte();
	}

	public int readUnsignedShort()
	{
		return r.readUnsignedShort();
	}

	public void readFully(ref sbyte[] data)
	{
		r.read(ref data);
	}

	public int available()
	{
		return r.available();
	}

	internal void read(ref sbyte[] byteData, int p, int size)
	{
		throw new NotImplementedException();
	}
}
