package aigilas.skills.impl;

import aigilas.creatures.BaseCreature;
import aigilas.creatures.StatType;
import aigilas.entities.Elements;
import aigilas.skills.AnimationType;
import aigilas.skills.BaseSkill;
import aigilas.skills.SkillId;
import aigilas.statuses.Status;
import aigilas.statuses.StatusFactory;

public class DartSkill extends BaseSkill {
    public DartSkill()

    {
        super(SkillId.DART, AnimationType.RANGED);

        addCost(StatType.MANA, 10);
        add(Elements.DARK);

    }

    @Override
    public void affect(BaseCreature target)

    {
        StatusFactory.apply(target, Status.Poison);
        target.applyDamage(5, _source);

    }

}