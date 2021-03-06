<template>
  <v-app>
    <v-app-bar
      app
      style="background-color: #202b36;"
      dark
      
      
    >
      <div class="d-flex align-center" >
        <v-img
          alt="GFOS-Logo"
          class="shrink mr-2"
          contain
          src="https://www.google.com/search?q=GFOS&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjR6MHw4MruAhURr6QKHZTRDQMQ_AUoAXoECA4QAw&biw=1280&bih=881#imgrc=3yx9PflhUfMLgM"
          transition="scale-transition"
          width="40"
          
        />

        <h1>Messengerprojekt</h1>
      </div>
    
    </v-app-bar>

    <v-main>
            
            

      <!--<ChatListe/>-->
      <Chat v-show="app_vue_chat_zeigen"></Chat>
      <Registrieren v-show ="app_vue_registrieren_zeigen"></Registrieren>
      <Settings v-show ="app_vue_settings_zeigen"></Settings>
      <Welcome v-show="welcome_vue_zeigen_"></Welcome>
    </v-main>
  </v-app>
</template>

<script>

//import ChatListe from './components/ChatListe';
import Registrieren from './components/Registrieren';
import Chat from './components/Chat';
import Settings from './components/Settings';
import Welcome from './components/Welcome';

export default {
  name: 'App',

  components: {
    //ChatListe,
    Registrieren,
    Chat,
    Settings,
    Welcome,
  },

  data: () => ({
    app_vue_settings_zeigen: false,
    app_vue_registrieren_zeigen: false,
    app_vue_chat_zeigen: false,
    welcome_vue_zeigen_: true,
    
  }),
  
};
</script>