<template>
    <v-container>
        <div style="background-color: #202b36; border-radius: 15px; padding: 16px;">
            <div v-for="item in testitems" :key="item" style="height: 100%;">
                <div v-if="item.author === 'me'" style="background-color: #2b5278; border-width: 1px; border-style: solid; border-radius: 15px; max-width: 400px; height: auto; position: relative; left: 50%;">
                    <v-list-item three-line>
                        <v-list-item-content>
                            <p style="color: white; white-space: pre-line;">{{item.msg}}</p>
                            <v-list-item-subtitle style="color: white;">{{item.time}}</v-list-item-subtitle>
                        </v-list-item-content>
                    </v-list-item>
                </div>
                <div v-else style="background-color: #252d30; border-width: 1px; border-style: solid; border-radius: 15px; max-width: 400px; height: auto; position: relative;">
                    <v-list-item three-line>
                        <v-list-item-content>
                            <div class="overline mb-4" style="color: white;">
                                {{item.author}}
                            </div>
                            <p style="color: white; white-space: pre-line;">{{item.msg}}</p>
                            <v-list-item-subtitle style="color: white;">{{item.time}}</v-list-item-subtitle>
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
      testitems: [{"author": "me", "msg": "Hallo Welt!", "time": "10:04"}, {"author": "notme", "msg": "Guten Morgen.", "time": "10:05"}, {"author": "notme", "msg": "Eine zweite Nachricht meinerseits.", "time": "10:06"}, {"author": "me", "msg": "Zum Schluss folgt eine Nachricht von mir. Am besten mit mehreren Zeilen! Hier folgt ein weiterer Text, um die Zeilen zu füllen. Damit kann überprüft werden, ob das Design richtig funktioniert und nicht eine fixe Höhe hat.\n\nDies sollte der letzte Satz sein, das Textfeld sollte nun mehrere Zeilen haben. Einen schönen Tag noch!", "time": "10:06"}, {"author": "me", "msg": "Hallo Welt!", "time": "10:04"}, {"author": "notme", "msg": "Guten Morgen.", "time": "10:05"}, {"author": "notme", "msg": "Eine zweite Nachricht meinerseits.", "time": "10:06"}, {"author": "me", "msg": "Zum Schluss folgt eine Nachricht von mir. Am besten mit mehreren Zeilen! Hier folgt ein weiterer Text, um die Zeilen zu füllen. Damit kann überprüft werden, ob das Design richtig funktioniert und nicht eine fixe Höhe hat.\n\nDies sollte der letzte Satz sein, das Textfeld sollte nun mehrere Zeilen haben. Einen schönen Tag noch!", "time": "10:06"}]
    }),
  }
</script>