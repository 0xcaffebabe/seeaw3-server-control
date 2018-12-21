package wang.ismy.seeaw3.common;

public interface Terminalable {

    void terminalCreated(String sessionId);

    void terminalOutput(String s);
}
