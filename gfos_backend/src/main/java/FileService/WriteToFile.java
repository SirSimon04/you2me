/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileService;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class WriteToFile{

    public WriteToFile(){
    }

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
