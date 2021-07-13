<template>
    <v-container>
        <div class="mx-auto" max-width="1000px" style="margin:100px">
            <v-row justify="center">
                    <v-card
                    color="#202b36"
                    >
                        <v-tabs
                        v-model="tab"
                        background-color="primary"
                        dark
                        fixed-tabs
                        >
                            <v-tab
                            href="#feed"
                            >
                                Feed
                            </v-tab>

                            <v-tab-item
                            value="feed"
                            >

                                    <v-card
                                    :dark="getDarkmode()"
                                    max-width="700"
                                    tile
                                    rounded="true"
                                    min-height="400px"
                                    v-if="channelFeed.posts.length !== 0"
                                    >
                                        <div
                                        id="postsList"
                                        >   
                                            <v-list 
                                                rounded
                                                :dark="getDarkmode()"
                                                min-height="400px"
                                                max-height="400px"
                                                min-width="700"
                                                class="overflow-y-auto"
                                                width="536px"
                                                >
                                                    <template 
                                                    v-for="(item, i) in channelFeed.posts"
                                                    >
                                                        <v-card
                                                        :key="i"
                                                        color="blue"
                                                        min-height="80"
                                                        style="margin:15px"
                                                        rounded="true"
                                                        elevation="24"
                                                        >
                                                            <v-card-title>
                                                                <span class="title font-weight-light"
                                                                v-text='item.timestring'></span>
                                                                <v-spacer/>
                                                            </v-card-title>

                                                            <v-card-text class="headline font-weight-bold">
                                                                "{{item.post.inhalt}}"
                                                            </v-card-text>

                                                            <v-card-actions>
                                                                <v-list-item class="grow">
                                                                    <v-list-item-avatar>
                                                                    <v-img
                                                                    v-if="item.profilbild === undefined"
                                                                    class="elevation-6"
                                                                    alt=""
                                                                    src="data:image/png;base64,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"
                                                                    ></v-img>
                                                                    <v-img
                                                                    v-else
                                                                    :src="item.profilbild"
                                                                    >
                                                                    </v-img>
                                                                    </v-list-item-avatar>

                                                                    <v-list-item-content>
                                                                    <v-list-item-title>{{item.post.sender}}</v-list-item-title>
                                                                    </v-list-item-content>

                                                                    <v-row
                                                                    align="center"
                                                                    justify="end"
                                                                    >
                                                                    <v-progress-circular
                                                                    indeterminate
                                                                    color="red"
                                                                    :size="50"
                                                                    v-if=" loading_heart === true && loading_id === item.post.nachrichtid"
                                                                    >
                                                                        <v-icon 
                                                                        class="mr-1"
                                                                        v-if="item.likedbyme === undefined"
                                                                        @click="like(item.post.nachrichtid);"
                                                                        large
                                                                        >
                                                                            mdi-heart
                                                                        </v-icon>
                                                                    
                                                                        <v-icon 
                                                                        class="mr-1"
                                                                        v-else
                                                                        color="red"
                                                                        @click="like(item.post.nachrichtid);"
                                                                        large
                                                                        >
                                                                            mdi-heart
                                                                        </v-icon>
                                                                    </v-progress-circular>
                                                                    <div
                                                                    v-else
                                                                    >   
                                                                        <v-icon 
                                                                        class="mr-1"
                                                                        v-if="item.likedbyme === undefined && ( !loading_heart || loading_id !== item.post.id)"
                                                                        @click="like(item.post.nachrichtid);"
                                                                        color="grey"
                                                                        >
                                                                            mdi-heart
                                                                        </v-icon>
                                                                        <v-icon 
                                                                        class="mr-1"
                                                                        color="red"
                                                                        v-if="item.likedbyme !== undefined && (!loading_heart || loading_id !== item.post.id)"
                                                                        @click="like(item.post.nachrichtid);"
                                                                        >
                                                                            mdi-heart
                                                                        </v-icon>
                                                                    </div>
                                                                    
                                                                    <span class="subheading mr-2" style="padding:4px">{{item.post.countlikes}}</span>
                                                                    </v-row>
                                                                </v-list-item>
                                                            </v-card-actions>
                                                        </v-card>
                                                    
                                                    </template>
                                            </v-list>
                                        </div>
                                    </v-card>  
                                    <v-card
                                    v-else
                                    max-width="700"
                                    tile
                                    rounded="true"
                                    min-height="400px"
                                    :dark="getDarkmode()"
                                    max-height="400px"
                                    min-width="700"
                                    >
                                        <div
                                        style="height: 10 em; posiition: relative; display: flex; align-items: center; justify-content: center "
                                        >
                                            <p
                                            v-if="getDarkmode()"
                                            style="margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: white; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                            >
                                            Keiner deiner Freunde hat bisher einen Post veröffentlicht. Sei der Erste und fange an!
                                            </p>
                                            <p
                                            v-else
                                            style="margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: black; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                            >
                                            Keiner deiner Freunde hat bisher einen Post veröffentlicht. Sei der Erste und fange an!
                                            </p>

                                            <v-btn
                                            @click="openUpload();"
                                            color="blue"
                                            style="margin: 0; position: absolute; top: 60%; transform: translate(0, -50%); color: white;"
                                            >
                                                Hier gehts zum Posten
                                            </v-btn>


                                        </div>
                                    </v-card>
                               
                            </v-tab-item>
                            
                            <v-tab
                            href="#timeline"
                            >
                                Timeline
                            </v-tab>

                            <v-tab-item
                            value="timeline"
                            >
                                <v-card
                                min-height="400"
                                tile
                                rounded="true"
                                :dark="getDarkmode()"
                                max-width="700"
                                v-if="ownPosts.posts.length !== 0"
                                >
                                    <v-list 
                                        rounded
                                        :dark="getDarkmode()"
                                        min-height="400px"
                                        max-height="400px"
                                        min-width="700"
                                        class="overflow-y-auto"
                                        three-line
                                        >
                                            <template 
                                            v-for="(item, i) in ownPosts.posts"
                                            >
                                                <v-card
                                                :key="i"
                                                color="blue"
                                                min-height="80"
                                                style="margin:15px"
                                                rounded="true"
                                                elevation="24"
                                                >
                                                    <v-card-title>
                                                        <span class="title font-weight-light"
                                                        v-text='item.timestring'></span>
                                                    </v-card-title>

                                                    <v-card-text class="headline font-weight-bold">
                                                        "{{item.post.inhalt}}"
                                                    </v-card-text>

                                                    <v-card-actions>
                                                        <v-list-item class="grow">
                                                            <v-list-item-avatar>
                                                            <v-img
                                                            v-if="item.profilbild === undefined"
                                                            class="elevation-6"
                                                            alt=""
                                                            src="data:image/png;base64,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"
                                                            ></v-img>
                                                            <v-img
                                                            v-else
                                                            :src="item.profilbild"
                                                            >
                                                            </v-img>
                                                            </v-list-item-avatar>

                                                            <v-list-item-content>
                                                            <v-list-item-title>{{item.post.sender}}</v-list-item-title>
                                                            </v-list-item-content>

                                                            <v-row
                                                            align="center"
                                                            justify="end"
                                                            >
                                                                
                                                            <v-icon 
                                                            class="mr-1"
                                                            v-if="item.likedbyme === undefined"
                                                            color="grey"
                                                            >
                                                                mdi-heart
                                                            </v-icon>
                                                            <v-icon 
                                                            class="mr-1"
                                                            v-else
                                                            color="red"
                                                            >
                                                                mdi-heart
                                                            </v-icon>
                                                            <span class="subheading mr-2">{{item.post.countlikes}}</span>
                                                            </v-row>
                                                        </v-list-item>
                                                    </v-card-actions>
                                                </v-card>
                                                

                                            </template>
                                    </v-list>
                                </v-card>

                                <v-card
                                v-else
                                :dark="getDarkmode()"
                                max-width="700"
                                tile
                                rounded="true"
                                min-height="400px"
                                max-height="400px"
                                min-width="700"
                                >
                                    <div
                                    style="height: 10 em; posiition: relative; display: flex; align-items: center; justify-content: center "
                                    >
                                        <p
                                        v-if="getDarkmode()"
                                        style="text-align:center; margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: white; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                        >
                                        Du hast bisher keinen Post veröffentlicht. Fang direkt damit an!
                                        </p>
                                        <p
                                        v-else
                                        style="text-align:center; margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: black; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                        >
                                        Du hast bisher keinen Post veröffentlicht. Fang direkt damit an!
                                        </p>

                                        <v-btn
                                        @click="openUpload();"
                                        color="blue"
                                        style="margin: 0; position: absolute; top: 60%; transform: translate(0, -50%); color: white;"
                                        >
                                            Hier gehts zum Posten
                                        </v-btn>


                                    </div>
                                </v-card>
                            </v-tab-item>

                            <v-tab
                            href="#upload"
                            >
                                Upload
                            </v-tab>

                            <v-tab-item
                            value="upload"
                            >
                                <v-card
                                min-width="700"
                                tile
                                rounded="true"
                                min-height="400px"
                                :dark="getDarkmode()"
                                height="100%"
                                >

                                

                                    <v-card-title>   
                                        <v-row
                                        align="stretch"
                                        >
                                            <v-col>
                                            <span v-if="getDarkmode()">
                                            Veröffentliche einen neuen Post
                                            </span>
                                            <span v-else style="color:black">
                                            Veröffentliche einen neuen Post
                                            </span>
                                            </v-col>
                                                    
                                                </v-row>
                                    </v-card-title>

                                    <v-card-text>
                                        <v-card
                                                :key="i"
                                                color="blue"
                                                min-height="80"
                                                style="margin:15px"
                                                rounded="true"
                                                elevation="24"
                                                max-width="640px"
                                                >
                                                    <v-card-title>
                                                        <span class="title font-weight-light">{{date}}</span>
                                                        </v-card-title>

                                                    <v-card-text class="headline font-weight-bold" v-if="newPost === 'Teile deinen Freunden mit, was gerade passiert'">{{newPost}}
                                                    </v-card-text>
                                                    <v-card-text class="headline font-weight-bold" v-else>"{{newPost}}"
                                                    </v-card-text>

                                                    <v-card-actions>
                                                        <v-list-item class="grow">
                                                            <v-list-item-avatar>
                                                            <v-img
                                                            class="elevation-6"
                                                            alt=""
                                                            src="data:image/png;base64,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"
                                                            ></v-img>
                                                            </v-list-item-avatar>

                                                            <v-list-item-content>
                                                            <v-list-item-title>{{username}}</v-list-item-title>
                                                            </v-list-item-content>

                                                            <v-row
                                                            align="center"
                                                            justify="end"
                                                            >
                                                            
                                                                <v-icon 
                                                                class="mr-1"
                                                                color="grey"
                                                                >
                                                                    mdi-heart
                                                                </v-icon>
                                                            
                                                            
                                                            <span class="subheading mr-2">0</span>
                                                            </v-row>
                                                        </v-list-item>
                                                    </v-card-actions>
                                                </v-card>

                                                <v-text-field
                                                :dark="getDarkmode()"
                                                filled
                                                v-model="input"
                                                label="Gebe hier deinen Text ein"
                                                @input="update(input)"
                                                ref="inputRef"
                                                clearable
                                                @keydown.enter="postPost();"
                                                >
                                                </v-text-field>
                                    </v-card-text>
                                    <v-card-actions>
                                        <v-row
                                        align="end"
                                        >
                                            <v-col 
                                            class="text-right"
                                            >
                                                <v-btn
                                                rounded
                                                text
                                                :dark="getDarkmode()"
                                                @click="postPost();"
                                                :disabled="this.newPost === 'Teile deinen Freunden mit, was gerade passiert'"
                                                >
                                                    Veröffentlichen
                                                </v-btn>
                                            </v-col>
                                    
                                        </v-row>
                                    </v-card-actions>

                                </v-card>
                            </v-tab-item>

                        </v-tabs>                       
                    </v-card>
            </v-row>
        </div>
    </v-container>
</template>

<script>
import axios from 'axios';
import { EventBus } from './EventBus.js';
export default {
    

    data: () => ({
        channelFeed: {},
        ownPosts: {},
        image: window.CURRENT_IMAGE,
        newPost: "Teile deinen Freunden mit, was gerade passiert",
        date: "",
        tab: "feed",
        username: window.CURRENT_USERNAME,
        loading_heart: false,
        loading_id: -1,
    }),

    mounted() {
        this.loadData();
        EventBus.$on("APP_INTERVAL", (payload)=>{
            this.updateTime();
        })
        EventBus.$on("RELOAD", (payload) =>{
            this.loadData();
        });
        
    },

    methods: {

        getDarkmode(){
            return window.DARK_MODE
        },

        methodThatForcesUpdate() {
            this.$forceUpdate()
        },

        like(i) {
            this.loading_heart = true
            this.loading_id = i
            const post = {
                            "eigeneid": window.CURRENT_USER_ID,
                            "nachrichtid": i
                         }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/nachricht/like/1" + window.CURRENT_TOKEN , post, { headers})
            .then((resp)=>{
                this.loadData();
                setTimeout(() => {  
                    this.loading_heart = false 
                    this.loading_id = -1    
                }, 1000);
                
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },

        async loadData() {
            axios.get(window.IP_ADDRESS +"/GFOS/daten/nutzer/getChannel/" + window.CURRENT_USER_ID + "/" + window.CURRENT_TOKEN)
                .then((resp)=>{
                this.ownPosts = resp.data
            })
            .catch(error =>{
                console.error(error.response.data)
            })
            axios.get(window.IP_ADDRESS +"/GFOS/daten/nachricht/getChannelFeed/" + window.CURRENT_USER_ID + "/" + window.CURRENT_TOKEN)
                .then((resp)=>{
                this.channelFeed = resp.data
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },

        async postPost() {
            if(this.newPost === "Teile deinen Freunden mit, was gerade passiert"){
                this.loadData();
                return
            }
            const now = Date.now();
            const payload = {
                "eigeneId": window.CURRENT_USER_ID,
                "inhalt": this.newPost,
                "datumuhrzeit": now
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/nachricht/sendInChannel/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                this.loadData();
                
                EventBus.$emit("INFOMESSAGE", {'message': 'Dein Post wurde erfolgreich gesendet!', "icon": "SUCCESS"})
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
            await this.clearInput();
            this.newPost = "Teile deinen Freunden mit, was gerade passiert"
        },

        resetPost(){
            this.newPost = "Teile deinen Freunden mit, was gerade passiert"  
        },

        async clearInput() {
          this.$refs.inputRef.clearableCallback()
        },

        update(text) {
            this.newPost = text 
        },

        updateTime() {
            var now = new Date();
            this.date = this.fillStringZero(now.getHours(), 2) + ":" + this.fillStringZero(now.getMinutes(), 2)
        },

        fillStringZero(string, length) {
            string = string.toString();
            var zeroString = '';
            for (var i=length-string.length; i>0; i--) {
                zeroString += '0';
            }
            return zeroString + string;
        },

        openUpload(){
            this.tab = "upload"
        }



    },

}
</script>
