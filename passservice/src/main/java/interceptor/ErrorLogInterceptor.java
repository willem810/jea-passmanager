package interceptor;



import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.container.PreMatching;
import java.util.logging.Level;
import java.util.logging.Logger;

@LogError
@Interceptor
public class ErrorLogInterceptor {
    @AroundInvoke
    public Object log(InvocationContext context) throws Exception {
        try {
            return context.proceed();
        } catch (Exception e) {
            Logger.getLogger("ErrorLog").log(Level.WARNING, e.getMessage());
            throw e;
        }
    }
}
