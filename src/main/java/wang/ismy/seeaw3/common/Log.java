package wang.ismy.seeaw3.common;

import java.time.LocalDateTime;

public class Log {

    private static boolean enable = true;

    public static void error(String message){
        if (enable){
            System.err.println(LocalDateTime.now()+"---"+message);
        }

    }

    public static void info(String message){
        if (enable){
            System.out.println(LocalDateTime.now()+"---"+message);
        }

    }

    public static void disable(){
        enable = false;
    }

    public static void enable(){
        enable = true;
    }


}
