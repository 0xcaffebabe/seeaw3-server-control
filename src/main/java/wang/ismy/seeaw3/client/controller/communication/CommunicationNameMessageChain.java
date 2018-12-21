package wang.ismy.seeaw3.client.controller.communication;


import wang.ismy.seeaw3.client.BaseClient;
import wang.ismy.seeaw3.common.Log;
import wang.ismy.seeaw3.common.MessageChain;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.dto.Result;

public class CommunicationNameMessageChain implements MessageChain {


    private BaseClient baseClient;
    public CommunicationNameMessageChain(BaseClient client){

        baseClient = client;
    }

    @Override
    public void processMessage(Message request, Message response) {

        Result result = MessageChain.convertResult(request.getContent());

        try{
            if ("communicationName".equalsIgnoreCase(result.result("type").toString())){
                baseClient.setCommunicationName(result.result("name").toString());
                Log.info("分配到的通信名称:"+baseClient.getCommunicationName());
            }
        }catch (NullPointerException e){

        }

    }
}
