/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileService;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

/**
 * <h1>Die Klasse zum Schreiben in Dateien</h1>
 *
 * @author simon
 */
public class WriteToFile{

    public WriteToFile(){
    }

    /**
     * Diese Mtehode schreibt einen gegebenen Text in eine vorhandene Datei.
     *
     * @param filename Die Datei, in die etwas geschrieben werden soll
     * @param data Der Inhalt, der in die Datei geschrieben werden soll
     * @return Status der Methode
     */
    public boolean write(String filename, String data){
        try{
            System.out.println("Writing...");
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(data);
            myWriter.close();
            System.out.println("Finished Writing");
            return true;
        }catch(IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }
}
