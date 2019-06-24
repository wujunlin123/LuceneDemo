package Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnZip {

    static List<String> zipList;
    static{
        zipList=new ArrayList<>();
        zipList.add(".zip");
        zipList.add(".rar");
    }

    public static void unZip (File file,String directoryName){
//        String gzFileStr = "E:\\UserLog1\\2019-05-27-bpServer.0.zip";//E:\UserLog1\2019-05-27-bpServer.0.zip
        try (ZipFile zf = new ZipFile(file)){
            Enumeration<? extends ZipEntry> entries = zf.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String zipEntryName = entry.getName();

                File directory=new File(directoryName);
                directory.mkdir();

                System.out.println(zipEntryName);
                //生成解压文件、指定文件名
                FileOutputStream fileOutputStream = new FileOutputStream(directory.getAbsoluteFile()+"\\"+zipEntryName);
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

    public static boolean isZip(String fileName){
        return zipList.contains(fileName);
    }

    public static boolean isZip(File file){
        return isZip(file.getAbsolutePath());
    }

    public static void unZipDir(String path){//解压入口
        File file=new File(path);
        if(file.isFile()){
            String houzhui=path.substring(path.lastIndexOf("."),path.length());
            if(isZip(houzhui)){//是压缩文件
                String directoryName=path.substring(0,path.lastIndexOf(houzhui));
                File zipFile=new File(directoryName);
                if(!zipFile.exists()){//是否已经解压
                    unZip(file,directoryName);
                }
            }
        }else if(file.isDirectory()){//是一个文件夹，进入递归
            String[] names=file.list();
            for(String name:names){
                unZipDir(path+"\\"+name);
            }
        }
    }

    public static void main(String[] args) {
//        UnZip zipp = new UnZip();
//        zipp.test015();
//        System.out.println("aaa");
//        unZipDir("C:\\Users\\wuhansan\\Desktop\\UserLog");
//        System.out.println("bbb");
//建议使用这个路径测试C:\Users\wuhansan\Desktop\UserLog
    }

}
