package aigilas.creatures;

import aigilas.entities.Elements;
import aigilas.management.SpriteType;
import aigilas.strategies.Strategy;
import aigilas.strategies.StrategyFactory;
import sps.bridge.ActorType;

public class Hand extends BaseEnemy {
    public Hand() {
        super(ActorType.HAND, SpriteType.HAND);
        Compose(Elements.PHYSICAL);
        Strengths(StatType.STRENGTH, StatType.STRENGTH);
        _isBlocking = false;
        _strategy = StrategyFactory.create(Strategy.StraightLine, this);
    }
}
