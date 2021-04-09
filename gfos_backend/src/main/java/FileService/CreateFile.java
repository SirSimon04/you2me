/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileService;

import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors

public class CreateFile{

    public CreateFile(){
    }

    public boolean create(String s){
        try{
            System.out.println("Creating File...");
            File myObj = new File(s);
            if(myObj.createNewFile()){
                return true;
            }else{
                return false;
            }
        }catch(IOException e){
            System.out.println("An error occurred.");
            return false;
        }
    }
}
