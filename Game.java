import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

class Point {
    int x;
    int y;

    public Point(int x_, int y_) {
        x = x_;
        y = y_;
    }
}

class Ship {
    int rank = -1;
    char orient = 'n';
    ArrayList<Point> coords = new ArrayList<Point>();

    public Ship(int rank_, char oriented_, Point p) {
        rank = rank_;
        orient = oriented_;
        build(p);
    }

    private void build(Point p) {
        int h = orient == 'h' ? 1 : 0;

        for (int i = 0; i < rank; i++) {
            Point c = new Point(p.x + i * (1 - h), p.y + i * h);
            coords.add(c);
        }
    }

    static int getRank(int num) {
        int rank = 0;
        if (num < 4) {
            rank = 1;
        } else if (num < 7) {
            rank = 2;
        } else if (num < 9) {
            rank = 3;
        } else if (num < 10) {
            rank = 4;
        }
        return rank;
    }

    public boolean hasPoint(Point p) {
        for (Point c : coords) {
            if (c.x == p.x && c.y == p.y)
                return true;
        }

        return false;
    }
}

class Board {
    ArrayList<Ship> ships = new ArrayList<Ship>();
    static int fieldSize = 10;
    char[][] myField = new char[fieldSize][fieldSize];
    char[][] enemyField = new char[fieldSize][fieldSize];
    int enemyDeaths = 0;

    public void defaultGenerate() {
        for (int i = 0; i < fieldSize; i++)
            for (int j = 0; j < fieldSize; j++) {
                myField[i][j] = '\u25A0';
                enemyField[i][j] = '\u25A0';
            }
    }

    public void print() {
        System.out.print("\n\tOur ships\t\t\t\t\t\tEnemy ships\n");
        for (int j = 0; j < 2; j++) {
            System.out.print("   ");
            for (int i = 0; i < fieldSize; i++)
                System.out.print(i + "  ");
            System.out.print("\t\t\t");
        }
        System.out.print("\n");

        for (int i = 0; i < fieldSize; i++) {
            System.out.print(i + "  ");
            for (int j = 0; j < fieldSize; j++) {
                System.out.print(myField[i][j] + "  ");
            }
            System.out.print("\t\t\t");
            System.out.print(i + "  ");
            for (int j = 0; j < fieldSize; j++) {
                System.out.print(enemyField[i][j] + "  ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    private void printShip(char[][] field, ArrayList<Point> coords, char s) {
        for (Point p : coords) {
            field[p.x][p.y] = s;
        }
    }

    public Ship checkShip(int num, Point p, char orient) {
        if (orient != 'h' && orient != 'v') {
            System.out.print("Unknown direction. Available: H (or h) - horizontal, V (or v) - vertical. Please try again.\n");
            return null;
        }
        if ((p.x >= fieldSize) || (p.x < 0) || (p.y >= fieldSize) || (p.y < 0) ) {
            System.out.print("Incorrect coordinates. Please try again.\n");
            return null;
        }

        int checkCorrectShip = 0;
        int rank = 0;
        if (orient == 'h') {
            try {
                rank = Ship.getRank(num);
                if (p.y + rank > fieldSize)
                    throw new ArithmeticException("Incorrect coordinates or direction. Please try again.\n");

                checkCorrectShip = checkHorizontal(p, rank);
            } catch (ArithmeticException e) {
                System.out.print("Incorrect coordinates or direction. Please try again.\n");
                return null;
            }
        } else if (orient == 'v') {
            try {
                rank = Ship.getRank(num);
                if (p.x + rank > fieldSize)
                    throw new ArithmeticException("Incorrect coordinates or direction. Please try again.\n");

                checkCorrectShip = checkVertical(p, rank);
            } catch (ArithmeticException e) {
                System.out.print("Incorrect coordinates or direction. Please try again.\n");
                return null;
            }
        }

        if (checkCorrectShip == 0)
            return new Ship(rank, orient, p);
        else {
            if (checkCorrectShip == 2)
                System.out.print("The ship intersects with other ships or does not have a gap to them of at least 1 cell. Please try again\n");
            else
                System.out.print("Out of range! Please try again\n");
        }

        return null;
    }

    private int checkHorizontal(Point p, int len) {
        int next = p.y;
        for (int l = 0; l < len; l++) {
            if (myField[p.x][next] != '\u25A0')
                return 2;
            if ((next - 1 >= 0) && (myField[p.x][next - 1] != '\u25A0'))
                return 2;
            if ((p.x - 1 >= 0) && (myField[p.x - 1][next] != '\u25A0'))
                return 2;
            if ((next - 1 >= 0) && (p.x - 1 >= 0) && (myField[p.x - 1][next - 1] != '\u25A0'))
                return 2;
            if ((next + 1 < fieldSize) && (myField[p.x][next + 1] != '\u25A0'))
                return 2;
            if ((p.x + 1 < fieldSize) && (myField[p.x + 1][next] != '\u25A0'))
                return 2;
            if ((next + 1 < fieldSize) && (p.x + 1 < fieldSize) && (myField[p.x + 1][next + 1] != '\u25A0'))
                return 2;
            if ((next + 1 < fieldSize) && (p.x - 1 >= 0) && (myField[p.x - 1][next + 1] != '\u25A0'))
                return 2;
            if ((next - 1 >= 0) && (p.x + 1 < fieldSize) && (myField[p.x + 1][next - 1] != '\u25A0'))
                return 2;

            next += 1;
        }

        return 0;
    }

    private int checkVertical(Point p, int len) {
        int next = p.x;

        for (int l = 0; l < len; l++) {
            if (myField[next][p.y] != '\u25A0')
                return 2;
            if ((p.y - 1 >= 0) && (myField[next][p.y - 1] != '\u25A0'))
                return 2;
            if ((next - 1 >= 0) && (myField[next - 1][p.y] != '\u25A0'))
                return 2;
            if ((p.y - 1 >= 0) && (next - 1 >= 0) && (myField[next - 1][p.y - 1] != '\u25A0'))
                return 2;
            if ((p.y + 1 < fieldSize) && (myField[next][p.y + 1] != '\u25A0'))
                return 2;
            if ((next + 1 < fieldSize) && (myField[next + 1][p.y] != '\u25A0'))
                return 2;
            if ((p.y + 1 < fieldSize) && (next + 1 < fieldSize) && (myField[next + 1][p.y + 1] != '\u25A0'))
                return 2;
            if ((next + 1 < fieldSize) && (p.y - 1 >= 0) && (myField[next + 1][p.y - 1] != '\u25A0'))
                return 2;
            if ((next - 1 >= 0) && (p.y + 1 < fieldSize) && (myField[next - 1][p.y + 1] != '\u25A0'))
                return 2;

            next += 1;
        }

        return 0;
    }

    public void addShip(Ship ship) {
        ships.add(ship);

        ArrayList<Point> coords = ship.coords;
        printShip(myField, coords, '1');
    }

    public void doDamage(int response, Point p) {
        if (response == 0) {
            enemyField[p.x][p.y] = '~';
            print();
            System.out.print("You missed!\n\n");
        } else if (response == 1) {
            enemyField[p.x][p.y] = 'X';
            print();
            System.out.print("You has damaged the enemy!\n\n");
        } else if (response == 2) {
            enemyField[p.x][p.y] = 'X';
            enemyDeaths++;
            print();
            System.out.print("You has destroyed the enemy ship!\n\n");
        } else if (response == 3) {
            print();
            System.out.print("You've already fired at these coordinates!\n\n");
        }
    }

    public int checkMyDamage(Point p) {
        if (myField[p.x][p.y] == '\u25A0') {
            myField[p.x][p.y] = '~';
            print();
            System.out.print("The enemy missed!\n");
            return 0;
        } else if (myField[p.x][p.y] == 'X' || myField[p.x][p.y] == '~') {
            print();
            System.out.print("The enemy has already damaged here!\n");
            return 3;
        }

        for (Ship ship : ships) {
            if (ship.hasPoint(p)) {
                myField[p.x][p.y] = 'X';
                print();

                ship.rank--;
                if (ship.rank == 0) {
                    ships.remove(ship);
                    System.out.print("The enemy has destroyed our ship!\n");
                    return 2;
                } else {
                    System.out.print("The enemy has damaged our ship!\n");
                    return 1;
                }
            }
        }

        return 0;
    }

    public boolean checkEnd(String turn) {
        if (turn == "myTurn") {
            if (enemyDeaths == 10) {
                System.out.print("\n\n=========================VICTORY==========================\n");
                System.out.print("\tCongratulations! You've destroyed all the enemy ships!\n");
                System.out.print("==========================================================\n");
                return true;
            }
        } else if (turn == "enemyTurn") {
            if (ships.size() == 0) {
                System.out.print("\n\n=========================DEFEAT===========================\n");
                System.out.print("\tAll our ships are destroyed :( \n");
                System.out.print("==========================================================\n");
                return true;
            }
        }
        return false;
    }
}


public class Game {

    public static void launch(DataInputStream in, DataOutputStream out, Scanner scanner, boolean isServer) throws IOException {
        Board board = new Board();
        board.defaultGenerate();
        board.print();

        System.out.println("\nPlan your ships on the board!\n");
        for (int i = 0; i < 10; i++){
            System.out.println("Format: x y direction [NOTE: direction may be H or h (horizontal), V or v (vertical)]\n");
            inputField(board, i, scanner);
            board.print();
        }

        int ready = 1;
        out.writeInt(ready);
        System.out.print("\nWaiting for enemy...\n");
        int readyEnemy = in.readInt();
        System.out.print("\nEnemy is ready!\n");

        while(true) {
            if (isServer) {
                myTurn(board, in, out, scanner);
                if (board.checkEnd("myTurn"))
                    break;

                enemyTurn(board, in, out);
                if (board.checkEnd("enemyTurn"))
                    break;
            } else {
                enemyTurn(board, in, out);
                if (board.checkEnd("enemyTurn"))
                    break;

                myTurn(board, in, out, scanner);
                if (board.checkEnd("myTurn"))
                    break;
            }
        }
    }

    private static void myTurn(Board board, DataInputStream in, DataOutputStream out, Scanner scanner) {
        try {
            int response = 1;
            System.out.print("\n\n==========================================================\n");
            System.out.print("\t\tIt's your turn to go!");
            System.out.print("\n==========================================================\n");

            while (response == 1 || response == 2) {
                String myTurn;
                int x, y;
                while(true) {
                    try {
                        System.out.print("Coordinates (format x y): ");
                        myTurn = scanner.nextLine();
                        String[] chunk = myTurn.split(" ");
                        if (chunk.length != 2) {
                            System.out.print("Incorrect input. 2 parameters are expected.\n");
                            continue;
                        }
                        x = Integer.parseInt(chunk[0]);
                        y = Integer.parseInt(chunk[1]);
                        if ((x >= 0) && (x < board.fieldSize) && (y >= 0) && (y < board.fieldSize))
                            break;
                        else
                            System.out.print("Out of range; Please try again\n");
                    }
                    catch (NumberFormatException e) {
                        System.out.print("Wrong format; Please try again\n");
                    }
                }

                out.writeUTF(myTurn);
                response = in.readInt();
                board.doDamage(response, new Point(x, y));
            }
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        }
    }

    private static void enemyTurn(Board board, DataInputStream in, DataOutputStream out) {
        try {
            int response = 1;
            System.out.print("\n\n==========================================================\n");
            System.out.print("\t\tThe enemy are making Turn...");
            System.out.print("\n==========================================================\n");
            while (response == 1 || response == 2) {
                String enemyTurn = in.readUTF();
                String[] chunk = enemyTurn.split(" ");
                int x = Integer.parseInt(chunk[0]);
                int y = Integer.parseInt(chunk[1]);

                response = board.checkMyDamage(new Point(x, y));
                out.writeInt(response);
            }
        } catch (IOException e) {
            System.out.println("readline:" + e.getMessage());
        }
    }

    private static void inputField(Board board, int k, Scanner scanner) {
        if ((k >= 0) && (k < 4))
            System.out.print("Place 1-deck ship:\n");
        else if ((k >= 4) && (k < 7))
            System.out.print("Place 2-deck ship:\n");
        else if ((k >= 7) && (k < 9))
            System.out.print("Place 3-deck ship:\n");
        else if (k == 9)
            System.out.print("Place 4-deck ship:\n");

        while(true) {
            try {
                String string = scanner.nextLine();
                String[] chunk = string.split(" ");
                if (chunk.length != 3) {
                    System.out.print("Incorrect input. Must be: x y direction. Please try again\n");
                    continue;
                }

                int x = Integer.parseInt(chunk[0]);
                int y = Integer.parseInt(chunk[1]);
                char orient = Character.toLowerCase(chunk[2].charAt(0));

                Ship newShip = board.checkShip(k, new Point(x, y), orient);
                if (newShip != null) {
                    board.addShip(newShip);
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.print("NumberFormatException. Please try again\n");
            }
        }
    }
}
