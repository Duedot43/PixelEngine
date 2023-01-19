package syntax.pixelengine.tool;

import syntax.pixelengine.engine.Player;
import syntax.pixelengine.engine.Image;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        Argz argz = new Argz(args);
        File image = new File(argz.mapPath);
        Image img = new Image(image);
        Player map = new Player(img, img.mapImage,true);
        Console console = new Console(map);
        console.console();
    }
}
