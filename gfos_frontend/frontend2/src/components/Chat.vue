<template>
    <v-container>
        <div id="chatcontainer" style="background-color: #202b36; border-radius: 15px; padding: 16px;">
            <div v-for="item in messages" :key="item" style="height: 100%;">
                <div v-if="item.author === 'me'" style="background-color: #2b5278; border-width: 1px; border-style: solid; border-radius: 15px; max-width: 400px; height: auto; position: relative; left: 50%;">
                    <v-list-item three-line>
                        <v-list-item-content>
                            <p style="color: white; white-space: pre-line;">{{item.inhalt}}</p>
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
                            <p style="color: white; white-space: pre-line;">{{item.inhalt}}</p>
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
        messages: [
            {
                "inhalt": "Hallo Welt",
                "time": "10:04",
                "author": "me"
            },
            {
                "inhalt": "Hello World",
                "time": "10:05",
                "author": "notme"
            }
        ]
      /*testitems: [
          {"author": "me", "inhalt": "Hallo Welt!", "time": "10:04"},
          {"author": "notme", "inhalt": "Guten Morgen.", "time": "10:05"},
          {"author": "notme", "inhalt": "Eine zweite Nachricht meinerseits.", "time": "10:06"},
          {"author": "me", "inhalt": "Zum Schluss folgt eine Nachricht von mir. Am besten mit mehreren Zeilen! Hier folgt ein weiterer Text, um die Zeilen zu füllen. Damit kann überprüft werden, ob das Design richtig funktioniert und nicht eine fixe Höhe hat.\n\nDies sollte der letzte Satz sein, das Textfeld sollte nun mehrere Zeilen haben. Einen schönen Tag noch!", "time": "10:06"},
          {"author": "me", "inhalt": "Hallo Welt!", "time": "10:04"},
          {"author": "notme", "inhalt": "Guten Morgen.", "time": "10:05"},
          {"author": "notme", "inhalt": "Eine zweite Nachricht meinerseits.", "time": "10:06"},
          {"author": "me", "inhalt": "Zum Schluss folgt eine Nachricht von mir. Am besten mit mehreren Zeilen! Hier folgt ein weiterer Text, um die Zeilen zu füllen. Damit kann überprüft werden, ob das Design richtig funktioniert und nicht eine fixe Höhe hat.\n\nDies sollte der letzte Satz sein, das Textfeld sollte nun mehrere Zeilen haben. Einen schönen Tag noch!", "time": "10:06"}]
          */
    }),

    mounted() {
        function getNewest() {
            fetch('http://10.132.177.86:8080/GFOS/daten/nachricht/chat/1/newest').then(response => {
                if (response.status !== 200) {console.error('Code !== 200:' + response);
                console.warn('error!');
                    return
                }
                response.clone();
                response.json().then(data => {
                    return data;
                }).catch(error => {
                    console.error('An error occured while parsing the string:' + error);
                });
            });
        }
        function loadMessages() {
            fetch('http://localhost:8080/GFOS/daten/nachricht/1').then(response => {
                if (response.status !== 200) {console.error('Code !== 200:' + response);
                console.warn('error!');
                    return
                }
                response.clone();
                response.json().then(data => {
                    this.messages = data;
                    //begin
                    var elem = '<div style="height: 100%;"><div style="background-color: rgb(43, 82, 120); border-width: 1px; border-style: solid; border-top-left-radius: 15px; border-top-right-radius: 15px; border-bottom-right-radius: 15px; border-bottom-left-radius: 15px; max-width: 400px; height: auto; position: relative; left: 50%;"><div tabindex="-1" class="v-list-item v-list-item--three-line theme--light"><div class="v-list-item__content"><p style="color: white; white-space: pre-line;">' + data["inhalt"] + '</p><div class="v-list-item__subtitle" style="color: white;">' + data["datumuhrzeit"] + '</div></div></div></div><br></div>';
                    document.getElementById('chatcontainer').innerHTML += elem;
                }).catch(error => {
                    console.error('An error occured while parsing the string:' + error);
                });
            });
        }
        loadMessages();
        setTimeout(function() {
            if (this.messages[-1] !== getNewest()) {
                loadMessages();
            }
        }, 1000);
    },

    methods() {
    },
  }
</script>