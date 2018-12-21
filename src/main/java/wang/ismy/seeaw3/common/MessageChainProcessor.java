package wang.ismy.seeaw3.common;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.dto.Result;



import java.util.List;

@Component
public class MessageChainProcessor {

   private List<MessageChain> messageChainList;


    public MessageChainProcessor() {
    }

    public MessageChainProcessor(List<MessageChain> messageChainList) {
        this.messageChainList = messageChainList;
    }


    @Autowired
    public void setMessageChainList(List<MessageChain> messageChainList) {
        this.messageChainList = messageChainList;
    }

    public Message processMessage(Message request){
        Message response = new Message();
        for (MessageChain chain : messageChainList) {
            //捕获消息链的所有异常
            try{
                chain.processMessage(request, response);
            }catch (Throwable e){
                e.printStackTrace();
                response.setContent(new Gson().toJson(new Result()
                        .msg("error")
                        .result("message",e.getMessage())));
                break;
            }

        }
        response.setDestination(request.getDestination());
        return response;
    }


}
