public class MyTest {

    @BeforeEach (priority = 1)
    static void firstBefore(){
        System.out.println("firstBefore запущен");
    }
    @BeforeEach (priority = 2)
    static void secondBefore(){
        System.out.println("secondBefore запущен");
    }
    @Test (priority = 4)
    static void firstTest() {
        System.out.println("firstTest запущен");
    }
    @Test (priority = 3)
    @Skip
    static void secondTest() {
        System.out.println("secondTest запущен");
    }
    @Test (priority = 2)
    static void thirdTest() {
        System.out.println("thirdTest запущен");
    }
    @AfterEach
    static void After(){
        System.out.println("After запущен");
    }

}
