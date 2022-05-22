package ultimate.patterns.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class TestProxy {

    public static void main(String[] args) {
        GreetingService service = createMethodLoggingProxy(GreetingService.class);
        service.hello();
        service.gloryToUkraine();
    }

    /**
     * Creates a proxy of the provided class that logs its method invocations. If a method that
     * is marked with {@link LogInvocation} annotation is invoked, it prints to the console the following statement:
     * "[PROXY: Calling method '%s' of the class '%s']%n", where the params are method and class names accordingly.
     *
     * @param targetClass a class that is extended with proxy
     * @param <T>         target class type parameter
     * @return an instance of a proxy class
     */
    public static <T> T createMethodLoggingProxy(Class<T> targetClass) {
        MethodInterceptor interceptor = (obj, method, args, proxy) -> {
            if(method.isAnnotationPresent(LogInvocation.class)){
                System.out.printf("[PROXY: Calling method '%s' of the class '%s']%n", method.getName(), method.getDeclaringClass());
            }
            return proxy.invokeSuper(obj, args);
        };
        var enhancer = Enhancer.create(targetClass, interceptor);
        return targetClass.cast(enhancer);


    }
}
