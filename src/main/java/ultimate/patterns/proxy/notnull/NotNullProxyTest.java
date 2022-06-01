package ultimate.patterns.proxy.notnull;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class NotNullProxyTest {
    public static void main(String[] args) {
        NotNullProxyService pr = createNotNullProxy(NotNullProxyService.class);
        pr.test(null);
    }

    public static <T> T createNotNullProxy(Class<T> targetClass) {

        MethodInterceptor interceptor = (obj, method, args, proxy) -> {
            for (int i = 0; i < args.length; i++) {
                var params = method.getParameters();
                if (params[i].isAnnotationPresent(NotNull.class) && args[i] == null) {
                    throw new NullPointerException();
                }
            }
            return proxy.invokeSuper(obj, args);
        };
        return (T) Enhancer.create(targetClass, interceptor);
    }
}
