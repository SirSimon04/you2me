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
      <v-container>
        <v-row align="center">
          <img src ="../assets/disputatio_huge.png" style="position: relative; left: calc(50%)"/>
        </v-row>
      </v-container>
      </v-card-title>
  
     
  
      <v-card-actions center>
       
            <v-btn
            center
            style="position: relative; left: 385px"
            >
              Disputatio starten
            </v-btn>
  
        <v-spacer></v-spacer>
  
    
      </v-card-actions>
  
      
          <v-divider></v-divider>
  
          <v-card-text
          center
          style="color: white; text-align: center"
          
          >
            Herzlich Willkommen bei Disputatio. <br>Fangen Sie direkt an, indem Sie sich anmelden!
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