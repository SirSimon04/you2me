/**
 * Diese Klassen sind für das Filtern von unangebrachten Nachrichten
 * zuständig.
 */
package Service;

import Filter.Grawlox;
import java.io.File;
import java.io.IOException;

/**
 * <h1>Die Klasse zum Filtern von Nachrichten.</h1>
 * <p>
 * Diese Klasse beinhaltet alle Methoden, die von den Webserviceklassen
 * aufgerufen wird, falls ein Nutzer seinen Schimpfwortfilter in den Einstellungen aktiviert hat.</p>
 *
 * @author simon
 */
public class Filter{

    String path = "/swearwords.txt";
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(path).getFile());

    public Filter(){

    }

    /**
     * Diese Methode filtert einen eingegebenen Text. Das bedeutet, dass Wörter,
     * die in der Liste mit verbotenen Wörtern stehen, mit Sternchen überschrieben werden.
     *
     * @param s Der zu filternde Text
     * @return Den gefilterten Text
     * @throws IOException
     */
    public String filter(String s) throws IOException{
        Grawlox grawlox = Grawlox.createFromDefault(this.file);
        return grawlox.filter(s);
    }

    /**
     * Diese Methode überprüft, ob ein Text ein nicht zugelassenes Wort enthält.
     *
     * @param s Der zu überprüfende Text
     * @return Der Status, ob ein verbotenes Wort vorhanden ist.
     * @throws IOException
     */
    public boolean isProfane(String s) throws IOException{
        Grawlox grawlox = Grawlox.createFromDefault(this.file);
        return grawlox.isProfane(s);
    }
}
