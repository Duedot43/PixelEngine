package syntax.pixelengine.engine;

import java.awt.image.BufferedImage;

public class Player extends Map {
    private int[] playerColor = new int[3];
    private int[] playerInfo = new int[4];
    private int playerBitPos = 0;
    public BufferedImage mapImage = null;

    public Player(Image map, BufferedImage mapImage, boolean debug) {
        super(map, debug);
        this.mapImage = mapImage;
        int[][] player = super.getBitInfo(1);
        if (player == null) {
            new EngineError("Player bit not found"); // If the player bit is not found, throw an error
        }
        playerInfo = player[0]; // Get the player raw info from player
        playerBitPos = player[1][0]; // Get the player bit position from player
        playerColor = player[2]; // Get the player color from player
        System.out.println("[ENGINE]:  Player started");
    }

    // RIP bad decodePlayerDirection

    public int[] checkMapDirection(String direction, int[][][] stopFor) {
        int move = -1; // How many spaces we move before hitting a wall

        int ogX = playerInfo[1];
        int ogY = playerInfo[2];

        int currentX = ogX;
        int currentY = ogY;


        while (!imMOREArr(rawMap[currentX][currentY], stopFor)) {
            move++;
            int xy[] = moveDirection(direction, ogX, ogY, move);
            currentX = xy[0];
            currentY = xy[1];

            if ((currentX != 0 && currentY != 0) && (imMoreArr(rawMap[moveDirection(direction, ogX, ogY, move + 1)[0]][moveDirection(direction, ogX, ogY,move + 1)[1]], new int[][] { stop }))) {
                move += 2; // If the next block is a stop block, move 2 spaces
            }
            if ((currentX != 0 && currentY != 0) && (imMOREArr(rawMap[moveDirection(direction, ogX, ogY, move + 1)[0]][moveDirection(direction, ogX, ogY,move + 1)[1]], new int[][][] { door })) && super.getBitInfoColor(rawMap[moveDirection(direction, ogX, ogY, move + 1)[0]][moveDirection(direction, ogX, ogY,move + 1)[1]])[0][3] == 1) {
                move += 1; // If the next block is a door, move 1 space
            }

        }

        move--;

        
        return moveDirection(direction, ogX, ogY, move);
    }


    public int[] moveDirection(String direction, int x, int y, int move) {
        if (direction.equals("north")) {
            y -= move;
        } else if (direction.equals("east")) {
            x += move;
        } else if (direction.equals("south")) {
            y += move;
        } else if (direction.equals("west")) {
            x -= move;
        }

        return new int[] { x, y };
    }

    public void movePlayer(int x, int y) {
        mapImage = super.changeCanvis(this.playerInfo[1], this.playerInfo[2], super.floor, mapImage); // Remove current
                                                                                                      // player from map
        mapImage = super.changeCanvis(x, y, playerColor, mapImage); // Add player back in new map
        this.playerInfo[1] = x;
        this.playerInfo[2] = y;
        mapImage = super.changeBit(playerBitPos, playerInfo, mapImage); // Update player info on the image
    }

    public int[] getPlayerInfo() {
        return playerInfo;
    }

    public void useKey(int x, int y) {
        int[][] objInfo = super.getBitInfoColor(rawMap[x][y]);
        objInfo[0][3] = 1; // So the user now posseses the key
        if (objInfo[0][1] == 0) {
            new EngineError("Key does not have an ID");
        }
        if (objInfo[0][2] == 0) {
            new EngineError("Key does not have a door ID");
        }
        mapImage = super.changeBit(objInfo[1][0], objInfo[0], mapImage);
        mapImage = super.changeCanvis(x, y, super.floor, mapImage);
        System.out.println("Key " + objInfo[0][1] + " used");
    }

    public void useDoor(int x, int y) {
        int[][] objInfo = super.getBitInfoColor(rawMap[x][y]);
        int needKeyID = objInfo[0][2]; // Third bit is the key ID to open the door
        if (needKeyID == 0) {
            new EngineError("Door does not have a key ID");
        }
        if (objInfo[0][1] == 0) {
            new EngineError("Door does not have an ID");
        }
        if (super.getByteInfo(new int[] {6, objInfo[0][2]/*Key ID*/, objInfo[0][1]/*Door ID*/, 1}) != null) {
            objInfo[0][3] = 1;
            System.out.println("Door opened");
            mapImage = super.changeBit(objInfo[1][0], objInfo[0], mapImage); // Update door info to open
            mapImage = super.changeCanvis(x, y, super.floor, mapImage);
        } else {
            System.out.println("Door locked need key ID: " + needKeyID);
        }
    }
}
