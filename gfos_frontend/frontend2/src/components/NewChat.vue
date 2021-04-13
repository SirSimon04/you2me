<template>
    <v-container>
    </v-container>
</template>

<script>
import { EventBus } from './components/EventBus.js';
import { ImageContainer } from './components/ImageContainer.js';

export default {
    name: 'NewChat',

    data: () => ({
    }),

    mounted() {
        EventBus.$on('APP_INTERVAL', (payload) => {
        }); // EventBus.$on('APP_INTERVAL')
    },

    methods: {
    }
};

</script>