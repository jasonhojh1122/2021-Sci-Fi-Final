import org.jetbrains.annotations.NotNull;
import processing.core.*;

public class Screen {

    public int width = 192;
    public int height = 108;

    public PFont font;
    public int fontSize = 16;

    float fontOffsetX = 10;
    float fontOffsetY = 10;

    String asciiChars = " .:-=+*#%$@";
    char[] asciiTable;


    public Screen(String fontName) {
        initFont(fontName);
        initAsciiTable();
        clear();
    }

    private void initFont(String fontName) {
        font = App.proc.createFont(fontName, fontSize);
        App.proc.textFont(font);
        App.proc.textAlign(App.proc.LEFT, App.proc.TOP);
    }

    private void initAsciiTable() {
        asciiTable = new char[256];
        for (int i = 0; i < 256; i++) {
            asciiTable[i] = asciiChars.charAt((int)((double)i / 256d * (double)asciiChars.length()));
        }
    }

    public void drawImage(PImage image, int drawWidth, int drawHeight, int startX, int startY) {
        image.loadPixels();
        int escapeX = image.width / (drawWidth-1);
        int escapeY = image.height / (drawHeight-1);
        for (int x = 0; x < drawWidth && x + startX < width; x++) {
            for (int y = 0; y < drawHeight && y + startY < height; y++) {
                int color = image.pixels[y * escapeY * image.width + x*escapeX];
                char c = asciiTable[(int)App.proc.brightness(color)];
                drawChar(c, x+startX, y+startY, color);
            }
        }
    }

    public void drawImage(PImage image, int drawWidth, int drawHeight, int startX, int startY, int color) {
        image.loadPixels();
        int escapeX = image.width / drawWidth;
        int escapeY = image.height / drawHeight;
        for (int x = 0; x < drawWidth && x + startX < width; x++) {
            for (int y = 0; y < drawHeight && y + startY < height; y++) {
                int pc = image.pixels[y * escapeY * image.width + x*escapeX];
                char c = asciiTable[(int)App.proc.brightness(pc)];
                drawChar(c, x+startX, y+startY, color);
            }
        }
    }

    public void drawString(@NotNull String str, int startX, int startY, int color) {
        for (int x = 0; x < str.length() && x + startX < width; x++) {
            drawChar(str.charAt(x), x+startX, startY, color);
        }
    }

    public void setChar(char c, int x, int y, int color) {
        drawChar(c, x, y, color);
    }

    private void drawChar(char c, int x, int y, int color) {
        App.proc.fill(color);
        App.proc.text(c, fontOffsetX*x, fontOffsetY*y);
    }

    public void clear() {
        App.proc.background(0);
    }

}
