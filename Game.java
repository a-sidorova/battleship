import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;


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
