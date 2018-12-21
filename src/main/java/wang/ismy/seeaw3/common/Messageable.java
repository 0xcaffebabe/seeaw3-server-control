package wang.ismy.seeaw3.common;


import wang.ismy.seeaw3.dto.Message;

//实现了该接口的类都能接收消息
public interface Messageable {

    void receiveMessage(Message message);

    void destroyDestination(Destination destination);
}
