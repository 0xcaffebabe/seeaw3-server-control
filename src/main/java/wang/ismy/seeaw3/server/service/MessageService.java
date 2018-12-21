package wang.ismy.seeaw3.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ismy.seeaw3.common.Destination;
import wang.ismy.seeaw3.common.Log;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.server.BaseServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageService {

    /*
     * 名称与destination的映射
     * */
    private Map<String, Destination> nameDestinationMapping = new ConcurrentHashMap<>();


    public void sendMessage(String name, Message message) {
        Destination toDestination = nameDestinationMapping.get(name);
        Message sendMessage = new Message(message);
        sendMessage.setDestination(toDestination);
        try {
            sendMessage.sendMessage();
        } catch (Exception e) {
            Log.error(sendMessage + "发送消息失败:" + e.getMessage());
        }


    }


    public void putMapping(String name, Destination destination) {
        nameDestinationMapping.put(name, destination);
    }

    public Destination getDestinationByName(String name) {
        return nameDestinationMapping.get(name);
    }

    public Map<String, Destination> getNameDestinationMapping() {
        return nameDestinationMapping;
    }

    public void removeDestination(Destination destination) {
        for (String key : nameDestinationMapping.keySet()) {
            if (nameDestinationMapping.get(key).equals(destination)) {
                nameDestinationMapping.remove(key);
                break;
            }
        }
    }

    public boolean containsDestination(Destination destination){
        return nameDestinationMapping.containsValue(destination);
    }

    public String getNameByDestination(Destination destination){
        for (String key : nameDestinationMapping.keySet()){
            if (nameDestinationMapping.get(key).equals(destination)){
                return key;

            }
        }
        return null;
    }
}
