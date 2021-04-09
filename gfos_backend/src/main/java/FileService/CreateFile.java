/**
 * Diese Klassen sind für das Verwalten von Dateien
 * verantwortlich.
 */
package FileService;

import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors

/**
 * <h1>Die Klasse zum Erstellen von Nachrichten</h1>
 *
 * @author simon
 */
public class CreateFile{

    public CreateFile(){
    }

    /**
     * Diese Methode erstellt eine Datei mit einem gegebenen Namen. Der Dateityp
     * wird durch die Endung des übergebenen Namens festgelegt.
     *
     * @param s Der Dateiname
     * @return Status der Methode
     */
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
