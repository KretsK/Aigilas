package spx.entities;

import spx.bridge.EntityType;
import spx.core.Point2;

public interface IEntity {
    boolean contains(Point2 target);

    void Draw();

    void Update();

    void LoadContent();

    Point2 GetLocation();

    EntityType GetEntityType();

    boolean IsActive();

    boolean IsBlocking();

    void SetInactive();
}