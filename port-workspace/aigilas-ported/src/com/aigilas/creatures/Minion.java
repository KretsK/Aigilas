package com.aigilas.creatures;
    public class Minion  extends  ICreature
    {
        public Minion(int actorType,float coolDown)
        {
        	Construct(actorType,coolDown);
        }
        public void Init(ICreature source,SkillEffect effectGraphic)
        {
            _master = source;
            Setup(source.GetLocation(), _actorType, _baseStats,null,false);
            if (null != effectGraphic)
            {
                SetSkillVector(effectGraphic.GetDirection().Rotate180());
            }
            else
            {
                SetSkillVector(new Point2(0, 1));
            }
            _strategy.AddTargets(_master);
        }
        protected void Add(String skill)
        {
            if (_skills == null)
            {
                _skills = new SkillPool(this);
            }
            _skills.Add(skill);
        }
    }
