package wang.ismy.seeaw3.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import wang.ismy.seeaw3.dto.Command;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.dto.Result;

public interface MessageChain {

    void processMessage(Message request, Message response);

    static boolean isMatchCommand(Message message,String command){
        JsonObject jsonObject = new JsonParser().parse(message.getContent()).getAsJsonObject();
        return jsonObject.get("command").getAsString().equalsIgnoreCase(command);
    }

    static Command convertCommand(String json){
        return new Gson().fromJson(json,Command.class);
    }

    static Result convertResult(String json){
        return new Gson().fromJson(json,Result.class);
    }

    static boolean isCommunicationMessage(Result result) {
        try {
            return "communication".equalsIgnoreCase(result.getResult().get("type").toString());
        } catch (Exception e) {
            return false;
        }

    }
}
