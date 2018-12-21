package wang.ismy.seeaw3.common;

import wang.ismy.seeaw3.dto.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SubListenThread extends Thread{

    private Destination destination;

    private Messageable messageable;

    public SubListenThread(Destination destination,Messageable messageable) {
        this.destination = destination;
        this.messageable = messageable;
    }

    @Override
    public void run() {
        listen();
    }

    private void listen(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(destination.getSocket().getInputStream(),"UTF8"));
            String str = null;
            while ((str=br.readLine()) != null){
                // 每一行就是一条消息
                Message message = new Message();
                message.setContent(str);
                message.setDestination(destination);
                // 发送给消息处理器处理
                messageable.receiveMessage(message);

            }
        } catch (IOException e) {
            Log.error(destination+"消息处理出现异常:"+e.getMessage()+",判断该客户已经失去连接");
            messageable.destroyDestination(destination);
        }
    }
}
