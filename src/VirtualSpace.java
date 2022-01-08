import processing.video.*;

import java.util.concurrent.TimeUnit;

public class VirtualSpace implements Scene {

    Movie movie;
    float FPS = 15;
    int frame;
    boolean ended;
    boolean movieUpdated;

    public VirtualSpace() {
        movie = new Movie(App.proc, "VirtualSpace.mov");
    }

    @Override
    public void init() {
        App.proc.frameRate(FPS);
        ended = false;
        movie.play();
        System.out.println(movie.width + "X" + movie.height);
        frame = 0;
        movieUpdated = false;
        Nerve.init();
    }

    @Override
    public void update() {
        updateMovie();
        if (movieUpdated) {
            frame += 1;
            Htop.update(movie, frame);
            Nerve.update(movie, frame);
            updateWireframe();
            movieUpdated = false;
        }
    }

    @Override
    public void keyPressed() {
    }

    @Override
    public boolean isEnd() {
        return ended;
    }

    private void updateMovie() {
        if (movie.available()) {
            App.screen.clear();
            movie.read();
            App.screen.drawImage(movie, movie.width, movie.height, 0, 0);
            movieUpdated = true;
        }
        if (movie.time() >= movie.duration() - 0.1f) {
            ended = true;
            App.proc.frameRate(30);
        }
    }

    private void updateWireframe() {
        for (int y = 0; y < App.screen.height; y++) {
            App.screen.setChar('|', 144, y, App.colorPalette.mediumGreen);
        }
        for (int x = 145; x < App.screen.width; x++) {
            App.screen.setChar('-', x, Htop.height, App.colorPalette.mediumGreen);
        }
    }

    static class Htop {
        public static int height = 17;
        static int CPULength = 35;
        static int startTime = 5472000;
        static float[] storage = new float[10]; // cpu 1 2 3 4, mem, swap, tasks, thread, uptime

        public static void update(Movie movie, int frame) {
            updateCPU(movie, frame, 1, 1);
            updateCPU(movie, frame, 2, 3);
            updateCPU(movie, frame, 3, 5);
            updateCPU(movie, frame, 4, 7);
            updateMEM(movie, frame, 5, 9);
            updateSWP(movie, frame, 6, 11);
            updateTasks(movie, frame, 7, 13);
            updateUpTime(movie, frame, 8, 15);
        }

        public static void updateCPU(Movie movie, int frame, int id, int y) {
            App.screen.setChar((char)(id+48), 146, y, App.colorPalette.lightGreen);
            App.screen.setChar('[', 150, y, App.colorPalette.lightGreen);
            App.screen.setChar(']', 184, y, App.colorPalette.lightGreen);
            float low = movie.time() / movie.duration() * App.r(0.6f, 0.7f);
            float high = low + App.r(0.25f, 0.3f);
            updatePercentage(frame, id, y, low, high);
        }

        public static void updateMEM(Movie movie, int frame, int id, int y) {
            App.screen.drawString("MEM", 146, y, App.colorPalette.lightGreen);
            App.screen.setChar('[', 150, y, App.colorPalette.lightGreen);
            App.screen.setChar(']', 184, y, App.colorPalette.lightGreen);
            float low = App.r(0.6f, 0.7f);
            float high = low + movie.time() / movie.duration() * App.r(0.25f, 0.3f);
            updatePercentage(frame, id, y, low, high);
        }

        public static void updateSWP(Movie movie, int frame, int id, int y) {
            App.screen.drawString("SWP", 146, y, App.colorPalette.lightGreen);
            App.screen.setChar('[', 150, y, App.colorPalette.lightGreen);
            App.screen.setChar(']', 184, y, App.colorPalette.lightGreen);
            float low = App.r(0.5f, 0.7f);
            float high = low + movie.time() / movie.duration() * App.r(0.25f, 0.3f);
            updatePercentage(frame, id, y, low, high);
        }

        public static void updateTasks(Movie movie, int frame, int id, int y) {
            App.screen.drawString("Tasks: ", 146, y, App.colorPalette.lightGreen);
            if (frame % 5 == 0)
                storage[id] = movie.time() / movie.duration() * App.r(300, 500);
            App.screen.drawString(String.format("%1$3s", (int)storage[id]), 153, y, App.colorPalette.blue);
            App.screen.drawString("Threads: ", 158, y, App.colorPalette.lightGreen);
            if (frame % 5 == 0)
                storage[id+1] = movie.time() / movie.duration() * App.r(700, 9999);
            App.screen.drawString(String.format("%1$3s", (int)storage[id+1]), 167, y, App.colorPalette.blue);
        }

        public static void updateUpTime(Movie movie, int frame, int id, int y) {
            int millis = (int)(movie.time()*1000) + startTime;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
            App.screen.drawString("Up Time: " + hms, 146, y, App.colorPalette.lightGreen);
        }

        static void updatePercentage(int frame, int id, int y, float low, float high) {
            if (frame % 5 == 0)
                storage[id] = App.r(low, high);
            int c = (int)(CPULength * storage[id]);
            for (int x = 151; x < 151+c; x++) {
                if (x < 151 + CPULength/2)
                    App.screen.setChar('#', x, y, App.colorPalette.mediumGreen);
                else if (x < 151 + CPULength * 3 /4)
                    App.screen.setChar('#', x, y, App.colorPalette.blue);
                else
                    App.screen.setChar('#', x, y, App.colorPalette.red);
            }
            App.screen.drawString(String.format("%1$3s", (int)(storage[id]*100f))+"%", 187, y, App.colorPalette.lightGreen);
        }
    }

    static class Nerve {
        static int startX = 145;
        static int startY = 19;
        static int width = 47;
        static int height = 87;
        static char[][] nerves = new char[width][height];

        static float branchProb;

        static int[] lastPos = new int[8];
        static int latestRow;

        public static void init() {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    nerves[x][y] = ' ';
                }
            }
            latestRow = 0;
            branchProb = 0.15f;
            for (int i = 0; i < 8; i++) {
                nerves[2+i*6][latestRow] = '|';
                lastPos[i] = 2+i*6;
            }
        }

        public static void update(Movie movie, int frame) {
            branchProb = movie.time() / movie.duration() * App.r(0.2f, 0.4f);
            int newRow = (latestRow + 1) % height;
            for (int x = 0; x < width; x++) {
                nerves[x][newRow] = ' ';
            }
            for (int i = 0; i < 8; i++) {
                generate(latestRow, i);
            }
            latestRow = newRow;
            draw();
        }

        static void generate(int curRow, int nerveID) {
            float p = App.r01();
            char lastC = nerves[lastPos[nerveID]][curRow];
            int writeRow = (curRow + 1) % height;
            if (p < branchProb) {
                updateNewRight(lastC, nerveID, writeRow);
            }
            else if (p < branchProb * 2) {
                updateNewLeft(lastC, nerveID, writeRow);
            }
            else {
                updateNewStraight(nerveID, writeRow);
            }
        }

        static void updateNewRight(char lastC, int nerveID, int writeRow) {
            switch (lastC) {
                case '\\':
                    if (lastPos[nerveID] == 0) {
                        if (nerves[lastPos[nerveID]][writeRow] != ' ')
                            nerves[lastPos[nerveID]][writeRow] = '*';
                        else
                            nerves[lastPos[nerveID]][writeRow] = '|';
                    }
                    else {
                        lastPos[nerveID] -= 1;
                        if (nerves[lastPos[nerveID]][writeRow] != ' ')
                            nerves[lastPos[nerveID]][writeRow] = '*';
                        else
                            nerves[lastPos[nerveID]][writeRow] = '\\';
                    }
                    break;
                case '|':
                    if (nerves[lastPos[nerveID]][writeRow] != ' ')
                        nerves[lastPos[nerveID]][writeRow] = '*';
                    else {
                        if (lastPos[nerveID] == 0)
                            nerves[lastPos[nerveID]][writeRow] = '|';
                        else {
                            lastPos[nerveID] -= 1;
                            nerves[lastPos[nerveID]][writeRow] = '\\';
                        }
                    }
                    break;
                case '/':
                    if (nerves[lastPos[nerveID]][writeRow] != ' ')
                        nerves[lastPos[nerveID]][writeRow] = '*';
                    else
                        nerves[lastPos[nerveID]][writeRow] = '|';
                    break;
            }
        }

        static void updateNewLeft(char lastC, int nerveID, int writeRow) {
            switch (lastC) {
                case '/':
                    if (lastPos[nerveID] == width - 1) {
                        if (nerves[lastPos[nerveID]][writeRow] != ' ')
                            nerves[lastPos[nerveID]][writeRow] = '*';
                        else
                            nerves[lastPos[nerveID]][writeRow] = '|';
                    } else {
                        lastPos[nerveID] += 1;
                        if (nerves[lastPos[nerveID]][writeRow] != ' ')
                            nerves[lastPos[nerveID]][writeRow] = '*';
                        else
                            nerves[lastPos[nerveID]][writeRow] = '/';
                    }
                    break;
                case '|':
                    if (nerves[lastPos[nerveID]][writeRow] != ' ')
                        nerves[lastPos[nerveID]][writeRow] = '*';
                    else {
                        if (lastPos[nerveID] == width - 1)
                            nerves[lastPos[nerveID]][writeRow] = '|';
                        else {
                            lastPos[nerveID] += 1;
                            nerves[lastPos[nerveID]][writeRow] = '/';
                        }
                    }
                    break;
                case '\\':
                    if (nerves[lastPos[nerveID]][writeRow] != ' ')
                        nerves[lastPos[nerveID]][writeRow] = '*';
                    else
                        nerves[lastPos[nerveID]][writeRow] = '|';
                    break;
            }
        }

        static void updateNewStraight(int nerveID, int writeRow) {
            if (nerves[lastPos[nerveID]][writeRow] != ' ')
                nerves[lastPos[nerveID]][writeRow] = '*';
            else
                nerves[lastPos[nerveID]][writeRow] = '|';
        }

        static void draw() {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int indexY = (latestRow - y + height) % height;
                    char c = nerves[x][indexY];
                    if (c == '*')
                        App.screen.setChar(c, startX+x, startY+y, App.colorPalette.red);
                    else if (c == '|')
                        App.screen.setChar(c, startX+x, startY+y, App.colorPalette.lightGreen);
                    else
                        App.screen.setChar(c, startX+x, startY+y, App.colorPalette.blue);
                }
            }
        }
    }
}
