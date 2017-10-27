
public class Test {
    public static void main(String[] args) throws Exception {


        System.out.println(getI(0,"级"));
    }
    static int y=0;
    public static String getI(int x,String name){
         y=x+1;
         String s=y+name;

         if (y>4){
             return s;
         }
        return getI(y,s);
    }

}
