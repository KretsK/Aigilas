﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SPX.Core;

namespace SPX.Paths
{
    class Walk
    {
        private static List<Point2>[] __walks = new List<Point2>[1000];
        private static int _index = 0;

        public static List<Point2> Copy(List<Point2> walk)
        {
            if (__walks[0] == null)
            {
                for (int ii = 0; ii < __walks.Count(); ii++)
                {
                    __walks[ii] = new List<Point2>();
                }
            }
            _index = (_index + 1) % __walks.Count();
            __walks[_index].Clear();
            foreach (var point in walk)
            {
                __walks[_index].Add(point);
            }
            return __walks[_index];
        }
    }
}