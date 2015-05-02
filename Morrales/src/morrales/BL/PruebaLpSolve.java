/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package morrales.BL;

import lpsolve.*;

/**
 *
 * @author Edwin Gamboa
 */
public class PruebaLpSolve {

    public static void main(String[] args) {
        try {
            // Create a problem with 4 variables and 0 constraints
            LpSolve solver = LpSolve.makeLp(0, 6);

            // add constraints
            solver.strAddConstraint("0 0 1 1 0 0", LpSolve.EQ, 1);
            solver.strAddConstraint("0 0 0 0 1 1", LpSolve.EQ, 1);
            solver.strAddConstraint("0 0 10 0 5 0", LpSolve.LE, 15);
            solver.strAddConstraint("0 0 0 10 0 5", LpSolve.LE, 15);
            solver.strAddConstraint("0 0 6 0 2 0", LpSolve.LE, 8);
            solver.strAddConstraint("0 0 0 6 0 2", LpSolve.LE, 8);
            solver.strAddConstraint("-10000 0 1 0 1 0", LpSolve.LE, 0);
            solver.strAddConstraint("0 -10000 0 1 0 1", LpSolve.LE, 0);

            // set objective function
            solver.strSetObjFn("1 1 0 0 0 0");

            //Variables binarias
            int numColumns = solver.getNcolumns();
            for (int i = 1; i <= numColumns; i++) {
                solver.setBinary(i, true);
            }
            

            //Maximize
            //solver.setMaxim();
            // solve the problem
            solver.solve();
            
            
            // print solution
            System.out.println("Value of objective function: " + solver.getObjective());
            double[] var = solver.getPtrVariables();
            for (int i = 0; i < var.length; i++) {
                System.out.println("Value of var[" + i + "] = " + var[i]);
            }
            solver.printLp();
            solver.printSolution(1);
            solver.printObjective();
            solver.printConstraints(1);
            solver.writeLp("src/lp.lp");

            // delete the problem and free memory
            solver.deleteLp();
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
    }

}
