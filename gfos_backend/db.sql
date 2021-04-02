CREATE TABLE FOTO (ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), BASE64 VARCHAR(32000), PRIMARY KEY (ID));

CREATE TABLE BLACKLIST (ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), TOKEN VARCHAR(200), "TIMESTAMP" TIMESTAMP, PRIMARY KEY (ID));

CREATE TABLE NUTZER (ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), BENUTZERNAME VARCHAR(50) NOT NULL, VORNAME VARCHAR(50), NACHNAME VARCHAR(50), EMAIL VARCHAR(50) NOT NULL, PASSWORDHASH VARCHAR(255) NOT NULL, HANDYNUMMER VARCHAR(50), PROFILBILD INTEGER, INFO VARCHAR(256), ISADMIN BOOLEAN, VERIFICATIONPIN INTEGER, LASTONLINE BIGINT, ISONLINE BOOLEAN, MITGLIEDSEIT INTEGER, PRIMARY KEY (ID));

CREATE TABLE CHAT (CHATID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), ERSTELLDATUM BIGINT, "NAME" VARCHAR(50), BESCHREIBUNG VARCHAR(100), PROFILBILD INTEGER, LETZTENACHRICHT INTEGER, ISGROUP BOOLEAN, ISBLOCKED BOOLEAN, GOTBLOCKED BOOLEAN, NNEW INTEGER, PRIMARY KEY (CHATID));

CREATE TABLE NACHRICHT (NACHRICHTID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), SENDERID INTEGER NOT NULL, CHATID INTEGER NOT NULL, DATUMUHRZEIT BIGINT NOT NULL, INHALT VARCHAR(1024) NOT NULL, FOTO INTEGER, ANTWORTAUF INTEGER, SENDER VARCHAR(50), ISIMPORTANT BOOLEAN, READBYALL BOOLEAN, ISPLANNED BOOLEAN, PRIMARY KEY (NACHRICHTID));

Create Table Setting( nutzerId int not null primary key, darkmode boolean, lesebestätigung boolean, mailIfImportant boolean, foreign Key(nutzerId) references Nutzer(id) on delete cascade );

CREATE TABLE NIMMTTEIL (CHATID INTEGER NOT NULL, NUTZERID INTEGER NOT NULL, foreign key(chatId) references Chat(ChatId) on delete cascade, foreign key(nutzerId) references Nutzer(id) on delete cascade);

Create Table BefreundetMit( nutzer1Id integer not null, nutzer2Id integer not null, foreign Key(nutzer1Id) references Nutzer(id) on delete cascade,foreign Key(nutzer2Id) references Nutzer(id) on delete cascade);

Create Table isAdmin( nutzerId integer not null, chatid integer not null,foreign Key(nutzerId) references Nutzer(id) on delete cascade,foreign Key(chatId) references Chat(chatid) on delete cascade);

Create Table hatBlockiert( nutzer1Id integer not null, nutzer2Id integer not null, foreign Key(nutzer1Id) references Nutzer(id) on delete cascade,foreign Key(nutzer2Id) references Nutzer(id) on delete cascade);

Create Table hatGelesen( nutzerId integer not null, nachrichtId integer not null, foreign Key(nutzerId) references Nutzer(id) on delete cascade, foreign Key(nachrichtId) references Nachricht(nachrichtId) on delete cascade );

INSERT INTO ROOT.NUTZER (BENUTZERNAME, VORNAME, NACHNAME, EMAIL, PASSWORDHASH, HANDYNUMMER, PROFILBILD, INFO, ISADMIN, VERIFICATIONPIN, LASTONLINE, ISONLINE, MITGLIEDSEIT) VALUES ('MailAuth', NULL, NULL, 'simiquatsch1@gmail.com', 'Simi272727', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO ROOT.NUTZER (BENUTZERNAME, VORNAME, NACHNAME, EMAIL, PASSWORDHASH, HANDYNUMMER, PROFILBILD, INFO, ISADMIN) VALUES ('Simon', NULL, NULL, 'se@abc.de', 'db3b64d143fb02fe1f97a008a6364366a14ecdd5d84a98551bca8b4db4a81d3bfb7fbbbfb9a18f80a1ba6d74ce6e672abe16786820c8de282e1e7e4f092b6066', NULL, NULL, NULL, NULL); 
INSERT INTO ROOT.NUTZER (BENUTZERNAME, VORNAME, NACHNAME, EMAIL, PASSWORDHASH, HANDYNUMMER, PROFILBILD, INFO, ISADMIN) VALUES ('Jet3141', NULL, NULL, 'jet@abc.de', 'db3b64d143fb02fe1f97a008a6364366a14ecdd5d84a98551bca8b4db4a81d3bfb7fbbbfb9a18f80a1ba6d74ce6e672abe16786820c8de282e1e7e4f092b6066', NULL, NULL, NULL, NULL);
INSERT INTO ROOT.NUTZER (BENUTZERNAME, VORNAME, NACHNAME, EMAIL, PASSWORDHASH, HANDYNUMMER, PROFILBILD, INFO, ISADMIN) VALUES ('NoSkiller', NULL, NULL, 'no@abc.de', 'db3b64d143fb02fe1f97a008a6364366a14ecdd5d84a98551bca8b4db4a81d3bfb7fbbbfb9a18f80a1ba6d74ce6e672abe16786820c8de282e1e7e4f092b6066', NULL, NULL, NULL, NULL);

INSERT INTO ROOT.SETTING (NUTZERID, DARKMODE, "LESEBESTÄTIGUNG", MAILIFIMPORTANT) 
	VALUES (1, true, true, true);
INSERT INTO ROOT.SETTING (NUTZERID, DARKMODE, "LESEBESTÄTIGUNG", MAILIFIMPORTANT) 
	VALUES (2, true, true, true);
INSERT INTO ROOT.SETTING (NUTZERID, DARKMODE, "LESEBESTÄTIGUNG", MAILIFIMPORTANT) 
	VALUES (3, true, true, true);
INSERT INTO ROOT.SETTING (NUTZERID, DARKMODE, "LESEBESTÄTIGUNG", MAILIFIMPORTANT) 
	VALUES (4, true, true, true);
INSERT INTO ROOT.CHAT (ERSTELLDATUM, "NAME", BESCHREIBUNG, PROFILBILD, LETZTENACHRICHT, ISGROUP, ISBLOCKED, GOTBLOCKED, NNEW) 
	VALUES (1617210935174, 'GFOS', NULL, NULL, NULL, true, NULL, NULL, NULL);
INSERT INTO ROOT.CHAT (ERSTELLDATUM, "NAME", BESCHREIBUNG, PROFILBILD, LETZTENACHRICHT, ISGROUP, ISBLOCKED, GOTBLOCKED, NNEW) 
	VALUES (1617210935174, NULL, NULL, NULL, NULL, false, NULL, NULL, NULL);
INSERT INTO ROOT.CHAT (ERSTELLDATUM, "NAME", BESCHREIBUNG, PROFILBILD, LETZTENACHRICHT, ISGROUP, ISBLOCKED, GOTBLOCKED, NNEW) 
	VALUES (1617210935174, NULL, NULL, NULL, NULL, false, NULL, NULL, NULL);
INSERT INTO ROOT.ISADMIN (NUTZERID, CHATID) 
	VALUES (2, 1);
INSERT INTO ROOT.NIMMTTEIL (CHATID, NUTZERID) 
	VALUES (1, 3);
INSERT INTO ROOT.NIMMTTEIL (CHATID, NUTZERID) 
	VALUES (1, 4);
INSERT INTO ROOT.NIMMTTEIL (CHATID, NUTZERID) 
	VALUES (1, 2);
INSERT INTO ROOT.NIMMTTEIL (CHATID, NUTZERID) 
	VALUES (2, 2);
INSERT INTO ROOT.NIMMTTEIL (CHATID, NUTZERID) 
	VALUES (2, 4);
INSERT INTO ROOT.NIMMTTEIL (CHATID, NUTZERID) 
	VALUES (3, 3);
INSERT INTO ROOT.NIMMTTEIL (CHATID, NUTZERID) 
	VALUES (3, 2);
INSERT INTO ROOT.NACHRICHT (SENDERID, CHATID, DATUMUHRZEIT, INHALT, FOTO, ANTWORTAUF, SENDER, ISIMPORTANT, READBYALL, ISPLANNED) 
	VALUES (2, 1, 1617371400000, 'Hallo Leute, wie geht es euch so?', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO ROOT.NACHRICHT (SENDERID, CHATID, DATUMUHRZEIT, INHALT, FOTO, ANTWORTAUF, SENDER, ISIMPORTANT, READBYALL, ISPLANNED) 
	VALUES (3, 1, 1617371520000, 'Total gut, und euch so?', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO ROOT.NACHRICHT (SENDERID, CHATID, DATUMUHRZEIT, INHALT, FOTO, ANTWORTAUF, SENDER, ISIMPORTANT, READBYALL, ISPLANNED) 
	VALUES (4, 1, 1617371640000, 'Das freut mich, mir gehts auch gut', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO ROOT.NACHRICHT (SENDERID, CHATID, DATUMUHRZEIT, INHALT, FOTO, ANTWORTAUF, SENDER, ISIMPORTANT, READBYALL, ISPLANNED) 
	VALUES (2, 3, 1617371400000, 'Hey Jet3141!!', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO ROOT.NACHRICHT (SENDERID, CHATID, DATUMUHRZEIT, INHALT, FOTO, ANTWORTAUF, SENDER, ISIMPORTANT, READBYALL, ISPLANNED) 
	VALUES (2, 3, 1617371520000, 'Hey Simon!!', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO ROOT.NACHRICHT (SENDERID, CHATID, DATUMUHRZEIT, INHALT, FOTO, ANTWORTAUF, SENDER, ISIMPORTANT, READBYALL, ISPLANNED) 
	VALUES (2, 2, 1617371400000, 'Hey NoSkiller, wie gehts dir so?', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO ROOT.NACHRICHT (SENDERID, CHATID, DATUMUHRZEIT, INHALT, FOTO, ANTWORTAUF, SENDER, ISIMPORTANT, READBYALL, ISPLANNED) 
	VALUES (4, 2, 1617371520000, 'Hey Simon, mir gehts gut!!!', NULL, NULL, NULL, NULL, NULL, NULL);

