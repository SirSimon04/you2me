<template>
  <v-app>
    <v-app-bar
      app
      color="primary"
      dark
    >
      <div class="d-flex align-center">
        <v-img
          alt="GFOS-Logo"
          class="shrink mr-2"
          contain
          src="https://www.google.com/search?q=GFOS&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjR6MHw4MruAhURr6QKHZTRDQMQ_AUoAXoECA4QAw&biw=1280&bih=881#imgrc=3yx9PflhUfMLgM"
          transition="scale-transition"
          width="40"
        />

        <h1>Mäßenjer</h1>
      </div>
    
    </v-app-bar>

    <v-main>
            
            <v-card
      class="mx-auto"
      max-width="344"
    >
      <v-img
        src=""
        height="200px"
      ></v-img>
  
      <v-card-title>
        Warnung!
      </v-card-title>
  
      <v-card-subtitle>
        Funktionalität gefährdert
      </v-card-subtitle>
  
      <v-card-actions>
       <v-btn
              icon
              @click="app_vue_registrieren_zeigen = !app_vue_registrieren_zeigen"
              
            >
              <v-icon>{{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
            </v-btn>
           
            <v-btn
              icon
              @click="app_vue_settings_zeigen = !app_vue_settings_zeigen"
              
            >
              <v-icon>{{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
            </v-btn>
  
        <v-spacer></v-spacer>
  
    
      </v-card-actions>
  
      
          <v-divider></v-divider>
  
          <v-card-text>
            Der "Mäßenger" ist momentan in einer nicht funktionsfähigen Alpha-Version. Kommen Sie später wieder, um gravierende Fortschritte erleben zu können!
          </v-card-text>
        
    </v-card>

      <ChatListe/>
      <Chat/>
      <Registrieren v-show ="app_vue_registrieren_zeigen"></Registrieren>
      <Settings v-show ="app_vue_settings_zeigen"></Settings>
    </v-main>
  </v-app>
</template>

<script>

import ChatListe from './components/ChatListe';
import Registrieren from './components/Registrieren';
import Chat from './components/Chat';
import Settings from './components/Settings';

export default {
  name: 'App',

  components: {
    ChatListe,
    Registrieren,
    Chat,
    Settings,
  },

  data: () => ({
    app_vue_settings_zeigen: false,
    app_vue_registrieren_zeigen: false,
  }),
  
};
</script>