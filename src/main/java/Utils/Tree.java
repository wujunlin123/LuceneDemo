package Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Tree {
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