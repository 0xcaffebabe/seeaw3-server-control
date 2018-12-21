package wang.ismy.seeaw3;


import wang.ismy.seeaw3.client.ControlClient;
import wang.ismy.seeaw3.client.MasterClient;

import wang.ismy.seeaw3.common.Log;
import wang.ismy.seeaw3.dto.Command;


public class Main2 {

    public static void main(String[] args) {

        ControlClient controlClient = new ControlClient();
        MasterClient masterClient = new MasterClient();

        Command command = new Command();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        command.command("onLineClient")
                .param("token","71571187");
        masterClient.sendMessageIgnoreException(command);








    }
}
