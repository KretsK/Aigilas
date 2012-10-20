package aigilas.strategies;

import aigilas.creatures.BaseCreature;
import aigilas.strategies.impl.*;
import sps.bridge.ActorType;

public class StrategyFactory {
    public static BaseStrategy create(Strategy strategy, BaseCreature target, ActorType... actorTypes) {
        if (strategy == null) {
            return new NullStrategy(target);
        }
        switch (strategy) {
            case Attack:
                return new AttackStrategy(target, ActorType.Player);
            case AttackSelf:
                return new AttackSelfStrategy(target);
            case Confused:
                return new ConfusedStrategy(target);
            case ConfusedAndDying:
                return new ConfusedAndDyingStrategy(target);
            case ControlledByPlayer:
                return new ControlledByPlayer(target);
            case Flee:
                return new FleeStrategy(target, ActorType.Player);
            case MinionCloud:
                return new MinionCloudStrategy(target);
            case MinionExplode:
                return new MinionOneUseStrategy(target);
            case MinionFire:
                return new MinionFireStrategy(target);
            case MinionOneUse:
                return new MinionOneUseStrategy(target);
            case MinionRotate:
                return new MinionRotateStrategy(target);
            case Mutiny:
                return new AttackStrategy(target, ActorType.Non_Player);
            case StraightLineRotate:
                return new StraightLineRotateStrategy(target);
            case StraightLine:
                return new StraightLineStrategy(target);
            case TestBot:
                return new TestBotStrategy(target);
            default:
                try {
                    throw new Exception("An undefined strategy was passed into the strategy factory: " + strategy);
                }
                catch (Exception e) {

                    e.printStackTrace();
                }
                return null;
        }
    }
}