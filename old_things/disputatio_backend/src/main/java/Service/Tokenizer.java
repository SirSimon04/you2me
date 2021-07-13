/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import EJB.NutzerEJB;
import Entity.Nutzer;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * <h1>Die Java-Klassse zur Identifizierung und Authentifizierung der
 * Nutzer.</h1>
 * <p>
 * Diese Klasse erstellt ein neues Webtoken, welches die Identifizierung und
 * Authentifizierung der einzelnen Nutzer ermöglicht. Die erstellten Token sind
 * dabei nur begrenzt gültig und realisiert das automatische Beenden der Sitzung
 * nach einem definierten Zeitpunkt. Das Token wird bei Anfrage des Webservers
 * übergeben.</p>
 *
 * @author simon
 */
@Stateless
@LocalBean
public class Tokenizer{

    private final String SECRET = "As7FA2df!-,.8Gg345ms/dh(65hj"; // TOPSECRET!
    private final long DT = 12000000; // Token 120 Sekunden gültig
    private final boolean STATUS = true;

    /**
     * Diese Methode erstellt ein neues Token mit dem Nutzernamen des Nutzers.
     *
     * @param username Der Benutzername eines Nutzers
     * @return Das neue Token
     */
    public String createNewToken(String username){
        try{
            long t = (System.currentTimeMillis() / DT) * DT;
            Algorithm algorithm = Algorithm.HMAC256(SECRET + t);
            String token = JWT.create()
                    .withIssuer("GFOSProjekt")
                    .withSubject(username)
                    .sign(algorithm);
            return token;
        }catch(JWTCreationException exception){
            return "JWT-Creation failed.";
        }
    }

    /**
     * Diese Methode prüft ein Token auf seine Gültigkeit. Ist es gültig, wird
     * sein Gültigkeitszeitraum verlängert.
     *
     * @param token Das Webtoken
     * @return Neues Token oder Fehlermeldung
     */
    public String verifyToken(String token){
        try{
            long t = (System.currentTimeMillis() / DT) * DT;
            Algorithm algorithm = Algorithm.HMAC256(SECRET + t);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("GFOSProjekt")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return token; // das gültige Token wieder zurückgeben.
        }catch(JWTVerificationException ex1){
            try{
                // Wenn altes Token gerade (innerhalb eines Zeitfensters von
                // 2*DT) abgelaufen, automatisch um DT Milisekunden erneuern:
                long t = (System.currentTimeMillis() / DT) * DT - DT;
                Algorithm algorithm = Algorithm.HMAC256(SECRET + t);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("GFOSProjekt")
                        .build();
                DecodedJWT jwt = verifier.verify(token);
                return this.createNewToken(jwt.getSubject());
            }catch(JWTVerificationException ex2){
                return ""; // altes Token zu lange (> 2*DT) abgelaufen
            }
        }
    }

    /**
     * Dise Methode gibt den zu einem Token passenden Nutzernamen wieder.
     *
     * @param token Das Token
     * @return Der Benutzername
     */
    public String getUser(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        }catch(JWTDecodeException exception){
            return "";
        }
    }

    /**
     * Diese Methode gibt an, ob der Toeknizer an- oder ausgeschaltet ist.
     *
     * @return Boolean
     */
    public boolean isOn(){
        return STATUS;
    }
}
