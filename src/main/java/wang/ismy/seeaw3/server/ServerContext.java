package wang.ismy.seeaw3.server;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ServerContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServerContext.applicationContext = applicationContext;
    }

    public static <T> T get(Class<T> type){
        return applicationContext.getBean(type);
    }


}
