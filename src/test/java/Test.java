import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        List<String> list=new ArrayList<>();
        list.add("号的1");
        list.add("号的2");
        list.add("号的3");
        list.add("号的4");

        System.out.println(list.contains("号的1"));
    }
}
