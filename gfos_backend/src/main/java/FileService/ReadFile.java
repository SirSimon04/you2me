/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileService;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

/**
 * <h1>Die Klasse zum Auslesen von Dateien</h1>
 *
 * @author simon
 */
public class ReadFile{

    public ReadFile(){
    }

    /**
     * Diese Methode liest den Inhalt einer gegebenen Datei aus.
     *
     * @param filename Der Name der auszulesenden Datei
     * @return Der Inhalt der Datei, wenn vorhanden
     */
    public String read(String filename){
        try{
            System.out.println("Reading...");
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while(myReader.hasNextLine()){
                String data = myReader.nextLine();
                return data;
            }
            myReader.close();
        }catch(FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "";
        }
        return "";
    }
}
