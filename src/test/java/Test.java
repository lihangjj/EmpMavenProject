import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {

        List<String> list=new ArrayList<>();
        list.add("哈哈");
        list.add("哈哈1");
        list.add("哈哈2");
        Iterator<String> iterator=list.iterator();
        while (iterator.hasNext()){
            String s=iterator.next();
            if (s.equals("哈哈")){
                iterator.remove();
            }
        }
        System.out.println(""==null);


    }

}
