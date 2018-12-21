package wang.ismy.seeaw3.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wang.ismy.seeaw3.dto.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Destination {

    private Socket socket;

    public void sendMessage(Message message) throws IOException {
        Log.info("正在发送消息:"+message);
        PrintWriter printWriter =new PrintWriter(socket.getOutputStream(),true, Charset.forName("UTF8"));
        printWriter.println(message.getContent());
        printWriter.flush();
    }
}
