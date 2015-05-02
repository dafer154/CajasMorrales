/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/**
 *
 * @author Edwin Gamboa
 */
public class DAL {

    
    public ArrayList<String> leerTextoArchivo(String rutaArchivo) {



        FileReader archivo = null;
        String linea = "";

        ArrayList<String> datos = new ArrayList<String>();

        try {

            archivo = new FileReader(rutaArchivo); //lee el archivo
            BufferedReader lector = new BufferedReader(archivo);



            while ((linea = lector.readLine()) != null) {

                datos.add(linea);
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
