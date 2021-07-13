<template>
  <v-card
    class="mx-auto"
    max-width="300"
    tile
  >
    <v-list flat>
      <v-subheader>REPORTS</v-subheader>
      <v-list-item-group
        v-model="selectedItem"
        color="primary"
      >
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
        >
          <v-list-item-icon>
            <v-icon v-text="item.icon"></v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title v-text="item.text"></v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>
  </v-card>
</template>

<script>
import { EventBus } from './components/EventBus.js';
import { ImageContainer } from './components/ImageContainer.js';

export default {
    name: 'UserList',

    data: () => ({
        userList: []
    }),

    mounted() {
        EventBus.$on('APP_INTERVAL', (payload) => {
        }); // EventBus.$on('APP_INTERVAL')

        EventBus.$on('APP_INTERVAL', (payload) => {
        }); // EventBus.$on('APP_INTERVAL')
    },

    methods: {
    }
};

</script>