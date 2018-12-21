package wang.ismy.seeaw3.client.entity;


import lombok.Data;
import wang.ismy.seeaw3.common.Log;

import java.io.*;
import java.util.function.Consumer;

@Data
public class Terminal {

    private String sessionId;

    private Process process;

    private String terminalType; //powershell 或cmd

    private String charset; // 字符编码

    private OutputStream terminalWriter;

    private TerminalObserver observer; //终端观察者，当终端有信息输出时，将会调用相关接口

    private Thread onErrorThread;

    private Thread onOutputThread;


    public Terminal(String sessionId, String terminalType, String charset,TerminalObserver observer) {
        this.sessionId = sessionId;
        this.terminalType = terminalType;
        this.observer = observer;
        this.charset = charset;
        init();
    }

    public void input(String msg) throws IOException {
        terminalWriter.write((msg+"\n").getBytes());
        terminalWriter.flush();
    }

    public void inputIgnoreException(String msg){
        try {
            input(msg);
        } catch (IOException e) {
            Log.error("终端输入"+msg+"错误:"+e.getMessage());
        }
    }

    public void close(){
        process.destroy();

    }

    private void init() {

        try {
            process = Runtime.getRuntime().exec(terminalType);
            this.terminalWriter = process.getOutputStream();
            onErrorThread = createNotifyThread(process.getErrorStream(),observer::onError);
            onErrorThread.start();
            onOutputThread = createNotifyThread(process.getInputStream(),observer::onOutput);
            onOutputThread.start();
        } catch (IOException e) {
            Log.error("创建终端失败:" + e.getMessage());
        }
    }

    private Thread createNotifyThread(InputStream inputStream, Consumer<Character> consumer) {

        return new Thread(() -> {
            int c = -1;
            try {
                InputStreamReader reader = new InputStreamReader(inputStream,charset);
                while ((c = reader.read()) != -1) {
                    consumer.accept((char)c);
                }
            } catch (Exception e) {
                Log.error("终端出现异常："+e.getMessage());
                observer.onClose();
            }

        });

    }


}
