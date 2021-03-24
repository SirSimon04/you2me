package Utilities;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

 
/**
 * <h1>Die Klasse zum Erstellen von Response-Objekten</h1>
 * <p>Diese Klasse erstellt mit den gegeben Daten ein Response-Objekt zur fehlerfreien
 * Datenübertragung an das Frontend</p>
 * @author simon
 */
@Stateless
@LocalBean
public class Antwort {  
    /**
     * Diese Methode bildet mit dem gegebenem Wert ein Antwortobjekt für die Kommunikation mit dem Frontend.
     * Hier wird zusätzlich CORS im Header des Objektes erlaubt und der Status 200 gesetzt.
     * @param data Die zu übermittelnden Daten.
     * @return Reponseobjekt (200).
     */
    public Response generiereAntwort(String data){
        return Response
            .status(200)
            .header("Access-Control-Allow-Origin","*")
            .header("Access-Control-Allow-Credentials","true")
            .header("Access-Control-Allow-Headers",
                    "origin, content-type, accept, authorization")
            .header("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD")
            .entity(data)
            .allow("OPTIONS")
            .build();
    }
    
    /**
     * Diese Methode bildet mit dem gegebenem Wert ein Antwortobjekt für die Kommunikation mit dem Frontend.
     * Hier wird zusätzlich CORS im Header des Objektes erlaubt und der Status 401 gesetzt.
     * Dies ist für den Fall eines ungültigen Webtokens.
     * @param data Die zu übermittelnden Daten.
     * @return Reponseobjekt (401).
     */
    public Response generiereFehler401(String data){
        return Response
            .status(401)
            .header("Access-Control-Allow-Origin","*")
            .header("Access-Control-Allow-Credentials","true")
            .header("Access-Control-Allow-Headers",
                    "origin, content-type, accept, authorization")
            .header("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD")
            .entity(data)
            .build();
    }
    /**
     * Diese Methode bildet mit dem gegebenem Wert ein Antwortobjekt für die Kommunikation mit dem Frontend.
     * Hier wird zusätzlich CORS im Header des Objektes erlaubt und der Status 406 gesetzt.
     * Dies ist für den Fall von falschen beziehungsweise nicht gültigen Nutzereingaben.
     * @param data Die zu übermittelnden Daten.
     * @return Reponseobjekt (406).
     */
    public Response generiereFehler406(String data){
        return Response
            .status(406)
            .header("Access-Control-Allow-Origin","*")
            .header("Access-Control-Allow-Credentials","true")
            .header("Access-Control-Allow-Headers",
                    "origin, content-type, accept, authorization")
            .header("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD")
            .entity(data)
            .build();
    }
    /**
     * Diese Methode bildet mit dem gegebenem Wert ein Antwortobjekt für die Kommunikation mit dem Frontend.
     * Hier wird zusätzlich CORS im Header des Objektes erlaubt und der Status 500 gesetzt.
     * Dies ist für den Fall eines unerwarteten Server-Fehlers.
     * @param data Die zu übermittelnden Daten.
     * @return Reponseobjekt (500).
     */
    public Response generiereFehler500(String data){
        return Response
            .status(406)
            .header("Access-Control-Allow-Origin","*")
            .header("Access-Control-Allow-Credentials","true")
            .header("Access-Control-Allow-Headers",
                    "origin, content-type, accept, authorization")
            .header("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD")
            .entity(data)
            .build();
    }
}