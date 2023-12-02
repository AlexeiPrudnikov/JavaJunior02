
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestProcessor {

  /**
   * Данный метод находит все void методы без аргументов в классе, и запускеет их.
   * <p>
   * Для запуска создается тестовый объект с помощью конструткора без аргументов.
   */
  public static void runTest(Class<?> testClass) {
    final Constructor<?> declaredConstructor;
    try {
      declaredConstructor = testClass.getDeclaredConstructor();
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("Для класса \"" + testClass.getName() + "\" не найден конструктор без аргументов");
    }

    final Object testObj;
    try {
      testObj = declaredConstructor.newInstance();
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Не удалось создать объект класса \"" + testClass.getName() + "\"");
    }

    List<Method> beforeMethods = new ArrayList<>();
    for (Method method : testClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(BeforeEach.class)) {
        checkTestMethod(method);
        beforeMethods.add(method);
      }
    }
    BeforeComporator beforeComporator = new BeforeComporator();
    beforeMethods.sort(beforeComporator);
    List<Method> testMethods = new ArrayList<>();
    for (Method method : testClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Test.class)) {
        checkTestMethod(method);
        if (!method.isAnnotationPresent(Skip.class)) {
          testMethods.add(method);
        }
      }
    }
    TestComporator comporator = new TestComporator();
    testMethods.sort(comporator);

    List<Method> afterMethods = new ArrayList<>();
    for (Method method : testClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(AfterEach.class)) {
        checkTestMethod(method);
        afterMethods.add(method);
      }
    }
    beforeMethods.forEach(it -> runTest(it, testObj));
    testMethods.forEach(it -> runTest(it, testObj));
    afterMethods.forEach(it -> runTest(it, testObj));
  }

  private static void checkTestMethod(Method method) {
    if (!method.getReturnType().isAssignableFrom(void.class) || method.getParameterCount() != 0) {
      throw new IllegalArgumentException("Метод \"" + method.getName() + "\" должен быть void и не иметь аргументов");
    }
  }

  private static void runTest(Method testMethod, Object testObj) {
    try {
      testMethod.invoke(testObj);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException("Не удалось запустить тестовый метод \"" + testMethod.getName() + "\"");
    } catch (AssertionError e) {

    }
  }

}
