package factory;
import java.util.Locale;
import java.util.ResourceBundle;

public class DAOFactory {
    public static <T> T getInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getInstance(String className) {

        String trueName = ResourceBundle.getBundle("Class", Locale.getDefault()).getString(className);
        try {
            return (T) Class.forName(trueName).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
