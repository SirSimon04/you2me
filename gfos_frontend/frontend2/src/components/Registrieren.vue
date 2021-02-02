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
            Abbrechen
          </v-btn>
          <v-checkbox
            v-model="checkbox"
            :label="'Ich bin bereits registriert'"
            ></v-checkbox>
        </v-stepper-content>
  
        <v-stepper-content step="2">
          <v-card
            class="mb-12"
            color="white"
            height="200px"
          >
          <v-text-field
              v-model="vorname"
              :rules="nameRules"
              :counter="15"
              label="Vornamen eingeben"
              
            ></v-text-field>
            <v-text-field
              v-model="nachname"
              :rules="nameRules"
              :counter="15"
              label="Nachnamen eingeben"
              
            ></v-text-field>
          </v-card>
  
          <v-btn
            color="primary"
            @click="e1 = 3"
          >
            Weiter
          </v-btn>
  
          <v-btn text>
            Abbrechen
          </v-btn>
        </v-stepper-content>
  
        <v-stepper-content step="3">
          <v-card
            class="mb-12"
            color="grey lighten-1"
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
              v-model="passwort"
              :rules="passwordRules"
              label="Passwort"
              required
            ></v-text-field>
          </v-card>
  
          <v-btn
            color="primary"
            @click="e1 = 1"
          >
            Continue
          </v-btn>
  
          <v-btn text>
            Cancel
          </v-btn>
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>
    </v-container>
</template>

<script>
  export default {
    name: 'Registrieren',
  }
  
    
  
  

</script>