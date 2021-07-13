<template>
    <v-dialog
    v-model="dialog"
    max-width="290"
    >
        <v-card
        dark
        color="#202b36"
        max-width="300"
        >
            <v-card-title class="headline">
            Nachrichteninfo
            </v-card-title>

            <v-card-text>
                
                <v-list
                rounded
                color="#202b36"
                class="overflow-y-auto"
                >
                    <v-list-item-group
                    v-model="model"
                    dark
                    color="blue"
                    >
                        <v-list-item
                        v-for="(item, i) in infoData.gelesenVon"
                        :key="i"
                        >

                                <v-list-item-avatar>
                                    <div v-if="item.profilbild === undefined">
                                        <v-img 
                                        src='data:image/png;base64,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'
                                        ></v-img>
                                    </div>
                                    <div v-else>
                                        <v-img
                                        :src="item.profilbild.base64"
                                        ></v-img>
                                    </div>
                                </v-list-item-avatar>


                                <v-list-item-content>
                                    <v-list-item-title
                                    v-if="item.benutzername.localeCompare(benutzername) !== 0"
                                    >
                                        {{item.benutzername}}
                                    </v-list-item-title>
                                    <v-list-item-title
                                    v-else
                                    style="font-style:italic"
                                    >
                                        Du
                                    </v-list-item-title>
                                </v-list-item-content>

                                <v-list-item-icon>
                                        <v-img
                                        max-height="32px"
                                        max-width="32px"
                                        src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAIYQAACGEBaNePYgAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAL2SURBVFiF7ZbLaxRZFIe/U9XGTmI0bVWbSkdi4oigiSAuhkTBWYz42Pj6D1wJMgqTCJm4FlsRNYjK7BR0GRCZ2fVixsciQdzIwCx8pU3bKazuyqiUmE6s62I60o+yuk22/S3PPff8Pqh7qwoaNGiwDBzHabNtu3U5M6TexozjbNbRDyH+bpT0IXQDenF5XsGUwFNB3V8QubveMDLLFlBKyUw+fxjktMDOemUBH0ih/Aud8fhfSxKwXbefz+p3Jewqm6wUj154TEx52B/mEcBavYJdG1cx0NOKVjVR/aF0/WQiFkvXLZB13OMiagyIltbTboFkyibtFgKlfzBXMrrXomvNisqlWUEds0zzXk2BbM49J6jRynraLTB8N4NX8Im1RNjfZ6Brwp1Ju6yvLaozdnQ9iWoJH9SpTtO8XlrUysNzyaBwXymSKRuv4PNj72rGj2/jQL9Bc5NW2cqHT59JpmxU1QoayDU7lyubHykPl9+q98GjFx5pt0CsJcL5I5u4cT/D+JO3Qa0APHfmmEx7DGyovqEKOWfnclimmSxahYcDTEx5AOzvM8jMfgoNX2TylffNtaLEKEBkxslfAobChs28nwcgogkPn/9XM7x0T5hENp+fiyiNNxLwwEqR4lG9XXHgQvfU0+PTpSUM47ISfg1rDDjRNUm0N4WHK650xo1hDSBhGGNhEoM9q75bYGfPtz8RorhixY0hKLmGYRKDva1sNFbWHb7FirKju6VmeJnAogTICf5/l39FEzizz6ItqlOLWLPOyM8dgWdA4GxpeLFWje04B5Vot4BYaT37bp7zKZtnzlxg+FYrysieDta1VZ2Zjwi/dBrGzQCpYLKu243PVUEdKq0r4PFrj4lXH8m+KyBAV3sTgz2t7OhuCRgofyv8EwnT/Dcop+ZtmXFmfwJ/BGEfFY8sFOGB+HLRiq/9M7ytThzHSSyIfhildiNsB3qBxbu2ADIl8I8v6oG+oN/r6Gh/WbfsUpmenm5e7i9ZgwYNvgAg2g8QAmV/6gAAAABJRU5ErkJggg=="
                                        >
                                        </v-img>
                                </v-list-item-icon>

                            
                        </v-list-item>

                    </v-list-item-group>
                </v-list>

                <v-list
                rounded
                color="#202b36"
                class="overflow-y-auto"
                v-if="infoData.nichtgelesenVon.length !== 0"
                >
                    <v-list-item-group
                    v-model="model"
                    dark
                    color="blue"
                    >
                        <v-list-item
                        v-for="(item, i) in infoData.nichtgelesenVon"
                        :key="i"
                        >
                            <v-list-item-avatar>
                            <div v-if="item.profilbild === undefined">
                                <v-img 
                                src='data:image/png;base64,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'
                                ></v-img>
                            </div>
                            <div v-else>
                                <v-img
                                :src="item.profilbild.base64"
                                ></v-img>
                            </div>
                        </v-list-item-avatar>


                        <v-list-item-content>
                            <v-list-item-title
                            >
                                {{item.benutzername}}
                            </v-list-item-title>
                        </v-list-item-content>
                        </v-list-item>

                    </v-list-item-group>
                </v-list>

            </v-card-text>

            <v-card-actions>
            <v-spacer></v-spacer>

            <v-btn
                color="darken-1"
                text
                @click="dialog = false"
            >
                Schließen
            </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
import { EventBus } from './EventBus.js';
import { ImageContainer } from './ImageContainer.js';
import axios from 'axios';

export default {
    data: () => ({
        dialog: false,
        infoData: {},
        benutzername: window.CURRENT_USERNAME
    }),

    mounted() {
        EventBus.$on("OPENMESSAGEINFO", (payload) => {
            this.loadData(payload["id"]);
            this.dialog = true
        });

        EventBus.$on("RELOAD", (payload) =>{
            this.loadData();
        });
    },

    methods: {
        //
        loadData(id) {
            axios.get(window.IP_ADDRESS +"/GFOS/daten/nachricht/info/" + id + "/" + window.CURRENT_USER_ID + "/" + window.CURRENT_TOKEN)
            .then((resp)=>{
                this.infoData = resp.data
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        }
    },
}

window.closeInfo = function() {
    EventBus.$emit("CLOSEMESSAGEINFO", {})
}

</script>
