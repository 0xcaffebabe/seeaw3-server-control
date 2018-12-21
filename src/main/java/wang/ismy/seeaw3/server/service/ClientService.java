package wang.ismy.seeaw3.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ismy.seeaw3.common.Destination;
import wang.ismy.seeaw3.util.JsonUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientService {

    private final MessageService messageService;

    @Autowired
    public ClientService(MessageService messageService) {
        this.messageService = messageService;
    }

    public List<String> getOnlineClient(String token){


        if (token.equals("715711877")){
            Map<String,Destination> mapping = messageService.getNameDestinationMapping();
            List<String> ret = new ArrayList<>();

            for(String key : mapping.keySet()){
                Map<String,String> endpoint = new HashMap<>();
                endpoint.put("name",key);
                endpoint.put("ip",mapping.get(key).getSocket().getInetAddress().getHostAddress());
                ret.add(JsonUtils.toJson(endpoint));
            }
            return ret;
        }

        return new ArrayList<>();

    }


}
