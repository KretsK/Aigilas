﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SPX.Core;

namespace SPX.Paths
{
    class PathFactory
    {
        private static readonly Path[] __paths = new Path[50];
        private static int __pathIndex = 0;

        public static Path Create(Point2 source,Point2 dest)
        {
            return GetNext().Reset(source, dest);
        }

        public static Path Create(Path path)
        {
            return GetNext().Copy(path);
        }

        private static Path GetNext()
        {
            if (__paths[0] == null)
            {
                for (int ii = 0; ii < __paths.Length; ii++)
                {
                    __paths[ii] = new Path();
                }
            }
            __pathIndex = (++__pathIndex) % __paths.Length;
            return __paths[__pathIndex];
        }
    }
}
