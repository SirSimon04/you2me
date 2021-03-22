<template>
  <v-card
    style="background-color: #17212B; position: absolute; left: calc(50% - 187px); top: calc(50% - 200px);"
    class="mx-auto my-12"
    max-width="374"
    elevation="12"
  >

    <img style="position: relative; float: right; margin-top: 4px; margin-right: 4px;" width="20px" height="auto" onclick="window.closeInfo();" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQEAQAAADlauupAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QAAKqNIzIAAAAJcEhZcwAAAGAAAABgAPBrQs8AAAAHdElNRQflAxYUFyyU+7HbAAABY0lEQVQ4y5WS347BUBDGj030jmteAGl7gUvegiAeCg3xBP68BN6hVV5EG8TF/PZidlermt2dZJKT+eY733xzjjEvgbgueB6cTkgcI3EMpxN4HuK6Ji8Qy4LlEu53WK2QwQBaLc3hENZrxRYLxLKyZDkcwPeRWi1XhHodggDZ71OXqLLvI6WS+SWQUgmCAObzhOf7/VsZqlXodLLq3S5UKnpuNNSO4xhd2GqVbrxeod9/1vp9rXW7z9pmg0ynBs5nZDBIq/V6cLvBeKxkPad7RiMIQwNRBM1mduReDx4PzTRZ8XYbLpcPY8CYQiG7rne1VxxyLDzHTtrJsTCbwXr9/yVut8hkYsBx9EkaDQUqlWTj+2e0beXY9he4WOgP+8tHKpchDMHzEkXLQvZ7/WE6yVsytg3HI7LbIcXiy82WBfO5jrbZ6JLabc3RCLZbxTwvQ06rOA4yneqYUaQZhshk8uM5EZ8+/JkGG9VsvQAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAyMS0wMy0yMlQyMDoyMzo0NCswMDowMMlO3bkAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMjEtMDMtMjJUMjA6MjM6NDQrMDA6MDC4E2UFAAAAAElFTkSuQmCC">
    <v-container>
        <v-img style="position: relative; float: left; border-radius: 100%;" width="128" height="128" src="https://play-lh.googleusercontent.com/Jfi1sAHOOijxYrtXxfZp0b7hrHh7qh_PJ6NfELApffcoI7fJ30LgPn7cWTR9txbEUGmZ"></v-img>
        <v-card-title style="color: white;">{{username}}</v-card-title>
    </v-container>
    <br><br>

    <v-card-text>

        <!-- Add icon for chatting -->

      <div style="color: white;" class="my-2 subtitle-3">
        +49 172 573 0153
      </div>

      <div style="color: white;">Hallo ich bin {{username}} und das ist meine Beschreibung.</div>
    </v-card-text>

    <v-divider class="mx-4"></v-divider>

    <v-card-actions>
      <v-btn
        color="red lighten-2"
        text
        onclick="window.blockUser('PUT USERNAME HERE');"
      >
        Blockieren
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import { EventBus } from './EventBus.js';

export default {
    name: 'ChatInfo',

    data: () => ({
        username: 'PLACEHOLDER'
    }),

    mounted() {
        EventBus.$on('LOADPROFILE', (payload) => {
            this.username = payload['username'];
        }); //EventBus.$on('LOADPROFILE')
    },

    methods() {
    }
}

window.blockUser = function(username) {
    console.warn('User ' + username + ' is now blocked');
}

window.closeInfo = function() {
    EventBus.$emit('CLOSECHATINFO', {});
}

</script>