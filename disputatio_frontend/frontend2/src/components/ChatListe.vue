<template>
    <v-container>
        <v-container style="max-height: 750px; y-overflow: auto;" id='contactcontainer'>
        </v-container>
        <v-dialog
        v-model="dialog"
        max-width="300"
        >
            <v-card
            dark
            min-height="200px"
            >
                <v-card-title>
                    {{dialogName}}
                </v-card-title>

                <v-card-text>
                <div
                style="height: 10 em; posiition: relative; display: flex; align-items: center; justify-content: center "
                >

                    <v-btn
                    color="blue darken-1"
                    style="margin: 0; position: absolute; top: 45%; transform: translate(0, -50%);"
                    width="200px"
                    v-if="ispinned === 'true'"
                    @click="pin();"
                    >
                        Entpinnen
                    </v-btn>

                    <v-btn
                    color="blue darken-1"
                    style="margin: 0; position: absolute; top: 45%; transform: translate(0, -50%);"
                    width="200px"
                    v-else
                    @click="pin();"
                    >
                        Anpinnen
                    </v-btn>
                    
                    <v-btn
                    v-if="!getArchivedOpen()"
                    color="blue darken-1"
                    width="200px"
                    style="margin: 0; position: absolute; top: 65%; transform: translate(0, -50%);"
                    @click="archive();"
                    >
                        Archivieren
                    </v-btn>
                    <v-btn
                    v-else
                    color="blue darken-1"
                    width="200px"
                    style="margin: 0; position: absolute; top: 65%; transform: translate(0, -50%);"
                    @click="archive();"
                    >
                        Entarchivieren
                    </v-btn>

                    <v-btn
                    text
                    width="200px"
                    style="margin: 0; position: absolute; top: 86%; transform: translate(0, -50%);"
                    @click="dialog = false"
                    >
                        Schließen
                    </v-btn>

                </div>
                
                <v-spacer/>
                
            </v-card-text>
            </v-card>
        </v-dialog>
    </v-container>
    
</template>

<script>
import { EventBus } from './EventBus.js';
import axios from "axios";
import { ImageContainer } from './ImageContainer.js';

export default {
    name: 'ChatListe',

    data: () => ({
        dialog: false,
        dialogName: "",
        ispinned: false,
        chatid: -1,
        archived_open:   window.ARCHIVED_OPEN
    }),

    mounted() {
        var imageContainer = new ImageContainer();

        EventBus.$on('APP_INTERVAL', (payload) => {
            if (window.CURRENT_USER_ID === -1) return;
            fetch(window.IP_ADDRESS + '/GFOS/daten/chat/nutzerid/' + window.CURRENT_USER_ID + '/' + window.CURRENT_TOKEN).then(response => {
                if (response.status !== 200) {
                    if(response.status === 401){
                        EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                        EventBus.$emit("LOGOUT", {})
                    }
                    return null;
                }
                response.clone();
                response.json().then(conts => {
                    EventBus.$emit('RELOADCHAT', {});
                    reloadList(conts);
                }).catch(error => {
                    console.error('An error occured while parsing the string:' + error);
                });
            });
        });

        EventBus.$on("OPENPINMENU", (payload) =>{
            this.chatid = payload["chatid"]
            this.dialog = true
            this.dialogName = payload["name"]
            this.ispinned = payload["ispinned"]
        });

        function reloadList(conts) {
            var chats = ""
            if(window.ARCHIVED_OPEN){
                chats = "archived"
            }
            else{
                chats = "normal"
            }
            document.getElementById('contactcontainer').innerHTML = '';
            for (var i=0; i<conts[chats].length; i++) {
                var data = conts[chats][i];
                var chatid = data['chatid'];
                var isGroup = data['isgroup'];
                var lastMessage = data["letztenachricht"];
                var sender = '';
                var bgC = ""
                var nameC = ""
                var textC = ""
                if(window.DARK_MODE){
                    bgC = '#17212B'
                    nameC = '#FFFFFF'
                    textC = '#7F91A4'
                }
                else{
                    bgC = '#FFFFFF'
                    nameC = '#000000'
                    textC = '#000000'
                }
                
                if (lastMessage == undefined) sender = '<i>Hier gibts keine Nachrichten</i>';
                else {
                    var groupSender = '<span style="color: #48A1F4;">' + lastMessage['sender'] + ': </span>';
                    sender = (isGroup) ? groupSender : '';
                    if (lastMessage['senderid'] === window.CURRENT_USER_ID) sender = '<span style="color: #48A1F4;">Ich: </span>'
                    sender = sender + lastMessage['inhalt'];
                }

                var contextMenu = `window.openInfo(` + chatid + `, '` + data["name"] + `');`
                var pinContextMenu = `window.openContextMenu(` + chatid + `, '` + data["name"] + `' , '` + data["ispinned"] + `');`

                var unreadBadgeText = '' + data['nnew'];
                if (data['nnew'] >= 100) unreadBadgeText = '99+';

                var unreadBadgeTextSize = 16;
                if (data['nnew'] >= 10) unreadBadgeTextSize = 14;
                if (data['nnew'] >= 100) unreadBadgeTextSize = 12;

                var unreadBadgeTextOffset = 0;
                if (data['nnew'] >= 10) unreadBadgeTextOffset = 3;
                if (data['nnew'] >= 100) unreadBadgeTextOffset = 4;

                window.CURRENT_CHAT_NAME = data["name"]
                var name = data["name"]
                var profilePicture = (data['profilbild'] === undefined) ? imageContainer.getValue('USER') : data['profilbild']['base64'];
                var pinnedIcon = (data['ispinned'] === undefined) ? '' : '<img style="z-index: 100; position: relative; float: right; top: calc(50% - 4px); right: 8px;" height="24px" width="24px" src="' + imageContainer.getValue("PIN") + '">'
                var unreadBadge = (data['nnew'] === 0) ? '' : `
                <div style="z-index: 100; position: relative; float: right; top: 36px; left: -8px; width: 24px; height: 24px; margin-left: 8px; background-color: pink; border-radius: 100%;">
                    <div style="position: relative; left: 7px;">
                        <span style="position: relative; left: -` + unreadBadgeTextOffset + `px; font-size: ` + unreadBadgeTextSize + `px;">` + unreadBadgeText + `</span>
                    </div>
                </div>
                `;
                var elem = `
                <div onclick="window.openChat(` + chatid + ', ' + window.CURRENT_USER_ID + ', `' + name + '`' +  `);" class="mx-auto v-card v-sheet " style="height: 80px; max-width: 400px; background-color: ` + bgC + `; border-radius: 0px;">
                    ` + unreadBadge + pinnedIcon + `
                    <img oncontextmenu="` + contextMenu + `return false;" style="padding: 6px; position: relative; float: left; top: calc(50% - 32px); border-radius: 100%" src="` + profilePicture + `" width="64" height="64">
                    <div class="v-card__text font-weight-medium subtitle-1">
                        ` + ((isGroup) ? '<img style="margin-right: 4px; position: relative; float: left; top: 4px;" width="16px" height="16px" src="' + imageContainer.getValue("GROUP") + '">' : '') + `
                        <span oncontextmenu="` + pinContextMenu + `return false;" style="color: ` + nameC +`;">` + data["name"] + `</span>
                        <div class="font-weight-light">
                            <span style="color: `+ textC +`;" >` + sender + `</span>
                        </div>
                    </div>
                </div>
                `;
                document.getElementById('contactcontainer').innerHTML += elem;
            }

            window.chats = conts['normal'];
        }
    },

    methods: {

        getArchivedOpen(){
            return window.ARCHIVED_OPEN
        },

        newChat() {
            EventBus.$emit('NEWCHAT', {});
        },

        pin(){
            const payload = {
                "eigeneid": window.CURRENT_USER_ID,
                "chatid": this.chatid,
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/chat/pin/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                this.dialog = false
                this.dialogName = ""
                this.chatid = -1
                this.ispinned = false
            })
            .catch(error =>{
                console.error(error.response.data)
            })
        },

         archive(){
            const payload = {
                "eigeneid": window.CURRENT_USER_ID,
                "chatid": this.chatid,
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/chat/archive/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                this.dialog = false
                this.dialogName = ""
                this.chatid = -1
            })
            .catch(error =>{
                console.error(error.response.data)
            })
        }
    }
}

window.chats = [];

window.addContact = function(totalHeight) {
    EventBus.$emit('CHANGEHEIGHT', {'height': totalHeight});
}

window.openChat = function(chatid, userid, name) {
    window.addContact(document.getElementById('contactcontainer').clientHeight);
    (chatid);
    // Open chat with given chatId
    EventBus.$emit('OPENCHAT', {'chatid': chatid, 'userid': userid, "name": name});
}

window.openInfo = function(chatid, name) {
    // daten/chat/info/CHATID 4gruppe 56pn/NUTZERID/TOKEN
    for (var i=0; i<window.chats.length; i++) {
        if (window.chats[i]['chatid'] === chatid) {
            var base64 = (window.chats[i]['profilbild'] === undefined) ? undefined : window.chats[i]['profilbild']['base64'];
            EventBus.$emit('OPENCHATINFO', {'chatid': chatid, 'name': name, 'profilepicture': base64});
            return;
        }
    }
}

window.openContextMenu = function(chatid, name, ispinned){
    EventBus.$emit("OPENPINMENU", {'chatid': chatid, "name": name, "ispinned": ispinned});
}

window.ARCHIVED_OPEN = false
</script>
