﻿using System;
using System.IO;
using System.Text;
using System.Threading;
using UnityEngine;

public class Rms
{
    public static int status;

    public static sbyte[] data;

    public static string filename;

    private const int INTERVAL = 5;

    private const int MAXTIME = 500;

    public static void saveRMS(string filename, sbyte[] data)
    {
        if (Thread.CurrentThread.Name == Main.mainThreadName)
        {
            __saveRMS(filename, data);
        }
        else
        {
            _saveRMS(filename, data);
        }
    }

    public static sbyte[] loadRMS(string filename)
    {
        if (Thread.CurrentThread.Name == Main.mainThreadName)
        {
            return __loadRMS(filename);
        }
        return _loadRMS(filename);
    }

    public static string loadRMSString(string fileName)
    {
        sbyte[] array = loadRMS(fileName);
        if (array == null)
        {
            return null;
        }
        DataInputStream dataInputStream = new DataInputStream(array);
        try
        {
            string result = dataInputStream.readUTF();
            dataInputStream.close();
            return result;
        }
        catch (Exception ex)
        {
            Cout.println(ex.StackTrace);
        }
        return null;
    }

    public static byte[] convertSbyteToByte(sbyte[] var)
    {
        byte[] array = new byte[var.Length];
        for (int i = 0; i < var.Length; i++)
        {
            if (var[i] > 0)
            {
                array[i] = (byte)var[i];
            }
            else
            {
                array[i] = (byte)(var[i] + 256);
            }
        }
        return array;
    }

    public static void saveRMSString(string filename, string data)
    {
        DataOutputStream dataOutputStream = new DataOutputStream();
        try
        {
            dataOutputStream.writeUTF(data);
            saveRMS(filename, dataOutputStream.toByteArray());
            dataOutputStream.close();
        }
        catch (Exception ex)
        {
            Cout.println(ex.StackTrace);
        }
    }

    private static void _saveRMS(string filename, sbyte[] data)
    {
        if (status != 0)
        {
            Debug.LogError("Cannot save RMS " + filename + " because current is saving " + Rms.filename);
            return;
        }
        Rms.filename = filename;
        Rms.data = data;
        status = 2;
        int i;
        for (i = 0; i < 500; i++)
        {
            Thread.Sleep(5);
            if (status == 0)
            {
                break;
            }
        }
        if (i == 500)
        {
            Debug.LogError("TOO LONG TO SAVE RMS " + filename);
        }
    }

    private static sbyte[] _loadRMS(string filename)
    {
        if (status != 0)
        {
            Debug.LogError("Cannot load RMS " + filename + " because current is loading " + Rms.filename);
            return null;
        }
        Rms.filename = filename;
        data = null;
        status = 3;
        int i;
        for (i = 0; i < 500; i++)
        {
            Thread.Sleep(5);
            if (status == 0)
            {
                break;
            }
        }
        if (i == 500)
        {
            Debug.LogError("TOO LONG TO LOAD RMS " + filename);
        }
        return data;
    }

    public static void update()
    {
        if (status == 2)
        {
            status = 1;
            __saveRMS(filename, data);
            status = 0;
        }
        else if (status == 3)
        {
            status = 1;
            data = __loadRMS(filename);
            status = 0;
        }
    }

    public static int loadRMSInt(string file)
    {
        sbyte[] array = loadRMS(file);
        return (array != null) ? array[0] : (-1);
    }

    public static void saveRMSInt(string file, int x)
    {
        try
        {
            saveRMS(file, new sbyte[1] { (sbyte)x });
        }
        catch (Exception)
        {
        }
    }
    public static long loadRMSLong(string file)
    {
        sbyte[] array = loadRMS(file);
        if (array == null || array.Length < 8)
        {
            return -1L;
        }

        // Ghép 8 byte thành một giá trị long
        long result = 0L;
        for (int i = 0; i < 8; i++)
        {
            result |= ((long)(array[i] & 0xFF) << (8 * i));
        }

        return result;
    }
    public static void saveRMSLong(string file, long x)
    {
        try
        {
            // Tách long thành 8 byte
            sbyte[] array = new sbyte[8];
            for (int i = 0; i < 8; i++)
            {
                array[i] = (sbyte)((x >> (8 * i)) & 0xFF);
            }

            saveRMS(file, array);
        }
        catch (Exception)
        {
            // Xử lý ngoại lệ ở đây nếu cần
        }
    }
    public static string GetiPhoneDocumentsPath()
    {
        return Application.persistentDataPath;
    }

    private static string StringToHex(string str)
    {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = Encoding.UTF8.GetBytes(str);
        foreach (byte b in bytes)
        {
            stringBuilder.Append(b.ToString("X2"));
        }
        return stringBuilder.ToString();
    }

    private static void __saveRMS(string filename, sbyte[] data)
    {
        try
        {
            filename = StringToHex(filename);
            string text = GetiPhoneDocumentsPath() + "/" + filename;
            FileStream fileStream = new FileStream(text, FileMode.Create);
            fileStream.Write(ArrayCast.cast(data), 0, data.Length);
            fileStream.Flush();
            fileStream.Close();
            Main.setBackupIcloud(text);
        }
        catch (Exception e)
        {
            Debug.LogException(e);
        }
    }

    private static sbyte[] __loadRMS(string filename)
    {
        var filenamehex = filename;
        try
        {
            filenamehex = StringToHex(filename);
            FileStream fileStream = new FileStream(GetiPhoneDocumentsPath() + "/" + filenamehex, FileMode.Open);
            byte[] array = new byte[fileStream.Length];
            fileStream.Read(array, 0, array.Length);
            fileStream.Close();
            return ArrayCast.cast(array);
        }
        catch (Exception e)
        {
            //Debug.LogError("error load rms: " + filename + " - " + filenamehex);
            //Debug.LogError(e.ToString());
            return null;
        }
    }

    public static void clearAll()
    {
        Cout.LogError3("clean rms");
        FileInfo[] files = new DirectoryInfo(GetiPhoneDocumentsPath() + "/").GetFiles();
        foreach (FileInfo fileInfo in files)
        {
            try
            { fileInfo.Delete(); }
            catch { }
        }
    }

    public static void DeleteStorage(string path)
    {
        try
        {
            File.Delete(GetiPhoneDocumentsPath() + "/" + path);
        }
        catch (Exception)
        {
        }
    }

    public static string ByteArrayToString(byte[] ba)
    {
        string text = BitConverter.ToString(ba);
        return text.Replace("-", string.Empty);
    }

    public static byte[] StringToByteArray(string hex)
    {
        int length = hex.Length;
        byte[] array = new byte[length / 2];
        for (int i = 0; i < length; i += 2)
        {
            array[i / 2] = Convert.ToByte(hex.Substring(i, 2), 16);
        }
        return array;
    }

    public static void deleteRecord(string name)
    {
        try
        {
            PlayerPrefs.DeleteKey(name);
        }
        catch (Exception ex)
        {
            Cout.println("loi xoa RMS --------------------------" + ex.ToString());
        }
    }

    public static void clearRMS()
    {
        deleteRecord("data");
        deleteRecord("dataVersion");
        deleteRecord("map");
        deleteRecord("mapVersion");
        deleteRecord("skill");
        deleteRecord("killVersion");
        deleteRecord("item");
        deleteRecord("itemVersion");
    }

    public static void saveIP(string strID)
    {
        saveRMSString("NRIPlink", strID);
    }

    public static string loadIP()
    {
        string text = loadRMSString("NRIPlink");
        if (text == null)
        {
            return null;
        }
        return text;
    }
}
