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
     
      <Chatliste/>
      <Registrieren/>
    </v-main>
  </v-app>
</template>

<script>

import Chatliste from './components/Chatliste';
import Registrieren from './components/Registrieren'

export default {
  name: 'App',

  components: {
    
    Chatliste,
    Registrieren,
  },

  data: () => ({
    //
  }),
};
</script>
