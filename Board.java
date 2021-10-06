import java.util.ArrayList;

public class Board {
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
        try {
            rank = Ship.getRank(num);
            if ((orient == 'h' && p.y + rank > fieldSize) || (orient == 'v' && p.x + rank > fieldSize))
                throw new ArithmeticException("Incorrect coordinates or direction. Please try again.\n");

            checkCorrectShip = checkDst(p, rank, orient);
        } catch (ArithmeticException e) {
            System.out.print("Incorrect coordinates or direction. Please try again.\n");
            return null;
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

    private int checkDst(Point p, int len, char oriented) {
        int x = p.x;
        int y = p.y;

        for (int l = 0; l < len; l++) {
            if (myField[x][y] != '\u25A0')
                return 2;
            if ((y - 1 >= 0) && (myField[x][y - 1] != '\u25A0'))
                return 2;
            if ((x - 1 >= 0) && (myField[x - 1][y] != '\u25A0'))
                return 2;
            if ((y - 1 >= 0) && (x - 1 >= 0) && (myField[x - 1][y - 1] != '\u25A0'))
                return 2;
            if ((y + 1 < fieldSize) && (myField[x][y + 1] != '\u25A0'))
                return 2;
            if ((x + 1 < fieldSize) && (myField[x + 1][y] != '\u25A0'))
                return 2;
            if ((y + 1 < fieldSize) && (x + 1 < fieldSize) && (myField[x + 1][y + 1] != '\u25A0'))
                return 2;
            if ((y + 1 < fieldSize) && (x - 1 >= 0) && (myField[x - 1][y + 1] != '\u25A0'))
                return 2;
            if ((y - 1 >= 0) && (x + 1 < fieldSize) && (myField[x + 1][y - 1] != '\u25A0'))
                return 2;

            if (oriented == 'v')
                x++;
            else
                y++;
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
