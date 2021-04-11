/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * <h1>Die Klasse zum Verwalten von Dateien, die von Nutzern gesendet werden.</h1>
 *
 * @author simon
 */
public class FileService{

    public FileService(){
    }

    /**
     * Diese Methode erstellt eine Datei mit einem gegebenen Namen. Der Dateityp
     * wird durch die Endung des übergebenen Namens festgelegt.
     *
     * @param s Der Dateiname
     * @return Status der Methode
     */
    public boolean create(String s){
        System.out.println("hey");
        try{
            System.out.println("Try");
            System.out.println("Creating File...");
            File myObj = new File(s);
            if(myObj.createNewFile()){
                System.out.println("true");
                return true;
            }else{
                System.out.println("False");
                return false;
            }
        }catch(IOException e){
            System.out.println("An error occurred.");
            return false;
        }
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
