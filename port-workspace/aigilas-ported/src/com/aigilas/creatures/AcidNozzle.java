package com.aigilas.creatures;
public     class AcidNozzle  extends  Minion
    {
        public AcidNozzle()
            {
            _composition.add(Elements.EARTH);
            _strategy = StrategyFactory.Create(Strategy.MinionRotate,this);
        }
    }
