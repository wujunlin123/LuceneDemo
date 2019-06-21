package controller;

import Utils.Indexer;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static Utils.Indexer.dateFormatChange;
import static Utils.Indexer.sort;
import static Utils.PageUtil.pageBySubList;

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
