<template>
    <v-container id="chatWindowContainer">
        <div id="Test" style="position: absolute; visibility: hidden; height: auto; width: auto; white-space: nowrap;"></div>
        
        <div id='messageDropdownBox' style="display: none;">
            <v-btn v-if="currentMsgIsMine" id='editMessageButton' depressed color="primary" style="width: 100%; margin-bottom: 8px;" @click="editMessage();">Bearbeiten<br></v-btn>
            <v-btn v-if="currentMsgIsMine" id='deleteMessageButton' depressed color="error" style="width: 100%; margin-bottom: 8px;" @click="deleteMessage();">Löschen<br></v-btn>
            <v-btn id='reactMessageButton' depressed color="primary" style="width: 100%; margin-bottom: 8px;" @click="answerMessage();">Antworten<br></v-btn>
            <v-btn v-if="currentMsgIsMine" id='informationMessageButton' depressed color="success" style="width: 100%;" @click="openMessageInfo();">Informationen</v-btn>
        </div>
        <div id="chatcontainer">
        </div>
        <div id='editMessageBox' style="display: none; color: white; background-color: #2B5278; border-radius: 15px; width: 100%; padding: 8px;">
            Bearbeiten: TEXT
        </div>
        <div style="
                background-color: rgb(27, 66, 104);
                border-radius: 20px;
                min-height: 32px;
                max-height: 328px;
                overflow: hidden;
            ">
            <textarea
                style="
                    margin: 8px;
                    position: relative;
                    top: calc(50% - 16px);
                    color: white;
                    width: calc(100% - 16px);
                    max-height: 240px;
                    min-height: 32px;
                    height: 32px;
                    
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
            ></textarea>
            <span style="position: relative; float: left; left: calc(100% - 84px); height: 88px; margin-bottom: -24px;">
                <span>
                    <img id="isImportant_Button" onclick="window.changeImportancy();" style="position: relative; top: 0px;" width="20" height="20" src="">
                    <br>
                    <img id="plannedDate_Button" @click="showPickerDialog = !showPickerDialog;" style="position: relative; bottom: 0px;" width="20" height="20" src="">
                </span>
                <br>
                <img id="sendMessage_Button" onclick="window.sendMessage();" style="position: relative; bottom: 48px; left: 32px;" width="32" height="32" src="">
            </span>
        </div>
        <v-dialog
        v-model="showPickerDialog"
        max-width="600"
        >
            <v-card
            dark
            >
                <v-card-title>
                    Geplante Nachricht
                </v-card-title>

                <v-card-text>
                    Hier kannst du die Uhrzeit für die geplante Nachricht ändern. Um die Nachricht sofort zu senden, klicke einfach auf "Jetzt senden".
                </v-card-text>

                <v-card-content>
                    <v-datetime-picker dark label="Select Datetime" v-model="datetime" clearText="Abbruch" okText="Einstellen" :textFieldProps="textFieldProps" :datePickerProps="dateProps" :timePickerProps="timeProps">
                        <template slot="dateIcon">
                            <v-icon>
                                mdi-calendar
                            </v-icon>
                        </template>
                        <template dark slot="timeIcon">
                            <v-icon>
                                mdi-clock
                            </v-icon>
                        </template>
                    </v-datetime-picker>
                </v-card-content>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn @click="sendMessage('normal');">
                        Jetzt senden
                    </v-btn>
                    <v-btn color="primary" @click="sendMessage('planned');">
                        Geplant senden
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-container>
</template>

<script>
import { EventBus } from './EventBus.js';
import { ImageContainer } from './ImageContainer.js';

import Vue from 'vue';
import DatetimePicker from 'vuetify-datetime-picker';
 
Vue.use(DatetimePicker);
export default {
    name: 'Chat',

    data: () => ({
        showPickerDialog: false,
        datetime: 0,
        textFieldProps: {value: "Hallo?"},
        dateProps: {},
        timeProps: {format: '24hr'},
        currentMsg: window.currentMsg,
        currentMsgId: window.currentMsgId,
        currentMsgIsMine: window.currentMsgIsMine,
        loadMessagesFunc: null,
    }),

    mounted() {
        window.CURRENT_CHAT_ID = -1;
        var messages = [];
        var imageContainer = new ImageContainer();

        document.getElementById('sendMessage_Button').src = imageContainer.getValue('SEND');
        document.getElementById('isImportant_Button').src = imageContainer.getValue('UNIMPORTANT');
        document.getElementById('plannedDate_Button').src = imageContainer.getValue('PLANNED');
        setInterval(() => {
            this.currentMsgIsMine = window.currentMsgIsMine;
            this.currentMsgText = window.currentMsgText;
            this.currentMsgId = window.currentMsgId;
            this.currentMsg = window.currentMsg;
        }, 16);

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
            fetch(window.IP_ADDRESS + '/GFOS/daten/nachricht/chat/getNewest/' + window.CURRENT_CHAT_ID + '/' + window.CURRENT_USER_ID + '/' + window.CURRENT_TOKEN).then(response => {
                if (response.status !== 200) {
                    if(response.status === 401){
                        EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                        EventBus.$emit("LOGOUT", {})
                    }
                    return null;
                }
                response.clone();
                response.json().then(data => {
                    newest = data;
                    if (messages.length === 0) loadMsgs(true, false);
                    else {
                        var localNewest = null;
                        for (var i=0; i<messages.length-1; i++) {
                            localNewest = messages[messages.length - 1 - i];
                            if (localNewest['isplanned'] === false) {
                                break;
                            }
                        }
                        if (localNewest !== null) {
                            if (localNewest['nachrichtid'] !== newest['nachrichtid'])
                                loadMsgs(false, false);
                            if (localNewest['readbyall'] !== newest['readbyall'] || localNewest['inhalt'] !== newest['inhalt'] || localNewest['isMarked'] !== newest['isMarked'])
                                loadMsgs(false, true);

                        }
                    }
                }).catch(error => {
                    console.error('An error occured while parsing the string:' + error);
                });
            });
        }

        EventBus.$on('CHANGEHEIGHT', (payload) => {
            if(window.DARK_MODE){
                document.getElementById('chatcontainer').style = 'overflow: auto; min-height: 400px; height: "' + payload["height"] + 'px"; max-height: ' + payload["height"] + 'px; background-color: #0E1621; border-radius: 15px; padding: 16px;';
            }
            else{
                document.getElementById('chatcontainer').style = 'overflow: auto; min-height: 400px; height: "' + payload["height"] + 'px"; max-height: ' + payload["height"] + 'px; background-color: white; border-radius: 15px; padding: 16px;';
            }
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
            window.sendMessage();
        }); // EventBus.$on('KSC_SENDMESSAGE');

        EventBus.$on('RELOAD', (payload) => {
            loadMessages(false, true, payload['scrollTop']);
        });

        function loadMessages(animate, justRefresh, scrollTopPos) { // Reload all messages (will be called if a newer message is available)
            if (window.CURRENT_USER_ID === -1 || window.CURRENT_TOKEN == '') return;
            fetch(window.IP_ADDRESS + '/GFOS/daten/nachricht/chat/' + window.CURRENT_CHAT_ID + '/' + window.CURRENT_USER_ID + '/' + window.CURRENT_TOKEN).then(response => {
                if (response.status !== 200) {
                    if(response.status === 401){
                        EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                        EventBus.$emit("LOGOUT", {})
                    }
                    return null;
                }
                response.clone();
                response.json().then(msgs => {
                    EventBus.$emit('RECEIVEDMESSAGE', {'message': msgs[msgs.length-1]});
                    var diff = msgs.length - messages.length;

                    var cc = document.getElementById('chatcontainer');
                    var isBottom = cc.scrollTop - (cc.scrollHeight - cc.offsetHeight) == 0;

                    if (!animate) cc.scrollTop = cc.scrollHeight;

                    function getWidth(string) {
                        var fontSize = 12;
                        var testElem = document.getElementById("Test");
                        testElem.textContent = string;
                        testElem.style.fontSize = fontSize;
                        var height = (testElem.clientHeight + 1);
                        var width = (testElem.clientWidth + 1);
                        testElem.textContent = '';
                        return [width, height];
                    }

                    function cropText(text) {
                        var visibleWidth = getWidth(text)[0];
                        var cropped = '';
                        var lastCrop = '';
                        var charCount = 0;
                        for (var visI=0; visI<Math.ceil(visibleWidth / 376); visI++) {
                            lastCrop = '';
                            while (getWidth(lastCrop)[0] < 376) {
                                lastCrop += text[charCount];
                                charCount++;
                                if (text[charCount] === undefined) break;
                            }
                            cropped += lastCrop + '\n';
                        }
                        return cropped;
                    }

                    function limitTo(string) {
                        /*if (string.length <= length) return string;
                        var output = '';
                        for (var i=0; i<length; i++) {
                            output += string[i];
                        }
                        return output;*/
                        var cropped = '';
                        var lastCrop = '';
                        var charCount = 0;
                        while (getWidth(lastCrop)[0] < (312 - getWidth('...')[0])) {
                            lastCrop += string[charCount];
                            charCount++;
                            if (string[charCount] === undefined) break;
                        }
                        cropped = lastCrop + ((string[charCount] === undefined) ? '' : '...');
                        return cropped;
                    }

                    cc.innerHTML = '';
                    // Write all single messages into the chatcontainer
                    for (var i=0; i<msgs.length; i++) {
                        var myfunction = (a, b) => this.openMessageDropdown;
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

                        var answerTo = '';
                        if (data['antwortauf'] !== undefined) {
                            answerTo = `
                            <div style="background-color: rgba(0, 0, 0, 0.15); color: white; display: table; height: 64px; width: 100%; margin-bottom: 8px; border-radius: 10px;">
                                <div style="position: relative; display: table-cell; vertical-align: middle; top: 8px;">
                                    <div style="display: table; height: 32px; border-left: solid 2px #2222FF; position: relative; left: 16px; padding-left: 8px;">
                                        <div style="display: table-cell; vertical-align: middle; height: 32px;">
                                            ` + data['antwortauf']['sender'] + `
                                        </div>
                                    </div>
                                    <div style="display: table; height: 32px; border-left: solid 2px #2222FF; position: relative; left: 16px; margin-bottom: 16px; padding-left: 8px;">
                                        <div style="display: table-cell; vertical-align: middle; height: 32px;">
                                            ` + limitTo(data['antwortauf']['inhalt'], 28) + `
                                        </div>
                                    </div>
                                </div>
                            </div>
                            `;
                        }

                        var markedIcon = '<img id="markedIcon" onclick="window.window.markMessage(' + data["nachrichtid"] + ')" style="z-index: 100; position: relative; float: right; bottom: -16px; right: 16px;" height="16px" width="16px" src="' + imageContainer.getValue("STAR_UNFILLED") + '">';
                        if (data['isMarked']) markedIcon = '<img id="markedIcon" onclick="window.window.markMessage(' + data["nachrichtid"] + ')" style="z-index: 100; position: relative; float: right; bottom: -16px; right: 16px;" height="16px" width="16px" src="' + imageContainer.getValue("STAR") + '">';

                        if (data['senderid'] === window.CURRENT_USER_ID) {
                            var readByAllIcon = '';
                            if (data['readbyall'] !== undefined) readByAllIcon = '<img style="margin-right: 4px; position: relative; top: 2px;" height="16px" width="16px" src="' + imageContainer.getValue("VIEW") + '">';
                            var plannedIcon = '';
                            if (data['isplanned']) plannedIcon = '<img style="margin-right: 4px; position: relative; top: 4px;" height="16px" width="16px" src="' + imageContainer.getValue("CLOCK") + '">';
                            elem = `
                            <div id="myMessage" class="singlemsgcontainer" style="height: 100%; left: -400px;">
                                ` + markedIcon + `
                                <div id="visibleMessage" oncontextmenu="window.openMessageDropdown(` + data['nachrichtid'] + `, 'mine', '` + data['inhalt'] + `');return false;" style="padding: 12px; background-color: #2B5278; border-width: 1px; border-style: solid; border-top-left-radius: 15px; border-top-right-radius: 15px; border-bottom-right-radius: 15px; border-bottom-left-radius: 15px; max-width: 400px; height: auto; position: relative; right: calc(-100% + 400px);">
                                    <div tabindex="-1" class="v-list-item v-list-item--three-line theme--light" style="width: 100%;">
                                        <div class="v-list-item__content" style="width: 100%; display: table;">
                                            ` + answerTo + `
                                            <p style="color: white;">` + cropText(data['inhalt']) + `</p>
                                            <div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">` + readByAllIcon + plannedIcon + dateFormatted + `</div>
                                        </div>
                                    </div>
                                </div>
                                <br>
                            </div>`;
                        }
                        else {
                            markedIcon = '<img id="markedIcon" onclick="window.window.markMessage(' + data["nachrichtid"] + ')" style="z-index: 100; position: relative; float: left; bottom: -16px; left: calc(400px - 32px);" height="16px" width="16px" src="' + imageContainer.getValue("STAR_UNFILLED") + '">';
                            if (data['isMarked']) markedIcon = '<img id="markedIcon" onclick="window.window.markMessage(' + data["nachrichtid"] + ')" style="z-index: 100; position: relative; float: left; bottom: -16px; left: calc(400px - 32px);" height="16px" width="16px" src="' + imageContainer.getValue("STAR") + '">';
                            elem = `
                            <div id="otherMessage" class="singlemsgcontainer" style="height: 100%;">
                                ` + markedIcon + `
                                <div id="visibleMessage" oncontextmenu="window.openMessageDropdown(` + data['nachrichtid'] + `, 'other', '` + data['inhalt'] + `');return false;" style="padding: 12px; background-color: #182533; border-width: 1px; border-style: solid; border-radius: 15px; max-width: 400px; height: auto; position: relative;">
                                    <div tabindex="-1" class="v-list-item v-list-item--three-line theme--light">
                                        <div class="v-list-item__content" display: table;>
                                            <div class="overline mb-2" style="color: white;">` + data["sender"] + `</div>
                                            ` + answerTo + `
                                            <p style="color: white; white-space: pre-line;">` + cropText(data["inhalt"]) + `</p>
                                            <div class="v-list-item__subtitle" style="color: white; margin-top: 6px;">` + dateFormatted + `</div>
                                        </div>
                                    </div>
                                </div>
                                <br>
                            </div>`;
                        }

                        cc.innerHTML += elem;
                    }


                    var fadeTime = 0;
                    var chatContainerWidth = cc.clientWidth;
                    var fadeAnim = setInterval(function() {
                        var elems = document.getElementsByClassName('singlemsgcontainer');
                        var elem = null;
                        for (i=0; i<diff; i++) {
                            elem = elems[elems.length-i-1];
                            if (!elem) continue;
                            var fadeX = (1 - fadeTime) * -((elem.id == 'myMessage') ? chatContainerWidth : (chatContainerWidth / 2));
                            elem.style = `
                            position: relative;
                            left: ` + fadeX + `px;
                            `;
                        }
                        if (!elem) clearInterval(fadeAnim);
                        fadeTime += 0.01;
                        if (fadeTime > 1 && elem !== null) {
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

                    if (scrollTopPos === undefined) {
                        if (animate) {
                            if (isBottom) {
                                var scrollDiff = msgs.length - messages.length;
                                var use8s = scrollDiff > 40;
                                isBottom = cc.scrollTop - (cc.scrollHeight - cc.offsetHeight) == 0;
                                var time = 0;
                                var scrollAnim = setInterval(function() {
                                    if (scrollDiff < 60) {
                                        cc.scrollBy(0, f(time, use8s));
                                    } else {
                                        cc.scrollBy(0, fE(time, scrollDiff));
                                    }

                                    time += 0.01;
                                    isBottom = cc.scrollTop - (cc.scrollHeight - cc.offsetHeight) == 0;
                                    // Time should not exceed a value of 4 or 8
                                    if (isBottom || (time > (use8s ? 8 : 4) && scrollDiff < 60)) clearInterval(scrollAnim);
                                }, 10);
                            }
                        } else {
                            // second parameter to not scroll by 200 px
                            cc.scrollTop = (cc.scrollHeight - cc.offsetHeight) - ((justRefresh) ? 0 : 200);

                            var fr = 0;
                            var si = setInterval(() => {
                                cc.scrollBy(0, 5);
                                fr++;
                                if (fr > 40) clearInterval(si);
                            }, 16);
                        }
                    } else cc.scrollTop = scrollTopPos;

                    messages = msgs;
                })
            })
        } // loadMessages()
        window.loadMessagesFunc = loadMessages;
    }, // mounted()

    methods: {
        openMessageInfo() {
            EventBus.$emit("OPENMESSAGEINFO", {'id': window.currentMsgId});
            var dropdown = document.getElementById('messageDropdownBox');
            dropdown.style="display: none";
        },
        editMessage() {
            window.messageState = 'editing';
            document.getElementById('sendMessage_Area').value = window.currentMsgText;
            var editBox = document.getElementById('editMessageBox');
            var dropdown = document.getElementById('messageDropdownBox');
            editBox.textContent = 'Bearbeiten: ' + window.currentMsgText;
            editBox.style.display = 'block';
            dropdown.style.display = 'none';
        },
        answerMessage() {
            window.messageState = 'answering';
            var editBox = document.getElementById('editMessageBox');
            var dropdown = document.getElementById('messageDropdownBox');
            editBox.textContent = 'Antworten: ' + window.currentMsgText;
            editBox.style.display = 'block';
            dropdown.style.display = 'none';
        },
        deleteMessage() {
            // Bestätigungs-Dialog?
            var dropdown = document.getElementById('messageDropdownBox');
            dropdown.style.display = 'none';
            fetch(window.IP_ADDRESS + '/GFOS/daten/nachricht/delete/' + window.CURRENT_TOKEN, {
                mode: 'cors',
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'text/plain',
                },
                body: JSON.stringify({
                    nachrichtid: window.currentMsgId,
                    eigeneId: window.CURRENT_USER_ID,
                })
            }).then(response => {
                if (response.status !== 200) {
                    if(response.status === 401){
                        EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                        EventBus.$emit("LOGOUT", {})
                    }
                    return null;
                }
                response.clone();
                response.text().then(content => {
                    EventBus.$emit('RELOAD', {});
                }); 
            });
        },
        sendMessage(sendType) {
            if (!sendType || sendType === 'normal') {
                window.sendMessage('normal');
            } else if (sendType === 'planned') {
                if (this.datetime.getTime() > new Date().getTime()) {
                    window.sendMessage('planned', this.datetime.getTime());
                    this.showPickerDialog = false;
                } else {
                    EventBus.$emit("INFOMESSAGE", {"message": "Bitte wähle eine Zeit in der Zukunft aus!", "icon": "WARNING"});
                }
            }
        },
        // unused
        openMessageDropdown(messageId, messageType) {
            this.currentMsg = messageType;
            this.currentMsgId = messageId;

            var dropdown = document.getElementById('messageDropdownBox');
            var chatWindowRect = document.getElementById('chatWindowContainer').getBoundingClientRect();
            var messageRect = document.getElementById('myMessage').querySelector('#visibleMessage').getBoundingClientRect();
            var mouseXInChat = window.mouseX - chatWindowRect.x;
            
            dropdown.style = `
            padding: 16px;
            z-index: 101;
            background-color: rgba(16, 16, 32, 0.85);
            width: 256px;
            height: ` + (128 + 36) + `px;
            border-width: 1px;
            border-style: solid;
            border-color: white;
            border-radius: 15px;
            position: absolute;
            left: ` + (mouseXInChat - 100) + `px;
            top: ` + (window.mouseY - 32) + `px;
            display: ` + ((dropdown.style.display === 'block') ? 'none' : 'block') + `;`;
        }
    },
}
window.mouseX = 0;
window.mouseY = 0;
window.canSend = true;
window.isImportant = false;

window.changeImportancy = function() {
    if (!imageContainer) var imageContainer = new ImageContainer();
    window.isImportant = !window.isImportant;
    document.getElementById('isImportant_Button').src = imageContainer.getValue(((window.isImportant) ? 'IMPORTANT' : 'UNIMPORTANT'))
}

document.onmousemove = function onMouseMove(event) {
    event = event || window.event;
    window.mouseX = event.pageX;
    window.mouseY = event.pageY;
}

window.markMessage = function(messageId) {
    fetch(window.IP_ADDRESS + '/GFOS/daten/nachricht/mark/' + window.CURRENT_TOKEN, {
        mode: 'cors',
        method: 'POST',
        headers: {
            'Accept': 'application/json', // Antwort von Server
            'Content-Type': 'text/plain', // Wird an den Server gesendet
        },
        body: JSON.stringify({
            nachrichtid: messageId,
            eigeneid: window.CURRENT_USER_ID,
        })
    }).then(response => {
        if (response.status !== 200) {
                    if(response.status === 401){
                        EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                        EventBus.$emit("LOGOUT", {})
                    }
                    return null;
                }
        response.clone();
        response.text().then(content => {
            var cc = document.getElementById('chatcontainer');
            EventBus.$emit('RELOAD', {'scrollTop': cc.scrollTop});
        }); 
    });
}

window.openMessageDropdown = function(messageId, messageType, messageContent) {
    window.currentMsgId = messageId;
    window.currentMsgText = messageContent;
    window.currentMsgIsMine = (messageType === 'mine');
    window.messageState = undefined;

    var dropdown = document.getElementById('messageDropdownBox');
    var chatWindowRect = document.getElementById('chatWindowContainer').getBoundingClientRect();
    var mouseXInChat = window.mouseX - chatWindowRect.x;
    
    dropdown.style = `
    padding: 16px;
    z-index: 101;
    background-color: rgba(16, 16, 32, 0.80);
    width: 256px;
    border-width: 1px;
    border-style: solid;
    border-color: white;
    border-radius: 15px;
    position: absolute;
    left: ` + (mouseXInChat - 100) + `px;
    top: ` + (window.mouseY - 32) + `px;
    display: ` + ((dropdown.style.display === 'block') ? 'none' : 'block') + `;`;
}

window.sendMessage = function sendMessage(sendType, sendTime) {
    if (window.CURRENT_USER_ID === -1 || window.CURRENT_TOKEN == '') return;

    var content = document.getElementById('sendMessage_Area').value.trim();
    if (!window.canSend) return;
    if (content === '') {
        EventBus.$emit('INFOMESSAGE', {'message': 'Nachricht darf nicht leer sein!', "icon": "ERROR"})
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

        var left = 32 + x;
        var bottom = 48 + x;

        button.style = `
        position: relative;
        left: ` + left + `px;
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
                left: 32px;
                bottom: 48px;
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

    var route = 'nachricht/add';
    var sendData = JSON.stringify({
        senderid: window.CURRENT_USER_ID,
        inhalt: content,
        datumuhrzeit: ((sendType === 'normal' || sendType === undefined) ? currentMillis : sendTime),
        chatid: window.CURRENT_CHAT_ID,
        isimportant: window.isImportant,
    });

    if (window.messageState === 'editing') {
        route = 'nachricht/edit';
        sendData = JSON.stringify({
            senderid: window.CURRENT_USER_ID,
            inhalt: content,
            nachrichtid: window.currentMsgId,
        });
    } else if (window.messageState === 'answering') {
        sendData = JSON.stringify({
            senderid: window.CURRENT_USER_ID,
            inhalt: content,
            datumuhrzeit: ((sendType === 'normal' || sendType === undefined) ? currentMillis : sendTime),
            chatid: window.CURRENT_CHAT_ID,
            answerId: window.currentMsgId,
            isimportant: window.isImportant,
        });
    }



    fetch(window.IP_ADDRESS + '/GFOS/daten/' + route + '/' + window.CURRENT_TOKEN, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'text/plain'
        },
        body: sendData
    }).then(response => {
        if (response.status !== 200) {
            if(response.status === 401){
                EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                EventBus.$emit("LOGOUT", {})
            }
            return null;
        }
        response.clone();
        response.text().then(content => {

            var editBox = document.getElementById('editMessageBox');
            window.currentMsgId = -1;
            window.currentMsgText = '';
            window.currentMsgIsMine = false;
            window.messageState = null; // undefined || 'editing' || 'reacting'
            editBox.textContent = '';
            editBox.style.display = 'none';

            if(content === "youblocked"){
                EventBus.$emit('INFOMESSAGE', {'message': 'Du hast diesen Nutzer blockiert!', "icon": "ERROR"})
            }
            if(content === "gotblocked"){
                EventBus.$emit('INFOMESSAGE', {'message': 'Du wurdest von diesem Nutzer blockiert!', "icon": "ERROR"})
            }

            EventBus.$emit('RELOAD', {});
        }); 
    });
}

window.currentMsgId = -1;
window.currentMsgText = '';
window.currentMsgIsMine = false;
window.messageState = null; // undefined || 'editing' || 'reacting'
window.loadMessagesFunc = undefined;
</script>