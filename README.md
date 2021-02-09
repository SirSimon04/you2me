# gfos_messenger
The messenger for the GFOS-Contest

## Database
Name: db; <br />
User: root; <br />
Password: root; <br />

### Tabellen:      
Nutzer(int id primary key, String vorname length 50, String nachname length 50)

### Container: 
Anmelden/Registrieren <br />
Letzte Chats <br />
Chats <br />

### Colors:
Chatbackground #131C21 <br />
Selected-Chatbackground #32373A <br />
DateBackground #1F2932 <br />
Notification #085373 <br />
Message-Background #252D30 <br />
Navigationbars #1F2428 <br />

### Links
Colors: https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fmspoweruser.com%2Fwp-content%2Fuploads%2F2020%2F07%2Fdark-mode-whatsapp-web-2.jpg&f=1&nofb=1 <br />
Database: https://cdn.discordapp.com/attachments/801474474158325760/804453683517587486/unknown.png

### Routen: 
ip:8080/gfos/daten
#### GET
letzte Chats, <br />
alle Nachrichten aus einem Chat,- /nachricht/chat/{id}<br />
alle Nachrichten - /nachricht <br />
Suchen nach Benutzername - /nutzer/getUsernameById <br />
alle Nutzer, - /nutzer<br />
Nutzer nach Id, - /nutzer/id/{id}<br />
Nutzernamen nach id - /nutzer/getUsernameById/{id}<br />

#### Post
Nachricht senden, - /nachricht/add<br />
Nutzer erstellen - /nutzer/add <br />
Login - /nutzer/login <br />
Chat erstellen - /chat/add <br />
Nutzer zu Chat hinzufügen - /chat/takepart <br />

#### Put
Account updaten - /nutzer/update<br />

#### Delete
Account löschen - <br />
evtl: Nachricht löschen <br />


### Ideen
E-Mail Verifikation
 
