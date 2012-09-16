package spx.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.GdxNativesLoader;
import spx.graphics.ContentManager;
import spx.graphics.Renderer;
import spx.net.Client;

public class SpxManager {
    // This is the resolution used by the game internally
    public static int WindowHeight = Settings.Get().spriteHeight * Settings.Get().tileMapHeight;
    public static int WindowWidth = Settings.Get().spriteWidth * Settings.Get().tileMapWidth;

    // This is the resolution used to draw on the screen
    // 720
    // 1050
    public static int RenderHeight = WindowHeight;
    // 1280
    // 1680
    public static int RenderWidth = WindowWidth;
    private static ContentManager __assetHandler;
    public static Renderer Renderer;

    private static final String __menuBaseSprite = "MenuBase.png";
    private static final String __gameOverSprite = "GameOver.png";
    private static final String __particleSprite = "Particle.png";

    public static void SetContentManager(ContentManager assetHandler) {
        __assetHandler = assetHandler;
    }

    private static Texture GetAsset(String resourceName) {
        return __assetHandler.LoadTexture(resourceName);
    }

    public static Sprite GetParticleAsset() {
        return new Sprite(GetAsset(__particleSprite));
    }

    public static Sprite GetMenuBaseAsset() {
        return new Sprite(GetAsset(__menuBaseSprite));
    }

    public static Sprite GetSpriteAsset(int index) {
        return __assetHandler.LoadSprite(index);
    }

    public static Texture GetGameOverAsset() {
        return GetAsset(__gameOverSprite);
    }

    public static void SetupCamera(boolean isFullScreen) {

        Renderer = new Renderer();
    }

    public static Point2 GetCenter() {
        return new Point2(WindowWidth / 2, WindowHeight / 2);
    }

    public static Point2 GetDimensions() {
        return new Point2(WindowWidth, WindowHeight);
    }

    public static void Setup() {
        Client.Get();
        GdxNativesLoader.load();
        SetupCamera(false);
        SetContentManager(new ContentManager());
        Renderer = new Renderer();
    }
}