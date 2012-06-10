﻿using System;
using SPX.Entities;
using SPX.Core;

namespace Aigilas.Entities
{
    public class EntityFactory
    {
        public static Entity Create(int type, Point2 location)
        {
            switch (type)
            {
                case Aigilas.EntityType.FLOOR:
                    return new Floor(location);
                case Aigilas.EntityType.WALL:
                    return new Wall(location);
                case Aigilas.EntityType.DOWNSTAIRS:
                    return new Downstairs(location);
                case Aigilas.EntityType.UPSTAIRS:
                    return new Upstairs(location);
                default:
                    throw new Exception("An undefined int case was passed into the EntityFactory.");
            }
        }
    }
}