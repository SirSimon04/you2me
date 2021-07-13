<template>
  <v-container>
    <!--<div v-if="darkmode === true">-->
       <v-card class="mx-auto" max-width="600px"  :dark="darkmode">
        <v-card-title style ="font-size: calc(100%); margin: 15px; position: relative; top: 13px;">
          <div style="position: relative; left: 5px;">Einstellungsübersicht</div>
        </v-card-title>
          <v-card-text>Hier finden Sie eine Übersicht Ihrer Einstellungen. Bitte speichern Sie diese nach allen Änderungen.</v-card-text>
        <v-divider :dark="darkmode"></v-divider>
        <div v-if="darkmode === true">
          <v-alert border="left" dense outlined type="success">Darkmode: <div style="float: right"><v-btn  class="max-auto" style="font-size: 14px" @click="changeDarkmode(); speichern = true"  small text dense color="success">Aktiviert</v-btn></div></v-alert>
        </div>
        <div v-else>
          <v-alert border="left" dense outlined type="error">Darkmode: <div style="float: right"><v-btn  class="max-auto" style="font-size: 14px" @click="changeDarkmode(); speichern = true"  small text dense color="error">Deaktiviert</v-btn></div></v-alert>
        </div>
        <div v-if="schimpfwortfilter === true">
          <v-alert border="left" dense outlined type="success">Schimpfwortfilter: <div style="float: right"><v-btn  class="max-auto" style="font-size: 14px" @click="schimpfwortfilter= false; speichern = true"  small text dense color="success">Aktiviert</v-btn></div></v-alert>
        </div>
        <div v-else>
          <v-alert border="left" dense outlined type="error">Schimpfwortfilter: <div style="float: right"><v-btn  class="max-auto" style="font-size: 14px" @click="schimpfwortfilter= true; speichern = true"  small text dense color="error">Deaktiviert</v-btn></div></v-alert>
        </div>
        <div v-if="benachrichtigungen === true">
          <v-alert border="left" dense outlined type="success">Benachrichtigungen: <div style="float: right"><v-btn  class="max-auto" style="font-size: 14px" @click="benachrichtigungen= false; speichern = true"  small text dense color="success">Aktiviert</v-btn></div></v-alert>
        </div>
        <div v-else>
          <v-alert border="left" dense outlined type="error">Benachrichtigungen: <div style="float: right"><v-btn  class="max-auto" style="font-size: 14px" @click="benachrichtigungen= true; speichern = true"  small text dense color="error">Deaktiviert</v-btn></div></v-alert>
        </div>
        
        
        <div v-show="speichern">
        <v-divider :dark="darkmode"></v-divider>
          <v-card-text></v-card-text>
          
            <v-alert border="left"  dense outlined type="info">Sie dürfen das Speichern nicht vergessen! <div style="float: right"><v-btn  class="max-auto" style="font-size: 14px" @click="save();"  small text dense color="info">Speichern</v-btn></div></v-alert>
          </div>
          
      </v-card>
      <v-item-group>
        <v-container>
          <v-row>
            <v-col cols="12" md="4">
              <v-item>
                <v-card class="mx-auto" max-width="600px"  :dark="darkmode" min-width="300px;">
                  <v-card-title style ="font-size: calc(100%); margin: 15px; position: relative; top: 13px;">
                    <v-btn style="width: 65px; height: 65px;" @click=" changeDarkmode(); speichern = true">
                      <img src= "../assets/Sonnen_icon64.png" style =" position: relative; float: left; margin: 5px;" >
                    </v-btn>
                    <div style="position: relative; left: 5px;">Brightmode</div>
                  </v-card-title>
                  <v-divider :dark="darkmode"></v-divider>
                  <v-card-text>Aktivieren Sie den "Brightmode", um die Elemente der App im hellen Design anzuzeigen.</v-card-text>
                </v-card>
              </v-item>
            </v-col>
            <v-col cols="12" md="4">
              <v-item>
                <v-card class="mx-auto" max-width="600px"  :dark="darkmode" min-width="300px;">
                  <v-card-title style ="font-size: calc(100%); margin: 15px; position: relative; top: 13px;">
                    <v-btn style="width: 65px; height: 65px;" @click=" schimpfwortfilter = !schimpfwortfilter; speichern = true">
                      <img src= "../assets/Schimpfwortfilter_darkmode-true_icon64.png" style =" position: relative; float: left; margin: 5px;" >
                    </v-btn>
                    <div style="position: relative; left: 5px;">Schimpfwortfilter</div>
                  </v-card-title>
                  <v-divider :dark="darkmode"></v-divider>
                  <v-card-text>Vorsicht! Wenn Sie den "Schimpfwortfilter" deaktivieren, werden Ihnen anstößige Inhalte unzensiert angezeigt.</v-card-text>
                </v-card>
              </v-item>
            </v-col>
            <v-col cols="12" md="4">
              <v-item>
                <v-card class="mx-auto" max-width="600px"  :dark="darkmode" min-width="300px;">
                  <v-card-title style ="font-size: calc(100%); margin: 15px; position: relative; top: 13px;">
                    <v-btn style="width: 65px; height: 65px;" @click=" benachrichtigungen = !benachrichtigungen; speichern = true">
                      <img src= "../assets/Benachrichtigungen_icon64.png" style =" position: relative; float: left; margin: 5px;" >
                    </v-btn>
                    <div style="position: relative; left: 5px;">Benachrichtigungen</div>
                  </v-card-title>
                  <v-divider dark></v-divider>
                  <v-card-text>Aktivieren oder Deaktivieren Sie die Benachrichtigungen, um bei wichtigen Nachrichten keine automatischen E-Mails zu erhalten.</v-card-text>
                </v-card>
              </v-item>
            </v-col>
          </v-row>
        </v-container>
      </v-item-group>
        <v-card class="mx-auto" max-width="600px"   :dark="darkmode">
                  <v-card-title style ="font-size: calc(100%); margin: 15px; position: relative; top: 13px;">
                    <div style="position: relative; left: 5px;" v-if="analyse">
                      Analyse
                    </div>
                    <div v-else style="positon: relative; left: calc(50%)">
                        <v-btn class="mx-auto" dark elevation="5" @click="analyse= true; queryAndIndeterminate()">Analyse starten</v-btn>
                    </div>
                  </v-card-title>
                  <div v-show="analyse">
                  <v-card-content>
                    <v-card-subtitle>Insgesamt:</v-card-subtitle>
                    <div style="float: right">
                      <v-card-text v-if="nachrichtenzaehler != 0"> {{nachrichtenzaehler}} Nachrichten gesendet.</v-card-text>
                      <v-card-text v-else-if="nachrichtenzaehler > nachrichten"> {{nachrichten}} Nachrichten gesendet.</v-card-text>
                      <v-card-text v-else>Initialisieren...</v-card-text>
                    </div>
                    <v-progress-linear v-model="value" :active="show" :indeterminate="query" :query="true"></v-progress-linear>
                    <div style="float: right">
                      <v-card-text v-if="fotozaehler != 0"> {{fotozaehler}} Fotos gesendet.</v-card-text>
                      <v-card-text v-else-if="fotos === 0">Bisher keine Fotos versendet.</v-card-text>
                      <v-card-text v-else>Initialisieren...</v-card-text>
                    </div>
                    <v-progress-linear v-model="value" :active="show" :success="query" :indeterminate="query" :query="true"></v-progress-linear>
                    <br>
                    <v-card-subtitle>Chatübersicht: <div style="float: right;">Nachrichten | Fotos</div></v-card-subtitle>
                    <v-alert dense outlined text type="info" v-for="(item, i) in chatübersicht" :key="i">{{item.name}} <div style="float: right;" v-show="anzahlzeigen">{{item.anzahlNachrichten}} | {{item.anzahlFotos}}</div></v-alert>
                  </v-card-content>
                  </div>
                  <v-divider dark></v-divider>
                  <v-card-text>Die Ergebnisse beziehen sich auf die Daten, die über diesen Account gesendet wurden.</v-card-text>
                </v-card>
                <v-card class="mx-auto" max-width="600px"   :dark="darkmode">
                  <v-card-title style ="font-size: calc(100%); margin: 15px; position: relative; top: 13px;">
                    Markierte Nachrichten
                  </v-card-title>
                  <v-card-content>
                    <v-divider></v-divider>
                    <v-card-text>Hier finden Sie die Nachrichten, die Sie in den Chats markiert haben!</v-card-text>
                    <v-divider></v-divider>
                    <v-list
                    class="overflow-y-auto"
                    max-height="300px"
                    >
                        <v-card
                        v-for="(item, i) in markedMessages"
                        :key="i"
                        color="#2B5278"
                        min-heigth="110px"
                        style="margin:20px"
                        >
                          <v-card-title>

                            <span>
                              {{item.sender}}
                            </span>
      
                            <v-spacer/>  

                            <v-img
                            max-height="16px"
                            max-width="16px"
                            src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAIxQAACMUBIDGldwAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAALrSURBVFiFvZdPaxRZFMV/pyoxaqJ2VFQEcQi0op1Ro60O40YXcSZJR9SZBL+BGz+A+LcXovgBBHEp6sIgiiHGhUhEEIPaOsMoDANhFjKIKJUOsTXpdF0XGkmmu8qkKsnd1Xv3nPPjUq8eBTHKrne4dr3DjeNRFUfsJYcPgQy4FtXDiSo0yzryddQxTphlI/tEFnrPn3QiGg025HP9HXMKYIaEHfv2LJ2KOoVIovyLtt+BHycsbcw/7z84JwBmyMyOl63jZKNMYdqCwVxmP7C5Alpq8EX/vlkHQP6xwD3TaTM0awBerm0fkA5p2eLlWjIzDmCGvFzrWrCT3+uVdMrLta6d6iQmNQ3+2VZPkQYcGsy3FGIj0ABaD1Y3FcMJNYp4jTEAvJLpJWKAEgNL0tv/lbI+gLxc6wngNyAJ1E4zJGp9AP4Bbjiq0gVgdA7DAWoxzPXdi05iU4+H5v0ieDyHAM/csZrmxenud9/egbcvd9dVjyzoBu2e5fBH/qi1LPupdwgmnIIVqb7hEbeUAe7PYvjDsULVr+PhQPlR+e9p+8IFTukW0DzD4Q+KNYXMilTf8MTFsu/A6nR3IbHI2sFuz1Sy4O7wp9qW/4dXBABQsnckUVPXgXErfrjuePnCgTU/d30MgAuuof4Dy0rVI+/iALjFmuWLd958H7Qf+ikuVhfXxQkHKFZ9SobthwK4KjXGBXAdhXqEApivVFwAw0I9wm9DhdNPjSDGBMBiAwiiAQw9bV8OrAxUGn9J6hRqBp4Ft7Hqq1fFCvwzKrnWiFUwNP6W7Gxi684r43e6Gdu9XEtG0hlgU5mXM5YCHlTKCZyA+WUn4LVMh+uHCo31W3svj4cDSNjSbb3diaYdTZI6EQOToYPfg0AAR+MnwN4LHR3MF5KJbT2XtKdvLEgjZf1EU09XYl7tBpkOC95M9iqvsJ/THww7j188V5++lw/pKwdJdY0Cl978sffq/FL1EcN2TUcPfLkVpy2K4PUZeTEDIcER7+4AAAAASUVORK5CYII="
                            @click="unmark(item.nachrichtid)"
                            >
                            </v-img>
                            
                          </v-card-title>
                          <v-card-content>
                            <v-card-text
                            class="headline font-weight-bold"
                            >
                              {{item.inhalt}}
                            </v-card-text>
                          </v-card-content>
                          <v-card-subtitle
                          v-text="getDate(item.datumuhrzeit)"
                          >
                          </v-card-subtitle>
                        </v-card>
                    </v-list>
                  </v-card-content>
                </v-card>
      
        <v-card class="mx-auto" max-width="600px" :dark="darkmode">
          <v-card-title style ="font-size: calc(100%); margin: 15px; position: relative; top: 13px;">
              <img src= "../assets/disputio_big.png" style =" position: relative; float: left; margin: 5px;" class="mx-auto" @click="picture= !picture" v-if="picture === true">
              <img src= "../assets/disputio_medium.png" style =" position: relative; float: left; margin: 5px;" class="mx-auto" @click="picture= !picture" v-if="picture === false">
          </v-card-title>
            
            <v-card-text style="text-align: center">Version 1.0</v-card-text>
            <v-card-text style="text-align: center">Inh. Lukas Baginski, Simon Engel, Jan Thomas</v-card-text>
            <v-card-text style="text-align: center">Veröffentlicht: 2021</v-card-text>
            <v-card-text style="text-align: center" @click="dialog= true"><a>Impressum</a></v-card-text>
            <v-card-actions>
              <v-spacer/>
              <v-btn
              color="red"
              outlined
              @click="deleteDialog = true"
              >
                Account löschen
              </v-btn>
              <v-spacer/>
            </v-card-actions>
            
            <v-dialog v-model="dialog" fullscreen >
              <v-card :dark="darkmode">
                
                  
                
                <v-stepper v-model="e1" alt-labels>
                  <v-stepper-header :dark="darkmode">
                      <v-divider></v-divider>
                    <v-stepper-step step="1" @click="e1= 1">
                      FAQ
                    </v-stepper-step>
                      <v-divider></v-divider>
                    <v-stepper-step step="2" @click="e1= 2">
                      Nutzungsbedingungen
                    </v-stepper-step>
                      <v-divider></v-divider>

                  </v-stepper-header>

                  <v-stepper-items>
                    <v-stepper-content step="1">
                      <v-card class="mb-12" height="700">
                        <v-expansion-panels>
                          <v-expansion-panel>
                            <v-expansion-panel-header>
                                Wo kann ich Disputatio verwenden?
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                              Der Messengerdienst Dispuatio ist in allen gängigen Broswern ereeichbar und in der Zukunft auch als App im PlayStore und AppStore.
                            </v-expansion-panel-content>
                          </v-expansion-panel>
                          <v-expansion-panel>
                            <v-expansion-panel-header>
                                Kann ich Bilder verschicken?
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                              Momentan ist es leider noch nicht möglich, Fotos zu versenden, das bedauern wir sehr. Wir arbeiten aber in hohem Tempo an der Umsetzung dieser Funktion.
                            </v-expansion-panel-content>
                          </v-expansion-panel>
                          <v-expansion-panel>
                            <v-expansion-panel-header>
                                Wie kann ich mit Freunden chatten?
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                              Um einen Privatchat mit deinen Freunden zu erstellen, musst du diese erst als Freund hinzufügen. Dann kannst du von der Freundesliste aus einen Chat starten.
                            </v-expansion-panel-content>
                          </v-expansion-panel>
                          <v-expansion-panel>
                            <v-expansion-panel-header>
                                Wie kann ich Chats archivieren und anpinnen?
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                              Um Chats zu archivieren oder anzupinnen, musst du einen auf den Namen eines Chat einen Rechtsklick ausführen. Dann erscheint ein Menü, in dem du eine der beiden Aktionen ausführen kannst.
                            </v-expansion-panel-content>
                          </v-expansion-panel>
                          <v-expansion-panel>
                            <v-expansion-panel-header>
                                Was sind archivierte Chats?
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                              Archivierte Chats sind Chats, die du gerade nicht auf einer Startseite haben, aber trotzdem behalten willst. Sie sind durch einen Klick auf das Symbol unten links in der Chatansicht erreichbar.
                            </v-expansion-panel-content>
                          </v-expansion-panel>
                          <v-expansion-panel>
                            <v-expansion-panel-header>
                              Der Brightmode wird nicht korrekt angezeigt.
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                              Wenn das der Fall ist, sollten sie zuerst kontrollieren, ob Sie von ihrem Browser aus einen Darkmode eingestellt haben.
                              Falls das Problem immer noch auftritt, sollten Sie Ihre Daten erneut speichern und sich nochmals einloggen.                             
                            </v-expansion-panel-content>
                          </v-expansion-panel>
                          <v-expansion-panel>
                            <v-expansion-panel-header>
                              Meine Daten werden nicht neu geladen.
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                              Grundsätzlich können alle Daten durch den "Neu Laden"-Knopf neu vom Server angefragt werden.
                              Dabei können manchmal auch Probleme auftreten, dann einfach einmal die Seite neu laden und wieder einloggen.
                            </v-expansion-panel-content>
                          </v-expansion-panel>
                          <v-expansion-panel>
                            <v-expansion-panel-header>
                              Manche Knöpfe sind nicht klickbar.
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                              Wenn Knöpfe nicht klickbar sind, ist das in den meisten Fällen so gewollt.
                              Ein Beispiel dafür ist der Knopf beim Veröffentlichen von Posts, dieser wird erst erlaubt, wenn Text eingetragen wurde.
                            </v-expansion-panel-content>
                          </v-expansion-panel>
                        </v-expansion-panels>
                      </v-card>
                      <v-btn  @click="dialog=false" color="primary" :text="darkmode" :dark="darkmode" >
                        Schließen
                      </v-btn>
                      <v-btn  @click="e1 = 2" color="primary" :text="darkmode" :dark="darkmode" outlined>
                        Zu den Nutzungsbedingungen
                      </v-btn>
                    </v-stepper-content>
                     <v-stepper-content step="2">
                      <v-card class="overflow-y-auto mb-12" :dark="darkmode" height="700">
                        <v-card-subtitle>Nutzungsbedingungen</v-card-subtitle>
                        <v-card-text>
                          <p style="font-size:large; font-weight: bold;">§1 Allgemeine Bestimmungen</p>
                          <p style="font-size:large"> 1. Anmeldebedingungen</p>
                          <p> a. Jeder Account, welcher in dem Messenger-Dienst “Disputatio” angelegt wird, hat die angegebenen Pflichtfelder zu erfüllen. Diese Pflichtfelder sind: “Benutzername, Passwort und E-Mail-Adresse”. Andere Daten, die im Zuge der Registrierung angegeben werden, werden gemäß der Datenschutzerklärung (siehe Datenschutzerklärung) zur Verbesserung der Dienste verwendet, sind aber nicht angabepflichtig.</p>
                          <p>b. Sofern über die Pflichtfelder hinaus Angaben getätigt werden, müssen diese der Wahrheit entsprechen und nach bestem Wissen und Gewissen ausgefüllt werden. Verstöße gegen diese Bedingung werden nach §2 a geahndet und können- bei weit reichenden Folgen- weitere Konsequenzen haben.</p> 
                          <p>c. Jeder Mensch hat das Recht, über den Messenger-Dienst “Disputatio” so viele Accounts anzulegen, wie er möchte. Dabei wird allerdings darauf hingewiesen, dass bei einer sehr hohen Anzahl von Accounts pro Person die persönliche Organisation verkompliziert werden könnte.</p>
                          <p>d. Der Messenger-Dienst “Disputatio” ist kostenlos. Durch die Nutzung von Privatpersonen oder Unternehmen sollen keine Kosten entstehen. Darüber hinaus ist auf der gesamten Plattform des Messenger-Dienstes der gewerbliche Handel mit allen Waren, Dienstleistungen oder Ressourcen verboten.</p>
                          <p style="font-size:large">2. Ausschluss</p>
                          <p>  a. Sollten Nutzer auf dem Messenger-Dienst sich nicht nach Ordnungen der allgemeinen Nutzungsbedingungen verhalten oder durch sonstige Verhaltensweisen oder Begebenheiten (strafrechtliche Verfolgung etc. ) auffällig werden, behält sich der Betreiber vor, die jeweiligen Personen von der Nutzung auszuschließen. In diesem Fall entfallen §1 a-c sowie alle nachfolgenden Paragraphen.</p>
                          <p> b. Der Betreiber behält sich überdies vor, den Messenger-Dienst zu jeder Zeit einzustellen. Somit besteht kein Recht auf die Aushändigung gewisser Inhalte oder die Weiterführung der Dienstleistung. Zur Erklärung wird besonders wird auf §1 c verwiesen.</p>
                          <p style="font-size:large; font-weight: bold;">§2: Umgang mit Chatverläufen</p>
                          <p style="font-size:large">1. Inhalt von Chatverläufen</p>
                          <p> a. Der Inhalt der Chatverläufe im Messenger-Dienst “Disputatio” sind privat. Sie sind nur von den Personen, die an der jeweiligen Chatsituation beteiligt sind, zu lesen. Diese Funktion wird zum einen durch den Betreiber sichergestellt, muss aber von den Benutzern ebenso beachtet werden. Ausnahmen zu dieser Regelung finden sich in Absatz</p>
                          <p>b. Jeder Nutzer hat sich im Messenger-Dienst “Disputatio” gemäß gesellschaftlicher Konventionen zu verhalten. Dabei ist darauf zu achten, dass beim Verschicken von Textnachrichten, Bildern, Memes oder Stickern die Freiheit und Würde anderer Chatteilnehmer nicht begrenzt wird. Dieser Umstand wird aufgrund von Absatz 1 vom Betreiber nicht kontrolliert.</p>
                          <p>c. Beide vorrangegangenen Absätze entfallen, sofern durch oder gegen die Inhalte der Chats oder dessen Teilnehmer strafrechtlich ermittelt wird. In diesem Falle behält sich der Betreiber vor, die Inhalte sowie die eingetragenen Nutzerdaten den örtlichen Behörden zu übergeben. Dies wird allerdings nur durch gerichtliche Anordnung wirksam.</p>
                          <p>d. Im Allgemeinen wird für die Benutzung von “Disputatio” darum gebeten, einen friedvollen und respektvollen Umgang mit anderen Nutzern zu pflegen.</p>
                          <p style="font-size:large"> 2. Versendung von relevanten Inhalten</p>
                          <p> a. Der Betreiber des Messenger-Dienstes “Disputatio” übernimmt für den Inhalt von Textnachrichten sowie für den Inhalt von Bildnachrichten (siehe §2, 3) keine Haftung.</p>
                          <p>b. Sollte es zum begründeten Verdacht kommen, dass die Inhalte der Chats einer Person strafrechtlich relevant sein könnten, behält sich der Betreiber vor §2, 1a außer Kraft zu setzen. In diesem Falle können die Inhalte der Chatverläufe an Behörden der jeweiligen Länder weitergeben werden.</p>
                          <p>c. Darüber hinaus wird für die Sicherheit der Inhalte der Textnachrichten keine Haftung übernommen. “Disputatio” schützt die Chats zwar, ist allerdings im Falle von Datenklau o.Ä. nicht für eine Entschädigung heranzuziehen. Bemühungen, dem Datenklau entgegenzuwirken werden vom Betreiber unterstützt.</p>
                          <p style="font-size:large">Bildinhalten</p>
                          <p>b. Für den Inhalt der Bildinhalte übernimmt der Messenger keine Haftung. Sofern strafrechtlich relevante Inhalte auf Bildern zu sehen sind oder ein begründeter Verdacht zu dieser Annahme besteht, ist der Messenger bereit, Nutzungsdaten von Chatverläufen und deren Inhalte weiterzugeben.</p> 
                          <p> c. Darüber hinaus wird nicht für die Sicherheit und die Anonymität der Bildinhalte gehaftet. Deshalb wird darum gebeten, weniger relevante Bilder beim Nutzen des Messengers zu benutzen.</p> 
                          <p></p> 
                          <p></p>   
                          <p></p> 
                        </v-card-text>
                      </v-card>
                      <v-btn  @click="dialog=false" color="primary" :text="darkmode" :dark="darkmode">
                        Schließen
                      </v-btn>
                      <v-btn  @click="e1 = 1" color="primary" :text="darkmode" :dark="darkmode" outlined>
                        Zum FAQ
                      </v-btn>
                    </v-stepper-content>
                  </v-stepper-items>
                </v-stepper>
              </v-card>
            </v-dialog>
            
        </v-card>
        <v-dialog
        v-model="changedialog"
        width="550"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn 
            style="z-index: 2000; position: fixed; right: 30px; bottom: 30px" 
            class="mx-2" 
            fab 
            dark color="indigo" 
            v-bind="attrs"
            v-on="on"
            >
                <v-icon dark>mdi-account</v-icon>
            </v-btn>
          </template>
          <v-card class="mx-auto" max-width="600px"  :dark="darkmode" min-width="300px;">
          <v-card-title style ="font-size: calc(100%); margin: 15px; position: relative; top: 13px;">
            
            <div style="position: relative; left: 5px;">Nutzerübersicht</div>
          </v-card-title>
          <v-divider :dark="darkmode"></v-divider>
          <v-card-text>
            <v-text-field
                v-model="benutzername"
                
                label="Benutzername:"
                required
                :dark="darkmode"
              ></v-text-field>
              <v-text-field
              v-model="vorname"
              style="color: white"
              v-if="vorname !== undefined"
              label="Vorname:"
              :dark="darkmode"
              
            ></v-text-field>
            
            <v-text-field
              v-model="nachname"
              :rules="nameRules"
              v-if="nachname !== undefined"
              style="color: white"
              label="Nachname:"
              :dark="darkmode"
              
            ></v-text-field>
            
            <v-text-field
              v-model="email"
              :rules="emailRules"
              
              label="E-Mail:"
              style="color: white"
              :dark="darkmode"
            ></v-text-field>
            
            <v-text-field
              v-model="info"
              :rules="nameRules"
              v-if="info !== undefined"
              label="Info:"
              style="color: white"
              :dark="darkmode"
            ></v-text-field>

            <v-text-field
                  v-model="passwortAlt"
                  :rules="nameRules"
                  label="Passwort"
                  required
                  :dark="darkmode"
                  class="input-group--focused"
                :append-icon="passwordshow ? 'mdi-eye' : 'mdi-eye-off'"
                :type="passwordshow ? 'text' : 'password'"
                @click:append="passwordshow = !passwordshow"
                ></v-text-field>
            
            <v-row
            align="space-between"
            >
              <v-col>
                <v-btn :text="passwortändern" color="info" :outlined="!passwortändern" @click="passwortändern = !passwortändern">{{aenderungsplatzhalter}}</v-btn>
              </v-col>
              <v-spacer>
              </v-spacer>
              <v-col>
                <v-tooltip bottom
                v-if="passwortAlt === ''"
                >
                  <template v-slot:activator="{ on, attrs }" v-if="passwortAlt === ''">
                    <div
                    v-bind="attrs"
                    v-on="on"
                    v-if="passwortAlt === ''"
                    >
                      <v-btn
                      color="green darken-1"
                      @click="changeInfos();"
                      outlined
                      disabled
                      >
                        Einstellungen übernehmen
                      </v-btn>
                    </div>
                  </template>
                  <span>
                    Gib erst dein altes Passwort zur Bestätigung ein
                  </span>
              </v-tooltip>
                <v-btn
                v-else
                color="green darken-1"
                @click="changeInfos();"
                >
                  Einstellungen übernehmen
                </v-btn>
              </v-col>
            </v-row>
              
              
              <div v-if="passwortändern === true">
                <v-text-field
                  v-model="passwort1"
                  :rules="nameRules"
                  label="Neues Passwort"
                  required
                  :dark="darkmode"
                  class="input-group--focused"
                :append-icon="passwordshow ? 'mdi-eye' : 'mdi-eye-off'"
                :type="passwordshow ? 'text' : 'password'"
                @click:append="passwordshow = !passwordshow"
                ></v-text-field>
                <v-text-field
                  v-model="passwort2"
                  :rules="nameRules"
                  label="Passwort bestätigen"
                  required
                  :dark="darkmode"
                  class="input-group--focused"
                  
                  :append-icon="passwordshow ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="passwordshow ? 'text' : 'password'"
                  @click:append="passwordshow = !passwordshow"
                ></v-text-field>
                
            
                <v-btn @click="passwortaendern()"  color="purple">Eingabe</v-btn>
                <v-alert border="left" dense outlined type="error" v-show="warnung">{{warnungsmeldung}}</v-alert>
              </div>
          </v-card-text>
        </v-card>
        </v-dialog>
        <v-dialog
        v-model="deleteDialog"
        width="500"
        >
          <v-card
          color="white"
          >
            <v-card-title>
              <p
              style="color:red"
              >
                Account löschen
              </p>
            </v-card-title>
            <v-card-subtitle
            style="color:red"
            >
              Möchtest du deinen Account wirklich löschen? Diese Aktion ist nicht wiederrufbar und alle deine 
              Daten gehen verloren.
            </v-card-subtitle>
            <v-divider>
            </v-divider>
            <v-card-actions>
              <v-btn
              outlined
              width="200px"
              color="primary"
              @click="deleteDialog = false"
              >
                Abbrechen
              </v-btn>
              <v-spacer/>
              <v-btn
              color="red"
              width="200px"
              @click="deleteAccount()"
              >
                <span
                style="color:white"
                >
                  Löschen
                </span>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
  </v-container>
</template>
<style>
  .v-card--reveal {
  bottom: 0;
  opacity: 1 !important;
  position: absolute;
  width: 100%;
  background-color: #17212B;
  }
</style>
<script>
import { EventBus } from './EventBus.js';
import axios from "axios";
import Impressum from './Impressum.vue';
  export default {
    
    name: 'Settings',
    
    data(){
      return {
        darkmode: window.darkmode,
        schimpfwortfilter: false,
        benachrichtigungen: false,
        speichern: false,
        picture: true,
        dialog: false,
        deleteDialog: false,
        impressum_zeigen: true,
        value: 0,
        query: false,
        show: true,
        interval1: 0,
        passwortändern: false,
        nachrichten: 0,
        nachrichtenzaehler: 0,
        fotos: 0,
        fotozaehler: 0,
        passwordshow: false,
        benutzername: '',
        vorname: null,
        nachname: null,
        email: null,
        info: null,
        warnungsmeldung: 'Warnung!',
        warnung: false,
        aenderungsplatzhalter: 'Passwort Ändern',
        chatübersicht: [],
        markedMessages: [],
        changedialog: false,
        passwortAlt: "",
        e1: 1,
        
      }
    },
    mounted(){
      this.loadData();
      /*this.queryAndIndeterminate();*/

      EventBus.$on("RELOAD", (payload) =>{
            this.loadData();
        });

    },

    beforeDestroy (){
      clearInterval(this.interval)
      
    },

    methods: {

        changeDarkmode() {
          this.darkmode = !this.darkmode
          window.DARK_MODE = this.darkmode
          this.methodThatForcesUpdate();
          EventBus.$emit("DARKMODE", {"darkmode": this.darkmode});
        },

        methodThatForcesUpdate() {
            this.$forceUpdate()
        },

        loadData() {
          axios.get(window.IP_ADDRESS +"/GFOS/daten/nutzer/getSettings/" + window.CURRENT_USER_ID + "/" + window.CURRENT_TOKEN)
          .then((resp)=>{
            this.darkmode = resp.data.settings.darkmode
            window.DARK_MODE = resp.data.settings.darkmode
            this.benachrichtigungen = resp.data.settings.mailifimportant
            this.schimpfwortfilter = resp.data.settings.wordfilter
            this.chatübersicht = resp.data.chatübersicht
            this.nachrichten = resp.data.anzahlSelbstGesendet
            this.fotos = resp.data.anzahlFotosSelbstGesendet
            this.markedMessages = resp.data.markedMessages
            this.benutzername = resp.data.self.benutzername
            if(resp.data.self.vorname !== undefined){
              this.vorname = resp.data.self.vorname
            }
            if(resp.data.self.nachname !== undefined){
              this.nachname = resp.data.self.nachname
            }
            this.email = resp.data.self.email
            if(resp.data.self.info !== undefined){
              this.info = resp.data.self.info
            }
          })
          .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },

        save() {
          this.speichern = false
           const payload = {
                "nutzerid": window.CURRENT_USER_ID,
                "darkmode": this.darkmode,
                "wordfilter": this.schimpfwortfilter,
                "mailifimportant": this.benachrichtigungen
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
          axios.post(window.IP_ADDRESS +"/GFOS/daten/nutzer/setSettings/" + window.CURRENT_TOKEN , payload, { headers})
          .then((resp)=>{
            if(resp.data === true){
              EventBus.$emit("INFOMESSAGE", {"message": "Deine Einstellungen wurden erfolgreich gespeichert!", "icon":"SUCCESS"})
            }
          })
          .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },
        queryAndIndeterminate(){
          this.query = true,
          this.show = true,
          this.value = 0,
          this.nachrichtenzaehler = 0,
          this.fotozaehler = 0,
          this.anzahlzeigen = false,
          
          setTimeout(() =>{
            this.query = false
            
              this.interval = setInterval(() => {
                if (this.value === 80){
                  clearInterval(this.interval)
                }
              this.value += 20
              var i
              for (i = 0; i< this.nachrichten/5; i++){
                this.nachrichtenzaehler++
              }
              for (i = 0; i< this.fotos/5; i++){
                this.fotozaehler++
              }             
            }, 900 
            )
            
          }, 1500)
          this.anzahlzeigen = true
        },
        clearqueryAndIndeterminate(){
          clearInterval(this.interval)
        
          return {
        
        value: 0,
        query: false,
        show: true,
        interval: 0,
        nachrichten: 12456,
        nachrichtenzaehler: 0,
        
        
      }
        },
        passwortaendern(){
          if(this.passwort1 === this.passwort2){
            this.warnung = false,
            this.passwortändern = false,
            this.speichern = true,
            this.aenderungsplatzhalter = 'Passwort geändert'
          }
          else{
            this.warnung = true
            this.warnungsmeldung = 'Bitte kontrollieren Sie die Bestätigung'
          }
        },

        getDate(date){
          var d = new Date(date);
          return d.toLocaleString().slice(0, -3)
        },

        fillStringZero(string, length) {
            string = string.toString();
            var zeroString = '';
            for (var i=length-string.length; i>0; i--) {
                zeroString += '0';
            }
            return (zeroString + string);
        },

        changeInfos() {
          const payload = {
                "eigeneId": window.CURRENT_USER_ID,
                "altesPasswort": this.passwortAlt,
                "info": this.info,
                "benutzername": this.benutzername,
                "vorname": this.vorname,
                "nachname": this.nachname,
                "email": this.email,
                "passwort": this.passwort2
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/nutzer/update/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
              this.changedialog = false
                EventBus.$emit("INFOMESSAGE", {'message': 'Deine Änderungen wurden erfolgreich gespeichert.', "icon": "SUCCESS"})
                this.loadData();
            })
            .catch(error =>{
                if(error.response.data === "PW falsch"){
                  EventBus.$emit("INFOMESSAGE", {'message': 'Das eingegebene Passwort ist nicht korrekt.', "icon": "ERROR"})
                }
                if(error.response.data === "Benutzername bereits vergeben"){
                  EventBus.$emit("INFOMESSAGE", {'message': 'Der eingebene Nutzername ist schon vergeben.', "icon": "ERROR"})
                }
                if(error.response.data === "Email bereits vergeben"){
                  EventBus.$emit("INFOMESSAGE", {'message': 'Die eingegebene E-Mail ist bereits vergeben.', "icon": "ERROR"})
                }
                if(error.response.data === "Vorname nicht zulässig"){
                  EventBus.$emit("INFOMESSAGE", {'message': 'Der eingebene Vorname ist nicht zulässig.', "icon": "ERROR"})
                }
                if(error.response.data === "Nachname nicht zulässig"){
                  EventBus.$emit("INFOMESSAGE", {'message': 'Der eingebene Nachname ist nicht zulässig.', "icon": "ERROR"})
                }
                if(error.response.data === "Info nicht zulässig"){
                  EventBus.$emit("INFOMESSAGE", {'message': 'Die eingegebene Info ist nicht zulässig.', "icon": "ERROR"})
                }
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
            })
        },

        unmark(i){
          const payload = {
                "eigeneid": window.CURRENT_USER_ID,
                "nachrichtid": i
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/nachricht/mark/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                this.loadData();
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },

        deleteAccount(){
             axios.get(window.IP_ADDRESS +"/GFOS/daten/nutzer/delete/" + window.CURRENT_USER_ID + "/" + window.CURRENT_TOKEN)
          .then((resp)=>{
            this.deleteDialog = false
            EventBus.$emit("INFOMESSAGE", {"message": "Du hast deinen Account erfolgreich gelöscht!", "icon":"SUCCESS"});
            EventBus.$emit("LOGOUT", {});
          })
          .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        }

    }
    
  }

  window.DARK_MODE = true
</script>
