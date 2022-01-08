import processing.core.PApplet;

import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

public class App extends PApplet {

    public static PApplet proc;
    public static Screen screen;
    public static ColorPalette colorPalette;

    Scene curScene;
    Queue<Scene> scenes;

    public static void main(String[] args) {
        PApplet.main("App", args);
    }

    public static float r01() {
        return proc.random(0, 1);
    }

    public static float r(float a, float b) {
        return proc.random(a, b);
    }

    @Override
    public void settings() {
        fullScreen();
    }

    @Override
    public void setup() {
        proc = this;
        background(0);
        colorPalette = new ColorPalette();
        screen = new Screen("3270-Regular.ttf");
        scenes = new ArrayDeque<>();
        scenes.add(new StartScene());
        scenes.add(new HackingScene());
        scenes.add(new VirtualSpace());
        scenes.add(new ErrorScene());
        scenes.add(new FaceScene());
        try {
            scenes.add(new EndScene());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        curScene = scenes.peek();
        Objects.requireNonNull(curScene).init();
    }

    @Override
    public void draw() {
        if (keyPressed) {
            System.out.println("PRESSED");
            curScene.keyPressed();
        }
        if (curScene.isEnd()) {
            curScene.end();
            scenes.remove();
            scenes.add(curScene);
            curScene = scenes.peek();
            Objects.requireNonNull(curScene, "Null scene.").init();
        }
        else {
            curScene.update();
        }
    }
}
