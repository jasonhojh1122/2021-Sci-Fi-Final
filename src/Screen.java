import org.jetbrains.annotations.NotNull;
import processing.core.*;

public class Screen {

    public int width = 192;
    public int height = 108;
    public char[][] screen;
    public int[][] colors;
    public boolean dirty;

    public final PFont font;
    public int fontSize = 16;

    float fontOffsetX = 10;
    float fontOffsetY = 10;

    String asciiChars = " .:-=+*#%$@";
    char[] asciiTable;


    public Screen(String fontName) {
        font = App.proc.createFont(fontName, fontSize);
        App.proc.textFont(font);
        App.proc.textAlign(App.proc.LEFT, App.proc.TOP);
        dirty = true;
        screen = new char[width][height];
        colors = new int[width][height];
        asciiTable = new char[256];
        for (int i = 0; i < 256; i++) {
            asciiTable[i] = asciiChars.charAt((int)((double)i / 256d * (double)asciiChars.length()));
        }
        System.out.println(asciiTable);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                screen[x][y] = asciiTable[0];
                colors[x][y] = App.proc.color(0, 0, 0);
            }
        }
    }

    public void drawImage(PImage image, int width, int height, int startX, int startY) {
        image.loadPixels();
        int escapeX = image.width / width;
        int escapeY = image.height / height;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int color = image.pixels[y * escapeY * image.width + x*escapeX];
                screen[x+startX][y+startY] = asciiTable[(int)App.proc.brightness(color)];
                colors[x+startX][y+startY] = color;
            }
        }
        dirty = true;
    }

    public void drawText(@NotNull String str, int startX, int startY, int color) {
        for (int x = 0; x < str.length(); x++) {
            screen[x+startX][startY] = str.charAt(x);
            colors[x+startX][startY] = color;
        }
        dirty = true;
    }

    public void refresh() {
        for (int x = 0; x < screen.length; x++) {
            for (int y = 0; y < screen[0].length; y++) {
                App.proc.fill(colors[x][y]);
                App.proc.text(screen[x][y], fontOffsetX*x, fontOffsetY*y);
            }
        }
        dirty = false;
    }

    public boolean isDirty() {
        return dirty;
    }
}
