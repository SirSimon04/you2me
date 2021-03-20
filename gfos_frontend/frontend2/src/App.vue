<template>
    <v-app style="background-color: #17212B;">
        <v-app-bar app style="background-color: #17212B;" dark>
            <div class="d-flex align-center" >
                <template lang="html">
                    <img src= "./assets/disputio_medium.png" />
                    <!--<img src="./assets/disputio_sign.png" />-->
                </template>
                <h1>{{user}}</h1>
            </div>
        </v-app-bar>
        
        <v-main>
            <ChatListe v-show="app_vue_chatliste_zeigen"></ChatListe>
            <Chat v-show="app_vue_chat_zeigen"></Chat>
            <Registrieren v-show ="app_vue_registrieren_zeigen"></Registrieren>
            <Settings v-show ="app_vue_settings_zeigen"></Settings>
            <Welcome v-show="welcome_vue_zeigen"></Welcome>
        </v-main>
    </v-app>
</template>

<script>
//import { EventBus } from './components/EventBus.js';
import ChatListe from './components/ChatListe';
import Registrieren from './components/Registrieren';
import Chat from './components/Chat';
import Settings from './components/Settings';
import Welcome from './components/Welcome';


export default {
    name: 'App',
  
    components: {
        ChatListe,
        Registrieren,
        Chat,
        Settings,
        Welcome,
    },
    
    data: () => ({
        welcome_vue_zeigen: false,
        app_vue_registrieren_zeigen: true,
        app_vue_settings_zeigen: false,
        app_vue_chatliste_zeigen: true,
        app_vue_chat_zeigen: true,
        user: Registrieren.benutzername,
    }),    
};
</script>