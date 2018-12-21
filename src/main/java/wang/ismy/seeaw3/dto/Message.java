package wang.ismy.seeaw3.dto;

import lombok.Data;
import wang.ismy.seeaw3.common.Destination;

import java.io.IOException;

@Data
public class Message {


    private Destination destination;

    private String content;

    public Message(Message message) {
        this.content = message.getContent();
        this.destination = message.getDestination();
    }

    public Message(){

    }

    public void sendMessage() throws IOException {
        destination.sendMessage(this);
    }

}
