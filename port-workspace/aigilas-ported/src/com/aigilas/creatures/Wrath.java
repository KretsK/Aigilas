package com.aigilas.creatures;
public     class Wrath  extends  AbstractCreature
    {
        public Wrath(){
            Strengths(StatType.STRENGTH, StatType.STRENGTH);
            Add(SkillId.DISMEMBERMENT);
        }
    }