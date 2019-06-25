package demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Utils.Indexer.changeTimeRegex;

public class TEST {
    public static void main(String[] args){
        String STR = "E:\\UserLog\\hivenode03\\2019-06-06-fpServer.19\\2019-06-06-fpServer.19";
        String str = "2019-06-06 13:47:13.338";
        System.out.println("2:"+changeTimeRegex(str));
        System.out.println(str.replaceAll("[:| |\\-|\\.|-]",""));
        String str1 = "E:\\UserLog\\hivenode03\\2019-06-06-fpServer.19\\2019-06-06-fpServer.19";
        System.out.println("1:"+str1.replaceAll("[\\\\|:|\\.|-]",""));
        System.out.println(str.replaceAll("-",""));
        System.out.println(str.replaceAll("-","").replaceAll(":",""));
        System.out.println(str.replaceAll("-","").replaceAll(":","").replaceAll(" ",""));
        System.out.println(str.replaceAll("-","").replaceAll(":","").replaceAll(" ","").replaceAll(".",""));


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
