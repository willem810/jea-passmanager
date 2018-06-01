package interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Logging
@Interceptor
public class LoggingInterceptor {
    Logger log = Logger.getLogger(LoggingInterceptor.class.getName());

    @AroundInvoke
    public Object log(InvocationContext context) throws Exception {
        String ctxData = context.getContextData().toString();
        String name = context.getMethod().getName();
        String params = Arrays.toString(context.getParameters());
        log("LOGGING: name = " + name + " params = " + params + ", Context data: " + ctxData);

        Object resp = context.proceed();
        if (resp == null) {
            return resp;
        }

        log("LOGGING: Response = " + resp.toString());

        return resp;
    }

    private void log(String message) {
        log.log(Level.INFO, message);
    }
}
