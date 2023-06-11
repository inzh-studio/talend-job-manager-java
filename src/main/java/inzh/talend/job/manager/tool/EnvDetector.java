package inzh.talend.job.manager.tool;

/**
 * Local environnement detector.
 *
 * @author Jean-Raffi Nazareth
 */
public class EnvDetector {

    public static Env getDetected() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.startsWith("win") ? Env.DOS : Env.UNIX;
    }
}
