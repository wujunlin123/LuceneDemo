package Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.junit.jupiter.api.Test;
import pojo.Log;

import java.io.*;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static Utils.PageUtil.pageBySubList;

public class Indexer {
    static String indexPath="D:\\luceneIndex";

    public static boolean createIndex(){
        try{
        // 1、创建一个Director对象，指定索引库保存的位置。
        // 把索引库保存在内存中
        // Directory directory = new RAMDirectory();
        // 把索引库保存在磁盘
        Directory directory = FSDirectory.open(new File(indexPath).toPath());
        // 2、基于Directory对象创建一个IndexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
        // 3、读取磁盘上的文件，对应每个文件创建一个文档对象。
        File dir = new File("E:\\UserLog\\hivenode01\\hivecore\\bpLog\\new");
        File[] files = dir.listFiles();

            List<Log> logs=new ArrayList<>();
            int count = 0;
        for (File f : files) {
            // 取文件名
            String fileName = f.getName();
            // 文件的路径
            String filePath = f.getPath();
            System.out.println("wenianming:"+fileName+"----------------"+"filepath:"+filePath);
            // 文件的内容
            ArrayList<String> arrayList = new ArrayList<>();
            try {
                File file = new File(filePath);
                if(file.isFile()) {
                    InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
                    BufferedReader bf = new BufferedReader(inputReader);            // 按行读取字符串
                    String str;
                    while ((str = bf.readLine()) != null) {
                        int l = str.indexOf("SobeyHive");
                        if (l == -1) {
                            continue;
                        }
                        int n = str.indexOf("level");
                        if (n == -1) {
                            continue;
                        }
                        arrayList.add(str);
                    }
                    bf.close();
                    inputReader.close();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }		// 对ArrayList中存储的字符串进行处理
            int length = arrayList.size();
            int[] array = new int[length];

            for (int i = 0; i < length; i++) {
                String s = arrayList.get(i) ;
//                System.out.println(s);
//                System.out.println("arrayList.get(i).length:"+arrayList.get(i).length()+"-----s.length:"+s.length());
                int n = s.indexOf("{");
                int m = s.length();
                String str2 = s.substring(n, m);
                JSONObject pa = JSONObject.parseObject(str2);
                String level = pa.getString("level");
                String time = pa.getString("time");
             //   String time1 = dateFormatChange(time);
                String detail = pa.getString("detail");
              //  System.out.println(pa.getString("level") + "," + time );
                //  array[i] = Integer.parseInt(s);


                Log log = new Log(filePath, level, time, detail);
                logs.add(log);
                // 文件的大小
                long fileSize = FileUtils.sizeOf(f);
                // 创建Field
                // 参数1：域的名称，参数2：域的内容，参数3：是否存储
                Field fieldName = new TextField("name", fileName, Field.Store.YES);
                Field fieldPath = new StoredField("path", filePath);
                Field fileTime = new TextField("time", time, Field.Store.YES);
                Field fileLevel = new TextField("level", level, Field.Store.YES);
                Field fieldContent = new TextField("content", detail, Field.Store.YES);
                Field fieldSizeValue = new LongPoint("size", fileSize);
                Field fieldSizeStore = new StoredField("size", fileSize);
                // 创建文档对象
                Document document = new Document();
                // 向文档对象中添加域
                document.add(fieldName);
                document.add(fieldPath);
                document.add(fileTime);
                document.add(fileLevel);
                document.add(fieldContent);
                // document.add(fieldSize);
                document.add(fieldSizeValue);
                document.add(fieldSizeStore);
                // 5、把文档对象写入索引库
                indexWriter.addDocument(document);
                count++;
            }
            System.out.println("count:"+count);

        }
//            sort(logs);
        int count1 = 0;
//            for(Log log:logs){
//                count1++;
//                System.out.println(log);
//            }
//            System.out.println("zongshudddddd"+count1);
//            System.out.println("zongshu:"+count);
        // 6、关闭indexwriter对象
        indexWriter.close();

        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static  String dateFormatChangeBack(String valueIn ){

        String formatIn = "yyyy-MM-dd-HH-mm-ss-SSS";
        String formatOut = "yyyy-MM-dd HH:mm:ss.SSS";
        LocalDateTime ldt;
        ldt = LocalDateTime.parse(valueIn, DateTimeFormatter.ofPattern(formatIn));
        //  System.out.println("< " + ldt);

        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        String out = DateTimeFormatter.ofPattern(formatOut).format(zdt);
        // System.out.println("> " + out);
        return out;
    }


    public static  String dateFormatChange(String valueIn ){

        String formatIn = "yyyy-MM-dd HH:mm:ss.SSS";
        String formatOut ="yyyy-MM-dd-HH-mm-ss-SSS";
        LocalDateTime ldt;
        ldt = LocalDateTime.parse(valueIn, DateTimeFormatter.ofPattern(formatIn));
      //  System.out.println("< " + ldt);

        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        String out = DateTimeFormatter.ofPattern(formatOut).format(zdt);
       // System.out.println("> " + out);
        return out;
    }






    public static void sort(List<Log> logs){
        Collections.sort(logs, new Comparator<Log>() {
            @Override
            public int compare(Log o1, Log o2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String time1 = o1.getDate();
                String time2 = o2.getDate();
                try {
                    return simpleDateFormat.parse(time2).compareTo(simpleDateFormat.parse(time1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    public static void clean(){
        File[] files=new File(indexPath).listFiles();
        for(File file:files){
            file.delete();
        }

    }

    static Log parse(String str){
        Log log=JSONArray.parseObject(str,Log.class);
        return log;
    }

    public static void main(String[] args) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
        List<Log> logs=new ArrayList<>();
       //createIndex();
        //searchByTimeAndLevel("2019-05-27 14:58:13.643","INFO","D:\\luceneIndex");
       searchByTime("2019-05-27","D:\\luceneIndex");

        //searchByKeyword("WARN");
       // queryByMKeyWord("D:\\luceneIndex");
        //queryByTime("D:\\luceneIndex","2019-05-27");
//        logs.addAll(searchIndex());
        //searchByFileName();
//queryByLevel("D:\\luceneIndex","ERROR");

//        logs.clear();
//        logs.addAll(search(path,"素材媒体权限"));

//        logs.clear();
//        logs.addAll(searchByKeyword("RestAPI"));



    }
    /**
     * 根据多个关键字从多个字段Field检索文件
     *
     * @param searchByTime，searchByLevel，indexFile
     *            日期，级别，存放索引的目录

     * @throws IOException
     * @throws ParseException
     */
    public static List<Log> searchByTimeAndLevel(String searchByTime, String searchByLevel,String indexFile) {
        List<Log> logs=new ArrayList<>();
        //搜索
        // 1、创建一个Director对象，指定索引库的位置
        Directory directory = null;
        try {
            directory = FSDirectory.open(new File(indexFile).toPath());
        // 2、创建一个IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher index_search = new IndexSearcher(indexReader);
        //要查找的字符串数组
        String[] stringQuery = {QueryParser.escape(searchByTime),searchByLevel};
        //待查找字符串对应的字段
        String[] fields = {"time", "level"};
        //Occur.MUST表示对应字段必须有查询值， Occur.MUST_NOT 表示对应字段必须没有查询值，Occur.SHOULD表示对应字段应该存在查询值（但不是必须）
        BooleanClause.Occur[] occ = {BooleanClause.Occur.MUST,BooleanClause.Occur.MUST};
        Analyzer analyzer = new StandardAnalyzer();
            Query query = MultiFieldQueryParser.parse(stringQuery, fields, occ, analyzer);
            TopDocs topDocs = index_search.search(query, 10000000);
            long startTime=System.currentTimeMillis();
            long endTime=System.currentTimeMillis();
            for(ScoreDoc scoreDoc:topDocs.scoreDocs) {
                Document document=index_search.doc(scoreDoc.doc);
                String fieldPath = document.get("path");
                String fileTime = document.get("time");
                String fileLevel = document.get("level");
                String fileContent = document.get("content");
                Log log = new Log(fieldPath, fileLevel, fileTime, fileContent);
                if(log.getDate().contains(searchByTime)){
                    logs.add(log);
                }

            }
            sort(logs);
            for(Log log:logs){
                System.out.println(log);
            }
            System.out.println("匹配    "+searchByTime+",+"+searchByLevel+"   总共花费："+(endTime-startTime)+"毫秒，查询到"+logs.size()+"条记录");
        } catch (IOException e2){
            e2.printStackTrace();
        } catch (org.apache.lucene.queryparser.classic.ParseException e3) {
            e3.printStackTrace();
        }
        return logs;
    }
    /**
     * 根据关键字Time检索文件
     *
     * @param Time
     *            日期
     * @param indexFile
     *            目录路径
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public static List<Log> searchByTime(String searchByTime, String indexFile) {
        List<Log> logs=new ArrayList<>();
        try{
            Directory directory = FSDirectory.open(new File(indexFile).toPath());
            IndexReader reader = DirectoryReader.open(directory);

            Analyzer analyzer=new StandardAnalyzer();
            QueryParser queryParser=new QueryParser("time", analyzer);
            Query query= null;
            query = queryParser.parse(QueryParser.escape(searchByTime));

            IndexSearcher searcher = new IndexSearcher(reader);
            long startTime=System.currentTimeMillis();
            TopDocs topDocs=searcher.search(query, 1000000);
            long endTime=System.currentTimeMillis();
            for(ScoreDoc scoreDoc:topDocs.scoreDocs) {
                Document document=searcher.doc(scoreDoc.doc);
                String fieldPath = document.get("path");
                String fileTime = document.get("time");
         //       String fileTime1 = dateFormatChangeBack(fileTime);
                String fileLevel = document.get("level");
                String fileContent = document.get("content");
                Log log = new Log(fieldPath, fileLevel, fileTime, fileContent);
                // System.out.println(log);
                if(log.getDate().contains(searchByTime)){
                    logs.add(log);
                }

            }
            sort(logs);
            int count = 0;
            for(Log log:logs){
                count++;
                System.out.println(count+"、 "+log);
            }
//            System.out.println("-----------------------");
//            System.out.println(pageBySubList(logs,20,1));
            System.out.println("匹配    "+searchByTime+"   总共花费："+(endTime-startTime)+"毫秒，查询到"+logs.size()+"条记录");
        } catch (IOException e2){
            e2.printStackTrace();
        } catch (org.apache.lucene.queryparser.classic.ParseException e3) {
            e3.printStackTrace();
        }
        return logs;
    }
    /**
     * 根据关键字Level检索文件
     *
     * @param Level
     *            日志级别
     * @param indexFile
     *            目录路径
     * @throws IOException
     * @throws ParseException
     */
    public static List<Log> searchByLevel(String searchByLevel, String indexFile) {
        List<Log> logs=new ArrayList<>();
        try{
            Directory directory = FSDirectory.open(new File(indexFile).toPath());
            IndexReader reader = DirectoryReader.open(directory);

            Analyzer analyzer=new StandardAnalyzer();
            QueryParser queryParser=new QueryParser("level", analyzer);
            Query query= null;
            query = queryParser.parse(searchByLevel);
            IndexSearcher searcher = new IndexSearcher(reader);
            long startTime=System.currentTimeMillis();
            TopDocs topDocs=searcher.search(query, 1000000);
            long endTime=System.currentTimeMillis();
            for(ScoreDoc scoreDoc:topDocs.scoreDocs) {
                Document document=searcher.doc(scoreDoc.doc);
                String fieldPath = document.get("path");
                String fileTime = document.get("time");
                String fileLevel = document.get("level");
                String fileContent = document.get("content");
                Log log = new Log(fieldPath, fileLevel, fileTime, fileContent);
                // System.out.println(log);
                logs.add(log);
            }
            sort(logs);
            int count = 0;
            for(Log log:logs){
                count++;
                System.out.println(count+"、 "+log);
            }
            System.out.println("匹配    "+searchByLevel+"   总共花费："+(endTime-startTime)+"毫秒，查询到"+topDocs.totalHits+"条记录");
        } catch (IOException e2){
            e2.printStackTrace();
        } catch (org.apache.lucene.queryparser.classic.ParseException e3) {
            e3.printStackTrace();
        }
        return logs;
    }

    //搜索索引
    @Test
    public void testSearchIndex() throws IOException{
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File("D:\\luceneIndex").toPath());
        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建Indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //创建一个TermQuery（精准查询）对象，指定查询的域与查询的关键词
        //创建查询
        Query query = new TermQuery(new Term("level", "INFO"));
        //执行查询
        //第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 1000000000);
        //查询结果的总条数
        System.out.println("查询结果的总条数："+ topDocs.totalHits);
        //遍历查询结果
        //topDocs.scoreDocs存储了document对象的id
        //ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            //scoreDoc.doc属性就是document对象的id
            //int doc = scoreDoc.doc;
            //根据document的id找到document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            //文件名称
            System.out.println(document.get("path"));
            //文件内容
            System.out.println(document.get("time"));
            //文件大小
            System.out.println(document.get("level"));
            //文件路径
            System.out.println(document.get("content"));
            System.out.println("----------------------------------");
        }
        //关闭indexreader对象
        indexReader.close();
    }
}



