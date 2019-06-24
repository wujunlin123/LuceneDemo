package demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TEST {
    public static void main(String[] args){
        String real;
       String dir = "E:\\UserLog";
       String path = "UserLog\\";
        System.out.println(path.indexOf("\\"));
        System.out.println(path.length());
        path = path.substring(0, path.lastIndexOf("\\"));
       // System.out.println(path.substring(path.indexOf("\\")));
       // System.out.println(path.indexOf("\\"));
       // real = dir + path.substring(path.indexOf("\\"));
        //System.out.println(real);
    }

    private static List<String> getAllFilePaths(File filePath,List<String> filePaths){
        File[] files = filePath.listFiles();
        if(files == null){
            return filePaths;
        }
        for(File f:files){
            if(f.isDirectory()){
                filePaths.add(f.getPath());
                getAllFilePaths(f,filePaths);
            }else{
                filePaths.add(f.getPath());
            }
        }
        return filePaths;
    }
}
