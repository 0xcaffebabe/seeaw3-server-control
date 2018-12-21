package wang.ismy.seeaw3.client;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wang.ismy.seeaw3.client.controller.CommunicationMessageChain;
import wang.ismy.seeaw3.client.controller.OnLineClientMessageChain;
import wang.ismy.seeaw3.client.controller.communication.CommunicationNameMessageChain;
import wang.ismy.seeaw3.common.Destination;
import wang.ismy.seeaw3.common.Log;
import wang.ismy.seeaw3.common.MessageChainProcessor;
import wang.ismy.seeaw3.common.Terminalable;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.dto.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//  控制端
@Component
public class MasterClient extends BaseClient implements Terminalable{

    @Autowired
    private MessageChainProcessor processor=
            new MessageChainProcessor(
                    Arrays.asList(new CommunicationMessageChain(this),
                            new CommunicationNameMessageChain(this),
                            new OnLineClientMessageChain(this)
                            )
            );

    private List<String> sessionList = new ArrayList<>();

    private List<String> onLineList ;

    {
        Log.info("主控端准备就绪");
    }
    @Override
    public void receiveMessage(Message message) {
        processor.processMessage(message);
        Log.info("主控客户端收到消息:"+message);
    }

    @Override
    public void destroyDestination(Destination destination) {
        Log.error("主控与服务器失去了连接，准备重连");
        this.init();
    }

    public void addSession(String session){
        sessionList.add(session);
        System.err.print(session);
    }

    public String getSession(int index){
        return sessionList.get(index);
    }

    @Override
    public void terminalCreated(String sessionId) {
        Log.info("终端创建，:"+sessionId);
    }

    @Override
    public void terminalOutput(String s) {

        System.out.print(s);
    }

    public void onLineList(List<String> onLineList){
        this.onLineList = onLineList;
        System.err.println("在线用户:"+onLineList);
    }
}
