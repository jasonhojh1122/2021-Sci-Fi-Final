import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

public class FaceScene implements Scene {

    List<PImage> faces;
    int startX;
    int startY;
    int width;
    int height;
    int cnt;
    int backgroundCnt = 2500;
    int lastTime;
    int waitTimeSec = 5;
    String randomChar = "-=#";

    int faceID;

    boolean waiting;
    boolean faceShowed;

    public FaceScene() {
        faces = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            faces.add(App.proc.loadImage("Face_" + String.valueOf(i) + "_ccexpress.png"));
        }
        width = faces.get(0).width;
        height = faces.get(0).height;
        startX = App.screen.width / 2 - width / 2;
        startY = App.screen.height / 2 - height / 2;
    }

    @Override
    public void init() {
        cnt = 0;
        lastTime = App.proc.millis();
        waiting = true;
        faceShowed = false;
    }

    @Override
    public void update() {
        if (App.proc.frameCount % 5 != 0) return;
        App.screen.clear();
        drawBackground();
        if (waiting) {
            if (App.proc.millis() - lastTime > waitTimeSec * 1000) {
                waiting = false;
            }
        }
        if (!waiting && !faceShowed) {
            faceID = (int)App.r(0, 20);
            App.screen.drawImage(faces.get(faceID), width, height, startX, startY, App.colorPalette.mediumGreen);
            cnt += 1;
            if (cnt == 150) {
                waiting = true;
                lastTime = App.proc.millis();
                faceShowed = true;
            }
        }
        else if (waiting && faceShowed) {
            App.screen.drawImage(faces.get(faceID), width, height, startX, startY, App.colorPalette.mediumGreen);
        }
    }

    @Override
    public void keyPressed() {

    }

    @Override
    public void end() {
    }

    @Override
    public boolean isEnd() {
        return !waiting && faceShowed;
    }

    void drawBackground() {
        for (int i = 0; i < backgroundCnt; i++) {
            int w = (int)App.r(0, App.screen.width);
            int h = (int)App.r(0, App.screen.height);
            App.screen.setChar(randomChar.charAt((int)App.r(0, randomChar.length())), w, h, App.colorPalette.lightGreen);
        }
    }

}
