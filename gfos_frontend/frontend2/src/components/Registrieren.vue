
<template >

    <v-container>
    <div style="width: 1000px; postion: relative; left: 100px;">
     <v-stepper v-model="e1" style="margin: 20px; max-width: 700px;  ">
     <div v-if="loginzeigen === true">
      <v-stepper-header v-model="headeranzeigen" color="#5c7d9d">
        <v-stepper-step
          :complete="e1 > 1"
          color ="#5c7d9d"
          step="1"
        >
          Benutzernamen erstellen
        </v-stepper-step>
  
        <v-divider></v-divider>
  
        <v-stepper-step step="2" color ="#5c7d9d" :complete="e1 > 2">
          Daten angeben
        </v-stepper-step>
  
        <v-divider></v-divider>
  
        <v-stepper-step step="3" color ="#5c7d9d" >
          Login
        </v-stepper-step>
      </v-stepper-header>
      </div>
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
              
              :counter="25"
              label="Vornamen eingeben"
              
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text>Bitte geben Sie an dieser Stelle Ihren Vornamen ein. Dieses Feld ist kein Pflichtfeld.</v-card-text></div>
            <v-text-field
              v-model="nachname"
              :rules="nameRules"
              :counter="25"
              label="Nachnamen eingeben"
              
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text>Bitte geben Sie an dieser Stelle Ihren Nachnamen ein. Wenn Sie möchten, brauchen Sie dieses Feld nicht auszufüllen. Generell kann eine Namensangabe aber dabei helfen, von Ihren Bekannten gefunden zu werden.</v-card-text></div>
            <v-text-field
              v-model="email"
              :rules="emailRules"
              :counter="45"
              label="E-Mail eingeben"
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text>Bitte geben Sie hier Ihre E-Mail-Adresse ein. Diese benötigen wir für die 2-Faktor-Authentifizierung, das Feld ist Pflicht.</v-card-text></div>
            <v-text-field
              v-model="info"
              :rules="nameRules"
              :counter="256"
              label="Info eingeben"
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text>In der Info können Sie sich selbst in kurzen Worten beschreiben. Die Info wird für alle Ihre Kontakte sichtbar sein.</v-card-text></div>
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
          <div v-if="firmenkontenakzeptieren === true">
            <v-checkbox v-model="companyaccount_checkbox" label ="Ich möchte ein Firmenkonto erstellen."></v-checkbox>
          </div>
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
          <v-card-subtitle>Noch nicht angemeldet?</v-card-subtitle>
          <v-divider></v-divider>
          
          <v-btn text
          position = "right"
          @click= "e1 = 1, loginzeigen = true" style="margin: 5px">
            Zur Anmeldung
          </v-btn>
          <v-dialog transition="dialog-bottom-transition" max-width="600" style="colour: black">
            <template v-slot:activator="{ on, attrs }">
              <v-btn color="primary" dark v-bind="attrs" v-on="on" style =" position: relative; float: right; margin: 5px">Login</v-btn>
            </template>
  
            <v-card>
              <v-card-title class="headline grey lighten-2">
                Bestätigen  
              </v-card-title>
  
              <v-card-text>
                <div v-if="PINrichtig === true">
                  Sie sind nun ein registrierter Teil unseres Dienstes!
                </div>
                <div v-else>
                  Bitte geben Sie Ihre Bestätigungs-PIN ein. Die Bestätigungs-PIN wurde Ihnen per Mail zugesandt.
                  <v-container>
                    <v-row>
                      <v-col cols="4" md="2">
                        <v-text-field v-model="PINeins" :rules="pinRules"></v-text-field>
                      </v-col>
                      <v-col cols="4" md="2">
                        <v-text-field v-model="PINzwei" :rules="pinRules"></v-text-field>
                      </v-col>
                      <v-col cols="4" md="2" >
                        <v-text-field v-model="PINdrei" :rules="pinRules"></v-text-field>
                      </v-col>
                      <v-col cols="4" md="2">
                        <v-text-field v-model="PINvier" :rules="pinRules"></v-text-field>
                      </v-col>
                    </v-row>
                  </v-container>
                </div>
              </v-card-text>
  
              <v-divider></v-divider>
  
              <v-card-actions>
              <v-spacer></v-spacer>
                <v-btn color="primary" text @click="controlpin()">
                  Bestätigen
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
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
import { EventBus } from './EventBus.js';
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
      firmenkontenakzeptieren: false,
      valid: false,
      PINrichtig: false,
      p1: 1,
      p2: 1,
      p3: 1,
      p4: 1,
      passwordRules: [
      v => !!v || 'Dieses Feld ist erforderlich.',
      v => !v.contains('A') || 'Das Passwort muss einen Großbuchstaben enthalten.',
      v => v.length <= 10 || 'Das Passwort muss kürzer als 15 Zeichen sein.',
    ],
      emailRules: [
      v => !!v || 'Die E-Mail ist erforderlich.',
      v => /.+@.+/.test(v) || 'Bitte geben Sie eine reale E-Mail ein.',
    ], 
      pinRules: [
        v => !!v || 'Sie müssen alle Felder ausfüllen.',
        v => v.length <= 1 || 'Dieses Feld kann nur eine Zahl enthalten.'
      ]
    
      

    }
    },
    mounted(){
      function controlpin(){
        if(this.PINeins === this.p1 && this.PINzwei === this.p2 && this.PINdrei === this.p3 && this.PINvier === this.p4){
          this.PINrichtig = true;}
      }
    }
    
  }
  
</script>
