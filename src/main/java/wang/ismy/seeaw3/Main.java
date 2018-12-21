package wang.ismy.seeaw3;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import wang.ismy.seeaw3.server.BaseServer;
import wang.ismy.seeaw3.server.ServerContext;

public class Main {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:spring-server.xml");
        BaseServer server = ServerContext.get(BaseServer.class);



    }
}
