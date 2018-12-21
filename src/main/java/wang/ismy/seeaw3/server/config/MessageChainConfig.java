package wang.ismy.seeaw3.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wang.ismy.seeaw3.common.MessageChain;

import java.util.Arrays;
import java.util.List;

@Configuration
public class MessageChainConfig {

    @Bean("messageChainList")
    public List<MessageChain> messageChainList(MessageChain...messageChain){
        return Arrays.asList(messageChain);
    }
}
