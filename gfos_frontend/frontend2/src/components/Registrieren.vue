
<template >

    <v-container>
     <v-stepper v-model="e1">
      <v-stepper-header v-model="headeranzeigen" color="#5c7d9d">
        <v-stepper-step
          :complete="e1 > 1"
          color ="#5c7d9d"
          step="1"
        >
          Benutzernamen erstellen
        </v-stepper-step>
  
        <v-divider></v-divider>
  
        <v-stepper-step
        color ="#5c7d9d"
        
          :complete="e1 > 2"
          step="2"
        >
          Daten angeben
        </v-stepper-step>
  
        <v-divider></v-divider>
  
        <v-stepper-step step="3" color ="#5c7d9d" >
          Login
        </v-stepper-step>
      </v-stepper-header>
  
      <v-stepper-items color ="#5c7d9d">
        <v-stepper-content step="1">
          <v-card
            class="mb-12"
            
            height="200px"
          >
          <subheading>Schritt 1:</subheading>
          <h4>Benuzternamen erstellen</h4>
          <v-text-field
              v-model="benutzername"
              :rules="nameRules"
              :counter="20"
              label="Benutzernamen erstellen"
              required
            ></v-text-field>
            <v-text-field
              v-model="passwort"
              :rules="passwordRules"
              label="Passwort erstellen"
              required
            ></v-text-field>
            
          </v-card>
  
          <v-btn color="primary" @click="e1 = 2">
            Weiter
          </v-btn>
  
          <v-btn text>
            Impressum
          </v-btn>

          <v-btn  @click="e1 = 3">
            Zum Login
          </v-btn>
          
        </v-stepper-content>
  
        <v-stepper-content step="2">
          <v-card
            class="mb-12"
            
            max-height="2000px"
          >
          <div v-if="companyaccount_checkbox === true">
            <subheading>Persönliche Daten</subheading>
            <v-card-subtitle>Bitte loggen Sie sich hier mit Ihren persönlichen Daten ein.</v-card-subtitle>
          </div>
          <div v-else>
            <subheading>Schritt 2:</subheading>
          </div>
          <div v-if="companyaccount_checkbox === true">
            <v-text-field
              v-model="benutzername"
              :rules="nameRules"
              :counter="20"
              label="Benutzernamen erstellen"
              required
            ></v-text-field>
            <v-text-field
              v-model="passwort"
              :rules="passwordRules"
              label="Passwort erstellen"
              required
            ></v-text-field>
          </div>
          <div v-else>  
            <v-text-field
              v-model="vorname"
              :rules="nameRules"
              :counter="25"
              label="Vornamen eingeben*"
              required
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text>Bitte geben Sie an dieser Stelle Ihren Vornamen ein.</v-card-text></div>
            <v-text-field
              v-model="nachname"
              :rules="nameRules"
              :counter="25"
              label="Nachnamen eingeben*"
              required
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text>Bitte geben Sie an dieser Stelle Ihren Nachnamen ein.</v-card-text></div>
            <v-text-field
              v-model="email"
              :rules="emailRules"
              :counter="45"
              label="E-Mail eingeben"
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text>Bitte geben Sie hier Ihre E-Mail-Adresse ein. Diese benötigen wir für die 2-Faktor-Authentifizierung</v-card-text></div>
            <v-text-field
              v-model="info"
              :rules="nameRules"
              :counter="256"
              label="Info eingeben"
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text>In der Info können Sie sich selbst in kurzen Worten beschreiben.</v-card-text></div>
            <v-file-input
               truncate-length="15"
               label="Profilbild hinzufügen"
               v-model="profilbild"
            ></v-file-input>
            <div v-if="hilfe_checkbox === true"><v-card-text>Fügen Sie zur Ergänzung der Info ein Profilbild hinzu! Dieses Foto wird für alle Ihre Kontakte sichtbar sein.</v-card-text></div>
            </div>
            <div v-if="companyaccount_checkbox === true">
              <br>
              <subheading>Firmendaten</subheading>
            </div>
            <div v-else>
              <subheading></subheading>
            </div>
            <div v-if="companyaccount_checkbox === true">  
            <v-text-field
              v-model="firmenname"
              :rules="nameRules"
              :counter="25"
              label="Firmenname*"
              required
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true && companyaccount_checkbox === true"><v-card-text>Bitte geben Sie hier den Namen der Firma ein.</v-card-text></div>
            <v-text-field
              v-model="inhabervorname"
              :rules="nameRules"
              :counter="25"
              label="Vorname des Inhabers*"
              required
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true && companyaccount_checkbox === true"><v-card-text>Ihre Firma braucht einen Inhaber! Geben Sie hier bitte den Vornamen des Inhabers ein. Die Sichtbarkeit können Sie später in den Einstellungen anpassen.</v-card-text></div>
            <v-text-field
              v-model="inhabernachname"
              :rules="nameRules"
              :counter="25"
              label="Nachname des Inhabers*"
              required
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true && companyaccount_checkbox === true"><v-card-text>Bitte geben Sie hier den Nachnamen des Inhabers ein.</v-card-text></div>
            <v-text-field
              v-model="firmenpasswort"
              :rules="passwordRules"
              label="Passwort erstellen*"
              required
            ></v-text-field>
            <div v-if="hilfe_checkbox === true && companyaccount_checkbox === true"><v-card-text>Bitte erstellen Sie ein Passwort für den Account</v-card-text></div>
            <v-text-field
              v-model="firma_sicherungsnummer_5"
              :rules="numberRules"
              :counter="5"
              label="Sicherungsnummer erstellen*"
              ></v-text-field>
            <div v-if="hilfe_checkbox === true && companyaccount_checkbox === true"><v-card-text>Bitte erstellen Sie eine 5-Stellige Sicherheitsnummer. Diese Nummer ergänzt das Passwort und sollte nicht öffentlich bekannt sein.</v-card-text></div>
            <v-text-field
              v-model="firmen_sicherungsnummer_10"
              :rules="numberRules"
              :counter="10"
              label="Rückversicherungsnummer erstellen*"
              ></v-text-field>
            <div v-if="hilfe_checkbox === true && companyaccount_checkbox === true"><v-card-text>Bitte erstellen Sie eine 10-Stellige Rückversicherungsnummer. Diese wird gebraucht, wenn bei Registrierungsvorgängen die Anmeldedaten 3-Mal falsch eingeben werden.</v-card-text></div>
            
            <v-text-field
              v-model="firmenort"
              :rules="nameRules"
              :counter="45"
              label="Ort*"
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true && companyaccount_checkbox === true"><v-card-text>Bitte geben Sie hier ein, in welcher Stadt Ihre Firma beheimatet ist. Wenn Sie mehrere Aussenstellen, Filialien, etc. haben, geben Sie bitte die Stadt des Hauptsitzes an.</v-card-text></div>
            <v-text-field
              v-model="plz"
              :rules="numberRules"
              :counter="5"
              label="PLZ*"
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true && companyaccount_checkbox === true"><v-card-text>Bitte ergänzen Sie die Ortsangabe durch die Postleitzahl.</v-card-text></div>
            <v-text-field
              v-model="slogan"
              :rules="nameRules"
              :counter="256"
              label="Slogan"
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true && companyaccount_checkbox === true"><v-card-text>Wenn Sie möchten, können Sie der Firma hier einen Slogan geben.</v-card-text></div>
            <v-file-input
               truncate-length="15"
               label="Firmenlogo"
               v-model="firmenlogo"
            ></v-file-input>
            <div v-if="hilfe_checkbox === true && companyaccount_checkbox === true"><v-card-text>Hier können Sie ein Firmenlogo ergänzen.</v-card-text></div>
            <div v-if="companyaccount_checkbox === false">
              
          </div>
          </div>
          
          <v-checkbox v-model="companyaccount_checkbox" label ="Ich möchte ein Firmenkonto erstellen."></v-checkbox>
          <v-checkbox v-model="hilfe_checkbox" label ="Ich benötige Hilfe."></v-checkbox>
          <v-checkbox v-model="settings_checkbox" label ="Ich bin mit den Nutzungsbedingungen und der Datenschutzuerklärung einverstanden."></v-checkbox>
            
          </v-card>
        <div v-if="settings_checkbox=== true">  
         <v-btn color="primary" @click="e1 = 3">
            Weiter
          </v-btn>
          </div>
          
          <v-btn text
           @click="e1 = 1">
            Zurück
          </v-btn><br><br>
          
        </v-stepper-content>
  
        <v-stepper-content step="3">
          <v-card
            class="mb-12"
            
            max-height="500px"
          >
          
          <h4>Login</h4>
          <div v-if="companyaccount_checkbox === true">
            <v-text-field
              v-model="firmenname"
              :rules="nameRules"
              :counter="25"
              label="Firmenname"
              required  
            ></v-text-field>
            <v-text-field
              v-model="plz"
              :rules="numberRules"
              :counter="5"
              label="Postleitzahl"
            ></v-text-field>
            <v-text-field
              v-model="firmenpasswort"
              :rules="passwordRules"
              label="Passwort"
              required
            ></v-text-field>
            
            <v-text-field
              v-model="firma_sicherungsnummer_5"
              :rules="numberRules"
              :counter="5"
              label="Sicherungsnummer"
              ></v-text-field>
          </div>
          <div v-else>
            <v-text-field
                v-model="benutzername"
                :rules="nameRules"
                :counter="20"
                label="Benutzername"
                required
              ></v-text-field>
              <v-text-field
                v-model="passwort"
                :rules="nameRules"
                label="Passwort"
                required
              ></v-text-field>
          </div>
          </v-card>
         
        <v-dialog
          transition="dialog-bottom-transition"
          max-width="600"
        >
        
        
          <template v-slot:activator="{ on, attrs }">
            <v-btn v-bind="attrs" v-on="on" color= "#5c7d9d">Login</v-btn>
          </template>
        
          <template v-slot:default="dialog">
            <div v-if="companyaccount_checkbox === true">
            <div v-if="benutzername === undefined || passwort === undefined">
              <v-alert border="bottom" color="red" type="error">Die Anmeldedaten sind teilweise leer.</v-alert>
            </div>
            <div v-else>
              <v-card :loading="loading" class="mx-auto my-12" max-width="374">
                <template slot="progress">
                  <v-progress-linear
                    color="deep-purple"
                    height="10"
                    indeterminate
                  ></v-progress-linear>
                </template>
              
              
                <v-img
                  height="250"
                  src="https://cdn.vuetifyjs.com/images/cards/cooking.png"
                ></v-img>
            
                <v-card-title>{{firmenname}}</v-card-title>
                <v-card-text> Inh. {{inhabervorname}} {{inhabernachname}}</v-card-text>
                
            
                <v-divider class="mx-4"></v-divider>
            
                <v-card-title>Firmendaten</v-card-title>
            
                <v-card-text>{{plz}} {{firmenort}}</v-card-text>
            
                <v-card-actions>
                  <v-card-text>{{slogan}}</v-card-text>
                </v-card-actions>
              </v-card>
            </div>
            </div>
            <div v-else>
                            
                              <div v-if="benutzername === undefined || passwort === undefined"><v-toolbar color="#5c7d9d" dark>Fehler aufgetreten.</v-toolbar></div>
                              <div v-else><v-toolbar color="#5c7d9d" dark>Herzlichen Glückwunsch!</v-toolbar></div>
                              <v-card-text>
                              <div v-if="benutzername === undefined || passwort === undefined">
                                <div class="text-p2 pa-12">Bitte tragen Sie zunächst alle erforderlichen Daten ein.</div>
                              </div>
                              <div v-else>
                                <div class="text-p2 pa-12">Sie sind nun registrierter Teil unseres Messenger-Dienstes. Sie können hier kostenlos mit Ihren Freunden chatten, arbeiten, in Kontakt bleiben, sich die Zeit vertreiben und vieles mehr! Wollen Sie eine kurze Anleitung für die Nutzun ansehen?<br><br> Eingeloggt sind Sie als:<br><br>
                              </div>
                              <div v-if="benutzername === undefined || passwort === undefined">
                              </div>
                              <div v-else>
                                <v-card class="mx-auto" max-width="344">
                                  <v-img height="200px">{{profilbild}}</v-img>
                                        <v-card-title>
                                          {{benutzername}} 
                                        </v-card-title>
                                        <v-card-subtitle>{{info}}</v-card-subtitle>
                                        
                                        <v-card-subtitle>
                                          Vorname: {{vorname}}<br>
                                          Nachname: {{nachname}}<br>
                                          E-Mail: {{email}}
                                        </v-card-subtitle>
                                    
                                            <v-divider></v-divider>
                                            <v-card-text>
                                            <div v-if="benutzername === 'JET3141'">Herzlich Willkommen, Herr Thomas</div>
                                            <div v-else-if="benutzername === 'SirSimon04'">Salute, Simon!</div>
                                            <div v-else-if="benutzername === 'SimpusMaximus'">Ein wunderherlichen, Lukas!</div>
                                            <div v-if="benutzername !== 'JET3141'">Hallo, {{benutzername}}. Danke, dass Sie sich angemeldet haben!</div>
                                            </v-card-text>
                                </v-card>
            
                              </div>
             
          </div>
                
                  </v-card-text>
                        <v-card-actions class="justify-end">
                          <div v-if="benutzername === undefined || passwort === undefined">
                            <v-alert border="bottom" color="red" type="error">Die Anmeldedaten sind teilweise leer.</v-alert>
                          </div>
                          <div v-else>
                            <v-btn
                              text
                              v-on:click="test()"
                            >Einstellungen</v-btn>
                            <v-btn text @click="dialog.value = false">Zum Messenger</v-btn>
                          </div>
                        </v-card-actions>
                    
                  
              
              
            </div>
          </template>
        </v-dialog>
      
          
          
  
          
          <v-divider></v-divider>
          <v-btn text
          position = "right"
          @click= "e1 = 1">
            Registrieren
          </v-btn>
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>
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
      headeranzeigen: false,
      checkbox: true,
      dialog: false,
      test: 'nicht betätigt',
      settings_checkbox: false,
      companyaccount_checkbox: false,
      hilfe_checkbox: false,
      

    }
    }
    
  }
</script>
