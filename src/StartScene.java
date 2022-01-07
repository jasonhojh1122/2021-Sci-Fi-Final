import processing.core.PImage;

public class StartScene implements Scene {

    boolean ended;
    PImage logo;
    int logoSize = 60;
    int logoStartX;
    int logoStartY;
    int logoCurShift;
    int logoMaxShift = 5;
    boolean movingDown;

    String hintText = "Press enter to start.";
    int hintStartX;
    int hintStartY;

    public StartScene() {
        logo = App.proc.loadImage("Logo.png");
        ended = false;
    }

    @Override
    public void init() {
        int size = 60;
        logoStartX = App.screen.width / 2 - size / 2;
        logoStartY = App.screen.height / 2 - size / 2 - 10;
        logoCurShift = 0;
        movingDown = true;
        hintStartX = App.screen.width / 2 - hintText.length() / 2;
        hintStartY = App.screen.height / 2 + logoSize / 2 + 5;
    }

    @Override
    public void update() {
        if (App.proc.frameCount % 20 != 0) return;
        if (App.proc.frameCount % 40 == 0)
            App.screen.clear();
        shiftLogo();
        drawLogo(logoStartY + logoCurShift);
        drawHint();
    }

    @Override
    public void keyPressed() {
        if (App.proc.key == '\n') {
            ended = true;
        }
    }

    @Override
    public boolean isEnd() {
        return ended;
    }

    private void shiftLogo() {
        if (movingDown) {
            logoCurShift--;
            if (logoCurShift < -logoMaxShift) {
                logoCurShift++;
                movingDown = false;
            }
        }
        else {
            logoCurShift++;
            if (logoCurShift > logoMaxShift) {
                logoCurShift--;
                movingDown = true;
            }
        }
    }

    private void drawLogo(int y) {
        App.screen.drawImage(logo, logoSize, logoSize, logoStartX, y);
    }

    private void drawHint() {
        App.screen.drawString(hintText, hintStartX, hintStartY, App.colorPalette.white);
    }
}
