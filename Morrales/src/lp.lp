/* Objective function */
min: +C1 +C2;

/* Constraints */
+C3 +C4 = 1;
+C5 +C6 = 1;
+6 C3 +2 C5 <= 8;
+10 C3 +5 C5 <= 15;
+6 C4 +2 C6 <= 8;
+10 C4 +5 C6 <= 15;
-1000 C1 +C3 +C5 <= 0;
-1000 C2 +C4 +C6 <= 0;

/* Variable bounds */
C1 <= 1;
C2 <= 1;
C3 <= 1;
C4 <= 1;
C5 <= 1;
C6 <= 1;

/* Integer definitions */
int C1,C2,C3,C4,C5,C6;
