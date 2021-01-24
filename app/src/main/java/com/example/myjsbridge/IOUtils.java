package com.example.myjsbridge;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Xml.Encoding;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import androidx.annotation.NonNull;

public final class IOUtils {
    private static final String TAG = "IOUtils";

    private static final int BUFFER_SIZE = 1024; // 流转换的缓存大小
    private static final int CONNECT_TIMEOUT = 3000; // 从网络下载文件时的连接超时时间

    /**
     * 从Assets读取文字
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readStringFromAssets(Context context, String fileName) {
        return readStringFromAssets(context, fileName,
                Encoding.UTF_8.toString());
    }

    /**
     * 从Assets读取文字
     *
     * @param context
     * @param fileName
     * @param encoding
     * @return
     */
    public static String readStringFromAssets(Context context, String fileName,
                                              String encoding) {
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            is = context.getAssets().open(fileName);
            byte[] buffer = new byte[BUFFER_SIZE];

            baos = new ByteArrayOutputStream();
            while (true) {
                int read = is.read(buffer);
                if (read == -1) {
                    break;
                }
                baos.write(buffer, 0, read);
            }
            return baos.toString(encoding);
        } catch (Exception e) {
            e.printStackTrace()
            ;
            return "";
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace()
                ;
            }
        }
    }

    /**
     * 从资源中读取文字
     *
     * @param context
     * @param resId
     * @return
     */
    public static String readStringFromRes(Context context, int resId) {
        return readStringFromRes(context, resId, Encoding.UTF_8.toString());
    }

    /**
     * 从资源中读取文字
     *
     * @param context
     * @param resId
     * @param encoding
     * @return
     */
    public static String readStringFromRes(Context context, int resId,
                                           String encoding) {
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            is = context.getResources().openRawResource(resId);
            byte[] buffer = new byte[BUFFER_SIZE];

            baos = new ByteArrayOutputStream();
            while (true) {
                int read = is.read(buffer);
                if (read == -1) {
                    break;
                }
                baos.write(buffer, 0, read);
            }
            return baos.toString(encoding);
        } catch (Exception e) {
            e.printStackTrace()
            ;
            return "";
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace()
                ;
            }
        }
    }

    /**
     * 从指定路径的文件中读取Bytes
     */
    public static byte[] readBytes(String path) {
        try {
            File file = new File(path);
            return readBytes(file);
        } catch (Exception e) {
            e.printStackTrace()
            ;
            return null;
        }
    }

    /**
     * 从指定资源中读取Bytes
     */
    public static byte[] readBytes(Context context, int resId) {
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(resId);
            return readBytes(is);
        } catch (Exception e) {
            e.printStackTrace()
            ;
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace()
                ;
            }
        }
    }

    /**
     * 从File中读取Bytes
     */
    public static byte[] readBytes(File file) {
        FileInputStream fis = null;
        try {
            if (!file.exists()) {
                return null;
            }
            fis = new FileInputStream(file);
            return readBytes(fis);
        } catch (Exception e) {
            e.printStackTrace()
            ;
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace()
                ;
            }
        }
    }

    /**
     * 从Url中读取Bytes
     */
    public static byte[] readBytes(URL url) {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.connect();
            is = conn.getInputStream();
            return readBytes(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace()
                ;
            }
        }
    }

    /**
     * 从InputStream中读取Bytes
     */
    public static byte[] readBytes(InputStream is) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = 0;
            while ((length = is.read(buffer, 0, BUFFER_SIZE)) != -1) {
                baos.write(buffer, 0, length);
                baos.flush();
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace()
            ;
            return null;
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace()
                ;
            }
        }
    }

    public static byte[] readBytes(Object object) {
        if (object == null) return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
            }
        }
    }

    public static String readString(String path) {
        return readString(path, Encoding.UTF_8.toString());
    }

    public static String readString(String path, String encoding) {
        try {
            byte[] data = readBytes(path);
            if (data != null) {
                return new String(data, encoding);
            }
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace()
            ;
            return "";
        }
    }

    public static String readStringFromSD(String fileName) {
        return readStringFromSD(fileName, Encoding.UTF_8.toString());
    }

    public static String readStringFromSD(String fileName, String encoding) {
        try {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return "";
            }
            String strPath = Environment.getExternalStorageDirectory() + "/"
                    + fileName;
            return readString(strPath, encoding);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 将InputStream写入File
     */
    public static boolean writeToFile(File file, InputStream is) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = 0;
            while ((length = is.read(buffer, 0, BUFFER_SIZE)) != -1) {
                fos.write(buffer, 0, length);
                fos.flush();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace()
            ;
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean writeToFile(File file, String text) {
        return writeToFile(file, text, Encoding.UTF_8.toString(), false);
    }

    public static boolean writeToFile(File file, String text, boolean append) {
        return writeToFile(file, text, Encoding.UTF_8.toString(), append);
    }

    public static boolean writeToFile(File file, String text, String encoding) {
        try {
            return writeToFile(file, text.getBytes(encoding), false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeToFile(File file, String text, String encoding,
                                      boolean append) {
        try {
            return writeToFile(file, text.getBytes(encoding), append);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeToFile(File file, byte[] buffer) {
        return writeToFile(file, buffer, false);
    }

    public static boolean writeToFile(File file, byte[] buffer, boolean append) {
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file, append);
            fos.write(buffer);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static boolean writeToSD(String fileName, String text) {
        return writeToSD(fileName, text, Encoding.UTF_8.toString(), false);
    }

    public static boolean writeToSD(String fileName, String text, boolean append) {
        return writeToSD(fileName, text, Encoding.UTF_8.toString(), append);
    }

    public static boolean writeToSD(String fileName, String text,
                                    String encoding) {
        return writeToSD(fileName, text, encoding, false);
    }

    public static boolean writeToSD(String fileName, String text,
                                    String encoding, boolean append) {
        try {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return false;
            }
            String strPath = Environment.getExternalStorageDirectory() + "/"
                    + fileName;
            File file = new File(strPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            return writeToFile(file, text, encoding, append);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean writeToSDFromAssets(@NonNull Context context, @NonNull String fileName, @NonNull String outPath) {
        AssetManager assetManager = context.getAssets();
        String newFileName = outPath + "/" + fileName;
        try (InputStream in = assetManager.open(fileName);
             OutputStream out = new FileOutputStream(newFileName)
        ) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 下载文件至存储卡
     */
    public static File downloadFileToSD(String strUrl, String dirPath) {
        return downloadFile(strUrl, Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/"
                + dirPath, null);
    }

    /**
     * 下载文件至存储卡
     */
    public static File downloadFileToSD(String strUrl, String dirPath,
                                        String saveName) {
        return downloadFile(strUrl, Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/"
                + dirPath, saveName);
    }

    /**
     * 下载文件至指定目录
     */
    public static File downloadFile(String strUrl, String dirPath) {
        return downloadFile(strUrl, dirPath, null);
    }

    /**
     * 下载文件至指定目录
     *
     * @param strUrl   文件的url
     * @param dirPath  存储文件的目录
     * @param saveName 存储的文件名
     */
    public static File downloadFile(String strUrl, String dirPath,
                                    String saveName) {
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String fileEx = strUrl.substring(strUrl.lastIndexOf(".") + 1,
                    strUrl.length()).toLowerCase();
            String fileName = strUrl.substring(strUrl.lastIndexOf("/") + 1,
                    strUrl.lastIndexOf("."));

            trustAllHttpsCertificates();
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            URL myURL = new URL(strUrl);
            conn = (HttpURLConnection) myURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.connect();
            is = conn.getInputStream();

            if (saveName == null) {
                saveName = fileName + "." + fileEx;
            }
            File file = new File(dirPath, saveName);
            writeToFile(file, is);
            return file;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace()
                ;
            }
        }
    }

    /**
     * https请求信任证书
     *
     * @throws Exception
     */

    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    private static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }


    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) return file.delete();
        return false;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean isListEmpty(List list) {
        return list == null || list.size() <= 0;
    }

    public static boolean isArrayEmpty(Object[] array) {
        return array == null || array.length <= 0;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean createFolder(File targetFolder) {
        if (targetFolder.exists()) {
            if (targetFolder.isDirectory()) return true;
            //noinspection ResultOfMethodCallIgnored
            targetFolder.delete();
        }
        return targetFolder.mkdirs();
    }

    /**
     * Delete file or folder.
     *
     * @param file file.
     * @return is succeed.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean delFileOrFolder(File file) {
        if (file == null || !file.exists()) {
            // do nothing
        } else if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File sonFile : files) {
                    delFileOrFolder(sonFile);
                }
            }
            file.delete();
        }
        return true;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
