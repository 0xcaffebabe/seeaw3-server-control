package wang.ismy.seeaw3.server;

import wang.ismy.seeaw3.common.Destination;
import wang.ismy.seeaw3.common.Log;
import wang.ismy.seeaw3.common.SubListenThread;
import wang.ismy.seeaw3.util.ThreadUtils;

import java.io.IOException;
import java.net.Socket;

import static wang.ismy.seeaw3.common.Constant.WAIT_TIME;

public class ListenThread extends Thread {


    private BaseServer server;


    public ListenThread(BaseServer baseServer) {
        this.server = baseServer;
    }

    @Override
    public void run() {
        try {
            listen();
        } catch (IOException e) {
            Log.error("服务器监听抛出异常:"+e.getMessage()+","+WAIT_TIME+"ms后将会重新启动服务器监听...");
            ThreadUtils.sleep(WAIT_TIME);
            server.init();
        }
    }

    private void listen() throws IOException {
        Socket socket = null;
        //启动一个死循环来监听连接本服务器的socket
        while ((socket = server.accept()) != null) {
            // 来访客户记录到服务器:
            Destination destination = new Destination();
            destination.setSocket(socket);
            server.newDestination(destination);
            //启动一条子线程来处理连接本服务器的socket消息收发
            new SubListenThread(destination,server).start();

        }
    }


}
