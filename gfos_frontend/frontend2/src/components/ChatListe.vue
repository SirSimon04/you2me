<template>
    <v-container id='contactcontainer'>
        <!--<v-hover>
            <v-card slot-scope="{ hover }" :style="`background-color: ${hover ? '#202B36' : '#17212B'}`" class="mx-auto" height="100px" max-width="400px">
                <v-card-text class="font-weight-medium mt-12 text-center subtitle-1">
                    CONTENT
                </v-card-text>
            </v-card>
        </v-hover>-->
    </v-container>
</template>

<script>
import { EventBus } from './EventBus.js';
import { ImageContainer } from './ImageContainer.js';

export default {
    name: 'ChatListe',

    data: () => ({
    }),

    mounted() {
        var imageContainer = new ImageContainer();

        console.warn(window.CURRENT_USER_ID);

        EventBus.$on('APP_INTERVAL', (payload) => {
            if (window.CURRENT_USER_ID === -1) return;
            fetch(window.IP_ADDRESS + '/GFOS/daten/chat/nutzerid/' + window.CURRENT_USER_ID + '/' + window.CURRENT_TOKEN).then(response => {
                if (response.status !== 200) {
                    console.error('Code !== 200:' + response);
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

        function reloadList(conts) {
            document.getElementById('contactcontainer').innerHTML = '';
            for (var i=0; i<conts['normal'].length; i++) {
                var data = conts['normal'][i];
                var chatid = data['chatid'];
                var isGroup = data['isgroup'];
                var lastMessage = data["letztenachricht"];
                var sender = '';
                
                if (lastMessage == undefined) sender = '<i>Hier gibts keine Nachrichten</i>';
                else {
                    var groupSender = '<span style="color: #48A1F4;">' + lastMessage['sender'] + ': </span>';
                    sender = (isGroup) ? groupSender : '';
                    if (lastMessage['senderid'] === window.CURRENT_USER_ID) sender = '<span style="color: #48A1F4;">Ich: </span>'
                    sender = sender + lastMessage['inhalt'];
                }

                var contextMenu = `window.openInfo(` + chatid + `, '` + data["name"] + `');`

                var unreadBadgeText = '' + data['nnew'];
                if (data['nnew'] >= 100) unreadBadgeText = '99+';

                var unreadBadgeTextSize = 16;
                if (data['nnew'] >= 10) unreadBadgeTextSize = 14;
                if (data['nnew'] >= 100) unreadBadgeTextSize = 12;

                var unreadBadgeTextOffset = 0;
                if (data['nnew'] >= 10) unreadBadgeTextOffset = 3;
                if (data['nnew'] >= 100) unreadBadgeTextOffset = 4;


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
                <div onclick="window.openChat(` + chatid + ', ' + window.CURRENT_USER_ID + `);" class="mx-auto v-card v-sheet theme--light" style="height: 80px; max-width: 400px; background-color: rgb(23, 33, 43); border-radius: 0px;">
                    ` + unreadBadge + pinnedIcon + `
                    <img oncontextmenu="` + contextMenu + `return false;" style="padding: 6px; position: relative; float: left; top: calc(50% - 32px); border-radius: 100%" src="` + profilePicture + `" width="64" height="64">
                    <div class="v-card__text font-weight-medium subtitle-1">
                        ` + ((isGroup) ? '<img style="margin-right: 4px; position: relative; float: left; top: 4px;" width="16px" height="16px" src="' + imageContainer.getValue("GROUP") + '">' : '') + `
                        <span style="color: white;">` + data["name"] + `</span>
                        <div class="font-weight-light">
                            <span style="color: #7F91A4;">` + sender + `</span>
                        </div>
                    </div>
                </div>
                `;
                document.getElementById('contactcontainer').innerHTML += elem;
            }

            window.chats = conts['normal'];
        }
    }
}

window.chats = [];

window.addContact = function(totalHeight) {
    console.log(totalHeight + 'px');
    EventBus.$emit('CHANGEHEIGHT', {'height': totalHeight});
}

window.openChat = function(chatid, userid) {
    window.addContact(document.getElementById('contactcontainer').clientHeight);
    console.log(chatid);
    // Open chat with given chatId
    EventBus.$emit('OPENCHAT', {'chatid': chatid, 'userid': userid});
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
</script>
