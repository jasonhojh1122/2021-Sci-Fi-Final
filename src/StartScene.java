import processing.core.PImage;

import java.awt.event.KeyEvent;

public class StartScene implements Scene {

    PImage logo;
    boolean isEnd;
    boolean hasUpdated;

    public StartScene() {
        logo = App.proc.loadImage("Logo.png");
        isEnd = false;
    }

    @Override
    public void init() {
        int size = 60;
        int startX = App.screen.width / 2 - size / 2;
        int startY = App.screen.height / 2 - size / 2 - 10;
        App.screen.drawImage(logo, size, size, startX, startY);
        String text = "Press enter to start.";
        App.screen.drawText(text, App.screen.width / 2 - text.length() / 2,
                            App.screen.height / 2 + size / 2+ 5, App.proc.color(255));
    }

    @Override
    public void update() {
    }

    @Override
    public void keyPressed() {
        if (App.proc.keyCode == KeyEvent.VK_ENTER) {
            isEnd = true;
        }
    }

    @Override
    public boolean isEnd() {
        return isEnd;
    }
}
