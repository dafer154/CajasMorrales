/* Objective function */
min: +C1 +C2;

/* Constraints */
+C3 +C4 = 1;
+C5 +C6 = 1;
+6.098 C3 +2.987 C5 <= 7.9876;
+10.987 C3 +5.988 C5 <= 15.456;
+6.098 C4 +2.987 C6 <= 7.9876;
+10.987 C4 +5.988 C6 <= 15.456;
-1000000 C1 +C3 +C5 <= 0;
-1000000 C2 +C4 +C6 <= 0;

/* Variable bounds */
C1 <= 1;
C2 <= 1;
C3 <= 1;
C4 <= 1;
C5 <= 1;
C6 <= 1;

/* Integer definitions */
int C1,C2,C3,C4,C5,C6;
