<template>
  <v-card
    style="background-color: #17212B; position: absolute; left: calc(50% - 187px); top: calc(50% - 200px - 500px); z-index:20000"
    class="mx-auto my-12"
    min-width="360"
    max-width="720"
    elevation="12"
  >
   
   <img style="position: relative; float: right; margin-top: 4px; margin-right: 4px;" width="20px" height="auto" @click="closeInfo" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQEAQAAADlauupAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QAAKqNIzIAAAAJcEhZcwAAAGAAAABgAPBrQs8AAAAHdElNRQflAxYUFyyU+7HbAAABY0lEQVQ4y5WS347BUBDGj030jmteAGl7gUvegiAeCg3xBP68BN6hVV5EG8TF/PZidlermt2dZJKT+eY733xzjjEvgbgueB6cTkgcI3EMpxN4HuK6Ji8Qy4LlEu53WK2QwQBaLc3hENZrxRYLxLKyZDkcwPeRWi1XhHodggDZ71OXqLLvI6WS+SWQUgmCAObzhOf7/VsZqlXodLLq3S5UKnpuNNSO4xhd2GqVbrxeod9/1vp9rXW7z9pmg0ynBs5nZDBIq/V6cLvBeKxkPad7RiMIQwNRBM1mduReDx4PzTRZ8XYbLpcPY8CYQiG7rne1VxxyLDzHTtrJsTCbwXr9/yVut8hkYsBx9EkaDQUqlWTj+2e0beXY9he4WOgP+8tHKpchDMHzEkXLQvZ7/WE6yVsytg3HI7LbIcXiy82WBfO5jrbZ6JLabc3RCLZbxTwvQ06rOA4yneqYUaQZhshk8uM5EZ8+/JkGG9VsvQAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAyMS0wMy0yMlQyMDoyMzo0NCswMDowMMlO3bkAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMjEtMDMtMjJUMjA6MjM6NDQrMDA6MDC4E2UFAAAAAElFTkSuQmCC">
    <v-container>
        <v-img v-if="isGroup && selfIsAdmin" id="pfp" style="position: relative; float: left; border-radius: 100%;" width="128" height="128" :src="image" @contextmenu="openUpload($event)" />
        <v-img v-else id="pfp" style="position: relative; float: left; border-radius: 100%;" width="128" height="128" :src="image"/>
        <v-card-title style="color: white;">{{username}}</v-card-title>
    </v-container>
    <br><br>
    
    <v-card-text>

        <div v-if="blockedMe">
            <span style="color: white;"><i>Du wurdest von diesem Nutzer blockiert</i></span>
        </div>
        <div v-else>
            <!-- Add icon for chatting -->

            <div v-if="user !== undefined" style="color: white;" class="my-2 subtitle-3">
                <span>Email: {{user.email}}</span><br>
                <span v-if="user.handynummer !== undefined">Handynummer: {{user.handynummer}}</span>
            </div>

            <div style="color: white; padding:20px">
                <span v-if="description === undefined"><i>Keine Beschreibung vorhanden</i></span>
                <span v-else>{{description}}</span>
            </div>
        </div>

        <v-spacer/>

        <v-divider
        v-show="isGroup"
        dark
        >
        </v-divider>

        <v-list
        v-show="isGroup"
        rounded
        color="#17212B"
        class="overflow-y-auto"
        >
            <v-subheader
            >
                <span
                style="color:grey; text-align:center; font-style:italic"
                >
                    MITGLIEDER
                </span>
            </v-subheader>

            <v-list-item-group
            v-model="model"
            dark
            color="blue"
            >
                <v-list-item
                v-for="(item, i) in userlist"
                :key="i"
                @contextmenu.stop="contextMenu($event, item.benutzername, item.isadmin, i);"
                >
                    <v-list-item-avatar>
                        <div v-if="item.profilbild === undefined">
                            <v-img 
                            src='data:image/png;base64,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'
                            ></v-img>
                        </div>
                        <div v-else>
                            <v-img
                            :src="item.profilbild.base64"
                            ></v-img>
                        </div>
                    </v-list-item-avatar>

                    <v-list-item-title
                    v-if="item.benutzername.localeCompare(benutzername) !== 0"
                    >
                        {{item.benutzername}}
                    </v-list-item-title>
                    <v-list-item-title
                    v-else
                    style="font-style:italic"
                    >
                        Du
                    </v-list-item-title>

                    <v-list-item-subtitle
                    v-if="item.isadmin"
                    >
                        <span
                        style="color:grey; text-align:center; font-style:italic"
                        >
                            Admin
                        </span>
                    </v-list-item-subtitle>
                </v-list-item>
            </v-list-item-group>
        </v-list>
    </v-card-text>

    <v-divider class="mx-4"></v-divider>

    <v-card-actions>
        <div v-if="isGroup">
            <v-btn
                color="red lighten-2"
                text
                @click="leaveGroup();"
            >
                <span>Verlassen</span>
            </v-btn>
        </div>
        <div v-else>
            <v-row
            >
                <v-col>
                    <v-btn
                    color="red lighten-2"
                    text
                    @click="blockUser(username);"
                    :key="username"
                >
                    <span v-if="blocked">Entblockieren</span>
                    <span v-else>Blockieren</span>
                </v-btn>
                </v-col>
                <v-spacer/>
                <v-col>
                    <v-btn
                    color="red lighten-2"
                    text
                    @click="deleteDialog = true"
                    >
                        Löschen
                    </v-btn>
                </v-col>
            </v-row>
           
            
        </div>
        <v-spacer>
        </v-spacer>
        <v-btn
        style="padding:10px"
        color="#006400"
        fab
        small
        @click="openAdd();"
        v-if="isGroup && userlist[0].isadmin"
        >
            <v-icon>
                mdi-plus
            </v-icon>
        </v-btn>
        <v-spacer>
        </v-spacer>
    </v-card-actions>
    <v-dialog
    v-model="dialog"
    max-width="290"
    >
        <v-card
        dark
        min-height="200px"
        >
            <v-card-title class="headline">
            {{removingUsername}}
            </v-card-title>

            <v-card-text>
                <div
                style="height: 10 em; posiition: relative; display: flex; align-items: center; justify-content: center "
                >

                    <v-btn
                    v-if="removingIsAdmin"
                    color="green darken-1"
                    style="margin: 0; position: absolute; top: 45%; transform: translate(0, -50%);"
                    @click="toAdmin();"
                    width="230px"
                    >
                    Adminstatus entfernen
                    </v-btn>
                    
                    <v-btn
                    v-if="!removingIsAdmin"
                    color="green darken-1"
                    style="margin: 0; position: absolute; top: 45%; transform: translate(0, -50%);"
                    @click="toAdmin();"
                    width="230px"
                    >
                    Zu Admin machen
                    </v-btn>

                    <v-btn
                    color="red darken-1"
                    style="margin: 0; position: absolute; top: 66%; transform: translate(0, -50%);"
                    @click="removeUser(removingUsername);"
                    width="230px"
                    >
                    Aus Gruppe entfernen
                    </v-btn>

                    <v-btn
                    color="darken-1"
                    text
                    style="margin: 0; position: absolute; top: 86%; transform: translate(0, -50%);"
                    @click="dialog = false"
                    >
                        Schließen
                    </v-btn>

                </div>
                
                <v-spacer/>
                
            </v-card-text>

            <v-card-actions>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog
    v-model="addDialog"
    max-width="320"
    >
        <v-card
        dark
        min-height="200px"
        color="#17212B"
        >
            <v-card-title class="headline"
            style="text-align:center;"
            >
            Wähle einen Nutzer zum Hinzufügen aus   
            </v-card-title>

            <v-card-text
            v-if="addingUsers.length !== 0"
            >
                <v-list
                rounded
                color="#17212B"
                class="overflow-y-auto"
                >
                    <v-list-item-group
                    v-model="addmodel"
                    dark
                    color="blue"
                    >
                        <v-list-item
                        v-for="(item, i) in addingUsers"
                        :key="i"
                        v-bind="attrs"
                        v-on="on"
                        >
                        <v-list-item-title>
                            {{item.benutzername}}
                        </v-list-item-title>
                        <v-list-item-action>
                            <v-btn
                            fab
                            color="green"
                            small
                            @click="addUser(item.benutzername);"
                            >
                                <v-icon>
                                    mdi-plus
                                </v-icon>
                            </v-btn>
                        </v-list-item-action>
                        </v-list-item>
                    </v-list-item-group>
                </v-list>
            </v-card-text>
            <v-card-text
            v-else
            >
                <div
                style="posiition: relative; display: flex; align-items: center; justify-content: center "
                >
                    <p
                    style="margin: 0; position: absolute; top: 55%; transform: translate(0, -50%); color: white; padding:30px; text-align:center"
                    >
                    Alle deine Freunde sind bereits in der Gruppe
                    </p>


                </div>
            </v-card-text>

            <v-spacer>
            </v-spacer>

            <v-card-actions>
                <v-spacer>
                </v-spacer>
                <v-btn
                dark
                v-if="addingUsers.length === 0"
                @click="addDialog = false"
                >
                    Schließen
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog
        v-model="deleteDialog"
        width="500"
        >
          <v-card
          color="white"
          >
            <v-card-title>
              <p
              style="color:red"
              >
                Chat löschen
              </p>
            </v-card-title>
            <v-card-subtitle
            style="color:red"
            >
              Möchtest du diesen Chat wirklich löschen? Diese Aktion ist nicht wiederrufbar und alle deine 
              Daten gehen verloren.
            </v-card-subtitle>
            <v-divider>
            </v-divider>
            <v-card-actions>
              <v-btn
              outlined
              width="200px"
              color="primary"
              @click="deleteDialog = false"
              >
                Abbrechen
              </v-btn>
              <v-spacer/>
              <v-btn
              color="red"
              width="200px"
              @click="deleteChat()"
              >
                <span
                style="color:white"
                >
                  Löschen
                </span>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-dialog
        v-model="imageDialog"
        width="500"
        >
          <v-card
          dark
          >
            <v-card-title>
              <p
              >
                Gruppeninfos verändern
              </p>
            </v-card-title>
            <v-card-text>
                <v-text-field
                v-model="gruppenname"
                dark
                style="color: white"
                label="Neuer Gruppenname"
                :rules="nameRules"
                >
                </v-text-field>
                <v-text-field
                v-model="gruppenbeschreibung"
                dark
                style="color:white"
                label="Neue Gruppenbeschreibung"
                :rules="desRules"
                >
                </v-text-field>
                <v-file-input
                    accept="image/*"
                    label="Gruppenbild hochladen"
                    v-model="profilbild"
                    dark
                    :rules="imageRules"
                    @change="getBase64(profilbild)"
                ></v-file-input>

            </v-card-text>
            <v-card-actions>
                <v-spacer/>
                <v-btn
                outlined
                width="250px"
                color="green"
                @click="changeInfos()"
                >
                    Informationen speichern
                </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
  </v-card>
</template>

<script>
import { EventBus } from './EventBus.js';
import { ImageContainer } from './ImageContainer.js';
import axios from "axios";

export default {
    name: 'ChatInfo',

    data: () => ({
        chatid: -1,
        username: '',
        description: undefined,
        isGroup: false,
        blockedMe: false,
        blocked: false,
        userlist: undefined,
        user: undefined,
        benutzername: window.CURRENT_USERNAME,
        selfIsAdmin: false,
        showRemove: false,
        dialog: false,
        removingUsername: "",
        removingIsAdmin: false,
        removingIndex: -1,
        addDialog: false,
        addingUsers: {},
        deleteDialog: false,
        image: "https://play-lh.googleusercontent.com/Jfi1sAHOOijxYrtXxfZp0b7hrHh7qh_PJ6NfELApffcoI7fJ30LgPn7cWTR9txbEUGmZ", //POPCAT
        imageDialog: false,
        imageRules: [
        value => !value || value.size < 32000 || 'Deine Datei ist größer als die maximal erlaubte Größe',
        ],
        nameRules: [
            value => value.length <= 50 || 'Der Name ist zu lang',
        ],
        desRules: [
            value => value.length <= 100 || 'Die Beschreibung ist zu lang',
        ],
        base64: null
    }),

    mounted() {
        
        var imageContainer = new ImageContainer();

        

        EventBus.$on('LOADPROFILE', (payload) => {
            this.chatid = payload['chatid'];
            this.username = payload['username'];
            this.description = undefined;
            this.isGroup = payload['isGroup'];
            this.blockedMe = payload['blockedMe'];
            this.blocked = payload['blocked'];
            this.userlist = undefined;
            this.user = undefined;

            this.image = (payload['profilepicture'] === undefined) ? imageContainer.getValue('USER') : payload['profilepicture'];

            if (!this.blockedMe) {
                this.description = payload['description'];
                var userData = payload['userdata'];
                if (this.isGroup) {
                    this.userlist = userData['userlist'];
                } else {
                    this.user = userData['user'];
                }
                this.selfIsAdmin = this.userlist[0]["isadmin"];
            }
        }); //EventBus.$on('LOADPROFILE')

        EventBus.$on("RELOAD", (payload) =>{
            this.loadData();
        });
    },

    methods: {
        blockUser(username) {
            if (window.CURRENT_USER_ID === -1 || window.CURRENT_TOKEN == '') return;
            fetch(window.IP_ADDRESS + '/GFOS/daten/nutzer/block/' + window.CURRENT_TOKEN, {
                mode: 'cors',
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'text/plain'
                },
                body: JSON.stringify({
                    eigeneId: window.CURRENT_USER_ID,
                    andererNutzerName: username,
                })
            }).then(response => {
                if (response.status !== 200) {
                    if(response.status === 401){
                        EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                        EventBus.$emit("LOGOUT", {})
                    }
                    return null;
                }
                response.clone();
                response.text().then(content => {
                    this.blocked = !this.blocked;
                });
            });
        },

        leaveGroup() {
            if (window.CURRENT_USER_ID === -1 || window.CURRENT_TOKEN == '') return;
            fetch(window.IP_ADDRESS + '/GFOS/daten/chat/leave/' + window.CURRENT_TOKEN, {
                mode: 'cors',
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'text/plain'
                },
                body: JSON.stringify({
                    eigeneId: window.CURRENT_USER_ID,
                    chatId: this.chatid,
                })
            }).then(response => {
                if (response.status !== 200) {
                    if(response.status === 401){
                        EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                        EventBus.$emit("LOGOUT", {})
                    }
                    return null;
                }
                response.clone();
                response.text().then(content => {
                    if (content === 'Einziger Admin') {
                        EventBus.$emit('INFOMESSAGE', {'message': 'Du bist der letzte Admin und kannst die Gruppe deswegen nicht verlassen.'})
                    }
                    else{
                        this.closeInfo();
                        EventBus.$emit('INFOMESSAGE', {'message': 'Du hast die Gruppe erfolgreich verlassen.', 'icon': 'SUCCESS'})
                    }
                    
                });
            });
        },

        closeInfo() {
            this.chatid = -1,
            this.username = '',
            this.description = undefined,
            this.isGroup = false,
            this.blockedMe = false,
            this.blocked = false,
            this.userlist = undefined,
            this.user = undefined,
            EventBus.$emit('CLOSECHATINFO', {});
        },

        contextMenu(e, name, isadmim, i){
            if(name === window.CURRENT_USERNAME)  {
                e.preventDefault();
                return
            }
            else if(this.userlist[0].isadmin){
                this.dialog = true;
                this.removingUsername = name
                e.preventDefault();
                this.showRemove = !this.showDelete
                this.removingIsAdmin = isadmim
                this.removingIndex = i
            }
            
        },

        toAdmin() {
            const payload = {
                "eigeneId": window.CURRENT_USER_ID,
                "chatId": this.chatid,
                "andererBenutzername": this.removingUsername
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/chat/zuAdmin/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                this.removingIsAdmin = !this.removingIsAdmin
                this.userlist[this.removingIndex]["isadmin"] = !this.userlist[this.removingIndex]["isadmin"]
            })
            .catch(error =>{
                console.error(error.response.data)
            })
        },

        loadData() {
            axios.get(window.IP_ADDRESS + "/GFOS/daten/nutzer/getAddable/" + this.chatid + "/" + window.CURRENT_USER_ID + "/" + window.CURRENT_TOKEN)
            .then((resp)=>{
                this.addingUsers = resp.data
            })
            .catch(error =>{
                console.error(error.response.status)
            })
        },

        loadUsers(){
            axios.get(window.IP_ADDRESS + "/GFOS/daten/chat/info/" + this.chatid + "/" + window.CURRENT_USER_ID + "/" + window.CURRENT_TOKEN)
            .then((resp)=>{
                this.userlist = resp.data.nutzerliste
            })
        },

        openAdd() {
            this.addDialog = true
            this.loadData()
        },

        addUser(name){
            const payload = {
                "eigeneId": window.CURRENT_USER_ID,
                "chatid": this.chatid,
                "benutzername": name
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/chat/takepart/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                this.addDialog = false
                this.loadUsers()
            })
            .catch(error =>{
                console.error(error.response.data)
            })
        },

        removeUser(name) {
            const payload = {
                "eigeneId": window.CURRENT_USER_ID,
                "chatId": this.chatid,
                "andererBenutzername": name
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/chat/entferneNutzer/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                this.dialog = false
                this.loadUsers()
            })
            .catch(error =>{
                console.error(error.response.data)
            })
        },

        deleteChat() {
            axios.get(window.IP_ADDRESS +"/GFOS/daten/chat/delete/" + this.chatid + "/" + window.CURRENT_TOKEN)
            .then((resp)=>{
                this.deleteDialog = false
                this.loadUsers()
                EventBus.$emit('CLOSECHATINFO', {});
                EventBus.$emit("RELOAD", {})
            })
            .catch(error =>{
                console.error(error.response.data)
            })
        },

        openUpload(e){
            e.preventDefault();
            this.imageDialog = true
        },

        changeInfos() {
            const payload = {
                                "chatid": this.chatid,
                                "eigeneid": window.CURRENT_USER_ID,
                                "name": this.gruppenname,
                                "beschreibung": this.gruppenbeschreibung,
                                "base64": window.IMAGE
                            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/chat/change/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                if(resp.data === true){
                    this.loadData()
                    this.closeInfo()
                    this.imageDialog = false
                    this.gruppenname = null
                    this.gruppenbeschreibung = null
                EventBus.$emit("INFOMESSAGE", {"message": "Deine Einstellungen wurden erfolgreich gespeichert!", "icon":"SUCCESS"})
                }
            })
            .catch(error =>{
                    if(error.response.data === "Kein gültiges Token"){
                        EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                        EventBus.$emit("LOGOUT", {})
                    }
                    
                })

        },

        getBase64(file) {
          var reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload = function () {
            var base64 = reader.result
            this.base64 = base64
            window.IMAGE = base64
          };
          
          reader.onerror = function (error) {
            console.error('getBase64 Error: ', error);
          };
        }
    }
}

window.closeInfo = function() {
    EventBus.$emit('CLOSECHATINFO', {});
}

window.IMAGE = null

</script>