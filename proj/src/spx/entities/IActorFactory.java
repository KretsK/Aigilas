package spx.entities;

import spx.bridge.ActorType;
import spx.core.Point2;

public interface IActorFactory {
    IActor Create(ActorType actorType, Point2 position);

    IActor GenerateActor(ActorType actorType);

    IActor CreateRandom(Point2 randomPoint);

    void ResetPlayerCount();

    void IncreasePlayerCount();

    int GetPlayerCount();
}