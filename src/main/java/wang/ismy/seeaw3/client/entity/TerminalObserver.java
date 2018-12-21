package wang.ismy.seeaw3.client.entity;

public interface TerminalObserver {


    void onError(char msg);

    void onOutput(char msg);

    void onClose();

}
