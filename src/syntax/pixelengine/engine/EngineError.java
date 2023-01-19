package syntax.pixelengine.engine;

public class EngineError{

    public static String version = "Beta 0.0.1";

    public EngineError(String error) {
        System.out.println("[ENGINE ERROR]: " + error);
        System.exit(1);
    }

    public void EngineWarning(String warning) {
        System.out.println("[ENGINE WARNING]: " + warning);
    }
}