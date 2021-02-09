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

        <h1>GFOS Messenger-Projekt</h1>
      </div>
    
    </v-app-bar>

    <v-main>
     
      <ChatListe/>
      <Chat/>
      <Registrieren/>
      <Settings/>
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
  }),
  
};
</script>