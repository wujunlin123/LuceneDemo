package controller;

import Utils.Indexer;
import Utils.Tree;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.Log;
import pojo.SimpleData;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static Utils.Indexer.dateFormatChange;
import static Utils.Indexer.sort;
import static Utils.PageUtil.pageBySubList;
import static Utils.Tree.breachs;

@Controller
@RequestMapping("logController")
public class LogController {
    public LogController(){
        System.out.println("LogController 实例化");
    }

    @RequestMapping(value="createIndex",produces="application/json;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String createIndex(HttpServletRequest req){
        Indexer.clean();
        if(Indexer.createIndex()){
            return "索引成功";
        }
        return "建立失败";
    }
    @RequestMapping(value="delIndex",produces="application/json;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String delIndex(HttpServletRequest req){
        Indexer.clean();
        return "删除索引";
    }
    @RequestMapping(value="creatTree",produces="application/json;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String creatTree(String seachFilePath,String seachFilePathDetail,String path){
        System.out.println("seachFilePathDetail:"+seachFilePathDetail);
        System.out.println("seachFilePath:"+seachFilePath);
      //  path = "E:\\UserLog";
       List<SimpleData> lists = null;
       breachs.clear();
       Tree.fullData(new File("E:\\UserLog"),breachs,0,1);
        System.out.println("breachs11111111111111"+breachs);
//        for(SimpleData breach : breachs){
//            lists.add(breach);
//            System.out.println(breach.toString());
//
//        }
//        System.out.println("---------------");
//        for(SimpleData list : lists){
//
//            System.out.println(list.toString());
//
//        }
        return JSONArray.toJSONString(breachs);
    }

    @RequestMapping(value="getKey",produces="application/json;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String getKey(String searchByTime,String searchByLevel,String currentPage){
        System.out.println("---"+searchByTime+"---"+searchByLevel+"--------++++");
        System.out.println("---"+searchByTime.length()+"---"+searchByLevel.length());
        List<Log> list = null;
        if(searchByTime.length()==0 && searchByLevel.length() == 0){
            Indexer.clean();
            Indexer.createIndex();
            String indexFile = "D:\\luceneIndex";

        }
        if(searchByTime.length() > 0 && searchByLevel.length() > 0){
            Indexer.clean();
            Indexer.createIndex();
            String indexFile = "D:\\luceneIndex";
            String searchByTime1;
            //searchByTime1 = dateFormatChange(searchByTime);
            list= Indexer.searchByTimeAndLevel(searchByTime,searchByLevel,indexFile);
        }
        if(searchByTime.length() !=0 && searchByLevel.length() == 0){
            Indexer.clean();
            Indexer.createIndex();
            String indexFile = "D:\\luceneIndex";
            String searchByTime1;
           // searchByTime1 = dateFormatChange(searchByTime);
            list= Indexer.searchByTime(searchByTime,indexFile);
            pageBySubList(list,20,1);
            System.out.println(pageBySubList(list,20,1));
        }
        if(searchByTime.length() == 0 && searchByLevel.length() != 0){
            Indexer.clean();
            Indexer.createIndex();
            String indexFile = "D:\\luceneIndex";
            list= Indexer.searchByLevel(searchByLevel,indexFile);
        }
//        if(searchByWord==null){
//            System.out.println("hhhh");
//        }else{
//            System.out.println("seachbyword = "+searchByWord.length());
//        }


        return JSONArray.toJSONString(list);
//        return JSONArray.toJSONString("aa");

    }

    @RequestMapping(value="getTree",produces="application/json;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String getTree(HttpServletRequest req){

        return "得到路径树";
    }
}
