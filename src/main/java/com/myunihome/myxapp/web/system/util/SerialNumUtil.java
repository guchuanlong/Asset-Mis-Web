package com.myunihome.myxapp.web.system.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 产生流水号工具类 
 * Date: Aug 27, 2015 <br>
 * Copyright (c) 2015 asiainfo.com <br>
 * 
 * @author liangbs
 */
public final class SerialNumUtil {
    
    private static String count = "000";
    private static String dateValue = "20131115";
    
    private SerialNumUtil(){};

    /**
     * 产生流水号
     */
    public synchronized static String getSerialNum() {
        long No = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nowdate = sdf.format(new Date());
        No = Long.parseLong(nowdate);
        if (!(String.valueOf(No)).equals(dateValue)) {
            count = "000";
            dateValue = String.valueOf(No);
        }
        String num = String.valueOf(No);
        num += getNo(count);
        num = "BIS-ST" + num;
        return num;
    }

    private static String getNo(String s) {
        String rs = s;
        int i = Integer.parseInt(rs);
        i += 1;
        rs = "" + i;
        for (int j = rs.length(); j < 3; j++) {
            rs = "0" + rs;
        }
        count = rs;
        return rs;
    }

    /*public static void main(String[] args) {
        System.out.println(getSerialNum());
    }*/

}
