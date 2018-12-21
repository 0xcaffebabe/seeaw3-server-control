package wang.ismy.seeaw3.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

    private String msg;

    private Map<String,Object> result = new HashMap<>();

    public Result msg(String msg){
        this.msg = msg;
        return this;
    }

    public Result result(String key,Object value){
        result.put(key,value);
        return this;
    }

    public Object result(String key){
        return result.get(key);
    }

}
