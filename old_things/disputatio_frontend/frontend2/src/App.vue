<template>
    <v-app dark style="background-color: #17212B;">
        <v-app-bar app :color="getColor()" >
            <div class="d-flex align-center" >
                <template lang="html">
                    <img src= "./assets/disputio_medium.png" />
                    <!--<img src="./assets/disputio_sign.png" />-->
                </template>
            </div>
            <v-spacer/>
            <v-tooltip
            bottom
            >
                <template v-slot:activator="{ on, attrs }">
                    <v-btn
                    v-bind="attrs"
                    v-on="on"
                    :color="getColor()"
                    @click="reload();"
                    v-show="loggedIn"
                    elevation="0"
                    >
                        <v-icon
                        v-if="darkmode"
                        color="white"
                        >
                            mdi-reload
                        </v-icon>
                        <v-icon
                        v-else
                        color="black"
                        >
                            mdi-reload
                        </v-icon>
                    </v-btn>
                </template>
                <span>
                    Lade die Daten neu
                </span>
            </v-tooltip>
            <v-tooltip
            bottom
            >
                <template v-slot:activator="{ on, attrs }">
                    <v-btn
                    v-show="loggedIn"
                    v-bind="attrs"
                    v-on="on"
                    :color="getColor()" 
                    elevation="0"
                    @click="logout()"
                    >
                        <v-icon
                        v-if="darkmode"
                        color="white"
                        >
                            mdi-logout
                        </v-icon>
                        <v-icon
                        v-else
                        color="black"
                        >
                            mdi-logout
                        </v-icon>
                    </v-btn>
                </template>
                <span>
                    Logge dich aus
                </span>
            </v-tooltip>
            
        </v-app-bar>
        
        <v-main>

            <v-tabs
            v-model="tab"
            background-color="blue"
            grow
            v-show="app_vue_navbar_zeigen"
            class="mx-auto"
            color="white"
            >
                <v-tab
                href="#chat"
                >
                    Chats
                </v-tab>
                <v-tab-item
                value="chat"
                >
                    <v-card
                    :color="getColor()"
                    height="2000px"
                    >
                        <v-layout row wrap >
                        <v-flex xs5>
                        <ChatListe style="position: relative; float: left; left: 0; top: 0;"></ChatListe>
                        
                        </v-flex>
                        <v-flex>
                            <v-card 
                            v-show="!app_vue_chat_loaded_once && darkmode"
                            style="min-height: 400px; height:1000; max-height:1000px; border-radius: 15px; padding: 16px;"
                            color="#0E1621"
                            >
                                <v-card-text>
                                    <div
                                    style="height: 10 em; posiition: relative; display: flex; align-items: center; justify-content: center "
                                    >
                                        <p
                                        style="margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: white; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                        >
                                        Du hast noch keinen Chat geöffnet. Klicke auf einen Chat in der Liste links oder erstelle einen neuen Chat.
                                        </p>


                                    </div>
                                </v-card-text>
                            </v-card>
                            <v-card 
                            v-show="!app_vue_chat_loaded_once && !darkmode"
                            style="min-height: 400px; height:1000; max-height:1000px; border-radius: 15px; padding: 16px;"
                            color="white"
                            >
                                <v-card-text>
                                    <div
                                    style="height: 10 em; posiition: relative; display: flex; align-items: center; justify-content: center "
                                    >
                                        <p
                                        style="margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: black; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                        >
                                        Du hast noch keinen Chat geöffnet. Klicke auf einen Chat in der Liste links oder erstelle einen neuen Chat.
                                        </p>


                                    </div>
                                </v-card-text>
                            </v-card>
                            <v-card
                            max-height="50px"
                            mix-width="400"
                            color="blue"
                            v-show="app_vue_chat_loaded_once"
                            >
                                <v-card-title>
                                    {{curent_chat_name}}
                                </v-card-title>
                            </v-card>
                            <Chat v-show="app_vue_chat_loaded_once" style="position: relative; float: right; right: 0; top: 0;"></Chat>
                        </v-flex>
                    </v-layout>
                    
                    </v-card>
                    
                </v-tab-item>

                <v-tab
                href="#channel"
                >                    
                    Channel
                </v-tab
                >

                <v-tab-item
                value="channel"
                >
                    <v-card
                    :color="getColor()"
                    height="2000px"
                    >
                        <Channel />
                    </v-card>
                </v-tab-item>

                <v-tab
                href="#friends"
                >
                    Freunde
                </v-tab>

                <v-tab-item
                value="friends"
                >
                    <v-card
                    :color="getColor()"
                    height="2000px"
                    >
                        <FriendList/>
                    </v-card>
                    
                </v-tab-item>

                <v-tab
                href="#settings"
                >
                    Einstellungen
                </v-tab>

                <v-tab-item
                value="settings"
                >
                    <v-card
                    :color="getColor()"
                    >
                        <Settings/>
                    </v-card>
                </v-tab-item>

            </v-tabs>

            


            <NachrichtenInfo v-show="app_vue_nachrichteninfo_zeigen"
            ></NachrichtenInfo>
            <Settings v-show="app_vue_settings_zeigen"/>
            <ChatInfo v-show="app_vue_chatinfo_zeigen"></ChatInfo>
            <NewGroup v-show="app_vue_newgroup_zeigen"/>
            

            <Registrieren v-show ="app_vue_registrieren_zeigen"></Registrieren>
            <Welcome v-show="welcome_vue_zeigen"></Welcome>
            <Impressum v-show="app_vue_impressum_zeigen"/>
            <v-btn 
            v-if="tab === 'chat' && loggedIn"
            style="z-index: 2000; position: fixed; right: 30px; bottom: 30px" 
            class="mx-2" 
            fab 
            dark color="indigo" 
            @click="newGroup();">
                <v-icon dark>mdi-plus</v-icon>
            </v-btn>
            <v-btn 
            v-if="tab === 'chat' && loggedIn"
            style="z-index: 2000; position: fixed; left: 30px; bottom: 30px" 
            class="mx-2" 
            fab 
            dark 
            :color="getColor()" 
            @click="changeArchive();"
            >
                <v-img
                max-height="40px"
                max-width="40px"
                :src="getArchiveImg()"
                >
                </v-img>
            </v-btn>
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
                <v-row
                align="center"
                >
                    <v-col>
                            <v-img
                            :src="infoMessageIcon"
                            >
                            </v-img>
                           
                    </v-col>

                    <v-col>
                        <pre style="color: white;">{{infoMessageContent}}</pre>
                    </v-col>
                </v-row>
                
                
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
import FriendList from './components/FriendList';
import NachrichtenInfo from './components/NachrichtenInfo';
import Channel from "./components/Channel.vue";
import NewGroup from "./components/NewGroup.vue"

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
        FriendList,
        NachrichtenInfo,
        Channel,
        NewGroup,
    },
    
    data: () => ({
        infoMessage: false,
        infoMessageContent: '',
        infoMessageIcon: "",
        infoMessageTimeout: 2000,

        welcome_vue_zeigen: false,
        app_vue_registrieren_zeigen: true,
        app_vue_settings_zeigen: false,
        app_vue_chatliste_zeigen: false,
        app_vue_chat_zeigen: false,
        app_vue_chatinfo_zeigen: false,
        app_vue_impressum_zeigen: false,
        app_vue_freundesliste_zeigen:false,
        app_vue_navbar_zeigen: false,
        app_vue_nachrichteninfo_zeigen: false,
        app_vue_chat_loaded_once: false,
        app_vue_channel_zeigen: false,
        app_vue_newgroup_zeigen: false,
        loggedIn: false,
        user: Registrieren.benutzername,

        tab: "",
        curent_chat_name: "",
        darkmode: true,
        archived_open: false
    }),

    methods: {
        newGroup(){
            this.app_vue_newgroup_zeigen = true
            EventBus.$emit("NEW_GROUP", {});
        },

        logout() {
            EventBus.$emit("LOGOUT", {});
        },

        reload() {
            EventBus.$emit("RELOAD", {});
        },

        getColor() {
            if(this.darkmode){
                return "#17212B"
            }
            else{
                return "#CDCDCD"
            }
        },
        changeArchive(){
            this.archived_open = !this.archived_open
            window.ARCHIVED_OPEN = !window.ARCHIVED_OPEN
        },

        getArchiveImg(){
            if(!this.archived_open){
                return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAI6gAACOoBxNcq+QAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAO1SURBVFiFvZZNax1VGMd/58zMnZmb1NzbWOjC1GDrRijoptBN8QuIuMimLsRN+glEFJSgC6HfoFmIFAqVIAh+gbgShYpQdKPGvkGSmuTeJPN2z5xzHhdJb5smaW4k9/5hFjNnzvP7n5nnec5RvEAffvL1mTCJr4Zx8+0oTd4IomQyipvjUZw20BCEsQJwtid4qHulqas8c6a3bqryd1+Xi1Vob9389IP1wxjqIGhjfOJ6s9V+Z3zy7KQOwhd5PFLeWfKN1bW8u/FDuZ19dPOrvWb6BmZnb0TBa2dut6cuvBc1kn3GTkK1qWTj4Z/f+aW1q/Pz1+q+gdnZG1F68fxS6+y5V4YBfl7dlXsPy7v/nJ+fv1ZrAHm19eWo4ACts9NTfrr9BYAG0E7eHBX8iQLj3+obsN1MF+v/jgxerD3GdPOgbwDRoVneorO0NHR4Z+kvzHKGVioA6NdYGCZgYP23P1DjIS9NTRPGjROBOtNj8/59fG5JkgmIno7tK/I4mQAL5d+rVGYTQgjHU9LWaeJTp1BH9AVxlmpri163Q52XYCGJJ2ioMUj2v39oNKU1adLeuanArhTY5RxrezhfA4IXD4BWGlAEQUQYxKAUIcnOVz2ijx2vzSlFGCWEBy3lf0oDeC9yYhEH1BOmBtjeuGe8tyODO1ezuX7P9A3UtopXHt6hNvnQ4cYUrDz6FeeqGJ7JAWsrlh/dIR07Q6t9jqgxdrLgXsZW5wFltoaop398bxKKUGaPKbNVgjAlTlqkzTZx2iIMj9cTnDVUZZeq6FBVXZytdgae22cPqQKFsxVFtkKRrQCgVYgOInQQoXRIoANE7URTIjjvEG/xrt65ZLCcGrgMvVi8tWDLQacMpN29QIZyAHmhdpkaoOrlJYyyFQjGFEXfgDVFkm93GIkJgXy7g+nlKTyTA1W5ha0rmuNtokY6FHZtCvKsi7OG3fzdm4TWGra6qwRBRCNpEkUpUSPmgMPzYBLB2h7GlPSqHO/2V8aBVeBcTZlvUrKJUju7nA4bBEGwW44ahebJMkQ8IHjn8WJxzuGswbsaOWKbObIMRQRrDVgzyJqPLT2UqMc1IMgvowaLqJ/7Biab5efAgxHy71+5NP1Z38DCwoKZHMtfF/gWGV4zUOARffvKpekLc3NzdvfZXl2emTndzJvXBd4F9fLJoGVNaf19kWYf/7SwsPGcqcN1eWbmdFqk74O6gqiLgkwq1CmliERQz8wXpRARjCCZUmoN0XeVdj+6TN1aXPymexjjP1c2shSEXShEAAAAAElFTkSuQmCC"
            }
            else{
                return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAA7VBMVEUAAABuibh1l8R2msh6pdJ7pNR+qtl2ncluj7dkhqpnha1xlb59qNZ/q9ppiK5vk7xlhalngqhjcpx1mMWMn8jBcX5vf7f0Qza5Ym5vf7lodqlxgbtygb1tfLOCfLBzg79hb51se7B3m8l6eKBjcZ5wf7ltfbVvfrdzgr5gbJhVX4BSXH1PWXZZZIlPWHRVYIBsfLJfa5VVX39QWXdVYYFYZIhvfrhwgLpfbJhWX4FcaZJvZ4tsY4RXYYRnXXhVYH9HUWlJUmpDS19KU25CSmBFTmZDTGFNVnNRW3hLVW9XSlxgVGxWSVumRkgAAADwi0nCAAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAHdElNRQflBBsIDCxouySYAAABmklEQVQ4y32T21qCQBSFdUQrk46maaOT5RQSSAIdsNJStJO+/+u0NieV1HXDfPP/e7G5IJOJk2Usp+QLBUXZYbt7mdUU90uqClhQk5RYMcEHSnS5xCnKQcgPk5uUoKqHxI/ULaFdjrcJxxBOTjfA8lmZnZBQya7n1ep5jYT6BW/U8isMn1sGrzbFJTUwLgRvtbJXJbUczuZz7Jo453UIbQkB4Uw2Wo3Gzc2tFKIZcBE0tHkkcM5wrDEcJXEGsR0ITHJNE1HIDuYxIVgHgo7RFUEL5qlHyDsIhlmh8ljQtHCelqqYXRIsqyNlIoiYi/uebTkk2K7tPvTqjxI7Shbu/1R/Ni3EIAFcd13d88x+v/8C/mq+WVGMuMG1dRi6PgAfeDG2hiR0wW2UkBDylPA+tEgB91LzED4gjMZDegkaAq4vzXvjEQn+xMGW0fstFA1DbhsTPxR8f/rpRJwE6rC/vicAsYD8gP8ahuMYxuf3eDINbxfCDHzmB+doYiGMEv4/ccMm7vv0Y+Ax38bJmM238XiPdKapP3y0fvYPB6KGvaEQR2kAAAAldEVYdGRhdGU6Y3JlYXRlADIwMjEtMDQtMjdUMDg6MTI6MDkrMDA6MDDR+f4hAAAAJXRFWHRkYXRlOm1vZGlmeQAyMDIxLTA0LTI3VDA4OjEyOjA5KzAwOjAwoKRGnQAAAABJRU5ErkJggg=="
            }
            
        }
    },


    mounted() {

        var imageContainer = new ImageContainer();

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
            this.infoMessageIcon = imageContainer.getValue(payload["icon"])
            document.getElementById('infoMessage').click();
        }); // EventBus.$on('INFOMESSAGE')

        EventBus.$on('LOGIN', (payload) => {
            window.CURRENT_USER_ID = payload['content']['id'];
            window.CURRENT_TOKEN = payload['content']['token'];
            window.CURRENT_IMAGE = payload["content"]["profilbild"];
            window.CURRENT_USERNAME = payload["content"]["benutzername"];
            this.app_vue_registrieren_zeigen = false;
            this.app_vue_navbar_zeigen = true;
            this.loggedIn = true;
            this.tab = "chat"
            if(payload["content"]["darkmode"]){
                EventBus.$emit("DARKMODE", {"darkmode": true})
            }
            else{
                EventBus.$emit("DARKMODE", {"darkmode": false})
            }
            window.DARK_MODE = this.darkmode
        }); // EventBus.$on('LOGIN')

        EventBus.$on("LOGOUT", (payload) =>{
            window.CURRENT_USER_ID = -1;
            window.CURRENT_TOKEN = '';
            window.CURRENT_IMAGE = undefined;
            window.CURRENT_USERNAME = "";
            window.CURRENT_CHAT_NAME = "";
            this.welcome_vue_zeigen = false,
            this.app_vue_registrieren_zeigen = true,
            this.app_vue_settings_zeigen = false,
            this.app_vue_chatliste_zeigen = false,
            this.app_vue_chat_zeigen = false,
            this.app_vue_chatinfo_zeigen = false,
            this.app_vue_impressum_zeigen = false,
            this.app_vue_freundesliste_zeigen =false,
            this.app_vue_navbar_zeigen = false,
            this.app_vue_nachrichteninfo_zeigen = false,
            this.app_vue_chat_loaded_once = false,
            this.app_vue_channel_zeigen = false,
            this.app_vue_newgroup_zeigen = false;
            this.loggedIn = false
            this.tab = ""
            this.darkmode = true
            EventBus.$emit("DARKMODE", {"darkmode": true})
        });

        EventBus.$on("RELOAD", (payload) =>{

        });

        EventBus.$on("DARKMODE", (payload)=>{
            this.darkmode = payload["darkmode"]
        });

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

        EventBus.$on("OPENMESSAGEINFO", (payload) => {
            this.app_vue_nachrichteninfo_zeigen = true;
        });

        EventBus.$on('CLOSEMESSAGEINFO', (payload) => {
            this.app_vue_nachrichteninfo_zeigen = false;
        }); // EventBus.$on('CLOSECHATINFO')

        EventBus.$on('OPENCHAT', (payload) => {
            this.curent_chat_name = payload["name"]
            this.app_vue_chat_loaded_once = true;
            this.tab = "chat"
        }); // EventBus.$on('OPENCHAT')

        EventBus.$on("CLOSE_NEW_GROUP", (payload) =>{
            this.app_vue_newgroup_zeigen = false
        });

        

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
window.CURRENT_IMAGE = undefined;
window.CURRENT_USERNAME = "";
window.CURRENT_CHAT_NAME = "";
window.IP_ADDRESS = 'http://localhost:8080';
window.DARK_MODE = true


</script>