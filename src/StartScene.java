import processing.core.PImage;

public class StartScene implements Scene {

    boolean ended;
    PImage logo;
    int logoSizeX = 60;
    int logoSizeY = 60;
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
        logoStartX = App.screen.width / 2 - logoSizeX / 2;
        logoStartY = App.screen.height / 2 - logoSizeY / 2 - 10;
        hintStartX = App.screen.width / 2 - hintText.length() / 2;
        hintStartY = App.screen.height / 2 + logoSizeY / 2 + 5;
        ended = false;
    }

    @Override
    public void init() {
        ended = false;
        logoCurShift = 0;
        movingDown = true;
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

    @Override
    public void end() {
        ended = false;
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
        App.screen.drawImage(logo, logoSizeX, logoSizeY, logoStartX, y);
    }

    private void drawHint() {
        App.screen.drawString(hintText, hintStartX, hintStartY, App.colorPalette.white);
    }
}
