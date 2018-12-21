package wang.ismy.seeaw3.server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wang.ismy.seeaw3.common.MessageChain;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.dto.Result;
import wang.ismy.seeaw3.server.service.ClientService;
import wang.ismy.seeaw3.util.JsonUtils;

@Component
public class OnlineClientMessageChain implements MessageChain {

    @Autowired
    private ClientService clientService;

    @Override
    public void processMessage(Message message, Message result) {
        if (MessageChain.isMatchCommand(message,"onLineClient")){
            Result ret = new Result();
            ret.msg("success")
                 .result("type","onLineClient")
                .result("onLineClientList",JsonUtils.toJson(clientService.getOnlineClient("715711877")));
            result.setContent(JsonUtils.toJson(ret));
        }

    }
}
