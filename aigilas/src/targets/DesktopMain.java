package targets;

import aigilas.Aigilas;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import sps.core.Logger;
import sps.core.Settings;
import sps.net.Server;

public class DesktopMain {
    public static void main(String[] args) {

        if (Settings.get().networkingEnabled) {
            Thread server = new Server();
            server.start();
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

        Logger.info("Launching the main game loop");
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Aigilas";
        cfg.width = Settings.get().resolutionWidth;
        cfg.fullscreen = Settings.get().fullScreen;
        cfg.height = Settings.get().resolutionHeight;
        cfg.useGL20 = true;
        new LwjglApplication(new Aigilas(), cfg);
    }
}
