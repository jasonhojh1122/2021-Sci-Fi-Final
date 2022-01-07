
public class ColorPalette {
    public int white;
    public int red;
    public int lightGreen;
    public int mediumGreen;
    public int darkGreen;
    public int blue;

    public ColorPalette() {
        white = App.proc.color(255);
        red = App.proc.color(140, 3, 3);
        lightGreen = App.proc.color(48, 191, 57);
        mediumGreen = App.proc.color(22, 115, 28);
        darkGreen = App.proc.color(2, 38, 1);
        blue = App.proc.color(11, 79, 217);
    }
}
