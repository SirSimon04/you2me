
<template >

    <v-container>
    <div style="width: 1000px; postion: relative; left: 100px;">
     <v-stepper v-model="e1" style="margin: 20px; max-width: 1200px;  " :dark="darkmode">
     
      <v-stepper-header v-model="headeranzeigen" color="#5c7d9d">
        <v-stepper-step color ="#5c7d9d" :complete="e1 > 1"  step="I">
          Nutzungsbedingungen
        </v-stepper-step>
  
        
  
        <v-stepper-step step="I" color ="#5c7d9d" :complete="e1 > 2">
          Datenschutzerklärung
        </v-stepper-step>
  
        
  
        <v-stepper-step step="I" color ="#5c7d9d" >
          Hilfe/Sonstiges
        </v-stepper-step>
      </v-stepper-header>
      
      <v-stepper-items color ="#5c7d9d">
        <v-stepper-content step="1">
          <v-card class="mb-12" max-height="10000px" :dark="darkmode">
          <subheading>Impressum</subheading>
          <h4>Nutzungsbedingungen</h4>
                    <v-card-text>
                        Die Folgenden Bedingungen sind bei der Benutzung des Messenger-Dienstes “Disputatio” einzuhalten. Sollten diese Regelungen missachtet werden, kann es zum Ausschluss der Benutzerin oder des Benutzers von der Plattform kommen. Die Bedingungen sind:<br>
                        <ol>
                            <li>Jeder Nutzer muss sich auf dieser Plattform den Allgemeinen gesellschaftlichen Konventionen entsprechend verhalten. Es wird zu keinem Zeitpunkt zu einer Kontrolle seitens des Betreibers kommen, sollten Regelverstöße in dieser Hinsicht allerdings durch strafrechtliche Maßnahmen auffällig werden, schützt die Umgebung der App nicht vor einer entsprechenden Verfolgung und distanziert sich von jegweligen Chatinhalten.
                            Darüber hinaus wird darum gebeten, anstößige oder beleidigende Texte, Bilder, Memes und Sticker nicht zu verschicken.</li>
                            <li>Jeder Nutzer muss zur Verwendung der Website angemeldet sein. Hierzu genügt die Ausfüllung aller angegebenen Pflichtfelder. Diese sind: Benutzername, Passwort, E-Mail-Adresse. Alle weiteren angegebenen Daten werden zur Verbesserung der Dienste nach der Datenschutzerklärung (siehe Datenschutzerklärung) verwendet, sind aber nicht verpflichtend.</li>
                            <li>Es gibt keine definierte Grenze an Accounts pro Person. Jeder Bewohner der Erde hat das Recht, unbegrenzt viele Accounts in unserer App kostenlos anzulegen.</li>
                        </ol> 
                    </v-card-text>
            
          </v-card>
  
          <v-btn color="primary" @click="e1 = 2">
            Datenschutzerklärung
          </v-btn>

          
        </v-stepper-content>
  
        <v-stepper-content step="2">
          <v-card class="mb-12" max-height="2000px" :dark="darkmode">
         
          
            <subheading>Impressum</subheading>
                <h4>Datenschutzerklärung</h4>
                    <v-card-text>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.   

Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.   

Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.   

Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.   

Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.   

At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, At accusam aliquyam diam diam dolore dolores duo eirmod eos erat, et nonumy sed tempor et et invidunt justo labore Stet clita ea et gubergren, kasd magna no rebum. sanctus sea sed takimata ut vero voluptua. est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat.   

Consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus.   

Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.   

Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.   

Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.   

Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo</v-card-text>
          </v-card>
          <v-btn color="primary" @click="e1 = 3">
            Hilfe/Sonstiges
          </v-btn>
        </v-stepper-content>
  
        <v-stepper-content step="3">
             <v-card class="mb-12" max-height="2000px" :dark="darkmode">
         
          
            <subheading>Impressum</subheading>
                <h4>Hilfe/Sonstiges</h4>
                    <v-card-text>
                      <v-row justify="center">
                        <v-expansion-panels accordion>
                          <v-expansion-panel>
                            <v-expansion-panel-header>Hilfe</v-expansion-panel-header>
                              <v-expansion-panel-content>
                                <v-expansion-panels accordion>
                                  <v-expansion-panel>
                                    <v-expansion-panel-header>Chat</v-expansion-panel-header>
                                      <v-expansion-panel-content>
                                      
                                      </v-expansion-panel-content>
                                    </v-expansion-panel>
                                    <v-expansion-panel>
                                    <v-expansion-panel-header>Kontakte/Freunde</v-expansion-panel-header>
                                      <v-expansion-panel-content>
                                      
                                      </v-expansion-panel-content>
                                    </v-expansion-panel>
                                    <v-expansion-panel>
                                    <v-expansion-panel-header>Login</v-expansion-panel-header>
                                      <v-expansion-panel-content>
                                      
                                      </v-expansion-panel-content>
                                    </v-expansion-panel>
                                    <v-expansion-panel>
                                    <v-expansion-panel-header>Einstellungen</v-expansion-panel-header>
                                      <v-expansion-panel-content>
                                        <v-list-item>
                                          <v-list-item-content>
                                            <v-list-item-title>Buttons können nicht angeklickt werden</v-list-item-title>
                                            <v-list-item-text color="grey">{{button_error_list_description}}</v-list-item-text>
                                          </v-list-item-content>
                                        </v-list-item>
                                        <v-list-item>
                                          <v-list-item-content>
                                            <v-list-item-title>Brightmode wird nicht angezeigt</v-list-item-title>
                                            <v-list-item-text color="grey">{{brightmode_error_list_description}}</v-list-item-text>
                                          </v-list-item-content>
                                        </v-list-item>
                                      </v-expansion-panel-content>
                                    </v-expansion-panel>
                                </v-expansion-panels>
                              </v-expansion-panel-content>
                            </v-expansion-panel>
                            <v-expansion-panel>
                            <v-expansion-panel-header>Sonstiges</v-expansion-panel-header>
                              <v-expansion-panel-content>
                              
                              </v-expansion-panel-content>
                            </v-expansion-panel>
                        </v-expansion-panels>
                      </v-row>
                    </v-card-text>
          </v-card>
          <v-btn color="primary" @click="e1 = 1">
            Nutzungsbedingungen
          </v-btn>        
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>
    </div>
    </v-container>

</template>
<style>
  .group {
    display: flex;
    flex: 1;
    justify-content: space-around;
    }
</style>
<script>
  export default {
    name: 'Registrieren',
    
    data(){
      return {
      e1: 3,
      color: "#ffffff",
      darkmode: true,
      brightmode_error_list_description: "Sollten Sie in den Einstellungen die Einstellung von 'Darkmode' auf 'Brightmode' geändert haben, und keinen nennenswerten Effekt bemerkt haben oder einen sehr geringfügigen, dann kontrollieren Sie bitte, ob Ihr Browser eine 'Darkmode'-Einstellung hat, die Sie aktiviert haben. In diesem Falle kann es nämlich dazu kommen, dass die Einstellung nicht übernommen wird.",
      button_error_list_description: "Es kann sein, dass die Website bei Ihnen einen Fehler aufweist. Aktualisieren Sie die Seite zunächst (Taste F5 drücken), um das Problem zu beheben. Sollten weiter Probleme auftreten, dann kontrollieren Sie die Funktionsfähigkeit Ihres Eingabegeräts und überprüfen Sie die Browser-Einstellungen."

    }
    }
    
  }
</script>
