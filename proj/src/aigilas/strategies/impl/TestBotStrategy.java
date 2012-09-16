package aigilas.strategies.impl;

import aigilas.creatures.ICreature;
import aigilas.skills.AnimationType;
import aigilas.skills.SkillLogic;
import aigilas.strategies.IStrategy;
import aigilas.strategies.Strategy;
import spx.bridge.ActorType;
import spx.bridge.EntityType;
import spx.core.RNG;
import spx.entities.EntityManager;
import spx.entities.HitTest;
import spx.entities.IEntity;
import spx.paths.PathFinder;

public class TestBotStrategy extends IStrategy {
    public TestBotStrategy(ICreature parent) {
        super(parent, Strategy.TestBot);

        _targets.AddTargetTypes(ActorType.NONPLAYER);
    }

    private IEntity _stairsTarget;

    @Override
    public void Act() {
        if (RNG.Next(0, 1000) == 2) {
            _parent.CycleActiveSkill(1);
        }
        if (RNG.Next(0, 100) <= 1) {
            _parent.UseActiveSkill();
        }
        if (AbleToMove()) {
            if (SkillLogic.IsSkill(_parent.GetActiveSkill(), AnimationType.RANGED)) {
                if (opponent != null) {
                    _parent.SetSkillVector(CalculateTargetVector(_parent.GetLocation(), opponent.GetLocation()));
                }
                if (_parent.GetSkillVector().GridX != 0 || _parent.GetSkillVector().GridY != 0) {
                    _parent.UseActiveSkill();
                }
            }
            if (targetPath.HasMoves()) {
                nextMove.Copy(targetPath.GetNextMove());
                _parent.MoveTo(nextMove);
                if (_stairsTarget != null && HitTest.IsTouching(_parent, _stairsTarget)) {
                    _parent.SetInteracting(true);
                } else {
                    _parent.SetInteracting(false);
                }
            }
        } else {
            if ((targetPath == null || !targetPath.HasMoves()) && EntityManager.GetObjects(EntityType.ACTOR).size() == 1) {
                _stairsTarget = EntityManager.GetObject(EntityType.DOWNSTAIRS);
                if (_stairsTarget != null) {
                    targetPath.Copy(PathFinder.FindNextMove(_parent.GetLocation(), _stairsTarget.GetLocation()));
                }
            }
        }
    }
}