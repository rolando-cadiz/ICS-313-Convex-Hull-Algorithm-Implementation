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

# Function Methods
## CrossProduct(Point a, Point b, Point c)
Determines the orientation (left, right, or collinear) of three points:<br>
- > 0 -> left turn
- < 0 -> right turn
- = 0 -> collinear
