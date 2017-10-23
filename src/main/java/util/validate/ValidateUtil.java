package util.validate;

public class ValidateUtil {
    /**
     * 验证字符串是否为空
     * @param data 要验证的数据
     * @return 如果数据为null返回false，否则返回true
     */
    public static boolean validateEmpty(String data) {
        if (data == null || "".equals(data)) {
            return false ;
        }
        return true ;
    }

    /**
     * 对数据进行正则验证
     * @param data 要验证的字符串数据
     * @param regex 正则表达式
     * @return 验证通过返回true，否则返回false
     */
    public static boolean validateRegex(String data,String regex) {
        if (validateEmpty(data)) { // 判断数据是否为空
            return data.matches(regex) ;    // 不为空用正则验证
        }
        return false ;
    }

    /**
     * 进行数据的重复判断
     * @param dataa 数据A
     * @param datab 数据B
     * @return 数据相同返回true，否则返回false
     */
    public static boolean validateSame(String dataa,String datab) {
        if (validateEmpty(dataa) && validateEmpty(datab)) {
            return dataa.equalsIgnoreCase(datab) ;
        }
        return false ;
    }
}
