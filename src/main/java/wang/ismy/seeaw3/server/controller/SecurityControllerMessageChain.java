package wang.ismy.seeaw3.server.controller;

import org.springframework.stereotype.Component;
import wang.ismy.seeaw3.common.MessageChain;
import wang.ismy.seeaw3.dto.Command;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.dto.Result;
import wang.ismy.seeaw3.server.exception.PermissionDeniedException;

@Component
public class SecurityControllerMessageChain implements MessageChain {

    private String token = "";

    @Override
    public void processMessage(Message request, Message response) {

        Command command = MessageChain.convertCommand(request.getContent());

        if (command.getCommand().equalsIgnoreCase("onLineClient")){


            if (command.getParameter("token").equals(token)){

          }else{
              throw new PermissionDeniedException("permission denied");
          }
        }
    }
}
