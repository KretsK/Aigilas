package aigilas.desktop;

import spx.core.XnaManager;
import spx.io.Client;
import spx.io.Server;
import xna.wrapper.Console;
import xna.wrapper.Environment;
import aigilas.ServerThread;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		System.out.println("Launching the main game loop");
		Thread server = new Thread(new ServerThread());
		// server.run();
		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Aigilas";
		cfg.width = XnaManager.RenderWidth;
		cfg.height = XnaManager.RenderHeight;
		new LwjglApplication(new Aiglias(), cfg);

		Server.Get().Close();
		Client.Get().Close();
		Console.WriteLine("Finished being a client");
		Environment.Exit();
	}
}