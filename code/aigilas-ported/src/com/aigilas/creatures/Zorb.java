package com.aigilas.creatures;
public     class Zorb  extends  AbstractCreature
    {
        public Zorb(){
            Strengths(StatType.MANA,StatType.HEALTH);
            Weaknesses(StatType.WISDOM,StatType.DEFENSE);
            Add(SkillId.FIREBALL);
        }
    }