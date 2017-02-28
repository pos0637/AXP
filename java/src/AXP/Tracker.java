package AXP;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日志追踪器
 */
public class Tracker
{
	private static final String LONG_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
    // 日志文件最大容量 = 5M
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    // 日志文件名称
    private static final String LOG_FILE_NAME = "Log.txt";

    // 备份日志文件名称
    private static final String LOG_BACKUP_FILE_NAME = "BackupLog.txt";

    private static String mFilePath;
    
    public static void Initialize(String filePath)
    {
    	mFilePath = filePath;
    }
    
    public static void d(String content)
    {
        WriteLogFile(MakeLog("Debug", content));
    }

    public static void i(String content)
    {
        WriteLogFile(MakeLog("Info", content));
    }

    public static void w(String content)
    {
        WriteLogFile(MakeLog("Warn", content));
    }

    public static void e(String content)
    {
        WriteLogFile(MakeLog("Error", content));
    }

    public static void e(Throwable e)
    {
        WriteLogFile(MakeLog("Error", GetStackTrace(e)));
    }

    public static void f(String content)
    {
        WriteLogFile(MakeLog("Fatal", content));
    }

    private static String MakeLog(String logType, String content)
    {
        if ((logType == null) || (logType.length() <= 0) || (content == null) || (content.length() <= 0))
            return null;

        // 获取调用堆栈信息
        StackTraceElement elem;
        try {
            elem = new Throwable().getStackTrace()[2];
        }
        catch (Exception e) {
            return null;
        }

        try {
            SimpleDateFormat date = new SimpleDateFormat(
                LONG_DATE_TIME_FORMAT, Locale.getDefault());
            String currentTime = date.format(new Date());
            return String.format(
                    "[%s] [%s] %s(%s): %s\r\n", currentTime,
                    logType, elem.getFileName(), elem.getLineNumber(), content);            
        }
        catch (Exception e) {
            Tracker.e(e);
            return null;
        }
    }

    private static String GetStackTrace(final Throwable throwable)
    {
        try {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw, true);
            throwable.printStackTrace(pw);
            return sw.getBuffer().toString();
        }
        catch (Exception e) {
            return null;
        }
    }

    private static void WriteLogFile(String log)
    {
        try {
            String filePath = mFilePath;
            if (filePath == null)
            	return;
            
            String logFileName = filePath + File.separatorChar + LOG_FILE_NAME;
            String backupLogFileName = filePath + File.separatorChar + LOG_BACKUP_FILE_NAME;

            // 检查日志文件存放目录是否存在
            File logPathFile = new File(filePath);
            if (!logPathFile.exists()) {
                if (!logPathFile.mkdirs())
                    return;
            }

            File logFile = new File(logFileName);
            if (logFile.exists() && (logFile.length() >= MAX_FILE_SIZE)) {
                // 日志文件超过大小，进行备份，并删除原文件
                CopyTo(logFileName, backupLogFileName, true);
            }

            DataOutputStream stream = new DataOutputStream(
                new FileOutputStream(logFile, true));
            stream.write(log.getBytes());
            stream.flush();
            stream.close();
        }
        catch (Exception e) {
        }
    }
    
    private static boolean CopyTo(String src, String dst, boolean cut)
    {
        try {
            File srcFile = new File(src);
            File dstFile = new File(dst);
            return CopyTo(srcFile, dstFile, cut);
        }
        catch (Exception e) {
            Tracker.e(e);
            return false;
        }
    }
    
    private static boolean CopyTo(File src, File dst, boolean cut)
    {
        if ((src == null) || (dst == null))
            return false;

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            if (dst.exists()) {
                if (!dst.delete())
                    return false;
            }

            inputStream = new FileInputStream(src);
            outputStream = new FileOutputStream(dst);
            FileDescriptor fd = outputStream.getFD();
            byte[] buffer = new byte[4 * 1024];
            int count = -1;

            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
                outputStream.flush();
                fd.sync();
            }

            if (cut) {
                if (src.exists()) {
                    if (!src.delete())
                        return false;
                }
            }

            return true;
        }
        catch (Exception e) {
            Tracker.e(e);

            if (dst.exists())
                dst.delete();

            return false;
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                }
                catch (IOException e) {
                }
            }
        }
    }    
}