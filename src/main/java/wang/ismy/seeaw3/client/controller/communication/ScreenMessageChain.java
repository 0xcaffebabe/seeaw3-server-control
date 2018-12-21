package wang.ismy.seeaw3.client.controller.communication;

import wang.ismy.seeaw3.client.BaseClient;
import wang.ismy.seeaw3.common.MessageChain;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.dto.Result;

public class ScreenMessageChain implements MessageChain {

    private BaseClient baseClient;


    public ScreenMessageChain(BaseClient baseClient) {
        this.baseClient = baseClient;
    }

    @Override
    public void processMessage(Message request, Message response) {

        Result result = MessageChain.convertResult(request.getContent());
        if (MessageChain.isCommunicationMessage(result)){

            Result subResult = MessageChain.convertResult(result.result("type").toString());


            if ("screen".equals(subResult.result("type"))){


            }
        }
    }
}
