package wang.ismy.seeaw3.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wang.ismy.seeaw3.common.MessageChain;
import wang.ismy.seeaw3.dto.Command;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.dto.Result;
import wang.ismy.seeaw3.server.service.MessageService;
import wang.ismy.seeaw3.util.JsonUtils;

import java.util.Random;

@Component
public class CommunicationMessageChain implements MessageChain {

    @Autowired
    private MessageService messageService;

    @Override
    public void processMessage(Message request, Message response) {
        Command command = MessageChain.convertCommand(request.getContent());

        if (isCommunicationCommand(command)) {
            String from = command.getParameter("from"); //  发送者
            String to = command.getParameter("to"); //  接收者

            /*
            * 如果发送来的这条通信消息是一条init消息
            * 将名称与destination的映射存入消息服务中
            * 并且生成一个name返回给客户端，客户端以此来通信
            */
            if (isInitCommunication(command)) {
                String name = null;
                //如果有这个destination
                if (messageService.containsDestination(request.getDestination())){
                    name = messageService.getNameByDestination(request.getDestination());
                }else{
                    // 若消息内容只是init
                    name = generateName(request);
                    messageService.putMapping(name, request.getDestination());
                }

                response.setContent(JsonUtils.toJson(
                        new Result().msg("success").result("type", "communicationName")
                                                    .result("name",name)
                ));

            } else {
                if (destinationExist(from) && destinationExist(to)) {
                    throw new RuntimeException("通信对象不存在");
                }

                // 如果不是init类型的消息，那就转发这条消息
                Message message = new Message();
                Result result = new Result()
                        .msg("success")
                        .result("type", "communication")
                        .result("content", command.getParameter("content"))
                        .result("origin", request.getDestination().getSocket().getInetAddress().getHostAddress())
                        .result("from", from)
                        .result("to", to);
                message.setContent(JsonUtils.toJson(result));
                messageService.sendMessage(to, message);

                response.setContent(JsonUtils.toJson(
                        new Result().msg("success").result("msg", "send out").result("type","")
                ));
            }
        }
    }

    private boolean isInitCommunication(Command command) {
        return "init".equals(command.getParameter("content"));
    }

    private boolean isCommunicationCommand(Command command) {
        return "communication".equals(command.getCommand());
    }

    // 根据传入的通信名称判断是否有对应的目标源
    private boolean destinationExist(String name) {
        return messageService.getDestinationByName(name) == null;
    }

    private String generateName(Message message){
        Random random = new Random();
        return message.getDestination().getSocket().getInetAddress().getHostAddress()+random.nextInt(99999);
    }
}
