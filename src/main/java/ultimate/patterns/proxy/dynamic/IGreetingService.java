package ultimate.patterns.proxy.dynamic;

public interface IGreetingService {

    void hello();

    @LogInvocation
    void gloryToUkraine();
}
