
<template >

    <v-container>
    <div class="mx-auto" max-width="1000px">
     <v-stepper v-model="e1" style="margin: 20px; max-width: 700px; background-color: #202b36;">
     <div v-if="loginzeigen === true">
      <v-stepper-header v-model="headeranzeigen" style="background-color: #182533; color: grey">
        <v-stepper-step :complete="e1 > 1" color ="#5c7d9d" step="1">
          <div v-if="e1 === 2 || e1 === 3" style="color: grey">Benutzernamen erstellen</div>
          <div v-else style= "color: white">Benutzernamen erstellen</div>
        </v-stepper-step>
  
        <v-divider></v-divider>
  
        <v-stepper-step step="2" color ="#5c7d9d" :complete="e1 > 2">
          <div v-if="e1 === 1 || e1 === 3" style="color: grey">Daten angeben</div>
          <div v-else style= "color: white">Daten angeben</div>
        </v-stepper-step>
  
        <v-divider></v-divider>
  
        <v-stepper-step step="3" color ="#5c7d9d" >
          <div v-if="e1 === 1 || e1 === 2" style="color: grey">Login</div>
          <div v-else style= "color: white">Login</div>
        </v-stepper-step>
      </v-stepper-header>
      </div>
      <v-stepper-items color ="#5c7d9d">
        <v-stepper-content step="1">
          <v-card
            class="mb-12"
            style="background-color: #202b36;"
            height="200px"
          >
          <subheading style="color: white">Schritt 1:</subheading>
          <h4 style="color: white">Benutzernamen erstellen</h4>
          <v-text-field
              v-model="benutzername"
              :rules="nameRules"
              :counter="20"
              label="Benutzernamen erstellen"
              required
              dark
            ></v-text-field>
            <v-text-field
              v-model="passwort"
              :rules="passwordRules"
              label="Passwort erstellen"
              required
              dark
              :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
              :type="show ? 'text' : 'password'"
              @click:append="show = !show"
            ></v-text-field>
            
          </v-card>
  
          <v-btn dark @click="e1 = 2">
            Weiter
          </v-btn>
  
          <v-btn text style="color: white">
            Impressum
          </v-btn>

          <v-btn  @click="e1 = 3" style="color: white" dark text>
            Zum Login
          </v-btn>
          
        </v-stepper-content>
  
        <v-stepper-content step="2">
          <v-card
            class="mb-12"
            style="background-color: #202b36;"
            max-height="2000px"
          >
          <div v-if="companyaccount_checkbox === true">
            <subheading style="color: white">Persönliche Daten</subheading>
            <v-card-subtitle style="color: white">Bitte loggen Sie sich hier mit Ihren persönlichen Daten ein.</v-card-subtitle>
          </div>
          <div v-else>
            <subheading style="color: white">Schritt 2:</subheading>
          </div>
          <div v-if="companyaccount_checkbox === true">
            <v-text-field
              v-model="benutzername"
              
              :counter="20"
              label="Benutzernamen erstellen"
              required
              style="color: white"
            ></v-text-field>
            <v-text-field
              v-model="passwort"
              :rules="passwordRules"
              label="Passwort erstellen"
              style="color: white"
              required
            ></v-text-field>
          </div>
          <div v-else>  
            <v-text-field
              v-model="vorname"
              style="color: white"
              :counter="25"
              label="Vornamen eingeben"
              dark
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text style="color: white">Bitte geben Sie an dieser Stelle Ihren Vornamen ein. Dieses Feld ist kein Pflichtfeld.</v-card-text></div>
            <v-text-field
              v-model="nachname"
              :rules="nameRules"
              :counter="25"
              style="color: white"
              label="Nachnamen eingeben"
              dark
              
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text style="color: white">Bitte geben Sie an dieser Stelle Ihren Nachnamen ein. Wenn Sie möchten, brauchen Sie dieses Feld nicht auszufüllen. Generell kann eine Namensangabe aber dabei helfen, von Ihren Bekannten gefunden zu werden.</v-card-text></div>
            <v-text-field
              v-model="email"
              :rules="emailRules"
              :counter="45"
              label="E-Mail eingeben"
              style="color: white"
              dark
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text style="color: white">Bitte geben Sie hier Ihre E-Mail-Adresse ein. Diese benötigen wir für die 2-Faktor-Authentifizierung, das Feld ist Pflicht.</v-card-text></div>
            <v-text-field
              v-model="info"
              :rules="nameRules"
              :counter="256"
              label="Info eingeben"
              style="color: white"
              dark
            ></v-text-field>
            <div v-if="hilfe_checkbox === true"><v-card-text style="color: white">In der Info können Sie sich selbst in kurzen Worten beschreiben. Die Info wird für alle Ihre Kontakte sichtbar sein.</v-card-text></div>
            <v-file-input
               truncate-length="15"
               label="Profilbild hinzufügen"
               v-model="profilbild"
               dark
               accept="image/*"
               :rules="imageRules"
               @change="getBase64(profilbild);"
            ></v-file-input>
            <div v-if="hilfe_checkbox === true"><v-card-text style="color: white">Fügen Sie zur Ergänzung der Info ein Profilbild hinzu! Dieses Foto wird für alle Ihre Kontakte sichtbar sein.<br><i>(Maximale Größe: 30KB)</i></v-card-text></div>
            </div>
            
              
          
          
          
          <v-checkbox v-model="hilfe_checkbox" label ="Ich benötige Hilfe." dark></v-checkbox>
          <v-checkbox v-model="settings_checkbox" label ="Ich bin mit den Nutzungsbedingungen und der Datenschutzuerklärung einverstanden." dark></v-checkbox>
            
          </v-card>
        <div v-if="settings_checkbox=== true">
        <v-btn dark @click="e1 = 3; submit();">Registrieren & Weiter</v-btn>
          </div>
          
          <v-btn text
           @click="e1 = 1" style = "color: white">
            Zurück
          </v-btn><br><br>
          
        </v-stepper-content>
  
        <v-stepper-content step="3">
          <v-card class="mb-12" max-height="500px" style="background-color: #202b36;">
          
          <h4 style = "color: white">Login</h4>
          
          
            <v-text-field
                v-model="benutzername"
                :rules="nameRules"
                :counter="20"
                label="Benutzername"
                required
                dark
                clearable
                ref="benutzername"
              ></v-text-field>
              <v-text-field
                v-model="passwort"
                :rules="nameRules"
                label="Passwort"
                required
                dark
                class="input-group--focused"
                @keydown.enter="login();"
                :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
                :type="show ? 'text' : 'password'"
                @click:append="show = !show"
                clearable
                ref="passwort"
              ></v-text-field>
          
          </v-card>
          <v-card-subtitle style = "color: white">Noch nicht angemeldet?</v-card-subtitle>
          <v-divider></v-divider>
          
          <v-btn text
          position = "right"
          @click= "e1 = 1, loginzeigen = true" style="margin: 5px; color: white">
            Zur Registrierung
          </v-btn>
          <v-btn dark color= "#444bab" @click="login();" style =" position: relative; float: right; margin: 5px;" elevation = "10">Login</v-btn>
          
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>
    </div>

        <!-- Bestätigungs-Pin -->
        <v-dialog v-model="verifyPin" transition="dialog-bottom-transition" max-width="600" style="colour: black">
            <template>
                <v-card style="background-color: #0E1621">
                    <div style="background-color: black;">
                        <v-card-title  style ="color: white; background-color: #0E1621" >
                        Bestätigen
                        </v-card-title>
                    </div>

                    <v-card-text>
                        <div style="color: white">Bitte geben Sie Ihre Bestätigungs-PIN ein. Die Bestätigungs-PIN wurde Ihnen per Mail zugesandt.</div>
                        <v-card elevation="2" style="text-align: center; border-radius: 10px; background-color: #17212B">
                            <v-container class="mx-auto">
                                <v-row align ="end">
                                    <v-col cols="1" md="2">
                                        <v-text-field  style="text-align: center;" v-model="PINeins" :rules="pinRules" center dark></v-text-field>
                                    </v-col>
                                    <v-col cols="1" md="2">
                                        <v-text-field v-model="PINzwei" :rules="pinRules" dark></v-text-field>
                                    </v-col>
                                    <v-col cols="1" md="2" >
                                        <v-text-field v-model="PINdrei" :rules="pinRules" dark></v-text-field>
                                    </v-col>
                                    <v-col cols="1" md="2">
                                        <v-text-field v-model="PINvier" :rules="pinRules" dark></v-text-field>
                                    </v-col>
                                </v-row>
                            </v-container>
                    
                        </v-card>
                        <div v-if="bestätigungshilfe === true" style="color: white">
                        Was Sie vor sich sehen, ist eine 2-Faktor-Authentifizierung. Dies ist eine Sicherungsmethode des Messenger-Dienstes, der Registrierungen ohne Zugriff auf die angegebene E-Mail-Adresse verhindern soll. Dabei wir Ihnen auf die angegebene E-Mail-Adresse eine Mail mit einem Code gesendet. Bitte geben Sie diesen Code in die oben stehenden Felder ein. Beachten Sie, dass pro Feld nur eine Zahl des vierstelligen Codes vorgesehen ist.
                        </div>
                    </v-card-text>

                    <v-divider></v-divider>

                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <div>
                            <v-btn color="white" text position = "right" @click= "bestätigungshilfe = !bestätigungshilfe">
                                Hilfe
                            </v-btn>
                            <v-btn color="#444bab" dark  v-on:click="verify()">
                                Bestätigen
                            </v-btn>
                        </div>
                    </v-card-actions>
                </v-card>
            </template>
        </v-dialog>

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
import axios from "axios";
  export default {
    
    name: 'Registrieren',
    
    data(){
      return {
        verifyPin: false,
      e1: 3,
      PINeins: null,
      PINzwei: null,
      PINdrei: null,
      PINvier: null,
      headeranzeigen: false,
      checkbox: true,
      dialog: false,
      test: 'nicht betätigt',
      settings_checkbox: false,
      companyaccount_checkbox: false,
      hilfe_checkbox: false,
      firmenkontenakzeptieren: false,
      valid: false,
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
        
      ],
      show: false,
      imageRules: [
        value => !value || value.size < 32000 || 'Deine Datei ist größer als die maximal erlaubte Größe',
      ],
      base64: null
    }
    },
    mounted(){

      EventBus.$on("LOGOUT", (payload)=>{
        this.clearFields();
      })
    },

    methods: {

      clearFields() {
        this.$refs.benutzername.clearableCallback()
        this.$refs.passwort.clearableCallback()
      },


        login() {
            fetch(window.IP_ADDRESS + '/GFOS/daten/nutzer/login', {
                mode: 'cors',
                method: 'POST',
                headers: {
                    'Accept': 'application/json', // Antwort von Server
                    'Content-Type': 'text/plain', // Wird an den Server gesendet
                },
                body: JSON.stringify({
                    benutzername: this.benutzername,
                    passwort: this.passwort,
                })
            }).then(response => {
                response.clone();
                response.json().then(content => {
                    if (content['id'] !== undefined) {
                        EventBus.$emit('LOGIN', {content});
                    } else if (content === 'verify') {
                        this.verifyPin = true;
                    } else if (content === 'passwordWrong') {
                        EventBus.$emit('INFOMESSAGE', {'message': 'Du hast das falsche Passwort eingegben!', "icon": "ERROR"});
                    } else if (content === "usernameWrong"){
                        EventBus.$emit('INFOMESSAGE', {'message': 'Der eingebene Nutzername existiert nicht!', "icon": "ERROR"});
                    }
                }); 
            })
            
        },
        submit() {
              fetch(window.IP_ADDRESS + '/GFOS/daten/nutzer/add', {
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
                      info: this.info,
                      base64: window.CURRENT_IMAGE
                  })
              }).then(response => {
                  response.clone();
                  response.json().then(content => {
                  }); 
              });
            
            
        },
        verify() {
            if (this.PINeins === undefined || this.PINzwei === undefined || this.PINdrei === undefined || this.PINvier === undefined) {
                return;
            }
            fetch(window.IP_ADDRESS + '/GFOS/daten/nutzer/verify', {
                mode: 'cors',
                method: 'POST',
                headers: {
                    'Accept': 'application/json', // Antwort von Server
                    'Content-Type': 'text/plain', // Wird an den Server gesendet
                },
                body: JSON.stringify({
                    name: this.benutzername,
                    pin: '' + this.PINeins + this.PINzwei + this.PINdrei + this.PINvier
                })
            }).then(response => {
                response.clone();
                response.json().then(content => {
                    if (content['id'] !== undefined) {
                        this.verifyPin = false;
                        EventBus.$emit('LOGIN', {content});
                    }
                }); 
            });
        },


        getBase64(file) {
          var reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload = function () {
            var base64 = reader.result
            this.base64 = base64
            window.CURRENT_IMAGE = base64
          };
          
          reader.onerror = function (error) {
            console.error('getBase64 Error: ', error);
          };
        }
    }
    
  }
</script>
