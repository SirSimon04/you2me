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
#### App-Background:
App #17212B <br />
#### Chatlist-Background:
Chatlist #17212B <br />
Chatlist-Selected #202B36 <br />
#### Chat-Background:
Chat #0E1621 <br />
Chat-Message #2B5278 <br />
Chat-Message-Other #182533 <br />
#### Font-Colors
Sent-By-Color #48A1F4 <br />
Message-Color #7F91A4 <br />

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
Ideen für Einstellungen im frontend:
    -Dark/bright-Mode
    -Lesebenachrichtigungen einstellen/austellen
    -Speicher verwalten (gesendete Daten)
    - Sicherheit verwalten --> SIcherheitsbestätigung per E-Mail anfordern.
    - Account anpassen
    

## Datenbank

Create Table Foto(
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
    (START WITH 1, INCREMENT BY 1),
    base64 VARCHAR(32000)
);

Create Table blacklist(
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
    (START WITH 1, INCREMENT BY 1),
    token VARCHAR(200),
    timestamp Timestamp
);
CREATE TABLE Nutzer
(
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
    (START WITH 1, INCREMENT BY 1),
    Benutzername VARCHAR(50) NOT NULL UNIQUE,
    vorname VARCHAR(50),
    nachname VARCHAR(50),
    email VARCHAR(50) NOT NULL UNIQUE,
    passwordhash VARCHAR(255) NOT NULL,
    handynummer VARCHAR(50),
    profilbild int,
    info VARCHAR(256),
    isAdmin boolean,
    VerificationPin int,
    lastOnline varchar(30),
    isOnline boolean,
    mitgliedSeit int,
    
    foreign key(profilbild) references Foto(id) on delete cascade
);

CREATE TABLE Nachricht 
(
    nachrichtId INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
    (START WITH 1, INCREMENT BY 1),
    senderId INT NOT NULL,
    chatId INT NOT NULL,
    datumUhrzeit int not null,
    inhalt VARCHAR(1024) NOT NULL,
    foto int,
    antwortauf int,
    sender varchar(50),
    isImportant boolean,
    readByAll boolean,
    isPlanned boolean,
    
    foreign key(foto) references Foto(id),
    foreign key(antwortauf) references Nachricht(nachrichtId)
);

CREATE TABLE Chat 
(
    chatId INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY
    (START WITH 1, INCREMENT BY 1),
    erstelldatum int,
    name VARCHAR(50),
    beschreibung VARCHAR(100),
    profilbild int,
    letzteNachricht int,
    isGroup boolean,
    isBlocked boolean,
    gotBlocked boolean,
    nNew integer,
    
    foreign key(profilbild) references Foto(id),
    foreign key(letzteNachricht) references Nachricht(nachrichtId)
);

create table NimmtTeil (
    chatId integer not null,
    nutzerId integer not null,

    primary Key(chatId, nutzerId),

    foreign key(chatId) references Chat(ChatId) on delete cascade,
    
    foreign key(nutzerId) references Nutzer(id) on delete cascade
);

Create Table BefreundetMit( 
    nutzer1Id integer not null,
    nutzer2Id integer not null,
   
    foreign Key(nutzer1Id) references Nutzer(id) on delete cascade,
    foreign Key(nutzer2Id) references Nutzer(id) on delete cascade
    
);

Create Table isAdmin(
    nutzerId integer not null,
    chatid integer not null,
    
    foreign Key(nutzerId) references Nutzer(id) on delete cascade,
    foreign Key(chatId) references Chat(chatid) on delete cascade
);

Create Table hatBlockiert(
    nutzer1Id integer not null,
    nutzer2Id integer not null,
   
    foreign Key(nutzer1Id) references Nutzer(id) on delete cascade,
    foreign Key(nutzer2Id) references Nutzer(id) on delete cascade
);

Create Table hatGelesen(
	nutzerId integer not null,
	nachrichtId integer not null,
	foreign Key(nutzerId) references Nutzer(id) on delete cascade,
	foreign Key(nachrichtId) references Nachricht(nachrichtId) on delete cascade
);

Create Table Setting(
	nutzerId int not null primary key,
	darkmode boolean,
	lesebestätigung boolean,
	mailIfImportant boolean,
	foreign Key(nutzerId) references Nutzer(id) on delete cascade
);


INSERT INTO ROOT.NUTZER (BENUTZERNAME, VORNAME, NACHNAME, EMAIL, PASSWORDHASH, HANDYNUMMER, PROFILBILD, INFO, ISADMIN) 
	VALUES ('MailAuth', Null, Null, 'simiquatsch1@gmail.com', 'Simi272727', NULL, NULL, NULL, NULL);

INSERT INTO ROOT.NUTZER (BENUTZERNAME, VORNAME, NACHNAME, EMAIL, PASSWORDHASH, HANDYNUMMER, PROFILBILD, INFO, ISADMIN) 
	VALUES ('Simon', NULL, NULL, 'se@abc.de', 'db3b64d143fb02fe1f97a008a6364366a14ecdd5d84a98551bca8b4db4a81d3bfb7fbbbfb9a18f80a1ba6d74ce6e672abe16786820c8de282e1e7e4f092b6066', NULL, NULL, NULL, NULL);
INSERT INTO ROOT.NUTZER (BENUTZERNAME, VORNAME, NACHNAME, EMAIL, PASSWORDHASH, HANDYNUMMER, PROFILBILD, INFO, ISADMIN) 
	VALUES ('Jet3141', NULL, NULL, 'jet@abc.de', 'db3b64d143fb02fe1f97a008a6364366a14ecdd5d84a98551bca8b4db4a81d3bfb7fbbbfb9a18f80a1ba6d74ce6e672abe16786820c8de282e1e7e4f092b6066', NULL, NULL, NULL, NULL);
INSERT INTO ROOT.NUTZER (BENUTZERNAME, VORNAME, NACHNAME, EMAIL, PASSWORDHASH, HANDYNUMMER, PROFILBILD, INFO, ISADMIN) 
	VALUES ('NoSkiller', NULL, NULL, 'no@abc.de', 'db3b64d143fb02fe1f97a008a6364366a14ecdd5d84a98551bca8b4db4a81d3bfb7fbbbfb9a18f80a1ba6d74ce6e672abe16786820c8de282e1e7e4f092b6066', NULL, NULL, NULL, NULL);

#### Things to do after Recreating of Entities
alle Listen aus Foto löschen
Listen in Nutzer hinzufügen

