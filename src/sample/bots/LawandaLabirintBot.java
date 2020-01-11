package sample.bots;

import sample.logic.Direction;
import sample.logic.GameState;
import sample.LabirintPlayer;

public class LawandaLabirintBot implements LabirintPlayer {

    private int mynumber;
    private int count = 0;
    private int myX = 0, myY = 0;
    private int targetX = 0, targetY = 0;
    private boolean focused = false;
    private GameState gameState;
    private Direction dir = Direction.NONE;


    @Override
    public void takeYourNumber(int number) {
        mynumber = number;
    }

    @Override
    public Direction step(GameState gameState) {
        this.gameState = gameState;
        findMe();
        if (myX == targetX && myY == targetY) focused = false;

        if (!focused) {
            findBest(myX,myY);
            focused = true;
        }

        if (myX < targetX) dir = Direction.BOTTOM;
        else if (myX > targetX) dir = Direction.UP;
        else if (myY < targetY) dir = Direction.RIGHT;
        else if (myY > targetY) dir = Direction.LEFT;

        if (hitCheck()) {
            //System.out.println("PIZDA");
            switch (dir) {
                case UP:
                    findBest(myX - 1, myY);
                    break;
                case BOTTOM:
                    findBest(myX+1,myY);
                    break;
                case LEFT:
                    findBest(myX,myY-1);
                    break;
                case RIGHT:
                    findBest(myX,myY+1);
                    break;
            }
            focused = true;
            step(gameState);
        }

        return dir;
    }

    private void findMe() {
        int[][] map = gameState.getMap();
        for (int i = 0; i < gameState.getWIDTH(); i++) {
            for (int j = 0; j < gameState.getHEIGHT(); j++) {
                if (map[i][j] == mynumber) {
                    myX = i;
                    myY = j;
                }
            }
        }
    }

    private void findBest(int ignoredX, int ignoredY) {
        int[][] map = gameState.getMap();
        double score = -123456;
        int bestX = 0;
        int bestY = 0;
        for (int i = 0; i < gameState.getWIDTH(); i++) {
            for (int j = 0; j < gameState.getHEIGHT(); j++) {
                if (map[i][j] > 0 && i != ignoredX && j != ignoredY) {
                    double buff = map[i][j] - (Math.abs(i - myX) + Math.abs(j - myY)) * 2;
                    if (buff > score) {
                        score = buff;
                        bestX = i;
                        bestY = j;
                    }
                }
            }
        }
        targetX = bestX;
        targetY = bestY;
    }

    private boolean hitCheck() {
        if (dir == Direction.UP && gameState.getMap()[myX - 1][myY] < 0) return true;
        if (dir == Direction.BOTTOM && gameState.getMap()[myX + 1][myY] < 0) return true;
        if (dir == Direction.RIGHT && gameState.getMap()[myX][myY + 1] < 0) return true;
        return dir == Direction.LEFT && gameState.getMap()[myX][myY - 1] < 0;
    }


    @Override
    public String getTeamName() {
        return "LawandaBot";
    }

}