package syntax.pixelengine.tool;

public class Argz {
    
    public String mapPath = null;

    public Argz(String[] args) {
        if (args[0].equals("--map")) {
            mapPath = args[1];
        }
    }
}
