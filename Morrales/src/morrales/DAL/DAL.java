/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package morrales.DAL;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/**
 *
 * @author Edwin Gamboa
 */
public class DAL {

    
    public ArrayList<Double> leerTextoArchivo(String rutaArchivo) {



        FileReader archivo = null;
        String linea = "";
       

        ArrayList<Double> datos = new ArrayList<Double>();

        try {

            archivo = new FileReader(rutaArchivo); //lee el archivo
            BufferedReader lector = new BufferedReader(archivo);
            
            linea = lector.readLine();
            datos.add(Double.parseDouble(linea)); 
            
             linea = lector.readLine();
            datos.add(Double.parseDouble(linea)); 
            
             linea = lector.readLine();
            datos.add(Double.parseDouble(linea)); 
            
            
           while ((linea = lector.readLine()) != null) {
               
                  
                   
                    datos.add(Double.parseDouble(linea.split(" ")[1]));
                    datos.add(Double.parseDouble(linea.split(" ")[2]));
               

               
            }
        } catch (FileNotFoundException e) {

            JOptionPane.showMessageDialog(null, "Archivo no encontrado");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error de I/O");
        } finally {
            if (archivo != null) {
                try {
                    archivo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datos;
    }
}
