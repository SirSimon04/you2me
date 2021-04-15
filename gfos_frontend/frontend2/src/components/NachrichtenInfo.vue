<template>
    <v-card
    style="background-color: #17212B; position: absolute; left: calc(50% - 187px); top: calc(50% - 200px);"
    class="mx-auto my-12"
    min-width="360"
    max-width="720"
    elevation="12"
    >
        <img style="position: relative; float: right; margin-top: 4px; margin-right: 4px;" width="20px" height="auto" onclick="window.closeInfo();" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQEAQAAADlauupAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QAAKqNIzIAAAAJcEhZcwAAAGAAAABgAPBrQs8AAAAHdElNRQflAxYUFyyU+7HbAAABY0lEQVQ4y5WS347BUBDGj030jmteAGl7gUvegiAeCg3xBP68BN6hVV5EG8TF/PZidlermt2dZJKT+eY733xzjjEvgbgueB6cTkgcI3EMpxN4HuK6Ji8Qy4LlEu53WK2QwQBaLc3hENZrxRYLxLKyZDkcwPeRWi1XhHodggDZ71OXqLLvI6WS+SWQUgmCAObzhOf7/VsZqlXodLLq3S5UKnpuNNSO4xhd2GqVbrxeod9/1vp9rXW7z9pmg0ynBs5nZDBIq/V6cLvBeKxkPad7RiMIQwNRBM1mduReDx4PzTRZ8XYbLpcPY8CYQiG7rne1VxxyLDzHTtrJsTCbwXr9/yVut8hkYsBx9EkaDQUqlWTj+2e0beXY9he4WOgP+8tHKpchDMHzEkXLQvZ7/WE6yVsytg3HI7LbIcXiy82WBfO5jrbZ6JLabc3RCLZbxTwvQ06rOA4yneqYUaQZhshk8uM5EZ8+/JkGG9VsvQAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAyMS0wMy0yMlQyMDoyMzo0NCswMDowMMlO3bkAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMjEtMDMtMjJUMjA6MjM6NDQrMDA6MDC4E2UFAAAAAElFTkSuQmCC">
        <v-card-title>
            <span
            style="color:white"
            >
                Info zur Nachricht
            </span>
        </v-card-title>
    </v-card>

</template>

<script>

import { EventBus } from './EventBus.js';
import { ImageContainer } from './ImageContainer.js';

export default {
    
}

window.closeInfo = function() {
    EventBus.$emit("CLOSEMESSAGEINFO", {})
}
</script>
