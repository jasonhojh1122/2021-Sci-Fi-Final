import java.util.ArrayList;
import java.util.List;

public class ColorPalette {
    public int white;
    public int red;
    public int lightGreen;
    public int mediumGreen;
    public int darkGreen;
    public int blue;

    List<Integer> colors;

    public ColorPalette() {
        colors = new ArrayList<>();
        white = App.proc.color(255);
        colors.add(white);
        red = App.proc.color(140, 3, 3);
        colors.add(red);
        lightGreen = App.proc.color(48, 191, 57);
        colors.add(lightGreen);
        mediumGreen = App.proc.color(22, 115, 28);
        colors.add(mediumGreen);
        darkGreen = App.proc.color(2, 38, 1);
        colors.add(darkGreen);
        blue = App.proc.color(11, 79, 217);
        colors.add(blue);
    }

    public int randomColor() {
        return colors.get((int)App.r(0, colors.size()));
    }

}
