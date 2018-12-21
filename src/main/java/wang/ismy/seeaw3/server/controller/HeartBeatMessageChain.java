package wang.ismy.seeaw3.server.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import wang.ismy.seeaw3.common.MessageChain;
import wang.ismy.seeaw3.dto.Command;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.dto.Result;
import wang.ismy.seeaw3.util.JsonUtils;

@Component
public class HeartBeatMessageChain implements MessageChain {
    @Override
    public void processMessage(Message request, Message response) {

        Command command = MessageChain.convertCommand(request.getContent());

        if ("heartBeat".equals(command.getCommand())){
            response.setContent(JsonUtils.toJson(
                    new Result()
                    .msg("success")
                    .result("type","heartBeat")
            ));
        }
    }
}
