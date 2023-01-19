package syntax.pixelengine.engine;

import java.awt.image.BufferedImage;
import java.util.Scanner;

public class Console extends Player{

    private Scanner input = new Scanner(System.in);
    
    public Console(Image map, BufferedImage mapImage, boolean debug) {
        super(map, mapImage, debug);
        System.out.println("[ENGINE]:  Console started");
    }

    public int startGameConsole() {
        System.out.println("Welcome to the PixelEngine console");
        System.out.println("Type 'help' for a list of commands\n\n");


        while (true) {
            String[][] directions = generateDirections(super.getPlayerInfo());
            System.out.println(directions[0][0]);
            System.out.print("> ");
            String command = input.nextLine();
            if (command.equals("help")) {
                System.out.println("help - Displays this message");
                System.out.println("north, east, south, west - Moves the player");
                System.out.println("quit - Quits the game");
            } else if ((command.equals("north") || command.equals("east") || command.equals("south") || command.equals("west")) && inArray(command, directions[1])) {
                int[] cords = super.checkMapDirection(command, new int[][][] {{wall}, {stop}, key, door});
                if (cords[0] == -1 && cords[1] == -1) {new EngineError("Player tried to move into a wall");}
                super.movePlayer(cords[0], cords[1]);
            } else if (command.equals("quit")) {
                System.out.println("Quitting game");
                return 0;
            } else if (command.equals("tp") && super.debug) {
                System.out.print("X: ");
                int x = input.nextInt();
                System.out.print("Y: ");
                int y = input.nextInt();
                super.movePlayer(x, y);
            } else if (command.equals("use")) { 
                int[] playerInfo = super.getPlayerInfo();
                int[] playerCords = new int[] {playerInfo[1], playerInfo[2]};
                if (super.imMOREArr(super.rawMap[playerCords[0]][playerCords[1] - 1], new int[][][] {key})) {
                    super.useKey(playerCords[0], playerCords[1] - 1);
                } else if (super.imMOREArr(super.rawMap[playerCords[0] + 1][playerCords[1]], new int[][][] {key})) {
                    super.useKey(playerCords[0] + 1, playerCords[1]);
                } else if (super.imMOREArr(super.rawMap[playerCords[0]][playerCords[1] + 1], new int[][][] {key})) {
                    super.useKey(playerCords[0], playerCords[1] + 1);
                } else if (super.imMOREArr(super.rawMap[playerCords[0] - 1][playerCords[1]], new int[][][] {key})) {
                    super.useKey(playerCords[0] - 1, playerCords[1]);
                }

                if (super.imMOREArr(super.rawMap[playerCords[0]][playerCords[1] - 1], new int[][][] {door})) {
                    super.useDoor(playerCords[0], playerCords[1] - 1);
                } else if (super.imMOREArr(super.rawMap[playerCords[0] + 1][playerCords[1]], new int[][][] {door})) {
                    super.useDoor(playerCords[0] + 1, playerCords[1]);
                } else if (super.imMOREArr(super.rawMap[playerCords[0]][playerCords[1] + 1], new int[][][] {door})) {
                    super.useDoor(playerCords[0], playerCords[1] + 1);
                } else if (super.imMOREArr(super.rawMap[playerCords[0] - 1][playerCords[1]], new int[][][] {door})) {
                    super.useDoor(playerCords[0] - 1, playerCords[1]);
                }
            } else {
                System.out.println("Command not found or command invalid");
            }
        }
    }

    public String[][] generateDirections(int[] playerInfo) {
        String directions = "Possable Directions: ";
        String[] directionsCompute = new String[16];
        int[] playerCords = new int[] {playerInfo[1], playerInfo[2]};
        int arrCount = 0;
        // Check for directions
        if (!isColor(playerCords[0], playerCords[1] - 1, wall)) {
            directions += "North ";
            directionsCompute[arrCount] = "north";
            arrCount++;
        }
        if (!isColor(playerCords[0] + 1, playerCords[1], wall)) { //east
            directions += "East ";
            directionsCompute[arrCount] = "east";
            arrCount++;
        }
        if (!isColor(playerCords[0], playerCords[1] + 1, wall)) { //south
            directions += "South ";
            directionsCompute[arrCount] = "south";
            arrCount++;
        }
        if (!isColor(playerCords[0] - 1, playerCords[1], wall)) { //west
            directions += "West ";
            directionsCompute[arrCount] = "west";
            arrCount++;
        } 
        if (directionsCompute[0] == null) { // If there is no spaces for the player to move
            directions += "None you are trapped!";
            directionsCompute[arrCount] = "trap";
            new EngineError("You are trapped! (No directions found");
            arrCount++;
        }


        // Check for doors
        if (super.imMOREArr(super.rawMap[playerCords[0]][playerCords[1] - 1], new int[][][] {door})) {
            directions += "door to the north ";
            directionsCompute[arrCount] = "door_north";
            arrCount++;
        }
        if (super.imMOREArr(super.rawMap[playerCords[0] + 1][playerCords[1]], new int[][][] {door})) { //east
            directions += "door to the east ";
            directionsCompute[arrCount] = "door_east";
            arrCount++;
        }
        if (super.imMOREArr(super.rawMap[playerCords[0]][playerCords[1] + 1], new int[][][] {door})) { //south
            directions += "door to the south ";
            directionsCompute[arrCount] = "door_south";
            arrCount++;
        }
        if (super.imMOREArr(super.rawMap[playerCords[0] - 1][playerCords[1]], new int[][][] {door})) { //west
            directions += "door to the west ";
            directionsCompute[arrCount] = "door_west";
            arrCount++;
        }


        // Check for keys
        if (super.imMOREArr(super.rawMap[playerCords[0]][playerCords[1] - 1], new int[][][] {key})) {
            directions += "key to the north ";
            directionsCompute[arrCount] = "key_north";
            arrCount++;
        }
        if (super.imMOREArr(super.rawMap[playerCords[0] + 1][playerCords[1]], new int[][][] {key})) { //east
            directions += "key to the east ";
            directionsCompute[arrCount] = "key_east";
            arrCount++;
        }
        if (super.imMOREArr(super.rawMap[playerCords[0]][playerCords[1] + 1], new int[][][] {key})) { //south
            directions += "key to the south ";
            directionsCompute[arrCount] = "key_south";
            arrCount++;
        }
        if (super.imMOREArr(super.rawMap[playerCords[0] - 1][playerCords[1]], new int[][][] {key})) { //west
            directions += "key to the west ";
            directionsCompute[arrCount] = "key_west";
        }


        return new String[][] {{directions}, directionsCompute};

    }
}
