import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

enum State {
    LOGIN,

}

public class App extends PApplet {

    public static PApplet proc;
    public static Screen screen;

    Scene curScene;
    Queue<Scene> scenes;

    public static void main(String[] args) {
        PApplet.main("App", args);
    }

    @Override
    public void settings() {
        fullScreen();
    }

    @Override
    public void setup() {
        proc = this;
        background(0);
        screen = new Screen("3270-Regular.ttf");
        scenes = new ArrayDeque<>();
        scenes.add(new StartScene());
        curScene = scenes.peek();
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
        if (screen.isDirty()) {
            screen.refresh();
        }
    }
}
