package wang.ismy.seeaw3.client.service;

import org.springframework.stereotype.Service;
import wang.ismy.seeaw3.client.entity.Terminal;
import wang.ismy.seeaw3.client.entity.TerminalObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TerminalService {

    private Map<String,Terminal> terminalMapping = new HashMap<>();

    public String openTerminal(TerminalObserver observer){
        //如果列表中有终端实体，全部关闭并清除
        terminalMapping.forEach((key, value) -> value.close());
        terminalMapping = new HashMap<>();
        String sessionId = generateTerminalId();
        Terminal terminal = new Terminal(sessionId,"powershell","GBK",observer);
        terminalMapping.put(sessionId,terminal);
        return sessionId;
    }

    public void input(String sessionId,String cmd){
        if ("".equals(sessionId)){

            terminalMapping.forEach((k,v)->{
                v.inputIgnoreException(cmd);
            });
        }else{
            Terminal terminal = terminalMapping.get(sessionId);
            if (terminal != null){
                terminal.inputIgnoreException(cmd);
            }
     }


    }

    private String generateTerminalId(){
        Random random = new Random();
        return String.valueOf(random.nextInt(999999999));
    }
}
