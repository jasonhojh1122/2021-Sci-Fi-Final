import java.util.ArrayList;
import java.util.List;

public class HackingScene implements Scene{

    int firstRow;

    int iceLines = 5;
    int iceStartY;
    int iceEndY;

    String hint = " Hold ENTER to hack.";
    int hintStartX;
    int hintY;
    int hintEndX;

    float clearProb;
    float randomProb;
    float minRandom;
    List<List<Integer>> randomNumbers;
    boolean ended;

    public HackingScene() {
        iceStartY = App.screen.height / 2 + 5;
        iceEndY = iceStartY + iceLines;
        hintStartX = App.screen.width / 2 - hint.length() / 2;
        hintEndX = hintStartX + hint.length();
        hintY = iceStartY - 5;
    }

    @Override
    public void init() {
        ended = false;
        App.screen.clear();
        firstRow = 0;
        clearProb = 1.0f;
        randomProb = 0.0f;
        minRandom = 0.0f;
        initRandomNumbers();
    }

    @Override
    public void update() {
        if (App.proc.frameCount % 5 != 0) return;
        App.screen.clear();
        updateIntegers();
        updateScreen();
    }

    @Override
    public void keyPressed() {
        if (App.proc.frameCount % 10 < 2 && App.proc.key == '\n') {
            randomProb = Math.min(randomProb + 0.005f, 0.9f);
            clearProb = Math.max(clearProb - 0.005f, 0.005f);
            minRandom = Math.min(minRandom + 0.01f, 7.0f);
            if (Math.abs(clearProb - 0.005) < 0.001) {
                ended = true;
            }
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

    private void initRandomNumbers() {
        randomNumbers = new ArrayList<>();
        for (int x = 0; x < App.screen.width; x++) {
            randomNumbers.add(new ArrayList<>());
            for (int y = 0; y < App.screen.height; y++) {
                randomNumbers.get(x).add(-1);
            }
        }
    }

    private void updateIntegers() {
        for (int x = 0; x < App.screen.width; x += 2) {
            setRandomNumber(x, firstRow, false);
        }
        for (int x = 0; x < App.screen.width; x += 2) {
            for (int y = 0; y < App.screen.height; y++) {
                int indexY = trueYToArrayIndex(y);
                int value = randomNumbers.get(x).get(indexY);
                if (y >= iceStartY && y < iceEndY) {
                    if (App.r01() <= clearProb) {
                        setRandomNumber(x, indexY, true);
                    }
                }
                else if (y < iceEndY && value != -1 && App.r01() < randomProb) {
                    setRandomNumber(x, indexY, false);
                }
            }
        }
        firstRow = (firstRow + 1) % App.screen.height;
    }

    private void setRandomNumber(int x, int y, boolean clear) {
        if (clear)
            randomNumbers.get(x).set(y, -1);
        else
            randomNumbers.get(x).set(y, (int)Math.floor(App.r(minRandom, 10)));
    }

    private void updateScreen() {
        drawHint();
        for (int x = 0; x < App.screen.width; x++) {
            for (int y = 0; y < App.screen.height; y++) {
                if (isHintArea(x, y))
                    continue;
                int yIndex = trueYToArrayIndex(y);
                int value = randomNumbers.get(x).get(yIndex);
                if (y >= iceStartY && y < iceEndY) {
                    App.screen.setChar(numberToICE(value), x, y, App.colorPalette.white);
                }
                else {
                    if (value == -1)
                        App.screen.setChar(' ', x, y, numberToColor(value));
                    else
                        App.screen.setChar((char)(value+48), x, y, numberToColor(value));
                }
            }
        }
    }

    private boolean isHintArea(int x, int y) {
        return y >= hintY - 1 && y <= hintY + 1 && x <= hintEndX && x >= hintStartX;
    }

    private void drawHint() {
        App.screen.drawString(hint, hintStartX, hintY, App.colorPalette.white);
    }

    private char numberToICE(int value) {
        if (value < 2)
            return '-';
        else if (value < 5)
            return '=';
        else if (value < 8)
            return '+';
        else
            return '#';
    }

    private int numberToColor(int value) {
        if (value < 2)
            return App.colorPalette.darkGreen;
        else if (value < 5)
            return App.colorPalette.mediumGreen;
        else if (value < 8)
            return App.colorPalette.lightGreen;
        else
            return App.colorPalette.red;
    }


    private int trueYToArrayIndex(int y) {
        return (firstRow - y + App.screen.height - 1) % App.screen.height;
    }

}
