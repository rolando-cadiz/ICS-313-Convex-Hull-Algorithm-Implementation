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


   # Scheme Implementation of Graham Scan Convex Hull Algorithm
In Scheme, the convex hull algorithm, such as Graham’s scan, would be written using pure functions and immutable data, unlike Java’s mutable objects and explicit loops. Points could be represented as pairs (x, y) instead of class instances, and iteration would be replaced by recursion. A function like 
(cross a b c) would compute the cross product directly, returning a numeric value without side effects. The lowest point p0 would be found with a higher-order function. Sorting by angle would use a comparator function passed to (sort ...) like Java’s P.sort(...), but it creates a new list instead of modifying        the old one. The actual scan and construction part of the code would recursively “push” and “pop” points by returning new lists, not altering a stack in place. Compared to Java, Scheme’s implementation feels more mathematical (hence a functional language). You describe how something will be transformed and create     new items based on these instructions.<br>

Coding this algorithm in a functional language (like Scheme) is conceptually easy and intuitive since we are simply writing out mathematical relationships and transformations. This is useful for keeping the code clear and comprehensive. Since the data sets are immutable, there's less of a chance of side-effect type of bugs. The drawback, I think, would be the recursive nature of this paradigm; performance may take a hit, especially if we are looking to find the convex hull of a massive set of points.

                (define (graham-scan points)
                  (let* ((p0 (apply min points
                            (lambda (p1 p2)
                              (if (= (y p1) (y p2))
                                  (< (x p1) (x p2))
                                  (< (y p1) (y p2))))))
                         (sorted (sort points
                               (lambda (a b)
                                 (let ((angle (cross p0 a b)))
                                   (if (= angle 0)
                                       (< (x a) (x b))
                                       (> angle 0)))))))

  # Prolog Implementation of Graham Scan Convex Hull Algorithm
In Prolog, the convex hull algorithm is expressed through predicates, logical statements that define relationships between points instead of procedural instructions. Each predicate (i.e point(x,y), cross(a,b,c,value), or left_turn(a,b,c)) declares facts or rules that the compiler can use to set up new creations. The graham_scan predicate, for example, describes the relationship between the set of points and the resulting hull list, relying on other predicates to check sorting order, angles/orientation, etc. Instead of loops or conditionals, Prolog’s compiler uses backtracking and unification to explore all combinations that satisfy those predicates. In Java, the control flow is explicitly programmed using mutable stacks and conditionals, but in Prolog, control comes from the logical predicate definitions. While Java executes instructions step by step, Prolog chooses which configuration of points fulfills the definition of a convex hull.<br>

Logic languages are better suited for pathfinding problems or problems with constraints (i.e you define the relationships of parameters and their required I/O with predicates). The declarative nature of Prolog may help when writing out your predicates/functions/whatever you would like to call them (you can tell I don't know much about Prolog). You can keep the code clean as well, just like functional languages. As a downside, logical languages don't inherently know all the answers to a defined relationship and what values predicates are should return, therefore it will perform a sort of "brute-force" search where it picks and returns values based on the defined predicates. Obviously, this can really bog down performance when dealing with large data sets.

                % Base case: hull is built
                build_hull([], Acc, Acc).
                build_hull([P|Rest], [Top,Next|Stack], Hull) :-
                    cross(Next, Top, P, C), C =< 0, !,
                    build_hull([P|Rest], [Next|Stack], Hull).
                build_hull([P|Rest], Stack, Hull) :-
                    build_hull(Rest, [P|Stack], Hull).

                graham_scan(Points, Hull) :-
                    predsort(compare_angle, Points, Sorted),
                    build_hull(Sorted, [], Hull).
