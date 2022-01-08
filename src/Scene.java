import java.awt.event.KeyEvent;

public interface Scene {
    void init();
    void update();
    void keyPressed();
    void end();
    boolean isEnd();
}
