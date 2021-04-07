<template>
  <v-card
    style="background-color: #17212B; position: absolute; left: calc(50% - 187px); top: calc(50% - 200px);"
    class="mx-auto my-12"
    min-width="360"
    max-width="720"
    elevation="12"
  >

    <img style="position: relative; float: right; margin-top: 4px; margin-right: 4px;" width="20px" height="auto" onclick="window.closeInfo();" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQEAQAAADlauupAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QAAKqNIzIAAAAJcEhZcwAAAGAAAABgAPBrQs8AAAAHdElNRQflAxYUFyyU+7HbAAABY0lEQVQ4y5WS347BUBDGj030jmteAGl7gUvegiAeCg3xBP68BN6hVV5EG8TF/PZidlermt2dZJKT+eY733xzjjEvgbgueB6cTkgcI3EMpxN4HuK6Ji8Qy4LlEu53WK2QwQBaLc3hENZrxRYLxLKyZDkcwPeRWi1XhHodggDZ71OXqLLvI6WS+SWQUgmCAObzhOf7/VsZqlXodLLq3S5UKnpuNNSO4xhd2GqVbrxeod9/1vp9rXW7z9pmg0ynBs5nZDBIq/V6cLvBeKxkPad7RiMIQwNRBM1mduReDx4PzTRZ8XYbLpcPY8CYQiG7rne1VxxyLDzHTtrJsTCbwXr9/yVut8hkYsBx9EkaDQUqlWTj+2e0beXY9he4WOgP+8tHKpchDMHzEkXLQvZ7/WE6yVsytg3HI7LbIcXiy82WBfO5jrbZ6JLabc3RCLZbxTwvQ06rOA4yneqYUaQZhshk8uM5EZ8+/JkGG9VsvQAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAyMS0wMy0yMlQyMDoyMzo0NCswMDowMMlO3bkAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMjEtMDMtMjJUMjA6MjM6NDQrMDA6MDC4E2UFAAAAAElFTkSuQmCC">
    <v-container>
        <img id="pfp" style="position: relative; float: left; border-radius: 100%;" width="128" height="128" src="https://play-lh.googleusercontent.com/Jfi1sAHOOijxYrtXxfZp0b7hrHh7qh_PJ6NfELApffcoI7fJ30LgPn7cWTR9txbEUGmZ">
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

            <div style="color: white;">
                <span v-if="description === undefined"><i>Keine Beschreibung vorhanden</i></span>
                <span v-else>{{description}}</span>
            </div>
        </div>
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
            <v-btn
                color="red lighten-2"
                text
                @click="blockUser(username);"
                :key="username"
            >
                <span v-if="blocked">Entblockieren</span>
                <span v-else>Blockieren</span>
            </v-btn>
        </div>
    </v-card-actions>
  </v-card>
</template>

<script>
import { EventBus } from './EventBus.js';
import { ImageContainer } from './ImageContainer.js';

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

            document.getElementById('pfp').src = (payload['profilepicture'] === undefined) ? imageContainer.getValue('USER') : payload['profilepicture'];

            if (!this.blockedMe) {
                this.description = payload['description'];
                var userData = payload['userdata'];
                if (this.isGroup) {
                    this.userlist = userData['userlist'];
                } else {
                    this.user = userData['user'];
                }
            }
        }); //EventBus.$on('LOADPROFILE')
    },

    methods: {
        blockUser(username) {
            fetch(window.IP_ADDRESS + '/GFOS/daten/nutzer/block/1', {
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
                response.clone();
                console.warn(response);
                response.text().then(content => {
                    this.blocked = !this.blocked;
                    console.warn(content);
                });
            });
        },

        leaveGroup() {
            fetch(window.IP_ADDRESS + '/GFOS/daten/chat/leave/1', {
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
                response.clone();
                console.warn(response);
                response.text().then(content => {
                    console.warn(content);
                    if (content === 'Einziger Admin') {
                        EventBus.$emit('INFOMESSAGE', {'message': 'Du bist der letzte Admin und kannst die Gruppe deswegen nicht verlassen.'})
                    }
                });
            });
        }
    }
}

window.closeInfo = function() {
    EventBus.$emit('CLOSECHATINFO', {});
}

</script>