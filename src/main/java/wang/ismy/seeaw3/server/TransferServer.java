package wang.ismy.seeaw3.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wang.ismy.seeaw3.common.*;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.server.service.MessageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransferServer extends BaseServer {

    // 用来处理到来的消息
    private MessageChainProcessor messageChainProcessor;

    @Autowired
    private MessageService messageService;

    @Override
    public void receiveDestination(Destination destination) {

        Log.info(destination + "到达，主机名:"+destination.getSocket().getInetAddress().getCanonicalHostName());

    }

    @Override
    public void receiveMessage(Message message) {
        Destination destination = message.getDestination();
        liveMapping.put(destination,System.currentTimeMillis());
        Log.info("收到消息:" + message);
        Message result = messageChainProcessor.processMessage(message);
        try {
            result.sendMessage();
        } catch (IOException e) {
            Log.error("发送消息时发生异常:"+e.getMessage());
        }
    }

    @Override
    public void destinationClose(Destination destination) {
        Log.info(destination + "离开");
        messageService.removeDestination(destination);
    }

    public List<String> getOnlineClient() {
        List<Destination> destinationList = this.destinationList;
        List<String> ret = new ArrayList<>();

        for (Destination destination : destinationList) {
            ret.add(destination.getSocket().getInetAddress().getHostAddress());
        }

        return ret;

    }

    @Autowired
    public void setMessageChainProcessor(MessageChainProcessor messageChainProcessor) {
        this.messageChainProcessor = messageChainProcessor;
    }


}
