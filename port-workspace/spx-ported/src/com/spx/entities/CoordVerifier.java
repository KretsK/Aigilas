package com.spx.entities;import com.spx.wrapper.*;import java.util.*;import com.spx.core.*;import com.spx.entities.*;
    public class CoordVerifier
    {
        public static boolean IsValid(Point2 position)
        {
            return (position.PosX >= 0 && position.PosY >= 0 && position.PosX < XnaManager.WindowWidth && position.PosY < XnaManager.WindowHeight);
        }

        public static boolean IsBlocked(Point2 target)
        {
            return EntityManager.IsLocationBlocked(target);
        }   

        public static boolean contains(Point2 target, int type)
        {
            return EntityManager.AnyContains(target, type);
        }
    }
