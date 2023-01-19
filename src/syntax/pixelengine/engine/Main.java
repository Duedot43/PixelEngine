package syntax.pixelengine.engine;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Arguments argz = new Arguments(args);
        File image = new File(argz.mapPath);
        Image img = new Image(image);
        Console console = new Console(img, img.mapImage, argz.debug);
        console.startGameConsole();
        System.exit(0);
    } // TODO document all the functions
}
