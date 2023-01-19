package syntax.pixelengine.engine;

import java.awt.image.BufferedImage;

public class Map {
    public int[] zero = new int[2];
    public int[] one = new int[2];
    public int[] wall = new int[2];
    public int[] floor = new int[2];
    public int[] stop = new int[2];
    public int[][] door = new int[255][2];
    public int[][] key = new int[255][2];
    public int[][] comments = new int[222][2];
    public int[] background = new int[2];
    public int[][] data = new int[256][3];
    public int[][][] rawMap = new int[256][256][3];
    public boolean debug = false;

    public Map(Image map, boolean debug) {
        this.rawMap = map.getMap();
        this.debug = debug;
        zero = rawMap[0][0];

        one = rawMap[1][0];

        background = rawMap[2][0];

        for (int i = 2; i <= 223; i++) {
            comments[i - 2] = rawMap[0][i];
        }


        for (int y = 0; y <= 255; y++) {
            int[] finBytes = new int[4]; // The decoded numbers from the image
            int finBytesCount = 0; // Counter for how many bytes have been added

            for (int x = 224; x <= 255; x+=8) { // For loop for incrimenting through 4 bytes
                int[] currentByte = new int[8]; // The current 
                int countCurrentByte = 0; //For seeing where we are in the currentByte array

                for (int b = x; b <= x + 7; b++) { // Determins if the bit in the image is a zero or a one then adds it to current byte
                    int[] currentBit = rawMap[b][y]; // Current bit on the image

                    if (currentBit[0] == one[0] && currentBit[1] == one[1] && currentBit[2] == one[2]) { // If the current bit is a one
                        currentByte[countCurrentByte] = 1;
                    } else if (currentBit[0] == zero[0] && currentBit[1] == zero[1] && currentBit[2] == zero[2]) { // If the current bit is a zero
                        currentByte[countCurrentByte] = 0;
                    } else { // IF the bit is unrecegionized it may be a mismached color
                        System.out.println("[ENGINE WARNING]: Invalid bit value\n\nZero:" + zero[0] + " " + zero[1] + " " + zero[2] + "\nOne:" + one[0] + " " + one[1] + " " + one[2] + "\nCurrent:" + currentBit[0] + " " + currentBit[1] + " " + currentBit[2] + "\nCords: " + b + " " + y);
                    }
                    countCurrentByte++; // We are done with that bit now move to the next
                }

                // Calculate byte value then add it to finBytes
                int byteValue = 0;
                int byteCount = 0;
                for (int i = 7 ; i >= 0; i--) {
                    if (currentByte[i] == 1) {
                        byteValue += (int) Math.pow(2, byteCount);
                    }
                    byteCount++;
                }

                finBytes[finBytesCount] = byteValue;
                finBytesCount++; // Add it to finBytes

            }
            data[y] = finBytes;
        }

        wall = getBitInfo(2)[2];
        floor = getBitInfo(3)[2]; // Get the wall and floor colors
        stop = getBitInfo(4)[2]; // Get the stop color

        int doorCount = 0; //Get door color info
        int[][][] doorInfo = getAllBitInfo(5);
        for (int x = 0; x <= doorInfo.length - 1; x++) {
            if (doorInfo[x][0][0] != 0) {
                door[doorCount] = doorInfo[x][2]; // Color info for the door
                doorCount++;
            }
        }

        int keyCount = 0;
        int[][][] keyInfo = getAllBitInfo(6);
        for (int x = 0; x <= keyInfo.length - 1; x++) {
            if (keyInfo[x][0][0] != 0) {
                key[keyCount] = keyInfo[x][2]; // Color info for the key
                keyCount++;
            }
        }


        if (wall == null) { new EngineError("Wall not found");} // If the wall is not found
        if (floor == null) { new EngineError("Floor not found");} // If the floor is not found

        System.out.println("[ENGINE]:  Map Data loaded"); // Report to the user that the map has loaded
    }

    public int[][] getBitInfo(int bit) {
        int[][] output = new int[3][4];
        for (int y = 0; y <= 255; y++) {
            if (data[y][0] == bit) {
                output[0] = data[y]; //Bit info
                output[1] = new int[] {y}; // Y location
                output[2] = this.rawMap[223][y]; // Color info
                return output;
            }
        }
        return null;
    }

    public int [][][] getByteInfo(int[] byteInfo) {
        int[][][] output = new int[255][3][4];
        int count = 0;
        for (int y = 0; y <= 255; y++) {
            if (data[y][0] == byteInfo[0] && data[y][1] == byteInfo[1] && data[y][2] == byteInfo[2] && data[y][3] == byteInfo[3]) {
                output[count][0] = data[y]; //Bit info
                output[count][1] = new int[] {y}; // Y location
                output[count][2] = this.rawMap[223][y]; // Color info
                return output;
            }
        }
        return null;
    }


    public int[][][] getAllBitInfo(int bit) {
        int[][][] output = new int[255][3][4];
        int count = 0;
        for (int y = 0; y <= 255; y++) {
            if (data[y][0] == bit) {
                output[count][0] = data[y]; //Bit info
                output[count][1] = new int[] {y}; // Y location
                output[count][2] = this.rawMap[223][y]; // Color info
                count++;
            }
        }
        return output;
    }

    public int[][] getBitInfoColor(int[] color) {
        int[][] output = new int[3][4];
        for (int y = 0; y <= 255; y++) {
            if (this.rawMap[223][y][0] == color[0] && this.rawMap[223][y][1] == color[1] && this.rawMap[223][y][2] == color[2]) {
                output[0] = data[y]; //Bit info
                output[1] = new int[] {y}; // Y location
                output[2] = this.rawMap[223][y]; // Color info
                return output;
            }
        }
        return null;
    }

    public BufferedImage changeBit(int bitId, int[] newByteInt, BufferedImage mapImage) { // The bit ID is the x value that the bit is located
        data[bitId] = newByteInt; // Change the data in the map

        int[][] newByteBit = new int[4][8]; // The new byte in bit form
        for (int x = 0; x <= newByteBit.length - 1; x++) {
            String byteStr = Integer.toBinaryString(newByteInt[x]); // Convert the byte to a string
            int length = byteStr.length();
            for (int z = 0; z < (8 - length); z++) { // Add zeros to the front of the string
                byteStr = "0" + byteStr;
            }
            for (int y = 0; y <= 7; y++) { // Convert the string to a bit array
                newByteBit[x][y] = Integer.parseInt(byteStr.substring(y, y + 1));
            }
        }

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 8; y++) {
                if (newByteBit[x][y] == 1) {
                    this.rawMap[224 + (y + (x * 8))][bitId] = one;
                } else {
                    this.rawMap[224 + (y + (x * 8))][bitId] = zero;
                }
            }
        }
        mapImage = new Image().changeMap(mapImage, rawMap); // Remap the image
        if (debug) { new Image().storeImage(mapImage, "map.debug.png"); } // Save the image to disk
        return mapImage;
    }

    /**
     * @param x        The x location of the pixel you want to change
     * @param y        The y location of the pixel you want to change
     * @param color    The color of the pixel you want to change
     * @param mapImage The image of the map
     * @return         The new map image
     */
    public BufferedImage changeCanvis(int x, int y, int[] color, BufferedImage mapImage) {
        if (x > 223 || y > 255) {
            new EngineError("Invalid canvis location data could be overwritten");
        }
        this.rawMap[x][y] = color;
        mapImage = new Image().changeMap(mapImage, rawMap); // Remap the image
        if (debug) { new Image().storeImage(mapImage, "map.debug.png"); } // Save the image to disk
        return mapImage;
    }

    public boolean isColor(int x, int y, int[] color) {
        int[] currentColor = this.rawMap[x][y];
        if (currentColor[0] == color[0] && currentColor[1] == color[1] && currentColor[2] == color[2]) {
            return true;
        } else {
            return false;
        }
    }

    public boolean inArray(String string, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null || array[i].equals(string)) {
                return true;
            }
        }
        return false;
    }

    public boolean compareArray(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean imMoreArr(int[] array1, int[][] array2) {

        for (int i = 0; i < array2.length; i++) {
            if (compareArray(array1, array2[i])) {
                return true;
            }
        }

        return false;
    }

    public boolean imMOREArr(int[] array1, int[][][] array2) {
        //MORE MORE!!! MOREEEEE!!!!! MOREEEEEEE!!!!!!
        for (int i = 0; i < array2.length; i++) {
            if (imMoreArr(array1, array2[i])) {
                return true;
            }
        }

        return false;
    }
}
