package Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import pojo.SimpleData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//生成ztre简单json数据
public class Tree {

    public static List<SimpleData> breachs = new ArrayList<>();

    public static void main(String[] args) {
        Tree tree = new Tree();

       // List<SimpleData> breach = new ArrayList<>();
        tree.fullData(new File("E:\\UserLog"),breachs,0,1);
        for(SimpleData breach:breachs){

            //System.out.println(breach.toString());
        }
//        System.out.println("+++++++++");
//        System.out.println(tree.fullData(new File("E:\\UserLog"),breachs,0,1));
//        System.out.println("---------");
    }
    public static int fullData(File root , List<SimpleData> breach, int pid, int id) {


        File[] fileList = root.listFiles();
        int len = fileList.length;
        int fatherpid = pid;
        int sonid = id;
        String path = "";
        for (int i = 0; i < len; i++) {
            if (fileList[i].isDirectory()) {
                //List<SimpleData>  breach = new ArrayList<SimpleData> ();
                SimpleData bsd = new SimpleData();
                bsd.setPid(fatherpid);
                bsd.setId(sonid);
                bsd.setName(fileList[i].getName());
                path = fileList[i].getPath();
                String path1 = path.replace("\\", "/");
                bsd.setPath(path1);
                breach.add(bsd);
                sonid = fullData(fileList[i], breach, sonid, sonid + 1);
            } else {
                SimpleData leaf = new SimpleData();
                leaf.setPid(fatherpid);
                leaf.setId(sonid);
                leaf.setName(fileList[i].getName());
                path = fileList[i].getPath();
                String path1 = path.replace("\\", "/");
                leaf.setPath(path1);
                breach.add(leaf);
                sonid++;
            }
        }
        return sonid;


    }
        public static String dir2json(String dir_path) {
        HashMap<String, Object> dirMap = new HashMap<String, Object>();
        File root = new File(dir_path);
        dir2map(root, dirMap);
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dirMap.get(root.getName()));
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean shouldSkip(String filename) {
        return filename.startsWith(".");
    }
    /**
     * @param node   文件节点
     * @param dirMap 表示文件所在目录的map
     */

    public static void dir2map(File node, HashMap<String, Object> dirMap) {
        //跳过隐藏文件等
        if (shouldSkip(node.getName())) {
            return;
        }
        //是文件，保存文件名和最后修改时间戳
        if (node.isFile()) {
            dirMap.put(node.getName(), node.lastModified());
        }
        //是目录，建立下一层map，并填充
        if (node.isDirectory()) {
            HashMap<String, Object> subDir = new HashMap<String, Object>();
            dirMap.put(node.getName(), subDir);
            for (String filename : node.list()) {
                dir2map(new File(node, filename), subDir);//填充
            }
        }
    }
}