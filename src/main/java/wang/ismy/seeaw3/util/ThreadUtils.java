package wang.ismy.seeaw3.util;

import static wang.ismy.seeaw3.common.Constant.WAIT_TIME;

public class ThreadUtils {

    public static void sleep(int ms){

        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(){
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
