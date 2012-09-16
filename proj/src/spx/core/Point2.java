package spx.core;

import java.util.ArrayList;
import java.util.List;

public class Point2 {
    public static Point2 Zero = new Point2(0, 0);
    private static float halfHeight = Settings.Get().spriteHeight / 2;
    private static float halfWidth = Settings.Get().spriteWidth / 2;

    public float X;
    public float Y;
    public float Weight;
    public int GridX;
    public int GridY;
    public float PosX;
    public float PosY;
    public float PosCenterX;
    public float PosCenterY;

    public static Point2[] _rotateTargets = {new Point2(1, 0), new Point2(1, 1), new Point2(0, 1), new Point2(0, -1), new Point2(-1, -1), new Point2(-1, 0), new Point2(-1, 1), new Point2(1, -1)};

    public static Point2[][] _locations = new Point2[Settings.Get().tileMapHeight][Settings.Get().tileMapWidth];

    public Point2() {
    }

    public Point2(float x, float y, int weight) {
        initThis(x, y, weight);
    }

    public Point2(Point2 target) {
        initThis(target.X, target.Y, 0);
    }

    public Point2(int x, int y) {
        initThis(x, y, 0);
    }

    public Point2(float x, float y) {
        initThis(x, y, 0);
    }

    private void initThis(float x, float y, int weight) {
        SetX(x);
        SetY(y);
        Weight = weight;
    }

    public void Reset(float x, float y) {
        SetX(x);
        SetY(y);
    }

    public void Copy(Point2 point) {
        if (point != null) {
            SetX(point.X);
            SetY(point.Y);
        }
    }

    public Point2 Add(Point2 target) {
        return new Point2(GridX + target.GridX, GridY + target.GridY);
    }

    public Point2 Add(int dX, int dY) {
        return new Point2(X + dX, Y + dY);
    }

    public Point2 Minus(Point2 target) {
        return new Point2(GridX - target.GridX, GridY - target.GridY);
    }

    @Override
    public int hashCode() {
        return GridX + 1000 * GridY;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Point2.class) {
            Point2 target = (Point2) obj;
            return target.GridX == GridX && target.GridY == GridY;
        }
        return false;
    }

    public void SetX(float xValue) {
        X = xValue;
        boolean isGrid = (Math.abs(X) < Settings.Get().tileMapWidth);
        PosX = (isGrid) ? X * Settings.Get().spriteWidth : X;
        PosCenterX = PosX + halfWidth;
        GridX = (isGrid) ? (int) X : (int) (X / Settings.Get().spriteWidth);
    }

    public void SetY(float yValue) {
        Y = yValue;
        boolean isGrid = (Math.abs(Y) < Settings.Get().tileMapWidth);
        PosY = (isGrid) ? Y * Settings.Get().spriteHeight : Y;
        PosCenterY = PosY + halfHeight;
        GridY = (isGrid) ? (int) Y : (int) (Y / Settings.Get().spriteHeight);
    }

    public void SetWeight(float weight) {
        Weight = weight;
    }

    public boolean IsZero() {
        return X == 0 && Y == 0;
    }

    public static float CalculateDistanceSquared(Point2 source, Point2 target) {
        return (float) (Math.pow(source.PosY - target.PosY, 2) + Math.pow(source.PosX - target.PosX, 2));
    }

    private static List<Point2> _neighbors = new ArrayList<Point2>();

    public List<Point2> GetNeighbors() {
        if (_locations[0][0] == null) {
            for (int ii = 0; ii < Settings.Get().tileMapHeight; ii++) {
                for (int jj = 0; jj < Settings.Get().tileMapWidth; jj++) {
                    _locations[ii][jj] = new Point2(jj, ii);
                }
            }
        }
        _neighbors.clear();
        for (int ii = -1; ii < 2; ii++) {
            for (int jj = -1; jj < 2; jj++) {
                if (ii != 0 || jj != 0) {
                    _neighbors.add(_locations[GridY + ii][GridX + jj]);
                }
            }
        }
        return _neighbors;
    }

    public boolean IsSameSpot(Point2 target) {
        return target.GridX == GridX && target.GridY == GridY;
    }

    public Point2 RotateClockwise() {
        if (GridX == 1) {
            if (GridY == -1) {
                return _rotateTargets[0];
            }
            if (GridY == 0) {
                return _rotateTargets[1];
            }
            if (GridY == 1) {
                return _rotateTargets[2];
            }
        }
        if (GridX == -1) {
            if (GridY == -1) {
                return _rotateTargets[3];
            }
            if (GridY == 0) {
                return _rotateTargets[4];
            }
            if (GridY == 1) {
                return _rotateTargets[5];
            }
        }
        if (GridX == 0) {
            if (GridY == 1) {
                return _rotateTargets[6];
            }
            if (GridY == -1) {
                return _rotateTargets[7];
            }
        }
        return Zero;
        /*
           * This is getting close, but the flipped Y coord is ticking me off. var
           * theta = Math.pI / 4f; var currentRotation = Math.atan2(-Y, X);
           * System.out.println(currentRotation); currentRotation -= theta; var x
           * = (float)Math.cos(currentRotation); var y =
           * (float)Math.sin(currentRotation); if(x!=0) { x = (1 / (Math.abs(x)))
           * * x; } if (y != 0) { y = (1 / (Math.abs(y))) * y; } var result = new
           * Point2(x,y); System.out.println(result); return result;
           */
    }

    public Point2 Rotate180() {
        return RotateClockwise().RotateClockwise().RotateClockwise().RotateClockwise();
    }

    public static float DistanceSquared(Point2 source, Point2 target) {
        return (float) (Math.pow(source.PosX - target.PosX, 2) + Math.pow(source.PosY - target.PosY, 2));
    }

    @Override
    public String toString() {
        return "(gX,gY) - (posX,posY) extends  (" + GridX + "," + GridY + ") - (" + PosX + "," + PosY + ")";
    }
}