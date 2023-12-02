import java.lang.reflect.Method;
import java.util.Comparator;

public class BeforeComporator implements Comparator<Method> {
    @Override
    public int compare(Method o1, Method o2) {
        if (o1.isAnnotationPresent(BeforeEach.class) && o2.isAnnotationPresent(BeforeEach.class)){
            BeforeEach test1 = o1.getAnnotation(BeforeEach.class);
            BeforeEach test2 = o2.getAnnotation(BeforeEach.class);
            return test1.priority() - test2.priority();
        }
        return 0;
    }
}
