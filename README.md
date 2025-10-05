# Java Implementation of Graham Scan Convex Hull Algorithm
This project implements the Graham Scan algorithm to find the convex hull of a set of 2D points in Java.
The convex hull is the smallest convex polygon that contains all points in a given dataset.

# Overview
The algorithm works by:
- Selecting the point with the lowest y-coordinate (and leftmost if tied) as the starting point p0.
- Sorting all other points based on the polar angle they make with p0.
- Iteratively constructing the convex hull using a stack, adding points that make a left turn, and removing points that make a right turn.
<br><br>This implementation demonstrates:
- Custom point comparison using Comparable.
- Sorting points by orientation via cross product.
- Handling collinear points with a distance-based tiebreaker.
- Constructing the hull in O(n log n) time.

## Point Class:
Represents a 2D point and implements Comparable<Point> for sorting by y (then x):
        class Point implements Comparable<Point> {
    double x, y;
    Point(double x, double y) { this.x = x; this.y = y; }

    public int compareTo(Point p) {
        if (this.y == p.y) return Double.compare(this.x, p.x);
        return Double.compare(this.y, p.y);
    }
}

## ConvexHullAlgorithm Class:
Implements the Graham Scan algorithm:

        grahamScan(List<Point> P) → returns a list of points on the hull in counterclockwise order.
        
        main() → sample test case to demonstrate usage.

# Function Methods:
## crossProduct(Point a, Point b, Point c)
Determines the orientation (left, right, or collinear) of three points:<br>

    crossProduct(a, b, c) = (b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)
  
- greater than 0 -> left turn
- less than 0 -> right turn
- equal to 0 -> collinear

## distanceSq(Point a, Point b)
In the case of collinear points, we choose the shortest distance from p0 to the collinear points. We forgo finding the square root of the distances to save computational power and because we don't explicitly care about the exact distance, just the relative distance (i.e, comparing 5 & 3 is the same as comparing 25 & 9).

        distanceSq(a, b) = (a.x - b.x)^2 + (a.y - b.y)^2

## grahamScan(List<Point> P)
The algorithm works by connecting edges along the outermost points in a counter-clockwise fashion, ensuring all points are within the boundary of the connecting edges.
<br> This is done by:
1. Choosing a pivot point (the lowest y-coordinate point).

2. Sorting all other points by the angle they make with this pivot.

3. Scanning through the sorted points and using the cross product to decide whether to “keep” or “discard” each point based on the turn direction.

           public static List<Point> grahamScan(List<Point> P) {
                int n = P.size();
                if (n < 3) return P;
        
                //p0 is the lowest y coordinate. if theres a tie, the left most point is p0.
                Point p0 = Collections.min(P);
        
                //sort by angle relative to p0 (pos,neg,zero)
                P.sort((a, b) -> {
                    double angle = crossProduct(p0, a, b);
                    if (angle == 0)  // means points are collinear so we must sort by distance
                        return Double.compare(distanceSq(p0, a), distanceSq(p0, b));
                    return -Double.compare(angle, 0); 
                });
        
                //we push points onto the stack and pop when we make a right turn. left turns are kept as part of the hull
                Stack<Point> S = new Stack<>();
                S.push(P.get(0));
                S.push(P.get(1));
                S.push(P.get(2));
        
                for (int i = 3; i < n; i++) {
                    while (S.size() >= 2) {
                        Point top = S.pop();
                        Point next = S.peek();
                        if (crossProduct(next, top, P.get(i)) > 0) { // > 0 means left turn
                            S.push(top);
                            break;
                        }
                    }
                    S.push(P.get(i));
                }
        
                return new ArrayList<>(S);
            }
