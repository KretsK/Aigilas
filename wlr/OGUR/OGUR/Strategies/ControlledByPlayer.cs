﻿using System;
using SPX.Entities;
using OGUR.Dungeons;
using OGUR.Entities;
using OGUR.Management;
using OGUR.Creatures;
using SPX.Core;
namespace OGUR.Strategies
{
    public class ControlledByPlayer : IStrategy
    {
        private bool _isCasting = false;
        private Point2 _keyVelocity = new Point2(0, 0);

        public ControlledByPlayer(ICreature parent) : base(parent)
        {
            _targets.AddTargetTypes(ActorType.NONPLAYER);
        }

        public override void Act()
        {
            if (Input.IsPressed(Commands.Start, _parent.GetPlayerIndex()))
            {
                _parent.SetPlaying(true);
            }
            if (Input.IsPressed(Commands.Back, _parent.GetPlayerIndex()))
            {
                _parent.SetPlaying(false);
            }
            if (_parent.IsPlaying())
            {
                var leftVelocity = (Input.IsPressed(Commands.MoveLeft,_parent.GetPlayerIndex())? -Stats.DefaultMoveSpeed: 0);
                var rightVelocity = ((Input.IsPressed(Commands.MoveRight, _parent.GetPlayerIndex())) ? Stats.DefaultMoveSpeed : 0);
                _keyVelocity.SetX(rightVelocity + leftVelocity);

                var downVelocity = ((Input.IsPressed(Commands.MoveDown, _parent.GetPlayerIndex())) ? Stats.DefaultMoveSpeed : 0);
                var upVelocity = ((Input.IsPressed(Commands.MoveUp, _parent.GetPlayerIndex())) ? -Stats.DefaultMoveSpeed : 0);
                _keyVelocity.SetY(upVelocity + downVelocity);

                if (Input.IsContext(Contexts.Free,_parent.GetPlayerIndex()))
                {
                    var skillCycleVelocity =
                        ((Input.IsPressed(Commands.CycleLeft, _parent.GetPlayerIndex()))? -1: 0)
                        +
                        ((Input.IsPressed(Commands.CycleRight, _parent.GetPlayerIndex()))? 1: 0);
                    _parent.CycleActiveSkill(skillCycleVelocity);

                    if (!_isCasting)
                    {
                        _parent.MoveIfPossible(_keyVelocity.X, _keyVelocity.Y);
                    }
                    var isPress = Input.IsPressed(Commands.Confirm, _parent.GetPlayerIndex());
                    if (!isPress)
                    {
                        _parent.SetInteraction(false);
                    }
                    if (isPress && !_parent.IsInteracting())
                    {
                        _parent.SetInteraction(true);
                    }
                }
                if (Input.IsPressed(Commands.Skill, _parent.GetPlayerIndex()))
                {
                    _isCasting = true;
                }

                if(_isCasting)
                {
                    if (!_keyVelocity.IsZero())
                    {
                        _parent.SetSkillVector(_keyVelocity);
                    }
                    if (_parent.GetSkillVector() == null)
                    {
                        _parent.SetSkillVector(new Point2(1, 0));
                    }
                    if (!_parent.GetSkillVector().IsZero())
                    {
                        _parent.UseActiveSkill();
                        _isCasting = false;
                    }
                }
                
                if (Input.IsPressed(Commands.Inventory, _parent.GetPlayerIndex()))
                {
                    Input.SetContext(_parent.ToggleInventoryVisibility()? Contexts.Inventory: Contexts.Free, _parent.GetPlayerIndex());
                }
            }
        }
    }
}