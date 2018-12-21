package wang.ismy.seeaw3.util;

import com.google.gson.Gson;

public class JsonUtils {

    private static final Gson GSON = new Gson();

    public static String toJson(Object src){
        return GSON.toJson(src);
    }
}
