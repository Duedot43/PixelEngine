package syntax.pixelengine.engine;

public class ErrCheck {
    private boolean verbose = false;
    private Map map;

    public ErrCheck(int[][][] rawMap, int[][] data, int[] zero, int[] one, int[] wall, int[] floor, int[] stop, int[][] door, int[][] key, int[][] comments, int[] background, Map map, boolean verbose) {
        this.map = map;
        this.verbose = verbose;
        // DATA CHECKING
        checkData(data);
    }

    public void checkData(int[][] data) {
        print("Checking doors...\n\n");
        for (int i = 0; i < data.length; i++) {
            if (data[i][0] == 5) {
                print("Door found at " + i);
                if (map.getByteInfo(new int[] {6, data[i][2], data[i][1], 0}) == null) {
                    System.out.println("Door at " + i + " has no key");
                } else {
                    print("Door at " + i + " has a key");
                }
            }
        }

        System.out.println("\n\nChecking keys...\n\n");

        for (int i = 0; i < data.length; i++) {
            if (data[i][0] == 6) {
                print("Key found at " + i);
                if (map.getByteInfo(new int[] {5, data[i][2], data[i][1], 0}) == null) {
                    System.out.println("Key at " + i + " has no door");
                } else {
                    print("Key at " + i + " has a door");
                }
            }
        }
    }

    public void print(String message) {
        if (verbose) {
            System.out.println(message);
        }
    }
}
