package ultimate.patterns.proxy.dynamic;

import java.lang.reflect.Proxy;

public class DynamicTest {
    public static void main(String[] args) {
        IGreetingService iservice = createMethodLoggingProxy(IGreetingService.class, new GreetingService());
        iservice.gloryToUkraine();
        iservice.hello();
    }

    public static <I, T extends I> I createMethodLoggingProxy(Class<I> targetInterface, T target) {
        return targetInterface.cast(Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), new CustomInvocationHandler<>(target))) ;
    }
}
