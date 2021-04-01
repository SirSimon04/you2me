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
        <div style="
                background-color: rgb(27, 66, 104);
                border-radius: 20px;
                min-height: 32px;
                max-height: 256px;
            ">
            <textarea
                style="
                    margin: 8px;
                    position: relative;
                    top: calc(50% - 16px);
                    color: white;
                    width: 90%;
                    max-height: 240px;
                    min-height: 32px;
                    
                    overflow: auto;
                "
                id="sendMessage_Area"
                maxlength="1024"
                oninput="
                    function ch(elem) {
                        elem.style.height = '48px';
                        elem.style.height = elem.scrollHeight + 'px';
                    }
                    ch(this);
                "
                placeholder="Schreibe eine Nachricht..."
                wrap="hard"
            >
            </textarea>
            <img onclick="window.sendMessage();" style="position: relative; right: -4px; bottom: 4px;" width="32" height="32" id="sendMessage_Button" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAGfSURBVEiJrdQvTxxRFAXw32SzBr4FHggSi6hpUrMYiimQ9HOgIHj4FljEQoIEWoFD0CYkVa0rbYLlVcydzWM6s7M7MMkTc/+cc+49M69IKXnLpyiKIT7gMwZSSm9ysIQD/ESKM34t6BAjjPGM33jKCEavUXuIXwF0hU84ivenmGQ4r9pNnIfaRxxjOfL7AX4UkxyklPRRe40dLGQ1Ffh+TPKMpVaCKWpXGmon4PF+hfEk36H2Bru52g7w5crcrGai9iLU/sEJVjtW9wI8YschbpgTfInCB+xhcQZfmsAXYpWHtVpb+BHqT7E2L3jEd3JzX3gQa9rD92g+w/qs4JG7xvl/8VrRANu4C6BLbMwAvhK5zakEWUOhvAJuo/G+DbzN3KkEteb3+Ib7lnyjuTMTZCSpWlctt9tkbnWK6lPqeoqiuMVjSmmjFr/B35TSu8bGWSYIEaOYYj2LrbaZO9eKMuPvcJbFTtrMnZsgALdD8RoWlddKo7l9CQbKn/FU+WO2mtuLIEgq4AdcdNb3IBgq766p5vYmCJKP+DrN3Or8A60pb5tuGI4XAAAAAElFTkSuQmCC">
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

        function sendMessage() {
            var content = document.getElementById('sendMessage_Area').text;
            var currentMillis = new Date.currentMillis;
            fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/nachricht/add/1', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'text/plain'
                },
                body: JSON.stringify({
                    senderid: CURRENT_USER_ID,
                    inhalt: content,
                    datumuhrzeit: currentMillis,
                    chatid: CURRENT_CHAT_ID,
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

            // Nachrichten mit dieser Animation einfügen: https://www.w3schools.com/css/tryit.asp?filename=trycss3_animation_speed
            function loadMessages() { // Reload all messages (will be called if a newer message is available)
                fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/nachricht/chat/' + CURRENT_CHAT_ID + '/1').then(response => {
                    if (response.status !== 200) {
                        console.error('Code !== 200:' + response);
                        return null;
                    }
                    response.clone();
                    response.json().then(msgs => {
                        var cc = document.getElementById('chatcontainer');
                        var isBottom = cc.scrollTop - (cc.scrollHeight - cc.offsetHeight) == 0;

                        cc.innerHTML = '';
                        // Write all single messages into the chatcontainer
                        for (var i=0; i<msgs.length; i++) {
                            var data = msgs[i];
                            var elem = '';
                            // Add icon for planned message (ifÖsent by me: icon; if sent by other: show if date is not a future date)
                            if (data['senderid'] === CURRENT_USER_ID) elem = '<div class="singlemsgcontainer" style="height: 100%;"><div style="background-color: #2B5278; border-width: 1px; border-style: solid; border-top-left-radius: 15px; border-top-right-radius: 15px; border-bottom-right-radius: 15px; border-bottom-left-radius: 15px; max-width: 400px; height: auto; position: relative; right: calc(-100% + 400px);"><div tabindex="-1" class="v-list-item v-list-item--three-line theme--light"><div class="v-list-item__content"><p style="color: white; white-space: pre-line;">' + data["inhalt"] + '</p><div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">' + data["datumuhrzeit"] + '</div></div></div></div><br></div>';
                            else elem = '<div class="singlemsgcontainer" style="height: 100%;"><div style="background-color: #182533; border-width: 1px; border-style: solid; border-radius: 15px; max-width: 400px; height: auto; position: relative;"><div tabindex="-1" class="v-list-item v-list-item--three-line theme--light"><div class="v-list-item__content"><div class="overline mb-2" style="color: white;">' + data["sender"] + '</div><p style="color: white; white-space: pre-line;">' + data["inhalt"] + '</p><div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">' + data["datumuhrzeit"] + '</div></div></div></div><br></div>';
                            cc.innerHTML += elem;
                        }

                        function f(t, u8s) {
                            var a = -1.25; // 4sec
                            if (u8s) a = -0.078125; // 8sec
                            
                            var d = 2; // 4sec
                            if (u8s) d = 4; // 8sec

                            var e = 20; // Max px/s

                            var pow = Math.pow((t - d), 4);
                            // -1.25 * (x - 2)^4 + 20
                            // -0.078125 * (x - 4)^4 + 20 if u8s
                            return a * pow + e;
                        }

                        if (isBottom) {
                            console.log('Is at bottom');
                            var use8s = msgs.length - messages.length > 60;
                            isBottom = cc.scrollTop - (cc.scrollHeight - cc.offsetHeight) == 0;
                            var time = 0;
                            var scrollAnim = setInterval(function() {
                                cc.scrollBy(0, f(time, use8s));

                                time += 0.01;
                                isBottom = cc.scrollTop - (cc.scrollHeight - cc.offsetHeight) == 0;
                                // Time should not exceed a value of 4
                                if (isBottom || time > (use8s ? 8 : 4)) clearInterval(scrollAnim);
                            }, 10);
                        }

                        /*
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
                        */

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
window.canSend = true;

window.sendMessage = function sendMessage() {
    if (!window.canSend) return;
    window.canSend = false;
    
    var content = document.getElementById('sendMessage_Area').value;
    document.getElementById('sendMessage_Area').value = '';
    var currentMillis = new Date().getTime();

    var time = 0;
    var button = document.getElementById('sendMessage_Button');
    var animationDuration = 0.75;

    function F(t) {
        var a = 100;
        var pot = t / animationDuration;
        return Math.pow(a, pot);
    }

    function f(t) {
        var pot = (4 * t) / 3;
        var z = Math.log1p(100) * Math.pow(100, pot);
        return (4 * z) / 3;
    }

    var sendAnim = setInterval(function() {
        var x = F(time);
        var r = (Math.atan(f(time)) * 180) / Math.PI * 1.025;

        var right = -4 - x;
        var bottom = 4 + x;

        button.style = `
        position: relative;
        right: ` + right + `px;
        bottom: ` + bottom + `px;
        transform: rotate(` + (-r + 82.78883453737954) + `deg);
        `;

        time += 0.01;
        if (time > animationDuration) {
            clearInterval(sendAnim);
            time = -0.25;
            var resetDuration = 0.5;
            var resetAnim = setInterval(function() {

                var opacity = time / resetDuration;

                button.style = `
                position: relative;
                right: -4px;
                bottom: 4px;
                opacity: ` + opacity + `;
                transform: rotate(0deg);
                `;

                time += 0.01;
                if (time > resetDuration) {
                    clearInterval(resetAnim);
                    window.canSend = true;
                }
            }, 10);
        }
    }, 10);

    fetch('http://' + this.IP_ADDRESS + ':8080/GFOS/daten/nachricht/add/1', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'text/plain'
        },
        body: JSON.stringify({
            senderid: this.CURRENT_USER_ID,
            inhalt: content,
            datumuhrzeit: currentMillis,
            chatid: this.CURRENT_CHAT_ID,
        })
    }).then(response => {
        response.clone();
        response.text().then(content => {
            console.warn(content);
        }); 
    });
}
</script>