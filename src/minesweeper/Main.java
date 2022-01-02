package minesweeper;
import java.util.*;

public class Main {

    public static int fieldSize;
    public static String[][] minefield;
    public static int noOfMines;
    public static List<Integer> mineArrayLocation;
    public static boolean firstMove;

    public static void main(String[] args) {
        fieldSize = 9;
        minefield = new String[fieldSize][fieldSize];
        noOfMines = 0;
        mineArrayLocation = new ArrayList<>(81);
        firstMove = true;

        printSafeField();
        setNoOfMines();
        printField(minefield);
        cycle();
    }

    public static void setNoOfMines(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many mines do you want on the field? ");
        noOfMines = scanner.nextInt();
    }

    public static void printSafeField( ) {
        for (int i = 0; i < fieldSize; i++){
            for (int j = 0; j < 9; j++){
                minefield[i][j] = ".";
            }
        }
    }

    public static void placeMines(int newMine){
        for (int i = 0; i < 81; i++)
            if(!(i % 10 == 9)) {
                mineArrayLocation.add(i);
            }
        Collections.shuffle(mineArrayLocation);

        for (int i = 0; i < noOfMines + newMine; i++) {

            minefield[mineArrayLocation.get(i)/10][mineArrayLocation.get(i)%10] =  "X";
        }

    }

    private static void cycle() {

        Scanner scanner = new Scanner(System.in);
        while (noOfMines > 0) {

            System.out.println("Set/delete mine marks (x and y coordinates):");
            int coordsY = scanner.nextInt() -1;
            int coordsX = scanner.nextInt() -1;
            String setter = scanner.next();

            switch (setter) {
                case "free":
                    checkMines(coordsX, coordsY);
                    break;
                case "mine":
                    flagMines(coordsX, coordsY);
                    break;
            }
            firstMove = false;
            printField(minefield);
        }
        if (noOfMines == 0) {
            System.out.println("Congratulations! You found all mines!");
        } else {
            System.out.println("You stepped on a mine and failed!");
        }
    }

    private static void checkMines(int x, int y) {

        for (int mineSearch : mineArrayLocation) {
            if (x == mineArrayLocation.get(mineSearch)/10 &&
                y == mineArrayLocation.get(mineSearch)%10) {
                if (firstMove) {
                    mineArrayLocation.remove(mineSearch);
                    placeMines(1);
                    // i need to check if the initial mine was removed here
                } else {
                    boom();
                }
            } else {
                floodFill(minefield,new boolean[minefield.length][minefield[0].length],x,y);
            }
        }
    }


    public static void boom() {
        // Displaying mines to the user after they stepped on the mine
        for (int triggeredMine : mineArrayLocation) {
            minefield[mineArrayLocation.get(triggeredMine)/10][mineArrayLocation.get(triggeredMine)/10] = "X";
        }
        noOfMines = -1;
    }

    public static void flagMines(int x, int y){
        int mineSearch = x * 10 + y;
        if (minefield[x][y].equals("*")) {
            minefield[x][y] = ".";
            noOfMines--;
        } else if (minefield[x][y].equals(".")) {
            minefield[x][y] = "*";
            if (isInList(mineSearch)) {
                noOfMines--;
            } else {
                noOfMines++;
            }
        } else{
            System.out.println("There is a number here!");
        }
    }

    public static boolean isInList(int arr){
        return mineArrayLocation.stream().anyMatch(a -> a == arr);
    }

    private static void printField(String[][] minefield) {
        System.out.println("  123456789");
        System.out.println("-|---------|");
        for (int i = 0; i < 9; i++) {
            System.out.print(i+1 + "|"
                    + minefield[i][0]
                    + minefield[i][1]
                    + minefield[i][2]
                    + minefield[i][3]
                    + minefield[i][4]
                    + minefield[i][5]
                    + minefield[i][6]
                    + minefield[i][7]
                    + minefield[i][8] + "|" + "\n");
        }
        System.out.println("-|---------|");
    }


    public static void floodFill(String[][] minefield,boolean[][] visited, int coordsX, int coordsY) {
        //quit if off the grid:
        if(coordsX < 0 || coordsX >= minefield.length || coordsY < 0 || coordsY >= minefield[0].length) return;

        //quit if visited:
        if(visited[coordsX][coordsY]) return;
        visited[coordsX][coordsY] = true;

        //quit if hit wall:
        if(minefield[coordsX][coordsY].equals("1")
                || minefield[coordsX][coordsY].equals("2")
                || minefield[coordsX][coordsY].equals("3")
                || minefield[coordsX][coordsY].equals("4")
                || minefield[coordsX][coordsY].equals("5")
                || minefield[coordsX][coordsY].equals("6")
                || minefield[coordsX][coordsY].equals("7")
                || minefield[coordsX][coordsY].equals("8")) return;

        //we want to visit places with periods in them:
        if(minefield[coordsX][coordsY].equals(".") || ("*".equals(minefield[coordsX][coordsY])))
            minefield[coordsX][coordsY] = "/";

        //recursively fill in all directions
        floodFill(minefield,visited,coordsX+1,coordsY);
        floodFill(minefield,visited,coordsX-1,coordsY);
        floodFill(minefield,visited,coordsX,coordsY+1);
        floodFill(minefield,visited,coordsX,coordsY-1);
    }
}