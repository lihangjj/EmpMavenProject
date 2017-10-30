import util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Test {
    public static void main(String[] args) throws Exception {

        String s="Mon Oct 30 00:00:00 CST 2017";
        Date date=DateUtil.getChinaUtilDate(new java.sql.Date(595959));


//        System.out.println(Date.valueOf(s));
        System.out.println(date);

    }

}
