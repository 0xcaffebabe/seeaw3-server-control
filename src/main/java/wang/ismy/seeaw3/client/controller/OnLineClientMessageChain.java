package wang.ismy.seeaw3.client.controller;

import com.google.gson.Gson;
import wang.ismy.seeaw3.client.BaseClient;
import wang.ismy.seeaw3.client.MasterClient;
import wang.ismy.seeaw3.common.Log;
import wang.ismy.seeaw3.common.MessageChain;
import wang.ismy.seeaw3.dto.Command;
import wang.ismy.seeaw3.dto.Message;
import wang.ismy.seeaw3.dto.Result;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OnLineClientMessageChain implements MessageChain {

    private BaseClient client;

    public OnLineClientMessageChain(BaseClient client) {
        this.client = client;
    }


    @Override
    public void processMessage(Message request, Message response) {

        Result result = MessageChain.convertResult(request.getContent());

        if ("onLineClient".equalsIgnoreCase(result.result("type").toString())) {

            List<String> onLineList = new Gson().fromJson(result.result("onLineClientList").toString(), List.class);
//
//            for (int i=0;i<onLineList.size();i++){
//
//
//
//                Map<String, String> endpoint = new Gson().fromJson(onLineList.get(i),Map.class);
//
//                System.out.print("index:"+i+",name:"+endpoint.get("name")+",ip:"+endpoint.get("ip"));
//
//                if (endpoint.get("name").equals(client.getCommunicationName())){
//                    System.out.println("(本机)");
//                }else{
//                    System.out.println();
//
//                }
//            }
//
//            System.err.println("输入序列号:");
//            Scanner scanner = new Scanner(System.in);
//            int index= scanner.nextInt();
//            Map<String,String> endpoint = new Gson().fromJson(onLineList.get(index),Map.class);

            //f1(endpoint);
            ((MasterClient)client).onLineList(onLineList);


        }
    }

    private void f1(Map<String,String> endpoint){
//        new Thread(() ->{
//            String to = endpoint.get("name");
//            String from = client.getCommunicationName();
//
//            Command command = new Command();
//            command.command("communication")
//                    .param("to",to)
//                    .param("from",from)
//                    .param("content","openTerminal");
//            client.sendMessageIgnoreException(command);
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//
//            String str =null;
//            Scanner scanner = new Scanner(System.in);
//            while ((str=scanner.nextLine()) != null){
//                command.command("communication")
//                        .param("to",to)
//                        .param("from",from)
//                        .param("content",new Gson().toJson(
//                                new Result()
//                                        .msg("success")
//                                        .result("type","terminalInput")
//                                        .result("sessionId",((MasterClient)client).getSession(0))
//                                        .result("cmd",str)
//
//                        ));
//                client.sendMessageIgnoreException(command);
//            }
//        }).start();
    }
}
