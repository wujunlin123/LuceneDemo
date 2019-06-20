package Utils;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Test {
    public static  String dateFormatChange(String valueIn ){

        String formatIn = "yyyy-MM-dd HH:mm:ss.SSS";
        String formatOut = "yyyy-MM-dd-HH-mm-ss-SSS";
        LocalDateTime ldt;
        ldt = LocalDateTime.parse(valueIn, DateTimeFormatter.ofPattern(formatIn));
        //  System.out.println("< " + ldt);

        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        String out = DateTimeFormatter.ofPattern(formatOut).format(zdt);
        // System.out.println("> " + out);
        return out;
    }
    public static void main(String[] args) throws ParseException {


        System.out.println(dateFormatChange("2019-05-27 14:58:48.228"));

        String[] stringQuery = {"123","321"};
        System.out.println(stringQuery);
        System.out.println(stringQuery.length);

    }
}