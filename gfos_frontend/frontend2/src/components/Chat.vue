<template>
    <v-container>
        <div id="chatcontainer" style="background-color: #0E1621; border-radius: 15px; padding: 16px;">
            <!-- Colors are not corrent. See in JS (current colors as seen in README)
            <div v-for="item in messages" :key="item" style="height: 100%;">
                <div v-if="item.senderid === '3'" style="background-color: #2b5278; border-width: 1px; border-style: solid; border-radius: 15px; max-width: 400px; height: auto; position: relative; left: 50%;">
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
                            <div class="overline mb-2" style="color: white;">
                                {{item.author}}
                            </div>
                            <p style="color: white; white-space: pre-line;">{{item.inhalt}}</p>
                            <v-list-item-subtitle style="color: white;">{{item.time}}</v-list-item-subtitle>
                        </v-list-item-content>
                    </v-list-item>
                </div>
                <br>
            </div>
            -->
        </div>
    </v-container>
</template>

<script>
import { EventBus } from './EventBus.js';

export default {
    name: 'Chat',

    data: () => ({
    }),

    mounted() {
        EventBus.$on('MYEVENT', (payload) => {
            console.log('THIS IS MY EVENT');
            console.log(payload);
        });
        EventBus.$on('OPENCHAT', (payload) => {
            var CURRENT_USER_ID = payload['userid']; //set in cookie?
            var CURRENT_CHAT_ID = payload['chatid'];
            var IP_ADDRESS = '84.191.205.25';

            var messages = [{
                "nachrichtid": 139,
                "senderid": 206,
                "chatid": 11,
                "datumuhrzeit": "2021-02-02 10:10:10",
                "inhalt": "22",
                "sender": "Simon",
                "chatList": []
            },
            {
                "nachrichtid": 140,
                "senderid": 206,
                "chatid": 11,
                "datumuhrzeit": "2021-02-02 10:10:10",
                "inhalt": "23",
                "sender": "Simon",
                "chatList": []
            },
            {
                "nachrichtid": 141,
                "senderid": 206,
                "chatid": 11,
                "datumuhrzeit": "2021-02-02 10:10:10",
                "inhalt": "25",
                "sender": "Simon",
                "chatList": []
            }];

            document.getElementById('chatcontainer').innerHTML = '';
            for (var i=0; i<messages.length; i++) {
                var data = messages[i];
                var elem = '';
                if (data['senderid'] === CURRENT_USER_ID) elem = '<div class="singlemsgcontainer" style="height: 100%;"><div style="background-color: #2B5278; border-width: 1px; border-style: solid; border-top-left-radius: 15px; border-top-right-radius: 15px; border-bottom-right-radius: 15px; border-bottom-left-radius: 15px; max-width: 400px; height: auto; position: relative; right: calc(-100% + 400px);"><div tabindex="-1" class="v-list-item v-list-item--three-line theme--light"><div class="v-list-item__content"><p style="color: white; white-space: pre-line;">' + data["inhalt"] + '</p><div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">' + data["datumuhrzeit"] + '</div></div></div></div><br></div>';
                else elem = '<div class="singlemsgcontainer" style="height: 100%;"><div style="background-color: #182533; border-width: 1px; border-style: solid; border-radius: 15px; max-width: 400px; height: auto; position: relative;"><div tabindex="-1" class="v-list-item v-list-item--three-line theme--light"><div class="v-list-item__content"><div class="overline mb-2" style="color: white;">' + data["sender"] + '</div><p style="color: white; white-space: pre-line;">' + data["inhalt"] + '</p><div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">' + data["datumuhrzeit"] + '</div></div></div></div><br></div>';
                document.getElementById('chatcontainer').innerHTML += elem;
            }

            function loadMessages() {
                fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/nachricht/chat/' + CURRENT_CHAT_ID + '/1').then(response => {
                    if (response.status !== 200) {
                        console.error('Code !== 200:' + response);
                        return null;
                    }
                    response.clone();
                    response.json().then(msgs => {
                        document.getElementById('chatcontainer').innerHTML = '';
                        for (var i=0; i<msgs.length; i++) {
                            var data = msgs[i];
                            var elem = '';
                            if (data['senderid'] === CURRENT_USER_ID) elem = '<div class="singlemsgcontainer" style="height: 100%;"><div style="background-color: #2B5278; border-width: 1px; border-style: solid; border-top-left-radius: 15px; border-top-right-radius: 15px; border-bottom-right-radius: 15px; border-bottom-left-radius: 15px; max-width: 400px; height: auto; position: relative; right: calc(-100% + 400px);"><div tabindex="-1" class="v-list-item v-list-item--three-line theme--light"><div class="v-list-item__content"><p style="color: white; white-space: pre-line;">' + data["inhalt"] + '</p><div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">' + data["datumuhrzeit"] + '</div></div></div></div><br></div>';
                            else elem = '<div class="singlemsgcontainer" style="height: 100%;"><div style="background-color: #182533; border-width: 1px; border-style: solid; border-radius: 15px; max-width: 400px; height: auto; position: relative;"><div tabindex="-1" class="v-list-item v-list-item--three-line theme--light"><div class="v-list-item__content"><div class="overline mb-2" style="color: white;">' + data["sender"] + '</div><p style="color: white; white-space: pre-line;">' + data["inhalt"] + '</p><div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">' + data["datumuhrzeit"] + '</div></div></div></div><br></div>';
                            document.getElementById('chatcontainer').innerHTML += elem;
                        }

                        var difference = msgs.length - messages.length;
                        var allSingles = document.getElementsByClassName('singlemsgcontainer');
                        var scrollValue = 0; // Funktioniert, scrollt aber ein paar Pixel weiter (das doppelte?)
                        for (i=allSingles.length-1; i>=difference; i--) {
                            scrollValue += allSingles[i].scrollHeight;
                        }
                        var count = 0;
                        var id = setInterval(function() {
                            window.scrollBy(0, 5);
                            count += 15;
                            if (count >= scrollValue) clearInterval(id);
                        }, 16);

                        messages = msgs;
                    }).catch(error => {
                        console.error('An error occured while parsing the string:' + error);
                    });
                });
            }
            
            /*const options = {
                method: 'POST',
                body: JSON.stringify({'benutzername': 'Simon', 'passwort': 'Test1234'}),
                headers: new Headers({
                    'Access-Control-Allow-Origin': '*',
                    'Access-Control-Allow-Credentials': 'true',
                    'Access-Control-Allow-Headers': 'origin, content-type, accept, authorization',
                    'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS, HEAD',
                    'Content-Type': 'application/json'
                })
            }

            console.warn('DAS IST DER START');
            fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/nutzer/login', options)
                .then(res => res.json())
                .then(res => {
                    console.log('HIER BIN ICH');
                    console.log(res);

                });*/

            setInterval(function() {
                // Neuste Nachricht checken
                var newest = null;
                fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/nachricht/chat/getNewest/' + CURRENT_CHAT_ID + '/1').then(response => {
                    if (response.status !== 200) {
                        console.error('Code !== 200:' + response);
                        return null;
                    }
                    response.clone();
                    response.json().then(data => {
                        newest = data;
                        if (messages.length === 0) loadMessages();
                        else if (messages[messages.length - 1]['nachrichtid'] !== newest['nachrichtid']) { // Trigger hier verändern bzw. hinzufügen, falls sich der Benutzer unbenennt / löscht
                            loadMessages();
                        }
                    }).catch(error => {
                        console.error('An error occured while parsing the string:' + error);
                    });
                });
                // Ende neuste Nachricht checken
            }, 1000);
        }); //EventBus.$on()
    },

    methods() {
    },
  }
</script>