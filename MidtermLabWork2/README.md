# Programming Assignment 1 - 3x3 Matrix Determinant Solver

## Student Information
- Student Name: MALANO, JAZAN KIERR J.
- Course: Math 101 - Linear Algebra
- School: University of Perpetual Help System DALTA, Molino Campus
- Date Completed: March 16, 2026
- Github Repo: https://github.com/[your-username]/uphsd-cs-malano-jazankierr

## Assigned Matrix
\[
M = \begin{bmatrix}
1 & 2 & 4 \\
3 & 5 & 6 \\
2 & 4 & 8
\end{bmatrix}
\]

## Files Included
- DeterminantSolver.java
- determinant_solver.js

## How to Run
### Java
```
javac DeterminantSolver.java
java DeterminantSolver
```

### JavaScript (Node.js)
```
node determinant_solver.js
```

## Final Determinant Value
- det(M) = 0 (SINGULAR - the matrix has no inverse)

## Sample Output (Java and JavaScript)
```text
=======================================================
  3x3 MATRIX DETERMINANT SOLVER
  Student: MALANO, JAZAN KIERR J.
  Assigned Matrix:
=======================================================
  |  1  2  4 |
  |  3  5  6 |
  |  2  4  8 |
=======================================================

Expanding along Row 1 (cofactor expansion):

  Step 1 - Minor M11: det([5,6],[4,8]) = (5*8) - (6*4) = 40 - 24 = 16
  Step 2 - Minor M12: det([3,6],[2,8]) = (3*8) - (6*2) = 24 - 12 = 12
  Step 3 - Minor M13: det([3,5],[2,4]) = (3*4) - (5*2) = 12 - 10 = 2

  Cofactor C11 = (+1) * 1 * 16 = 16
  Cofactor C12 = (-1) * 2 * 12 = -24
  Cofactor C13 = (+1) * 4 * 2 = 8

  det(M) = 16 + (-24) + 8
=======================================================
  DETERMINANT = 0
  The matrix is SINGULAR - it has no inverse.
=======================================================
```
