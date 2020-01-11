package sample.bots;

import sample.LabirintPlayer;
import sample.logic.Direction;
import sample.logic.Game;
import sample.logic.GameState;

import java.util.*;

public class SemiUltimateBot implements LabirintPlayer {

    private int mynumber;
    private int enemyNumber;
    private int count = 0;
    private Direction resultDirection = Direction.NONE;
    private int[][] map;
    private GameState gameState;
    private Position myPosition;
    private Position maxPosition;
    Map<Integer, List<Direction>> strategies;

    @Override
    public void takeYourNumber(int number) {
        mynumber = number;
        enemyNumber = mynumber == -1 ? -2 : -1;
    }

    @Override
    public Direction step(GameState gameState) {
        map = gameState.getMap();
        this.gameState = gameState;
        myPosition = findNumberPos(mynumber, map);
        strategies = new HashMap<>();
        maxPosition = findMaxAndNearest();
        List<List<Direction>> dirPaths = findPositivePathBetweenPositions(myPosition, maxPosition);
        for (List<Direction> onePath : dirPaths) {
            int[][] clonedMap = cloneMap(map);
            int sumForOnePath = 0;
            for (int i = 0; i < onePath.size(); i++) {
                Direction currentDir = onePath.get(i);
                if (currentDir == Direction.UP) {
                    Position myPosInCloned = findNumberPos(mynumber, clonedMap);
                    sumForOnePath += clonedMap[myPosInCloned.i - 1][myPosInCloned.j];
                    clonedMap[myPosInCloned.i - 1][myPosInCloned.j] = mynumber;
                    clonedMap[myPosInCloned.i][myPosInCloned.j] = 0;
                }
                if (currentDir == Direction.BOTTOM) {
                    Position myPosInCloned = findNumberPos(mynumber, clonedMap);
                    sumForOnePath += clonedMap[myPosInCloned.i + 1][myPosInCloned.j];
                    clonedMap[myPosInCloned.i + 1][myPosInCloned.j] = mynumber;
                    clonedMap[myPosInCloned.i][myPosInCloned.j] = 0;
                }
                if (currentDir == Direction.LEFT) {
                    Position myPosInCloned = findNumberPos(mynumber, clonedMap);
                    sumForOnePath += clonedMap[myPosInCloned.i][myPosInCloned.j - 1];
                    clonedMap[myPosInCloned.i][myPosInCloned.j - 1] = mynumber;
                    clonedMap[myPosInCloned.i][myPosInCloned.j] = 0;
                }
                if (currentDir == Direction.RIGHT) {
                    Position myPosInCloned = findNumberPos(mynumber, clonedMap);
                    sumForOnePath += clonedMap[myPosInCloned.i][myPosInCloned.j + 1];
                    clonedMap[myPosInCloned.i][myPosInCloned.j + 1] = mynumber;
                    clonedMap[myPosInCloned.i][myPosInCloned.j] = 0;
                }
            }
            strategies.put(sumForOnePath, onePath);
        }
        int maxValue = 0;
        for (Map.Entry<Integer, List<Direction>> entry : strategies.entrySet()) {
            if (entry.getKey() > maxValue) {
                maxValue = entry.getKey();
            }
        }
        resultDirection = strategies.get(maxValue).get(0);
        if (!enemyHitCheck(resultDirection)) {
            return resultDirection;
        } else {
            for (int i = 0; i < strategies.get(maxValue).size(); i++) {
                if (strategies.get(maxValue).get(i) != resultDirection) {
                    return strategies.get(maxValue).get(i);
                }
            }
        }

        return Direction.NONE;
    }

    private boolean enemyHitCheck(Direction dir) {

        if (dir == Direction.UP && map[myPosition.i - 1][myPosition.j] == enemyNumber) {
            return true;
        }
        if (dir == Direction.BOTTOM && map[myPosition.i + 1][myPosition.j] == enemyNumber) {
            return true;
        }
        if (dir == Direction.RIGHT && map[myPosition.i][myPosition.j + 1] == enemyNumber) {
            return true;
        }
        if (dir == Direction.LEFT && map[myPosition.i][myPosition.j - 1] == enemyNumber) {
            return true;
        }
        return false;
    }


    @Override
    public String getTeamName() {
        return "UltimateBot";
    }

    private List<List<Direction>> findPositivePathBetweenPositions(Position from, Position to) {
        List<List<Direction>> dirStrategies = new ArrayList<>();
        int verticalAbs = Math.abs(from.i - to.i);
        int horizontalAbs = Math.abs(from.j - to.j);
        Direction horizontal = Direction.NONE;
        Direction vertical = Direction.NONE;
        if (to.i > from.i) {
            vertical = Direction.BOTTOM;
        }
        if (to.i < from.i) {
            vertical = Direction.UP;
        }
        if (to.j > from.j) {
            horizontal = Direction.RIGHT;
        }
        if (to.j < from.j) {
            horizontal = Direction.LEFT;
        }
        List<Direction> simplePath = new ArrayList<>();
        if (vertical != Direction.NONE) {
            for (int i = 0; i < verticalAbs; i++) {
                simplePath.add(vertical);
            }
        }
        if (horizontal != Direction.NONE) {
            for (int i = 0; i < horizontalAbs; i++) {
                simplePath.add(horizontal);
            }
        }
        dirStrategies.add(simplePath);

        /* non complete generation */
        if (simplePath.size() > 1) {
            List<Direction> oneMoreSwaped = new ArrayList<>(simplePath);
            for (int i = 0; i < simplePath.size(); i++) {
                for (int j = i; j < simplePath.size(); j++) {
                    oneMoreSwaped = new ArrayList<>(oneMoreSwaped);
                    int randomIndex = new Random().nextInt(oneMoreSwaped.size() - 1);
                    Direction removedDir = oneMoreSwaped.get(j);
                    oneMoreSwaped.remove(j);
                    oneMoreSwaped.add(removedDir);
                    if (!dirStrategies.contains(oneMoreSwaped)) {
                        dirStrategies.add(oneMoreSwaped);

                    }
                }
            }
        }
        return dirStrategies;
    }

    private Position findMaxAndNearest() {
        Position maxPos = null;
        int max = -1;
        List<Position> maxs = new ArrayList<>();
        for (int i = 0; i < gameState.getHEIGHT(); i++) {
            for (int j = 0; j < gameState.getWIDTH(); j++) {
                if (map[i][j] >= max) {
                    maxs.add(new Position(i, j));
                    max = map[i][j];
                }
            }

        }
        for (int i = 0; i < maxs.size(); i++) {
            Position pos = maxs.get(i);
            if (map[pos.i][pos.j] != max) {
                maxs.remove(i);
                i--;
            }
        }
        int minPathLength = gameState.getHEIGHT() * gameState.getWIDTH();
        int minPathIndex = gameState.getHEIGHT() * gameState.getWIDTH();
        for (int i = 0; i < maxs.size(); i++) {
            int horizontalLength;
            horizontalLength = Math.abs(maxs.get(i).j - myPosition.j);
            int verticalLength;
            verticalLength = Math.abs(maxs.get(i).i - myPosition.i);
            int totalLength = verticalLength + horizontalLength;
            if (totalLength < minPathLength) {
                minPathLength = totalLength;
                minPathIndex = i;
            }
        }
        return maxs.get(minPathIndex);
    }

    private boolean isPositionValid(Position position) {
        if (position.i < gameState.getHEIGHT() && position.j < gameState.getWIDTH()) {
            return true;
        }
        return false;
    }

    private int[][] cloneMap(int[][] map) {
        int[][] cloneMap = new int[gameState.getHEIGHT()][gameState.getWIDTH()];
        for (int i = 0; i < gameState.getWIDTH(); i++) {
            for (int j = 0; j < gameState.getWIDTH(); j++) {
                cloneMap[i][j] = map[i][j];
            }

        }
        return cloneMap;
    }

    private Position findNumberPos(int numberToFind, int[][] map) {
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

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        LabirintPlayer ultimateBot = new SemiUltimateBot();
        LabirintPlayer volk = new LawandaLabirintBot();

        int myWinCount = 0;
        int enemyWinCount = 0;
        for (int i = 0; i < 1000; i++) {
            System.out.println("game " + i);
            try {
                Game game = new Game(ultimateBot, volk, 100);
                while (game.getGameState().getTimeToEnd() > 0) {
                    game.tick();
                }
                if (game.getSum1() > game.getSum2()) {
                    myWinCount++;
                } else {
                    enemyWinCount++;
                }
            } catch (Throwable t) {
                //myWinCount++;
            }
        }

        System.out.println("me "+ myWinCount);
        System.out.println("enemy " + enemyWinCount);
    }
}
