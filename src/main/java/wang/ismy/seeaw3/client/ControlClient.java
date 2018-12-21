package wang.ismy.seeaw3.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wang.ismy.seeaw3.client.controller.CommunicationMessageChain;
import wang.ismy.seeaw3.client.controller.communication.CommunicationNameMessageChain;
import wang.ismy.seeaw3.common.Destination;
import wang.ismy.seeaw3.common.Log;
import wang.ismy.seeaw3.common.MessageChainProcessor;
import wang.ismy.seeaw3.dto.Message;

import java.util.Arrays;

// 被控端
@Component
public class ControlClient extends BaseClient {

    @Autowired
    private MessageChainProcessor processor=
            new MessageChainProcessor(
                    Arrays.asList(new CommunicationMessageChain(this),new CommunicationNameMessageChain(this))
            );

    {
        Log.info("被控端准备就绪");
    }

    @Override
    public void receiveMessage(Message message) {
        this.serverLastResponse = System.currentTimeMillis();
        processor.processMessage(message);
        Log.info("被控客户端收到消息:"+message);
    }

    @Override
    public void destroyDestination(Destination destination) {
        Log.error("被控客户端与服务器失去了连接，准备重连");
        this.init();
    }
}
