<template>
    <v-container>
        <div id="chatcontainer">
            <!--
                Colors are not corrent. See in JS (current colors as seen in README)
                This is outdated. Don't use this as a reference
            
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
        var IP_ADDRESS = '91.49.179.104';
        var CURRENT_USER_ID = -1;
        var CURRENT_CHAT_ID = -1;
        var messages = [];

        function testPost() {
            fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/nutzer/testPost', {
                mode: 'cors',
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'text/plain'
                },
                body: 'Hallo'/*JSON.stringify({
                    test: 'Hallo'
                })*/
            }).then(response => {
                response.clone();
                console.warn(response);
                response.text().then(content => {
                    console.warn(content);
                });
            });
        }

        function testPost2() {
            var current = new Date();
            fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/nachricht/add/1', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'text/plain'
                },
                body: JSON.stringify({
                    senderid: 2,
                    inhalt: 'Hallo Welt',
                    datumuhrzeit: current.toLocaleString(),
                    chatid: 1,
                })
            }).then(response => {
                response.clone();
                response.text().then(content => {
                    console.warn(content);
                }); 
            });
        }

        function testPost3() {
            fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/nutzer/login', {
                mode: 'cors',
                method: 'POST',
                headers: {
                    'Accept': 'application/json', // Antwort von Server
                    'Content-Type': 'text/plain', // Wird an den Server gesendet
                },
                body: JSON.stringify({
                    benutzername: 'Simon',
                    passwort: 'Test1234'
                })
            }).then(response => {
                response.clone();
                response.text().then(content => {
                    console.warn(content);
                }); 
            });
        }

        testPost3();

        function fetchIfNewest(loadMsgs) {
            // Check for newest message and reload all messages if there is a newer one
            var newest = null;
            fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/nachricht/chat/getNewest/' + CURRENT_CHAT_ID + '/1').then(response => {
                if (response.status !== 200) {
                    console.error('Code !== 200:' + response);
                    return null;
                }
                response.clone();
                response.json().then(data => {
                    newest = data;
                    if (messages.length === 0) loadMsgs();
                    else if (messages[messages.length - 1]['nachrichtid'] !== newest['nachrichtid']) { // Trigger hier verändern bzw. hinzufügen, falls sich der Benutzer unbenennt / löscht
                        loadMsgs();
                    }
                }).catch(error => {
                    console.error('An error occured while parsing the string:' + error);
                });
            });
        }

        var interval = setInterval(function() {}, 1000);
        
        EventBus.$on('MYEVENT', (payload) => {
            console.log('THIS IS MY EVENT');
            console.log(payload);
        });

        EventBus.$on('CHANGEHEIGHT', (payload) => {
            document.getElementById('chatcontainer').style = 'overflow: auto; min-height: 400px; height: "' + payload["height"] + 'px"; max-height: ' + payload["height"] + 'px; background-color: #0E1621; border-radius: 15px; padding: 16px;';
        });

        EventBus.$on('OPENCHAT', (payload) => {
            clearInterval(interval); // Stop current loading interval to not load multiple chats at the same time

            CURRENT_USER_ID = payload['userid']; //set in cookie?
            CURRENT_CHAT_ID = payload['chatid'];
            messages = [];

            document.getElementById('chatcontainer').innerHTML = '';

            function loadMessages() { // Reload all messages (will be called if a newer message is available)
                fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/nachricht/chat/' + CURRENT_CHAT_ID + '/1').then(response => {
                    if (response.status !== 200) {
                        console.error('Code !== 200:' + response);
                        return null;
                    }
                    response.clone();
                    response.json().then(msgs => {
                        document.getElementById('chatcontainer').innerHTML = '';
                        // Write all single messages into the chatcontainer
                        for (var i=0; i<msgs.length; i++) {
                            var data = msgs[i];
                            var elem = '';
                            if (data['senderid'] === CURRENT_USER_ID) elem = '<div class="singlemsgcontainer" style="height: 100%;"><div style="background-color: #2B5278; border-width: 1px; border-style: solid; border-top-left-radius: 15px; border-top-right-radius: 15px; border-bottom-right-radius: 15px; border-bottom-left-radius: 15px; max-width: 400px; height: auto; position: relative; right: calc(-100% + 400px);"><div tabindex="-1" class="v-list-item v-list-item--three-line theme--light"><div class="v-list-item__content"><p style="color: white; white-space: pre-line;">' + data["inhalt"] + '</p><div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">' + data["datumuhrzeit"] + '</div></div></div></div><br></div>';
                            else elem = '<div class="singlemsgcontainer" style="height: 100%;"><div style="background-color: #182533; border-width: 1px; border-style: solid; border-radius: 15px; max-width: 400px; height: auto; position: relative;"><div tabindex="-1" class="v-list-item v-list-item--three-line theme--light"><div class="v-list-item__content"><div class="overline mb-2" style="color: white;">' + data["sender"] + '</div><p style="color: white; white-space: pre-line;">' + data["inhalt"] + '</p><div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">' + data["datumuhrzeit"] + '</div></div></div></div><br></div>';
                            document.getElementById('chatcontainer').innerHTML += elem;
                        }

                        // Scroll down to newest message (if user is at the button)
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
            
            interval = setInterval(fetchIfNewest(loadMessages), 1000); // Set new interval after stopping old on EventBus.$on() to load only one chat at a time

        }); // EventBus.$on('OPENCHAT')
    }, // mounted()
  }
</script>