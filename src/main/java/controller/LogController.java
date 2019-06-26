package controller;

import Utils.Indexer;
import Utils.Tree1;
import Utils.UnZip;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.Log;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Utils.PageUtil.pageBySubList;

@Controller
@RequestMapping("logController")
public class LogController {
    public static String real = null;
    public LogController(){
        System.out.println("LogController 实例化");
    }
    @RequestMapping(value="searchLogByPath",produces="application/json;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String searchLogByPath(String dir,String path ,String curr) throws IOException, InterruptedException {
        System.out.println(curr);
        if(path.indexOf("\\") == path.length() - 1){
            real = dir;
        }else {
            path = path.substring(0, path.lastIndexOf("\\"));
            real = dir + path.substring(path.indexOf("\\"));
        }
        System.out.println("path:"+path);
        System.out.println("目录真实路径："+real);
        //创建索引库
        Indexer.createIndexByDir(real);
        List list=new ArrayList();
        list.addAll(Indexer.searchIndexByPath(real));
        //list= Indexer.readAll();
        return JSON.toJSONString(list);
    }
    @RequestMapping(value="delIndex",produces="application/json;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String delIndex(HttpServletRequest req){
        Indexer.clean();
        return "删除索引";
    }
    @Test
    @RequestMapping(value="creatTree",produces="application/json;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    //生成目录树
    public String creatTree(String seachFilePath,String seachFilePathDetail,String path) throws IOException {
        System.out.println("seachFilePathDetail:"+seachFilePathDetail);
        System.out.println("seachFilePath:"+seachFilePath);
        //  path = "E:\\UserLog";
        UnZip.unZipDir(seachFilePath);//解压缩
        Indexer.clean();
        Indexer.createIndexByDir(seachFilePath);//生成索引
        Tree1 tree1 = new Tree1();
        return JSON.toJSONString(tree1.scan(seachFilePath));
    }
    @RequestMapping(value="getKey",produces="application/json;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String getKey(String seachFilePath, String searchByTime, String searchByLevel, String currentPage) throws IOException, InterruptedException {
        String realPath;
        realPath = real+"\\*";

        System.out.println("---"+seachFilePath+"---"+seachFilePath+"--------++++");
        // seachFilePath.replaceAll("\","\\");
        System.out.println("---"+searchByTime+"---"+searchByLevel+"--------++++");
        System.out.println("---"+searchByTime.length()+"---"+searchByLevel.length());
        List<Log> list = null;
        if(searchByTime.length()==0 && searchByLevel.length() == 0){
            System.out.println(222);
            //Indexer.clean();
            //创建索引库
            Indexer.createIndexByDir(seachFilePath);
           //将所有信息打印在右边
            list= Indexer.readAll();
        }
        if(searchByTime.length() > 0 && searchByLevel.length() > 0){
            System.out.println(333);
            //Indexer.clean();
            System.out.println("real:"+real);
            Indexer.createIndexByDir(real);
            list= Indexer.searchByTimeAndLevelAndPath(searchByTime,searchByLevel,realPath);
        }
        if(searchByTime.length() >0 && searchByLevel.length() == 0){
            System.out.println(444);
            //Indexer.clean();
            Indexer.createIndexByDir(real);
            System.out.println("real:"+real);
            list= Indexer.searchByTimeAndPath(searchByTime,realPath);
            pageBySubList(list,20,1);
            System.out.println(pageBySubList(list,20,1));
        }

        if(searchByTime.length() == 0 && searchByLevel.length() > 0){
            System.out.println(11111);
            System.out.println("real:"+real);
            //Indexer.clean();
            Indexer.createIndexByDir(real);
            list= Indexer.searchByLevelAndPath(searchByLevel,realPath);
        }

        return JSONArray.toJSONString(list);
//        return JSONArray.toJSONString("aa");
    }
    @RequestMapping(value="getTree",produces="application/json;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String getTree(HttpServletRequest req){
        return "得到路径树";
    }
}
