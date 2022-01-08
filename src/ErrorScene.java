
public class ErrorScene implements Scene {

    int cnt;
    int cntLimit = 60;
    int drawCnt;
    int lastDrawTime;
    int waitTime;
    String randChars = "!@#$%&*WBK";

    @Override
    public void init() {
        cnt = 1;
        waitTime = 0;
        lastDrawTime = 0;
        drawCnt = 15;
    }

    @Override
    public void update() {
        if (App.proc.millis() - lastDrawTime < waitTime) return;
        lastDrawTime = App.proc.millis();
        draw();
        waitTime = (int)Math.min(1f/cnt * 1000 + App.r(50, 200), 1000);
        cnt += 1;
    }

    @Override
    public void keyPressed() {

    }

    @Override
    public void end() {
    }

    @Override
    public boolean isEnd() {
        return cnt >= cntLimit;
    }

    void draw() {
        for (int i = 0; i < drawCnt + cnt*cnt; i++) {
            int w = (int)App.r(0, App.screen.width);
            int h = (int)App.r(0, App.screen.height);
            int s = (int)App.r(App.screen.fontSize, App.screen.fontSize*3);
            App.screen.setChar(randChars.charAt((int)App.r(0, randChars.length())),
                                w, h, App.colorPalette.randomColor());
        }
    }


}
