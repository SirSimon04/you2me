<template>
    <v-container>
        <v-card
      class="mx-auto"
      max-width="1000"
      style="background-color: #202b36; border-radius: 15px; padding: 16px;"
    >
      
      <v-card-title
      style="color: white;"
      >
        Willkommen
      </v-card-title>
  
      <v-card-subtitle
      style="color: white;"
      >
        Funktionalität gefährdert
      </v-card-subtitle>
  
      <v-card-actions>
       
            <v-btn
            center>
              Mäßenjer starten
            </v-btn>
  
        <v-spacer></v-spacer>
  
    
      </v-card-actions>
  
      
          <v-divider></v-divider>
  
          <v-card-text
          center
          style="color: white;"
          >
            Herzlich willkommen beim Mäßenjer. Melden Sie sich an, um alle Funktionen nutzen zu können.
          </v-card-text>
        
    </v-card>
    </v-container>
</template>

<script>
  export default {
    name: 'Chat',

    data: () => ({
    
    }),

    mounted() {
        
  }
}
</script>