package syntax.pixelengine.tool;

import syntax.pixelengine.engine.EngineError;
import syntax.pixelengine.engine.Player;
import java.util.Scanner;

public class Console {
    
    private Player map;
    private Scanner scanner = new Scanner(System.in);

    public Console(Player map) {
        this.map = map;
    }

    public void console() {
        System.out.println("PixelEngine Tool\n\n");
        int x;
        int y;
        int[] rgb = new int[3];
        int doorId;
        int keyId;
        int doorY = 0;
        while (true) {
            System.out.print("Enter a command: ");
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("move player")) {
                System.out.print("X: ");
                x = scanner.nextInt();
                System.out.print("Y: ");
                y = scanner.nextInt();
                map.movePlayer(x, y);
                System.out.println("Player moved to " + x + ", " + y);
            } else if (command.equalsIgnoreCase("add door")) {
                System.out.print("X: ");
                x = scanner.nextInt();
                System.out.print("Y: ");
                y = scanner.nextInt();

                System.out.print("R: ");
                rgb[0] = scanner.nextInt();
                System.out.print("G: ");
                rgb[1] = scanner.nextInt();
                System.out.print("B: ");
                rgb[2] = scanner.nextInt();

                System.out.print("Door ID: ");
                doorId = scanner.nextInt();
                System.out.print("Key ID: ");
                keyId = scanner.nextInt();

                for (int p = 0; p <= map.data.length; p++) {
                    if (map.data[p][0] == 1 && map.data[p][1] == 1 && map.data[p][2] == 1 && map.data[p][3] == 1) {
                        doorY = p;
                        map.data[p][0] = 5;
                        map.data[p][1] = doorId;
                        map.data[p][2] = keyId;
                        map.data[p][3] = 0;
                        break;
                    }
                }
                if (doorY == 0) {
                    new EngineError("No more data can be added!");
                }

                map.mapImage = map.changeCanvis(x, y, rgb, map.mapImage); // Add the real door
                map.mapImage = map.changeCanvis(223, doorY, rgb, map.mapImage); // Add the door byte color
                doorY = 0;
            } else if (command.equalsIgnoreCase("add key")) {
                System.out.print("X: ");
                x = scanner.nextInt();
                System.out.print("Y: ");
                y = scanner.nextInt();

                System.out.print("R: ");
                rgb[0] = scanner.nextInt();
                System.out.print("G: ");
                rgb[1] = scanner.nextInt();
                System.out.print("B: ");
                rgb[2] = scanner.nextInt();

                System.out.print("Key ID: ");
                keyId = scanner.nextInt();
                System.out.print("Door ID: ");
                doorId = scanner.nextInt();

                for (int p = 0; p <= map.data.length; p++) {
                    if (map.data[p][0] == 1 && map.data[p][1] == 1 && map.data[p][2] == 1 && map.data[p][3] == 1) {
                        doorY = p;
                        map.data[p][0] = 5;
                        map.data[p][1] = keyId;
                        map.data[p][2] = doorId;
                        map.data[p][3] = 0;
                        break;
                    }
                }
                if (doorY == 0) {
                    new EngineError("No more data can be added!");
                }

                map.mapImage = map.changeCanvis(x, y, rgb, map.mapImage); // Add the real door
                map.mapImage = map.changeCanvis(223, doorY, rgb, map.mapImage); // Add the door byte color
                doorY = 0;
            }
        }
    }
}
