package sample.logic;

import sample.LabirintPlayer;

import java.lang.reflect.Field;

import static sample.logic.Direction.*;


public class Game {

    private GameState gameState;
    private LabirintPlayer team1;
    private LabirintPlayer team2;
    private int sum1;
    private int sum2;
    private boolean ended = false;

    public Game(LabirintPlayer team1, LabirintPlayer team2, int timeToEnd) {
        this.gameState = new GameState(timeToEnd);
        this.team1 = team1;
        this.team2 = team2;
    }

    public void tick() throws NoSuchFieldException, IllegalAccessException {
        //раздаем номера ботам
        giveTeamNumbers();

        int[][] actualMap;
        Field mapField = gameState.getClass().getDeclaredField("map");
        actualMap = (int[][]) mapField.get(gameState);
        Position position1Team = findNumberPos(actualMap, -1);
        Position position2Team = findNumberPos(actualMap, -2);
        Direction dir1 = team1.step(gameState);
        switch (dir1) {
            case LEFT:
                if (position1Team.j == 0) {
                    throw new RuntimeException("Выход за " + LEFT.name() + "границу массива командой " + team1.getTeamName());
                }
                if (position1Team.j - 1 == position2Team.j && position1Team.i == position2Team.i) {
                    throw new RuntimeException("Попытка наезда от " + team1.getTeamName());
                }
                sum1 += actualMap[position1Team.i][position1Team.j - 1];
                actualMap[position1Team.i][position1Team.j] = 0;
                actualMap[position1Team.i][position1Team.j - 1] = -1;
                break;
            case UP:
                if (position1Team.i == 0) {
                    throw new RuntimeException("Выход за " + UP.name() + "границу массива командой " + team1.getTeamName());
                }
                if (position1Team.i - 1 == position2Team.i && position1Team.j == position2Team.j) {
                    throw new RuntimeException("Попытка наезда от " + team1.getTeamName());
                }
                sum1 += actualMap[position1Team.i - 1][position1Team.j];
                actualMap[position1Team.i][position1Team.j] = 0;
                actualMap[position1Team.i - 1][position1Team.j] = -1;
                break;
            case BOTTOM:
                if (position1Team.i == gameState.getHEIGHT() - 1) {
                    throw new RuntimeException("Выход за " + BOTTOM.name() + "границу массива командой " + team1.getTeamName());
                }
                if (position1Team.i + 1 == position2Team.i && position1Team.j == position2Team.j) {
                    throw new RuntimeException("Попытка наезда от " + team1.getTeamName());
                }
                sum1 += actualMap[position1Team.i + 1][position1Team.j];
                actualMap[position1Team.i][position1Team.j] = 0;
                actualMap[position1Team.i + 1][position1Team.j] = -1;
                break;
            case RIGHT:
                if (position1Team.j == gameState.getWIDTH() - 1) {
                    throw new RuntimeException("Выход за " + RIGHT.name() + "границу массива командой " + team1.getTeamName());
                }
                if (position1Team.j + 1 == position2Team.j && position1Team.i == position2Team.i) {
                    throw new RuntimeException("Попытка наезда от " + team1.getTeamName());
                }
                sum1 += actualMap[position1Team.i][position1Team.j + 1];
                actualMap[position1Team.i][position1Team.j] = 0;
                actualMap[position1Team.i][position1Team.j + 1] = -1;
                break;
            case NONE:
                break;

        }
        position1Team = findNumberPos(actualMap, -1);
        position2Team = findNumberPos(actualMap, -2);
        Direction dir2 = team2.step(gameState);
        switch (dir2) {
            case LEFT:
                if (position2Team.j == 0) {
                    throw new RuntimeException("Выход за " + LEFT.name() + "границу массива командой " + team2.getTeamName());
                }
                if (position2Team.j - 1 == position1Team.j && position1Team.i == position2Team.i) {
                    throw new RuntimeException("Попытка наезда от " + team2.getTeamName());
                }
                sum2 += actualMap[position2Team.i][position2Team.j - 1];
                actualMap[position2Team.i][position2Team.j] = 0;
                actualMap[position2Team.i][position2Team.j - 1] = -2;
                break;
            case UP:
                if (position2Team.i == 0) {
                    throw new RuntimeException("Выход за " + UP.name() + "границу массива командой " + team2.getTeamName());
                }
                if (position2Team.i - 1 == position1Team.i && position1Team.j == position2Team.j) {
                    throw new RuntimeException("Попытка наезда от " + team2.getTeamName());
                }
                sum2 += actualMap[position2Team.i - 1][position2Team.j];
                actualMap[position2Team.i][position2Team.j] = 0;
                actualMap[position2Team.i - 1][position2Team.j] = -2;
                break;
            case BOTTOM:
                if (position2Team.i == gameState.getHEIGHT() - 1) {
                    throw new RuntimeException("Выход за " + BOTTOM.name() + "границу массива командой " + team2.getTeamName());
                }
                if (position2Team.i + 1 == position1Team.i && position1Team.j == position2Team.j) {
                    throw new RuntimeException("Попытка наезда от " + team2.getTeamName());
                }
                sum2 += actualMap[position2Team.i + 1][position2Team.j];
                actualMap[position2Team.i][position2Team.j] = 0;
                actualMap[position2Team.i + 1][position2Team.j] = -2;
                break;
            case RIGHT:
                if (position2Team.j == gameState.getWIDTH() - 1) {
                    throw new RuntimeException("Выход за " + RIGHT.name() + "границу массива командой " + team2.getTeamName());
                }
                if (position2Team.j + 1 == position1Team.j && position1Team.i == position2Team.i) {
                    throw new RuntimeException("Попытка наезда от " + team2.getTeamName());
                }
                sum2 += actualMap[position2Team.i][position2Team.j + 1];
                actualMap[position2Team.i][position2Team.j] = 0;
                actualMap[position2Team.i][position2Team.j + 1] = -2;
                break;
            case NONE:
                break;

        }
        for (int i = 0; i < gameState.getHEIGHT(); i++) {
            for (int j = 0; j < gameState.getWIDTH(); j++) {
                System.out.print(actualMap[i][j] + " ");
            }
            System.out.println();

        }
        Field fieldWithScore1 = gameState.getClass().getDeclaredField("team1Score");
        fieldWithScore1.set(gameState, sum1);
        Field fieldWithScore2 = gameState.getClass().getDeclaredField("team2Score");
        fieldWithScore2.set(gameState, sum2);

        System.out.println();
        System.out.println();
        timerTick();
        if(gameState.getTimeToEnd() == 0) {
            ended = true;
        }


    }

    private Position findNumberPos(int[][] map, int numberToFind) {
        for (int i = 0; i < gameState.getHEIGHT(); i++) {
            for (int j = 0; j < gameState.getWIDTH(); j++) {
                if (map[i][j] == numberToFind) {
                    return new Position(i, j);
                }
            }

        }
        throw new IllegalStateException("Couldn't find number at the map");
    }

    private class Position {
        int i;
        int j;

        public Position(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    private void timerTick() throws NoSuchFieldException, IllegalAccessException {
        Field field = gameState.getClass().getDeclaredField("timeToEnd");
        field.set(gameState, (int) field.get(gameState) - 1);
        System.out.println(gameState.timeToEnd);
    }

    private void giveTeamNumbers() {
        team1.takeYourNumber(-1);
        team2.takeYourNumber(-2);
    }

    public GameState getGameState() {
        return gameState;
    }

    public LabirintPlayer getTeam1() {
        return team1;
    }

    public LabirintPlayer getTeam2() {
        return team2;
    }

    public int getSum1() {
        return sum1;
    }

    public int getSum2() {
        return sum2;
    }

    public boolean isEnded() {
        return ended;
    }
}
