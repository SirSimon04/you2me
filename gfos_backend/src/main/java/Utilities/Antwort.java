package Utilities;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

 

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