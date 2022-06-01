package java_test.cglib;

public class GreetingService {

    public void hello() {
        System.out.println("Hello from method");
    }

    @LogInvocation
    public void gloryToUkraine() {
        System.out.println("Hello from method gloryToUkraine");
    }
}
