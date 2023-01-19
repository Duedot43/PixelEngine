package syntax.pixelengine.engine;

import java.io.File;

public class Arguments {
    public String mapPath = "";
    public boolean debug = false;

    public Arguments(String[] args) {
        if (args.length == 0) {
            new EngineError("No arguments provided use --help for help");
        }

        if (inArray("--help", args) || inArray("-h", args)) {
            System.out.println("Welcome to PixelEngine!\n-h  --help for help\n-m  --map <map> to load a map\n-d  --debug puts the engine in debug mode enabling debug commands and debug map\n-V  --verify verify the map file\n-v  --version to get the version");
            System.exit(0);
        }
        if ((inArray("--map", args) || inArray("-m", args)) && args.length >= 2) {
            mapPath = args[1];
        }
        if (inArray("--version", args) || inArray("-v", args)) {
            System.out.println("PixelEngine version " + EngineError.version);
            System.exit(0);
        }
        if (inArray("--debug", args) || inArray("-d", args)) {
            System.out.println("[ENGINE]: Debug mode enabled");
            debug = true;
        } else if (inArray("--verify", args) || inArray("-V", args)) {
            if (mapPath.equals("")) {
                new EngineError("No map path provided use --help for help");
            }
            File image = new File(this.mapPath);
            Image img = new Image(image);
            Map map = new Map(img, true);
            new ErrCheck(map.rawMap, map.data, map.zero, map.one, map.wall, map.floor, map.stop, map.door, map.key, map.comments, map.background, map, true);
        }
        if (mapPath == "" || args[0] == null) {
            new EngineError("Invalid argument use --help for help");
        }
        // TODO fix this so it can handle arguments in any order `https://courses.cs.washington.edu/courses/cse341/99wi/java/tutorial/java/cmdLineArgs/parsing.html`
    }

    public boolean inArray(String string, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].equals(string)) {
                return true;
            }
        }
        return false;
    }
        
}
