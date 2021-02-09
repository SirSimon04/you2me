<template>
    <v-container>
        <div>
            <div v-for="item in testitems" :key="item">
                <div v-if="item.author === 'me'" style="border-width: 1px; border-style: solid; max-width: 400px; height:200px; float: right; position: relative;">
                    <v-list-item three-line>
                        <v-list-item-content>
                            <v-list-item-subtitle>{{item.msg}}</v-list-item-subtitle><br>
                            <v-list-item-subtitle>{{item.time}}</v-list-item-subtitle>
                        </v-list-item-content>
                    </v-list-item>
                </div>
                <div v-else style="border-width: 1px; border-style: solid; height: 200px; max-width: 400px; position: relative;">
                    <v-list-item three-line>
                        <v-list-item-content>
                            <div class="overline mb-4">
                                {{item.author}}
                            </div>
                            <v-list-item-subtitle>{{item.msg}}</v-list-item-subtitle><br>
                            <v-list-item-subtitle>{{item.time}}</v-list-item-subtitle>
                        </v-list-item-content>
                    </v-list-item>
                </div>
                <br>
            </div>
        </div>
    </v-container>
</template>

<script>
  export default {
    name: 'Chat',

    data: () => ({
      testitems: [{"author": "me", "msg": "Hallo Welt!", "time": "10:04"}, {"author": "notme", "msg": "Guten Morgen.", "time": "10:05"}]
    }),
  }
</script>