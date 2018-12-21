package wang.ismy.seeaw3.client;

import com.google.gson.Gson;
import wang.ismy.seeaw3.common.*;
import wang.ismy.seeaw3.dto.Command;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.util.ThreadUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static wang.ismy.seeaw3.common.Constant.WAIT_TIME;

public abstract class BaseClient implements Messageable {

    private static final int PORT = 2000;
    private static final String HOST_NAME = "ismy.wang";

    private Socket socket;
    private SubListenThread listenThread;
    private LiveThread liveThread;
    private String communicationName;
    protected long serverLastResponse = System.currentTimeMillis(); //服务器最后响应时间


    public BaseClient() {
        init();
    }


    public void sendMessage(Message message) throws IOException {
        message.sendMessage();
    }

    public void sendMessage(String msg) throws IOException {
        Message message = new Message();
        message.setDestination(new Destination(socket));
        message.setContent(msg);
        message.sendMessage();
    }

    public void sendMessage(Command command) throws IOException {
        Message message = new Message();
        message.setDestination(new Destination(socket));
        message.setContent(new Gson().toJson(command));
        message.sendMessage();
    }

    public void sendMessageIgnoreException(Message message) {
        try {
            message.sendMessage();
        } catch (IOException e) {
            Log.error("客户端发送消息失败:" + e.getMessage() + ",判断与服务器失去连接，" + WAIT_TIME + "ms后重连");
            ThreadUtils.sleep();
            init();
        }
    }

    public void sendMessageIgnoreException(String msg) {
        try {
            Message message = new Message();
            message.setDestination(new Destination(socket));
            message.setContent(msg);
            message.sendMessage();
            Log.info("客户端发送了一条消息:" + message);
        } catch (IOException e) {
            Log.error("客户端发送消息失败:" + e.getMessage() + ",判断与服务器失去连接，" + WAIT_TIME + "ms后重连");
            ThreadUtils.sleep();
            init();
        }
    }

    public void sendMessageIgnoreException(Command command) {
        try {
            Message message = new Message();
            message.setDestination(new Destination(socket));
            message.setContent(new Gson().toJson(command));
            message.sendMessage();
            Log.info("客户端发送了一条消息:" + message);
        } catch (IOException e) {
            Log.error("客户端发送消息失败:" + e.getMessage() + ",判断与服务器失去连接，" + WAIT_TIME + "ms后重连");
            ThreadUtils.sleep();
            init();
        }
    }

    public abstract void receiveMessage(Message message);

    public String getCommunicationName() {
        return communicationName;
    }

    public void setCommunicationName(String communicationName) {
        this.communicationName = communicationName;
    }


    protected void init() {
        serverLastResponse = System.currentTimeMillis();
        createConnection();
        startListenThread();
        createAndStartLiveThread();
        sendInitCommunication();

    }


    private void createConnection() {
        while (true) {
            try {
                socket = new Socket(InetAddress.getByAddress(new byte[]{120,79,6,(byte)172}), PORT);
                Log.info("客户端创建与服务器的连接成功");
                break;
            } catch (IOException e) {
                Log.error("客户端创建与服务器的连接失败，原因:" + e.getMessage() + "," + WAIT_TIME + "ms后重试");
                ThreadUtils.sleep();

            }
        }
    }

    private void startListenThread() {
        listenThread = new SubListenThread(new Destination(socket), this);
        listenThread.start();
    }

    private void createAndStartLiveThread() {
        liveThread = new LiveThread();
        liveThread.start();
    }

    private void sendInitCommunication() {
        Command command = new Command();
        command.command("communication")
                .param("content", "init");
        sendMessageIgnoreException(command);
    }

    class LiveThread extends Thread {
        public boolean flag = true;

        @Override
        public void run() {
            while (flag) {

                Command command = new Command();
                command.command("heartBeat")
                        .param("content", "")
                        .param("type", "heartBeat");
                sendMessageIgnoreException(command);
                //每两秒发送一条心跳

                // 如果服务器最后响应是在30秒之前，代表连接断了，重新连接服务器
                if (System.currentTimeMillis() - serverLastResponse >30000){
                    Log.error("服务器已经超过30秒无反应，准备重新初始化");
                    break;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            init();
        }
    }
}
