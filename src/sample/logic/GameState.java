package sample.logic;

import java.util.Random;

public class GameState {
    int[][] map;
    //int timeToNextGift;
    int timeToEnd;
    int team1Score;
    int team2Score;
    private final int WIDTH = 15;
    private final int HEIGHT = 15;

    public GameState(int timeToEnd) {
        this.timeToEnd = timeToEnd;
        this.team1Score = 0;
        this.team2Score = 0;
        map = generateMap();

    }


    private int[][] generateMap() {
        Random rnd = new Random();
        int[][] map = new int[WIDTH][HEIGHT];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH - i; j++) {
                map[i][j] = 0;
                if (rnd.nextInt(4) == 0) {
                    int cellValue = rnd.nextInt(10);
                    map[i][j] = cellValue;
                    map[HEIGHT - i - 1][WIDTH - j - 1] = cellValue;
                }
            }
        }
        int teamNumber = -1 * (1 + new Random().nextInt(2));
        map[0][0] = teamNumber;
        map[HEIGHT - 1][WIDTH - 1] = teamNumber == -1 ? -2 : -1;

        return map;
    }

    public int[][] getMap() {
        int[][] cloneMap = new int[WIDTH][HEIGHT];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                cloneMap[i][j] = map[i][j];
            }

        }
        return cloneMap;
    }

    public int getTimeToEnd() {
        return timeToEnd;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }
}
