<template>
    <v-container class='contactcontainer'>
        <v-hover>
            <v-card slot-scope="{ hover }" :style="`background-color: ${hover ? '#202B36' : '#17212B'}`" class="mx-auto" height="100px" max-width="400px">
                <v-card-text class="font-weight-medium mt-12 text-center subtitle-1">
                    CONTENT
                </v-card-text>
            </v-card>
        </v-hover>
    </v-container>
</template>

<script>
import EventBus from './EventBus'; // Herr Albrechts Anwort abwarten, EventBus lädt nicht (missing package?)

export default {
    name: 'ChatListe',

    data: () => ({
    }),

    mounted() {
        var IP_ADDRESS = '84.191.205.25';Ö
        var CURRENT_USER_ID = 107;

        //var chats = [];

        fetch('http://' + IP_ADDRESS + ':8080/GFOS/daten/chat/nutzerid/' + CURRENT_USER_ID + '/1').then(response => {
            if (response.status !== 200) {
                console.error('Code !== 200:' + response);
                return null;
            }
            response.clone();
            response.json().then(conts => {
                document.getElementById('contactcontainer').innerHTML = '';
                for (var i=0; i<conts.length; i++) {
                    var data = conts[i];
                    var chatid = data["chatid"];
                    var elem = '<div class="mx-auto v-card v-sheet theme--light" style="height: 100px; max-width: 400px; background-color: rgb(23, 33, 43);"><div class="v-card__text font-weight-medium mt-12 text-center subtitle-1"><span onclick="window.openChat(' + chatid + ');">' + data["name"] + '</span></div></div>';
                    document.getElementById('chatcontainer').innerHTML += elem;
                }

                /*var difference = conts.length - chats.length;
                var allSingles = document.getElementsByClassName('singlemsgcontainer');
                var scrollValue = 0; // Funktioniert, scrollt aber ein paar Pixel weiter (das doppelte?)
                for (i=allSingles.length-1; i>=difference; i--) {
                    scrollValue += allSingles[i].scrollHeight;
                }
                var count = 0;
                var id = setInterval(function() {
                    window.scrollBy(0, 5);
                    count += 15;
                    if (count > scrollValue) clearInterval(id);
                }, 16);*/

                //chats = conts;
            }).catch(error => {
                console.error('An error occured while parsing the string:' + error);
            });
        });
    }
}

window.openChat = function(chatid) {
    console.log(chatid);
    // Open chat with given chatId
    EventBus.$emit('OPENCHAT', {'chatid': chatid});
}
</script>
