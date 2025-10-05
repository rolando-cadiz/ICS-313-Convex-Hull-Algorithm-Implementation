import java.util.*;

class Point implements Comparable<Point> {
    double x, y;
    Point(double x, double y) { this.x = x; this.y = y; }

    public int compareTo(Point p) {
        if (this.y == p.y) return Double.compare(this.x, p.x);
        return Double.compare(this.y, p.y);
    }
}

public class ConvexHullAlgorithm {

    //the cross product of vectors ab and ac. we only need three points to determine angle direction
    private static double crossProduct(Point a, Point b, Point c) {
        return (b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x);
    }
    //squared distance between two points in case of collinear points
    private static double distanceSq(Point a, Point b) {
        double dx = a.x - b.x, dy = a.y - b.y;
        return dx*dx + dy*dy;
    }

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

    public static void main(String[] args) {
        List<Point> points = Arrays.asList(
            new Point(0.004, 5), 
            new Point(2.4, 22.4), 
            new Point(67, 69),
            new Point(453.123, 123.123), 
            new Point(3, 0), 
            new Point(0, 3),
            new Point(5, 3.4),
            new Point(8.9, 7.5),
            new Point(2.1, 99.99),
            new Point(100, 55)
        
        );

        List<Point> hull = grahamScan(new ArrayList<>(points));
        System.out.println("Convex Hull:");
        for (Point p : hull)
            System.out.println("(" + p.x + ", " + p.y + ")");
    }
}
