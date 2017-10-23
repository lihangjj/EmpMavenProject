package util.array;

import java.lang.reflect.Field;

public class ArrayUtil {
    public static Field[] getNewArray(Field[] obj) {//移除成员数组中的第一个元素
        Field[] newObj = new Field[obj.length - 1];
        for (int x = 0; x < obj.length-1; x++) {
            newObj[x] = obj[x + 1];
        }
        return newObj;
    }
}
