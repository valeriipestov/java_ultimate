package javaultimate.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CustomInvocationHandler<T> implements InvocationHandler {

    T original;

    public CustomInvocationHandler(T original) {
        this.original = original;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(LogInvocation.class)){
            System.out.printf("[PROXY: Calling method '%s' of the class '%s']%n", method.getName(), method.getDeclaringClass());
        }
        return method.invoke(original, args);

    }

}
