package wang.ismy.seeaw3.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Command {

    private String command;

    private Map<String,String> params = new HashMap<>();

    public Command command(String command){
        this.command = command;
        return this;
    }

    public Command param(String key,String value){
        params.put(key,value);
        return this;
    }

    public Command delteParam(String key){
        params.remove(key);
        return this;
    }

    public String getParameter(String key){
        return params.get(key);
    }


}
