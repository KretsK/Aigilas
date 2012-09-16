package aigilas.strategies.impl;

import aigilas.creatures.ICreature;

public class ConfusedAndDyingStrategy extends ConfusedStrategy {
    public ConfusedAndDyingStrategy(ICreature parent)

    {
        super(parent);

    }

    @Override
    public void Act() {
        super.Act();
        _parent.ApplyDamage(1.1f, null, false);
    }
}