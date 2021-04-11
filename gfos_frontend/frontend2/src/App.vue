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
            <v-layout row wrap >
                <v-flex xs5>
                    <ChatListe style="position: relative; float: left; left: 0; top: 0;" v-show="app_vue_chatliste_zeigen"></ChatListe>
                </v-flex>
                <v-flex>
                    <Chat style="position: relative; float: right; right: 0; top: 0;" v-show="app_vue_chat_zeigen"></Chat>
                </v-flex>
            </v-layout>
            <ChatInfo v-show="app_vue_chatinfo_zeigen"></ChatInfo>
            <Registrieren v-show ="app_vue_registrieren_zeigen"></Registrieren>
            <Settings v-show ="app_vue_settings_zeigen"></Settings>
            <Welcome v-show="welcome_vue_zeigen"></Welcome>
            <Impressum v-show="app_vue_impressum_zeigen"/>
        </v-main>
        <div class="text-center">
            <v-btn
                id="infoMessage"
                hidden
                dark
                color="orange darken-2"
                @click="infoMessage = true"
            >
                Open Snackbar
            </v-btn>
            <v-snackbar
                v-model="infoMessage"
                :timeout="infoMessageTimeout"
            >
                <pre style="color: white;">{{infoMessageContent}}</pre>
            </v-snackbar>
        </div>
    </v-app>
</template>

<script>
import { EventBus } from './components/EventBus.js';
import { ImageContainer } from './components/ImageContainer.js';
import ChatInfo from './components/ChatInfo';
import ChatListe from './components/ChatListe';
import Registrieren from './components/Registrieren';
import Chat from './components/Chat';
import Settings from './components/Settings';
import Welcome from './components/Welcome';
import Impressum from './components/Impressum';


export default {
    name: 'App',
  
    components: {
        ChatListe,
        Registrieren,
        Chat,
        Settings,
        Welcome,
        ChatInfo,
        Impressum,
    },
    
    data: () => ({
        infoMessage: false,
        infoMessageContent: '',
        infoMessageTimeout: 2000,

        welcome_vue_zeigen: false,
        app_vue_registrieren_zeigen: true,
        app_vue_settings_zeigen: false,
        app_vue_chatliste_zeigen: true,
        app_vue_chat_zeigen: true,
        app_vue_chatinfo_zeigen: false,
        app_vue_impressum_zeigen: false,
        user: Registrieren.benutzername,
    }),

    mounted() {
        var ALLOWED_KEYS = ['Control', 'Enter', 'Shift', 'Alt'];
        var pressedKeys = [];
        var commands = {
            'sendMessage': ['Control', 'Enter'],
        }

        window.app_interval = setInterval(function() {}, 1000);

        window.resetAppInterval = function() {
            clearInterval(window.app_interval);
            window.app_interval = setInterval(function() {
                EventBus.$emit('APP_INTERVAL', {});
            }, 1000);
        }

        window.resetAppInterval();

        EventBus.$on('INFOMESSAGE', (payload) => {
            this.infoMessageContent = payload['message'];
            var count = 1;
            for (var i=0; i<payload['message'].length; i++) {
                if (payload['message'][i] === ' ') count++;
            }
            this.infoMessageTimeout = parseInt(count * 0.3 * 1000);
            document.getElementById('infoMessage').click();
        }); // EventBus.$on('INFOMESSAGE')

        EventBus.$on('LOGIN', (payload) => {
            console.warn(payload['content']['id']);
            window.CURRENT_USER_ID = payload['content']['id'];
            window.CURRENT_TOKEN = payload['content']['token'];
            this.app_vue_registrieren_zeigen = false;
        }); // EventBus.$on('LOGIN')

        EventBus.$on('OPENCHATINFO', (payload) => {
            this.app_vue_chatinfo_zeigen = true;
            fetch(window.IP_ADDRESS + '/GFOS/daten/chat/info/' + payload["chatid"] + '/' + window.CURRENT_USER_ID + '/' + window.CURRENT_TOKEN).then(response => {
                response.clone();
                response.json().then(data => {
                    if (data['blockiertWorden']) {
                        EventBus.$emit('LOADPROFILE', {
                            'chatid': payload['chatid'],
                            'username': payload['name'],
                            'blocked': data['blockiert'],
                            'blockedMe': true,
                        });
                    } else {
                        EventBus.$emit('LOADPROFILE', {
                            'chatid': payload['chatid'],
                            'isGroup': data['isGroup'],
                            'username': payload['name'],
                            'profilepicture': payload['profilepicture'],
                            'description': data['beschreibung'],
                            'blocked': data['blockiert'],
                            'blockedMe': false,
                            'userdata': {
                                'userlist': data['nutzerliste'],
                                'user': data['nutzer'],
                                'commonChatlist': data['gemeinsameChatListe']
                            },
                        });
                    }
                })
            });
        }); // EventBus.$on('OPENCHATINFO')

        EventBus.$on('CLOSECHATINFO', (payload) => {
            this.app_vue_chatinfo_zeigen = false;
        }); // EventBus.$on('CLOSECHATINFO')

        function equals(array0, array1) {
            if (array0.length !== array1.length) return false;
            for (var i=0; i<array0.length; i++) {
                if (array0[i] !== array1[i]) {
                    return false;
                }
            }
            return true;
        } // equals(a0, a1)

        window.addEventListener("keydown", function (event) {
            if (pressedKeys.includes(event.key)) return;
            for (var i=0; i<ALLOWED_KEYS.length; i++) {
                if (event.key === ALLOWED_KEYS[i]) {
                    pressedKeys.push(event.key);
                    console.log(pressedKeys);
                    if (equals(pressedKeys, commands['sendMessage'])) {
                        EventBus.$emit('KSC_SENDMESSAGE', {});
                    }
                    break;
                }
            }
        }, true); // window.addEventListener("keydown")

        window.addEventListener("keyup", function (event) {
            pressedKeys = pressedKeys.filter(e => e !== event.key);
        }, true); // window.addEventListener("keyup")

    }
};
window.CURRENT_USER_ID = -1;
window.CURRENT_TOKEN = '';
window.IP_ADDRESS = 'http://339220729d3e.ngrok.io';


</script>