package Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnZip {
    public void test015 (){
        String gzFileStr = "E:\\UserLog1\\2019-05-27-bpServer.0.zip";
        File file = new File(gzFileStr);
        try (ZipFile zf = new ZipFile(gzFileStr);){
            Enumeration<? extends ZipEntry> entries = zf.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String zipEntryName = entry.getName();

                System.out.println(zipEntryName);
                FileOutputStream fileOutputStream = new FileOutputStream(file.getParent()+"\\"+zipEntryName);
                InputStream inputStream = zf.getInputStream(entry);
                byte[] b = new byte[1024*1024*5];
                int length = 0;
                while ((length = inputStream.read(b)) != -1) {
                    fileOutputStream.write(b, 0, length);
                }
                fileOutputStream.close();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UnZip zipp = new UnZip();
        zipp.test015();
    }

}
