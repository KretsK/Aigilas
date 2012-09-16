package spx.entities;

import aigilas.management.SpriteType;
import spx.bridge.DrawDepth;
import spx.bridge.EntityType;
import spx.core.Point2;
import spx.core.Settings;
import spx.graphics.AnimatedTexture;
import spx.graphics.SpriteInfo;

public class Entity implements IEntity {
    protected AnimatedTexture _graphic = new AnimatedTexture();

    protected boolean _isActive = true;
    protected Boolean _isBlocking;
    protected SpriteType _assetName;
    protected boolean _isOnBoard = true;
    private boolean _isInteracting = false;
    protected Point2 _location = new Point2(0, 0);
    protected EntityType _entityType;

    @Override
    public void LoadContent() {
        _graphic.LoadContent(_assetName);
    }

    @Override
    public void Draw() {
        if (_isOnBoard && _isActive) {
            _graphic.Draw();
        }
    }

    public void Hide() {
        _isOnBoard = false;
    }

    public void Show() {
        _isOnBoard = true;
    }

    protected void Initialize(Point2 location, SpriteType spriteType, EntityType entityType, DrawDepth depth) {
        _assetName = spriteType;
        _entityType = entityType;
        _location.Copy(location);
        _graphic.SetPosition(_location);
        _graphic.SetDrawDepth(depth);
    }

    @Override
    public void Update() {
    }

    public void SetLocation(Point2 location) {
        _graphic.SetPosition(location);
        _location.Copy(location);
    }

    private Point2 oldLocation = new Point2(0, 0);

    public void UpdateLocation(Point2 location) {
        oldLocation.Copy(_location);
        _graphic.SetPosition(location);
        _location.Copy(location);
        EntityManager.UpdateGridLocation(this, oldLocation);
    }

    private Point2 target = new Point2(0, 0);

    public boolean Move(float amountX, float amountY) {
        amountX = NormalizeDistance(amountX);
        amountY = NormalizeDistance(amountY);
        target.Reset(_location.PosX + amountX, _location.PosY + amountY);
        if (CoordVerifier.IsValid(target)) {
            UpdateLocation(target);
            return true;
        }
        return false;
    }

    private static int isNeg = 1;
    private static int factorsOfSpriteHeight = 0;

    private static float NormalizeDistance(float amount) {
        isNeg = (amount < 0) ? -1 : 1;
        amount = Math.abs(amount);
        factorsOfSpriteHeight = (int) Math.floor(amount / Settings.Get().spriteHeight);
        factorsOfSpriteHeight = (factorsOfSpriteHeight == 0 && amount != 0) ? 1 : factorsOfSpriteHeight;
        return (Settings.Get().spriteHeight * factorsOfSpriteHeight * isNeg);
    }

    @Override
    public boolean IsActive() {
        return _isActive;
    }

    @Override
    public void SetInactive() {
        _isActive = false;
    }

    @Override
    public boolean IsBlocking() {
        if (_isBlocking == null) {
            return false;
        }
        return _isBlocking;
    }

    @Override
    public Point2 GetLocation() {
        return _location;
    }

    public boolean IsGraphicLoaded() {
        return (_graphic != null);
    }

    protected void SetSpriteInfo(SpriteInfo sprite) {
        _graphic.SetSpriteInfo(sprite);
    }

    @Override
    public boolean contains(Point2 target)

    {
        return target.GridX == GetLocation().GridX && target.GridY == GetLocation().GridY;
    }

    public void SetInteraction(boolean isInteracting) {
        _isInteracting = isInteracting;
    }

    public boolean IsInteracting() {
        return _isInteracting;
    }

    public void SetInteracting(boolean isInteracting) {
        _isInteracting = isInteracting;
    }

    @Override
    public EntityType GetEntityType() {
        return _entityType;
    }
}