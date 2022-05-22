package javaultimate.proxy.dynamic;

public interface IGreetingService {

    void hello();

    @LogInvocation
    void gloryToUkraine();
}
