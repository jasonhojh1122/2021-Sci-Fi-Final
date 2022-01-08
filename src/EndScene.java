import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EndScene implements Scene{

    List<String> msg;
    boolean ended;

    public EndScene() throws FileNotFoundException {
        msg = new ArrayList<>();
        Scanner scanner = new Scanner(new File("data/error.txt"));
        while (scanner.hasNextLine()) {
            msg.add(scanner.nextLine());
        }
        scanner.close();
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
