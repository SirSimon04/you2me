
<template>
    <v-container>
        <v-stepper v-model="e1">
      <v-stepper-header>
        <v-stepper-step
          :complete="e1 > 1"
          step="1"
        >
          Benutzernamen erstellen
        </v-stepper-step>
  
        <v-divider></v-divider>
  
        <v-stepper-step
          :complete="e1 > 2"
          step="2"
        >
          Daten angeben
        </v-stepper-step>
  
        <v-divider></v-divider>
  
        <v-stepper-step step="3">
          Login
        </v-stepper-step>
      </v-stepper-header>
  
      <v-stepper-items>
        <v-stepper-content step="1">
          <v-card
            class="mb-12"
            color="white"
            height="200px"
          >
          <subheading>Schritt 1:</subheading>
          <h4>Benuzternamen erstellen</h4>
          <v-text-field
              v-model="Benuztername"
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
  
          <v-btn
            color="primary"
            @click="e1 = 2"
          >
            Weiter
          </v-btn>
  
          <v-btn text>
            Impressum
          </v-btn>
          
        </v-stepper-content>
  
        <v-stepper-content step="2">
          <v-card
            class="mb-12"
            color="white"
            height="500px"
          >
          <subheading>Schritt 2:</subheading>
          <v-text-field
              v-model="vorname"
              :rules="nameRules"
              :counter="15"
              label="Vornamen eingeben*"
              required
              
            ></v-text-field>
            <v-text-field
              v-model="nachname"
              :rules="nameRules"
              :counter="15"
              label="Nachnamen eingeben*"
              required
              
            ></v-text-field>
            <v-text-field
              v-model="email"
              :rules="emailRules"
              :counter="15"
              label="E-Mail eingeben"
              
            ></v-text-field>
            <v-text-field
              v-model="info"
              :rules="nameRules"
              :counter="256"
              label="Info eingeben"
              
            ></v-text-field>
            <v-file-input
               truncate-length="15"
               label="Profilbild hinzufügen"
            ></v-file-input>
            <v-dialog
        v-model="dialog"
        width="500"
      >
        <template v-slot:activator="{ on, attrs }">
          
          <v-btn
        class="mx-2"
        
        dark
        small
        color="primary"
        v-bind="attrs"
            v-on="on"
      >
      
        <v-icon dark>
          Hilfe
        </v-icon>
        
      </v-btn>
        </template>
  
        <v-card>
          <v-card-title class="headline grey lighten-2">
            Informationen: "Info"
          </v-card-title>
  
          <v-card-text>
            Vorname:
            <br>
            Bitte geben Sie Ihren Vornamen an. Ihr Name wird für keinen Nutzer der App ersichtlich sein, kann aber - im Falle einer strafrechtlichen Verfolgung oder anderen rechtlichen Maßnahmen- an Hoheitsgewalten weitergegeben werden. Dieses Feld ist Pflicht.
            <br>
            <br>
            Nachname:
            <br>
            Bitte geben Sie in diesem Feld Ihren Nachnamen an. Das Feld ergänzt das oben stehende Feld "Vorname". Dieses Feld ist Pflicht.
            <br>
            <br>
            Handynummer:
            <br>
            Die Handynummer macht es für Sie bei der Nutzung der App leicht, Kontakte zu finden und von Ihren Kontakten gefunden zu werden. Dieses Feld ist nicht Pflicht.
            <br>
            <br>
            E-Mail:
            <br>
            Bitte geben Sie in diesem Feld eine E-Mail-Adresse ein, unter der wir Sie im Ernstfall erreichen können und die Zwei-Faktor-Authentifizierung nach der Registrierung durchführbar ist. Dieses Feld ist Pflicht.
            <br>
            <br>
            Info:
            <br>
            Die Info (Bio, Status) beschreibt Sie in kurzen Worten. Die Info kann von allen Nutzern der App gesehen werden, sofern diese Ihr Profil aufrufen. Geben Sie bitte keine Personenbezogenen Daten wie Handy- oder Kreditkartennummer an und vermeiden Sie die Nennung Ihres genauen Wohnortes.
            <br>
            <br>
            Profilbild:
            <br>
            Das Profilbild ist Ihr Gestaltungsbereich. Hier können Sie sich oder ein schönes Motiv für Ihre Kontakte anzeigen lassen. Die Datenschutzbedingungen zum Profilbild lassen sich ändern. Es ist keine Pflicht, ein Profilbild anzugeben.
          </v-card-text>
  
          <v-divider></v-divider>
  
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="primary"
              text
              @click="dialog = false"
            >
              Verstanden 
              <v-icon dark>
              mdi-checkbox-marked-circle
            </v-icon>
            </v-btn>
            <v-btn
              color="primary"
              text
              @click="dialog = false"
               > <!-- Allgemein wäre der zweite Knopf als "Link" zu einer Anleitungs-Website, die hilflose Angestellte im Zweifel nutzen können.-->
              Nähere Informationen
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
          </v-card>
  
          <v-btn
            color="primary"
            @click="e1 = 3"
          >
            Weiter
          </v-btn>
  
          <v-btn text
           @click="e1 = 1">
            Zurück
          </v-btn>
        </v-stepper-content>
  
        <v-stepper-content step="3">
          <v-card
            class="mb-12"
            color="white"
            height="200px"
          >
          
          <h4>Login</h4>
          <v-text-field
              v-model="Benuztername"
              :rules="nameRules"
              :counter="20"
              label="Benutzername"
              required
            ></v-text-field>
            <v-text-field
              v-model="password"
              :rules="nameRules"
              label="Passwort"
              required
            ></v-text-field>
          </v-card>
  
          <v-btn
            color="primary"
            @click= "einloggen"
          >
            Login
          </v-btn>
  
          <v-btn text
          @click= "e1 = 1">
            Registrieren
          </v-btn>
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>
    </v-container>
</template>
<style>

</style>
  .group {
  display: flex;
  flex: 1;
  justify-content: space-around;
  }
<script>
  export default {
    name: 'Registrieren',
    
    data(){
      return {
      e1: 3,
      checkbox: true,
      dialog: false,
    }
  

    }
  }
  
 
  
</script>
