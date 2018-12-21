package wang.ismy.seeaw3.client.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import wang.ismy.seeaw3.client.BaseClient;
import wang.ismy.seeaw3.client.MasterClient;
import wang.ismy.seeaw3.client.entity.TerminalObserver;
import wang.ismy.seeaw3.client.service.ScreenService;
import wang.ismy.seeaw3.client.service.TerminalService;
import wang.ismy.seeaw3.common.Log;
import wang.ismy.seeaw3.common.MessageChain;
import wang.ismy.seeaw3.common.Terminalable;
import wang.ismy.seeaw3.dto.Command;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.dto.Result;
import wang.ismy.seeaw3.util.JsonUtils;

import java.io.UnsupportedEncodingException;


@Component
public class CommunicationMessageChain implements MessageChain {


    private TerminalService terminalService = new TerminalService();

    private ScreenService screenService = new ScreenService();

    private BaseClient client;

    public CommunicationMessageChain(BaseClient client) {
        this.client = client;
    }

    @Override
    public void processMessage(Message request, Message response) {

        Result result = MessageChain.convertResult(request.getContent());

        if (isSuccessMessage(result)) {

            if (isCommunicationMessage(result)) {

                String from = result.getResult().get("from").toString(); // 发送者
                String to = result.getResult().get("to").toString();

                if (isOpenTerminalCommand(result)) {
                    TerminalObserver observer = getTerminalObserver(from);
                    String sessionId = terminalService.openTerminal(observer);
                    sendSessionId(from, sessionId);
                }

                Result subResult = MessageChain.convertResult(result.getResult().get("content").toString());

                if ("terminalInput".equalsIgnoreCase(subResult.getResult().get("type").toString())) {
                    terminalService.input(subResult.getResult().get("sessionId").toString(),
                            subResult.getResult().get("cmd").toString());
                }

                if ("screen".equalsIgnoreCase(subResult.getResult().get("type").toString())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Command command = new Command()
                                    .command("communication")
                                    .param("to",from)
                                    .param("from",client.getCommunicationName())
                                    .param("content",JsonUtils.toJson(
                                            new Result()
                                                    .msg("success")
                                                    .result("type","screen")
                                                    .result("url",screenService.screenAndUpload())

                                    ));
                            client.sendMessageIgnoreException(command);
                        }
                    }).start();


                }

                if ("photo".equalsIgnoreCase(subResult.getResult().get("type").toString())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Command command = new Command()
                                    .command("communication")
                                    .param("to",from)
                                    .param("from",client.getCommunicationName())
                                    .param("content",JsonUtils.toJson(
                                            new Result()
                                                    .msg("success")
                                                    .result("type","photo")
                                                    .result("url",screenService.photographAndUpload())

                                    ));
                            client.sendMessageIgnoreException(command);
                        }
                    }).start();


                }



            }


        } else {

            Log.error("收到一条失败信息:" + request);
        }
    }

    private void sendSessionId(String to, String sessionId) {
        Command command = new Command();
        command.command("communication")
                .param("from", client.getCommunicationName())
                .param("to", to)
                .param("content", new Gson().toJson(
                        new Result().msg("success")
                                .result("type", "terminalCreated")
                                .result("content", sessionId)
                ));
        client.sendMessageIgnoreException(command);
    }

    private TerminalObserver getTerminalObserver(String to) {
        return new TerminalObserver() {
            @Override
            public void onError(char msg) {
                Command command = new Command();
                command.command("communication")
                        .param("from", client.getCommunicationName())
                        .param("to", to)
                        .param("content", new Gson().toJson(
                                new Result().msg("success")
                                        .result("type", "terminalOutput")
                                        .result("content", msg)
                        ));
                client.sendMessageIgnoreException(command);
            }

            @Override
            public void onOutput(char msg) {
                Command command = new Command();
                command.command("communication")
                        .param("to", to)
                        .param("from", client.getCommunicationName())
                        .param("content", new Gson().toJson(
                                new Result().msg("success")
                                        .result("type", "terminalOutput")
                                        .result("content", msg)
                        ));
                client.sendMessageIgnoreException(command);
            }

            @Override
            public void onClose() {

            }
        };
    }

    private boolean isOpenTerminalCommand(Result result) {
        return "openTerminal".equalsIgnoreCase(result.getResult().get("content").toString());
    }

    private boolean isCommunicationMessage(Result result) {
        try {
            return "communication".equalsIgnoreCase(result.getResult().get("type").toString());
        } catch (Exception e) {
            return false;
        }

    }

    private boolean isSuccessMessage(Result result) {
        return "success".equalsIgnoreCase(result.getMsg());
    }


    @Autowired
    @Qualifier("masterClient")
    public void setBaseClient(BaseClient client) {
        this.client = client;
    }
}
