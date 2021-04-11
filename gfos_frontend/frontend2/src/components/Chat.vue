<template>
    <v-container>
        <div id="chatcontainer">
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
import { ImageContainer } from './ImageContainer.js';

export default {
    name: 'Chat',

    data: () => ({
    }),

    mounted() {
        window.CURRENT_CHAT_ID = -1;
        var messages = [];
        var imageContainer = new ImageContainer();

        function loginTest() {
            fetch(window.IP_ADDRESS + '/GFOS/daten/nutzer/login', {
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

        // loginTest();

        function fillStringZero(string, length) {
            string = string.toString();
            var zeroString = '';
            for (var i=length-string.length; i>0; i--) {
                zeroString += '0';
            }
            return zeroString + string;
        }

        function fetchIfNewest(loadMsgs) {
            // Check for newest message and reload all messages if there is a newer one
            if (window.CURRENT_USER_ID === -1 || window.CURRENT_TOKEN == '') return;
            var newest = null;
            fetch(window.IP_ADDRESS + '/GFOS/daten/nachricht/chat/getNewest/' + window.CURRENT_CHAT_ID + '/' + window.CURRENT_TOKEN).then(response => {
                if (response.status !== 200) {
                    console.error('Code !== 200:' + response);
                    return null;
                }
                response.clone();
                response.json().then(data => {
                    newest = data;
                    if (messages.length === 0) loadMsgs();
                    else {
                        var localNewest = null;
                        for (var i=0; i<messages.length-1; i++) {
                            localNewest = messages[messages.length - 1 - i];
                            if (localNewest['isplanned'] === false) {
                                break;
                            }
                        }
                        if (localNewest !== null && (localNewest['nachrichtid'] !== newest['nachrichtid'] || localNewest['readbyall'] !== newest['readbyall'] || localNewest['inhalt'] !== newest['inhalt'])) {
                            loadMsgs();
                        }
                    }
                }).catch(error => {
                    console.error('An error occured while parsing the string:' + error);
                });
            });
        }
        
        EventBus.$on('MYEVENT', (payload) => {
            console.log('THIS IS MY EVENT');
            console.log(payload);
        });

        EventBus.$on('CHANGEHEIGHT', (payload) => {
            document.getElementById('chatcontainer').style = 'overflow: auto; min-height: 400px; height: "' + payload["height"] + 'px"; max-height: ' + payload["height"] + 'px; background-color: #0E1621; border-radius: 15px; padding: 16px;';
        });

        EventBus.$on('APP_INTERVAL', (payload) => {
            if (window.CURRENT_CHAT_ID !== -1) {
                fetchIfNewest(loadMessages);
            }
        });

        EventBus.$on('OPENCHAT', (payload) => {
            //window.CURRENT_USER_ID = payload['userid']; //set in cookie?
            window.CURRENT_CHAT_ID = payload['chatid'];
            messages = [];

            document.getElementById('chatcontainer').innerHTML = '';

        }); // EventBus.$on('OPENCHAT')

        EventBus.$on('KSC_SENDMESSAGE', (payload) => {
            console.log('EventBus: KSC_SENDMESSAGE calling function window.sendMessage();');
            window.sendMessage();
        }); // EventBus.$on('KSC_SENDMESSAGE');

        function loadMessages() { // Reload all messages (will be called if a newer message is available)
            if (window.CURRENT_USER_ID === -1 || window.CURRENT_TOKEN == '') return;
            console.log('loadMessages()');
            fetch(window.IP_ADDRESS + '/GFOS/daten/nachricht/chat/' + window.CURRENT_CHAT_ID + '/' + window.CURRENT_USER_ID + '/' + window.CURRENT_TOKEN).then(response => {
                if (response.status !== 200) {
                    console.error('Code !== 200:' + response);
                    return null;
                }
                response.clone();
                response.json().then(msgs => {
                    EventBus.$emit('RECEIVEDMESSAGE', {'message': msgs[msgs.length-1]});
                    var diff = msgs.length - messages.length;

                    var cc = document.getElementById('chatcontainer');
                    var isBottom = cc.scrollTop - (cc.scrollHeight - cc.offsetHeight) == 0;

                    cc.innerHTML = '';
                    // Write all single messages into the chatcontainer
                    for (var i=0; i<msgs.length; i++) {
                        var data = msgs[i];
                        var elem = '';
                        var date = new Date(data['datumuhrzeit']);
                        var now = new Date();
                        var dateFormatted = fillStringZero(date.getHours(), 2) + ':' + fillStringZero(date.getMinutes(), 2);
                        if (parseInt(date.getTime() / 86400000) < parseInt(now.getTime() / 86400000)) {
                            var extraDate = fillStringZero(date.getDate(), 2) + '.' + fillStringZero(date.getMonth() + 1, 2)
                            if (parseInt(date.getFullYear()) < parseInt(now.getFullYear())) {
                                extraDate += '.' + fillStringZero(date.getFullYear(), 2);
                            }
                            dateFormatted = extraDate + ' ' + dateFormatted;
                        }
                        if (data['senderid'] === window.CURRENT_USER_ID) {
                            var readByAllIcon = '';
                            if (data['readbyall'] !== undefined) readByAllIcon = '<img style="margin-right: 4px; position: relative; top: 2px;" height="16px" width="16px" src="' + imageContainer.getValue("VIEW") + '">';
                            var plannedIcon = '';
                            if (data['isplanned']) plannedIcon = '<img style="margin-right: 4px; position: relative; top: 4px;" height="16px" width="16px" src="' + imageContainer.getValue("CLOCK") + '">';
                            var markedIcon = '';
                            if (data['isMarked']) markedIcon = '<img style="z-index: 100; position: relative; float: right; bottom: -16px; right: 16px;" height="16px" width="16px" src="' + imageContainer.getValue("STAR") + '">'
                            elem = `
                            <div id="myMessage" class="singlemsgcontainer" style="height: 100%; left: -400px;">
                                ` + markedIcon + `
                                <div style="padding: 12px; background-color: #2B5278; border-width: 1px; border-style: solid; border-top-left-radius: 15px; border-top-right-radius: 15px; border-bottom-right-radius: 15px; border-bottom-left-radius: 15px; max-width: 400px; height: auto; position: relative; right: calc(-100% + 400px);">
                                    <div tabindex="-1" class="v-list-item v-list-item--three-line theme--light">
                                        <div class="v-list-item__content">
                                            <p style="color: white; white-space: pre-line;">` + data['inhalt'] + `</p>
                                            <div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">` + readByAllIcon + plannedIcon + dateFormatted + `</div>
                                        </div>
                                    </div>
                                </div>
                                <br>
                            </div>`;
                        }
                        else elem = '<div id="otherMessage" class="singlemsgcontainer" style="height: 100%;"><div style="padding: 12px; background-color: #182533; border-width: 1px; border-style: solid; border-radius: 15px; max-width: 400px; height: auto; position: relative;"><div tabindex="-1" class="v-list-item v-list-item--three-line theme--light"><div class="v-list-item__content"><div class="overline mb-2" style="color: white;">' + data["sender"] + '</div><p style="color: white; white-space: pre-line;">' + data["inhalt"] + '</p><div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">' + dateFormatted + '</div></div></div></div><br></div>';
                        cc.innerHTML += elem;
                    }

                    var fadeTime = 0;
                    var chatContainerWidth = cc.clientWidth;
                    var fadeAnim = setInterval(function() {
                        var elems = document.getElementsByClassName('singlemsgcontainer');
                        for (i=0; i<diff; i++) {
                            var elem = elems[elems.length-i-1];
                            var fadeX = (1 - fadeTime) * -((elem.id == 'myMessage') ? chatContainerWidth : (chatContainerWidth / 2));
                            elem.style = `
                            position: relative;
                            left: ` + fadeX + `px;
                            `;
                        }
                        fadeTime += 0.01;
                        if (fadeTime > 1) {
                            elem.style = `
                            position: relative;
                            left: 0px;
                            `;
                            clearInterval(fadeAnim);
                        }
                    }, 10);

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

                    function fE(t, count) {
                        return 1.0625 * Math.pow((t - (count / 18)), 2) + count;
                    }

                    if (isBottom) {
                        var scrollDiff = msgs.length - messages.length;
                        var use8s = scrollDiff > 60;
                        isBottom = cc.scrollTop - (cc.scrollHeight - cc.offsetHeight) == 0;
                        var time = 0;
                        var scrollAnim = setInterval(function() {
                            if (scrollDiff < 73) {
                                cc.scrollBy(0, f(time, use8s));
                                console.log('scrollDiff < 73: ' + f(time, use8s));
                            } else {
                                cc.scrollBy(0, fE(time, scrollDiff));
                                console.log('scrollDiff >= 73: ' + fE(time, scrollDiff));
                            }

                            time += 0.01;
                            isBottom = cc.scrollTop - (cc.scrollHeight - cc.offsetHeight) == 0;
                            // Time should not exceed a value of 4 or 8
                            if (isBottom || time > (use8s ? 8 : 4)) clearInterval(scrollAnim);
                        }, 10);
                    }

                    messages = msgs;
                }).catch(error => {
                    console.error('An error occured while parsing the string:' + error);
                });
            });
        } // loadMessages()
    }, // mounted()
}

window.canSend = true;
window.sendMessage = function sendMessage() {
    if (window.CURRENT_USER_ID === -1 || window.CURRENT_TOKEN == '') return;

    var content = document.getElementById('sendMessage_Area').value.trim();
    if (!window.canSend) return;
    if (content === '') {
        EventBus.$emit('INFOMESSAGE', {'message': 'Nachricht darf nicht leer sein!'})
        return;
    }
    window.canSend = false;
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

    fetch(window.IP_ADDRESS + '/GFOS/daten/nachricht/add/' + window.CURRENT_TOKEN, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'text/plain'
        },
        body: JSON.stringify({
            senderid: window.CURRENT_USER_ID,
            inhalt: content,
            datumuhrzeit: currentMillis,
            chatid: window.CURRENT_CHAT_ID,
        })
    }).then(response => {
        response.clone();
        response.text().then(content => {
            console.warn(content);
        }); 
    });
}
</script>