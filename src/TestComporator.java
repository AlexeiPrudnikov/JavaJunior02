import java.lang.reflect.Method;
import java.util.Comparator;

public class TestComporator implements Comparator<Method> {


    @Override
    public int compare(Method o1, Method o2) {
        if (o1.isAnnotationPresent(Test.class) && o2.isAnnotationPresent(Test.class)){
            Test test1 = o1.getAnnotation(Test.class);
            Test test2 = o2.getAnnotation(Test.class);
            return test1.priority() - test2.priority();
        }
        return 0;
    }
}
