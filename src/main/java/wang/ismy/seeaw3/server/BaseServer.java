package wang.ismy.seeaw3.server;

import wang.ismy.seeaw3.common.*;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.util.ThreadUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static wang.ismy.seeaw3.common.Constant.WAIT_TIME;

public abstract class BaseServer implements Messageable {

    private static final int LISTEN_PORT=2000;

    private ServerSocket serverSocket;

    protected List<Destination> destinationList = new LinkedList<>(); // 当前所有在线客户列表

    protected Map<Destination,Long> liveMapping = new ConcurrentHashMap<>(); //目标源最后通信时间

    private ListenThread listenThread = new ListenThread(this);

    public BaseServer() {

        init();
    }

    public Socket accept() throws IOException {
        return serverSocket.accept();
    }

    /*
    * 当服务器有新客户到达时会调用这个方法
    */



    public abstract void receiveDestination(Destination destination);

    /*
    * 当服务器有新消息到达时会调用这个方法
    */
    public abstract void receiveMessage(Message message);

    /*
    * 当客户断开时会调用这个方法
    */
    public abstract void destinationClose(Destination destination);

    public void init(){

        createListenSocket();
        startListenThread();

    }

    public void newDestination(Destination destination){
        //一旦有新客户，将会加入到在线用户列表
        destinationList.add(destination);
        liveMapping.put(destination,System.currentTimeMillis());
        receiveDestination(destination);
    }

    public void destroyDestination(Destination destination){
        //客户与服务器失去连接时会调用此方法
        destinationList.remove(destination);
        liveMapping.remove(destination);
        try {
            destination.getSocket().close();
        } catch (IOException e) {
            Log.error("关闭socket出现异常:"+e.getMessage());
        }
        Log.error("live mapping:"+liveMapping);
        destinationClose(destination);

    }

    public List<Destination> getDestinationListByIP(String ip){

        return destinationList.stream()
                .filter((i) -> i.getSocket().getInetAddress().getHostAddress().equals(ip))
                .collect(Collectors.toList());
    }
    private void createListenSocket() {
        //如果创建监听socket的时候发生异常,将会隔WAIT_TIME毫秒重试，直到成功
        while (true){
            try {
                serverSocket = new ServerSocket(LISTEN_PORT);
                Log.info("创建监听socket成功，正在监听...");
                break;
            } catch (IOException e) {
                Log.error("创建监听socket失败，原因:"+e.getMessage()+WAIT_TIME+"ms后重建");
                ThreadUtils.sleep(WAIT_TIME);
            }
        }

        startLiveThread();
    }

    private void startLiveThread(){
        new LiveThread().start();
    }
    private void startListenThread(){
        listenThread.start();
    }

    // 该线程作用：每隔10秒扫描liveMapping的每隔目标源 取出每个目标源的最后通信时间
    // 如果最后通信时间与当前时间相隔10秒以上，那就调用destroyDestination
    class LiveThread extends Thread{
        @Override
        public void run(){

            while(true){


                for (Destination key : liveMapping.keySet()){
                    //如果该源已有10秒没有发送消息了
                    if (System.currentTimeMillis() - liveMapping.get(key) >= 10000){
                        destroyDestination(key);
                    }
                }



                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
