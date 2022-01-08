import processing.core.PApplet;
import processing.core.PFont;

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
        scenes.add(new VirtualSpace());
        scenes.add(new HackingScene());
        curScene = scenes.peek();
        Objects.requireNonNull(curScene).init();
    }

    @Override
    public void draw() {
        if (keyPressed) {
            curScene.keyPressed();
        }
        if (curScene.isEnd()) {
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
