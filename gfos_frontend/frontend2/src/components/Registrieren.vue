
<template >

    <v-container>
    <div style="width: 1000px; postion: relative; left: 100px;">
     <v-stepper v-model="e1" style="margin: 20px; max-width: 700px;  ">
     <div v-if="loginzeigen === true">
      <v-stepper-header v-model="headeranzeigen">
        <v-stepper-step :complete="e1 > 1" color ="#5c7d9d" step="1">
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
          <v-card class="mb-12" max-height="500px">
          
          <h4>Login</h4>
          
          
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
              <v-btn dark v-bind="attrs" v-on="on" style =" position: relative; float: right; margin: 5px;">Login</v-btn>
            </template>
  
            <v-card>
              <v-card-title class="headline grey lighten-2">
                Bestätigen  
              </v-card-title>
  
              <v-card-text>
                
                <div v-if="PINrichtig === false">
                  Bitte geben Sie Ihre Bestätigungs-PIN ein. Die Bestätigungs-PIN wurde Ihnen per Mail zugesandt.
                  <v-card elevation="2" style="text-align: center; border-radius: 10px;">
                  <v-container>
                    <v-row align ="end">
                      <v-col cols="1" md="2">
                        <v-text-field  style="text-align: center;" v-model="PINeins" :rules="pinRules" center></v-text-field>
                      </v-col>
                      <v-col cols="1" md="2">
                        <v-text-field v-model="PINzwei" :rules="pinRules"></v-text-field>
                      </v-col>
                      <v-col cols="1" md="2" >
                        <v-text-field v-model="PINdrei" :rules="pinRules"></v-text-field>
                      </v-col>
                      <v-col cols="1" md="2">
                        <v-text-field v-model="PINvier" :rules="pinRules"></v-text-field>
                      </v-col>
                    </v-row>
                  </v-container>
                  
                  </v-card>
                  <div v-if="bestätigungshilfe === true">
                    Was Sie vor sich sehen, ist eine 2-Faktor-Authentifizierung. Dies ist eine Sicherungsmethode des Messenger-Dienstes, der Registrierungen ohne Zugriff auf die angegebene E-Mail-Adresse verhindern soll. Dabei wir Ihnen auf die angegebene E-Mail-Adresse eine Mail mit einem Code gesendet. Bitte geben Sie diesen Code in die oben stehenden Felder ein. Beachten Sie, dass pro Feld nur eine Zahl des vierstelligen Codes vorgesehen ist.
                  </div>
                </div>
                <div v-else>
                  Sie sind nun ein registrierter Teil unseres Dienstes!
                </div>
              </v-card-text>
  
              <v-divider></v-divider>
  
              <v-card-actions>
              <v-spacer></v-spacer>
              <div v-if="PINrichtig === false">
                <v-btn color="primary" text position = "right" @click= "bestätigungshilfe = !bestätigungshilfe">
                  Hilfe
                </v-btn>
                <v-btn color="primary" text v-on:click="printout()">
                  Bestätigen
                </v-btn>
              </div>
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
      p1: 1, //Muss über GET-Methode erfragt werden.
      p2: 1, //Muss über GET-Methode erfragt werden.
      p3: 1, //Muss über GET-Methode erfragt werden.
      p4: 1, //Muss über GET-Methode erfragt werden.
      bestätigungshilfe: false,
      passwordRules: [
        v => !!v || 'Dieses Feld ist erforderlich.',
        //v => !v.contains('A') || 'Das Passwort muss einen Großbuchstaben enthalten.',
        v => v.length <= 15 || 'Das Passwort muss kürzer als 15 Zeichen sein.',
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
      var IP_ADDRESS = '91.49.179.104';
      function printout(){
        console.log("Knopf gedrückt.")
      }
      function controlpin(){
        if(this.PINeins === this.p1 && this.PINzwei === this.p2 && this.PINdrei === this.p3 && this.PINvier === this.p4){
          this.PINrichtig = true;}
        console.log(this.Pinrichtig)
      }

      function submit() { //Einfach aus dem Chat.vue kopiert. Müsste angepasst werden.
            fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/nutzer/login', {
                mode: 'cors',
                method: 'POST',
                headers: {
                    'Accept': 'application/json', // Antwort von Server
                    'Content-Type': 'text/plain', // Wird an den Server gesendet
                },
                body: JSON.stringify({
                    benutzername: this.benutzername,
                    passwort: this.passwort,
                    vorname: this.vorname,
                    nachname: this.nachname,
                    email: this.email,
                    info: this.email,
                })
            }).then(response => {
                response.clone();
                response.text().then(content => {
                    console.warn(content);
                }); 
            });
        }
    }
    
  }
  
</script>
