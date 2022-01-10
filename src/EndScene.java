public class EndScene implements Scene {

    boolean ended;

    String[] msg;

    public EndScene() {
        msg = App.proc.loadStrings("error.txt");
    }

    @Override
    public void init() {
        App.proc.background(0);
        ended = false;
        int i = 1;
        for (String s : msg) {
            App.screen.drawString(s, 1, i, App.colorPalette.white);
            i += 2;
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void keyPressed() {
        if (App.proc.key == 'r') {
            ended = true;
        }
    }

    @Override
    public void end() {

    }

    @Override
    public boolean isEnd() {
        return ended;
    }
}
