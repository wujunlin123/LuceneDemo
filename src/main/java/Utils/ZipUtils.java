package Utils;


import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {
    public ZipUtils(){}

    /**
     * @param sourcefiles 源文件(服务器上的zip包存放地址)
     * @param decompreDirectory 解压缩后文件存放的目录
     * @throws IOException IO异常
     */
    @SuppressWarnings("unchecked")
    public static void unzip(String sourcefiles, String decompreDirectory) throws IOException {
        ZipFile readfile = null;
        try {
            readfile =new ZipFile(sourcefiles);
            Enumeration takeentrie =  readfile.entries();
            ZipEntry zipEntry = null;
            File credirectory = new File(decompreDirectory);
            credirectory.mkdirs();
            while (takeentrie.hasMoreElements()) {
                zipEntry = (ZipEntry) takeentrie.nextElement();
                String entryName = zipEntry.getName();
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    if (zipEntry.isDirectory()) {
                        String name = zipEntry.getName();
                        name = name.substring(0, name.length() - 1);
                        File  createDirectory = new File(decompreDirectory+ File.separator + name);
                        createDirectory.mkdirs();
                    } else {
                        int index = entryName.lastIndexOf("\\");
                        if (index != -1) {
                            File createDirectory = new File(decompreDirectory+ File.separator+ entryName.substring(0, index));
                            createDirectory.mkdirs();
                        }
                        index = entryName.lastIndexOf("/");
                        if (index != -1) {
                            File createDirectory = new File(decompreDirectory + File.separator + entryName.substring(0, index));
                            createDirectory.mkdirs();
                        }
                        File unpackfile = new File(decompreDirectory + File.separator + zipEntry.getName());
                        in = readfile.getInputStream(zipEntry);
                        out = new FileOutputStream(unpackfile);
                        int c;
                        byte[] by = new byte[1024];
                        while ((c = in.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                        out.flush();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new IOException("解压失败：" + ex.toString());
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ex) {

                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    in=null;
                    out=null;
                }

            }
            System.out.println("1111111");
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException("解压失败：" + ex.toString());
        } finally {
            if (readfile != null) {
                try {
                    readfile.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new IOException("解压失败：" + ex.toString());
                }
            }
        }
    }



        public static void main(String[] args) {
        try {
            unzip(String.valueOf(new File("E:\\UserLog1\\2019-05-27-bpServer.0.zip")), "E:\\UserLog1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
