package com.aigilas.entities;
    public class Altar  extends  Entity
    {
        private God _god;
        private Player _currentTarget;
        private List<IEntity> _offerings;

        public Altar(Point2 location,int godName)
        {
            _god = God.Get(godName);
            _graphic.SetColor(_god.GetColor());
            Initialize(location, SpriteType.ALTAR, com.aigilas.EntityType.ALTAR,com.aigilas.Depth.Altar);
        }

@Override
        {
            _currentTarget = (Player)EntityManager.GetTouchingCreature(this);
            if (_currentTarget != null)
            {
                if (_currentTarget.IsInteracting())
                {
                    _currentTarget.Pray(_god);
                }
                _offerings = EntityManager.GetEntities(com.aigilas.EntityType.ITEM, _location);
                for (IEntity offering:_offerings)
                {
                    _currentTarget.Sacrifice(_god,(GenericItem)offering);
                }
                TextManager.Add(new ActionText(_god.NameText, 1, (int) GetLocation().PosX, (int) GetLocation().PosY));
            }
        }

        public God GetGod()
        {
            return _god;
        }
    }