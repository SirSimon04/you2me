<template>
    <v-container>
        <v-card
        height="500px"
        width="380px"
        class="mx-auto my-12"
        elevation="12"
        style=" position: absolute; left: calc(50% - 100px); top: calc(50% - 400px - 500px); z-index:20000"
        :dark="getDarkmode()"
        >

            <v-card-title
            v-if="getDarkmode()"
            style="color:white"
            >
                Erstelle eine neue Gruppe
            </v-card-title>
            <v-card-title
            v-else
            style="color:black"
            >
                Erstelle eine neue Gruppe
            </v-card-title>
            <v-card-text>

                <v-text-field
                v-model="gruppenname"
                label="Gruppenname eingeben"
                :dark="getDarkmode()"
                :counter="50"
                > 
                </v-text-field>

                <v-text-field
                v-model="gruppenbeschreibung"
                label="Gruppenbeschreibung eingeben"
                :counter="100"
                :dark="getDarkmode()"
                > 
                </v-text-field>

                <v-list
                rounded
                class="overflow-y-auto"
                max-height="230px"
                min-height="230px"
                :dark="getDarkmode()"
                >
                    <v-subheader
                    :dark="getDarkmode()"
                    >
                        Wähle Nutzer für deine Gruppe aus
                    </v-subheader>
                    <v-list-item-group
                    v-model="addmodel"
                    :dark="getDarkmode()"
                    color="blue"
                    multiple
                    value="addUsers"
                    >
                        <v-list-item
                        v-for="(item, i) in addingUsers"
                        :key="i"
                        v-bind="attrs"
                        v-on="on"
                        @click="addUser(item.benutzername)"
                        :dark="getDarkmode()"
                        >
                        <v-list-item-avatar>
                                <v-img
                                v-if="item.profilbild === undefined"
                                class="elevation-6"
                                alt=""
                                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAIJQAACCUBaAtNGQAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAYfSURBVFiFnZdbbJTHFcd/M3uz115fwK4NdkxsGkjUlBCocSniEoOT2E6bBoeChGRZlfoQNRc1L4mqNGrVi0KeUFMhNQ+pSy64jUBVE5pUtMYhmOCkBXoJmGKZ2IuveHe9vuzut99+M32wvV6vd9dLztP3nfOfc/4zc87MGUGW0th4uMAQzuZ7qz2t6yqKd4W1zg1GlNBaU5Rr07lShIeGp8593h/4fcQmT3f/+Y3pbPyKlQB7m1o3IOQLAnGofnulezSkUFqnxEoB5bk2Oi96Q5bmhF1y5Mz77Te+FIHtBw7k5obyf45Wzwkh7Q07qrg1ba44I4AKj52/dXvRWpsCjlohXu7qao9kTaDhsbZ7LKVPgbgfYE/tWsajmYOWFzrxzcQwLQVAqQs++nR4IcjFqBXb//Ff3xpJHmdLVuxrbH1QIc6CqAbwuO14Sj3EVOplByjOc/D83krq7y3CZZcM+g0MDTOBMNGYAqi0SXGwZv0Df7/Z96+xtCswN3O6gdIF3Y7NZYRsDrZU5VNR6GLKiHFtJIQ3YOCQgq9VuHl8UwlFbnvcj2/W5I0LY4SnwnRfHk2IoG8pRG3XX9rjyjiBPXvacqSbi8ADiaQONtzNt7eUU+pxLJm1qTR2IRBpsihsKo5/PMSpzi+STf8M583u/OTdd8MAckEr88QvkoMD3FfmXhYcwCHTBwfIdUjuX+NOZdqaM5P/YjwuzJeaVs+lQpux9Hu/kqg0eSOken5PU1t5nABCvgDCngo8GkhZPVnJ7UkjtUGLfCn0ywCysfFwgdYcSufkmnfqSxPIOFaL1h3f+b5HGsLZDKTcLIBLfZMYprrj4DFLcbl/MhMkL8fUTRJUfSZUJGrReWX8jglcuOZnOhRbCVYvQSzL/GT543nvHSWj0pq3zw6uDBR6kwRdvRJu2Beh41wWDuflVPcwX4zNrojTUCNBFGTj9O2zg5y/6lsR13Pdz+/O3MzGJUChrfqezT8hxZ2QSs7/dwKNZmOFB7tNLrEZpqLjIy+/ee8GVvY5a0nQWdeZ0pq3Ogf51R96l9l+2XGNNzsH7iQ4QNAO4iZQkg5RU1lC7YMbcTlsRCIRvIODbF63vGq3rC9GOpzcVVVFQWEB0zNhPrt8nf5bE2mjC+gX9c1tr6P5QaLB6ZD8sPURttZtQ+QUkON0UuTJB0DPjGL1fQg6aapCYlv/MMKzFoDJ6Rki0Sg6MsWnn/Rw7PgZYpa1ZIiG39pqNmzOA55cUJau8vDrV57lro2bEHYXADHLwp3jQgiBcOaDw43pH8C0FJbSgMBetR1ZXAOAUorg7FwVCLuLyur1NO3ZxIWefxMKL3Y2QvCqtGbd7wOzAHabjVd/9hTOwvJlyxU2Fs91uXoDHZfC7HvpNPteOk3HpTCy5L6U2AVxFZVx5KdPIWX8Cp1Vs+4PZFfXsRkNHQDPtD2Ms7Bs2WCAUIJTpRSTVk78f9LKQSmVEptM4pm2R+enzztdXcdmJIBdcgQwt9bVpRwIYFkKwzSJRKNMBIMMjS4ez0Oj40xMBglFDAzTxMpQCt+oqwWISuQrMF///f+74j+4/7tf/ebunRmP5WjUJGxE0Ro8Hg8hS1JZtY7Gvbv4SulqDNMkGjXJdGgLRw7eG73H32w/2g4JHVHTtq+/qMOBJdSV1uiEN0Die8BmdzI07mNo3Ifd4UyJ0Vove0PocEC17K778cJ/nMCulpaRgd7PDxBb3L8Jv59oNPVbYNwXiH+PTQRSYqJRkwm/f1ERM+jrvfK9bc3N8aZ0yXm6veHxU72Xun+ElaaTSZDbCY7HJvwZkPNiGVz9x7lndzU8eTJRLZNxDzUdONp3+WKjCgWsZFuijCfM+rYvMwEdmVQ3r/Y8sfexQ68l25YRANj56P4Ph/5zvRJjOijSpFQ2WyDQaGNq6uq5nrXfeuiJP6XCpGxEAeb3qeizM6dbitaUvO4pq16lxdylacZiBIKLj9/g9AxG1MTlnGvfhbaYHukPDHuHnt7d3PJOuhgZCSxIbUPzSeDkBydO3L16TeFrxatX7ZiOOQoKPG5bKDSXK263C9/IgFXg1MGAz3fBOzT5dMvhwwMr+Qb4P8tVlgtK2NESAAAAAElFTkSuQmCC"
                                ></v-img>
                                <v-img
                                v-else
                                :src="item.profilbild.base64"
                                >
                                </v-img>
                        </v-list-item-avatar>
                        <v-list-item-title
                        v-if="getDarkmode()"
                        style="color:white"
                        >
                            {{item.benutzername}}
                        </v-list-item-title>
                        <v-list-item-title
                        v-else
                        style="color:black"
                        >
                            {{item.benutzername}}
                        </v-list-item-title>
                        </v-list-item>
                    </v-list-item-group>
                </v-list>

            </v-card-text>
            <v-card-actions>
                <v-row
                align="space-around"
                >
                    <v-col>
                        <v-btn
                        dark
                        color="red darken-1"
                        @click="closeNewChat();"
                        >
                            Abbrechen
                        </v-btn>
                    </v-col>
                    <v-spacer>
                    </v-spacer>
                    <v-col>
                        <v-btn
                        color="green darken-1"
                        dark
                        @click="createNewGroup()"
                        :disabled="addUsers.length === 0  || !gruppenname"
                        >
                            Absenden
                        </v-btn>
                    </v-col>
                </v-row>
            </v-card-actions>
        </v-card>
    </v-container>
</template>

<script>
import { EventBus } from './EventBus.js';
import { ImageContainer } from './ImageContainer.js';
import axios from "axios";

export default {
    name: 'NewChat',

    data: () => ({
        addingUsers: {},
        addUsers: [],
        gruppenname: '',
    }),

    mounted() {
        EventBus.$on('APP_INTERVAL', (payload) => {
        }); // EventBus.$on('APP_INTERVAL')

        EventBus.$on('NEW_GROUP', (payload) => {
            this.loadData();
        }); // EventBus.$on('APP_INTERVAL')

    },

    methods: {

        getDarkmode(){
            return window.DARK_MODE
        },

        loadData() {
            axios.get(window.IP_ADDRESS + "/GFOS/daten/nutzer/getAddable/" + "100" + "/" + window.CURRENT_USER_ID + "/" + window.CURRENT_TOKEN)
            .then((resp)=>{
                this.addingUsers = resp.data
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },

        closeNewChat() {
            EventBus.$emit("CLOSE_NEW_GROUP", {});
            this.gruppenbeschreibung = ""
            this.gruppenname = ""
        },

        addUser(name){
            if(this.addUsers.includes(name)){
                this.addUsers.pop(name)
            }
            else{
                this.addUsers.push(name)
            }
        },

        createNewGroup() {
            const now = Date.now();
            const payload = {
                "eigeneId": window.CURRENT_USER_ID,
                "erstelldatum": now,
                "name": this.gruppenname,
                "benutzernamen": this.addUsers
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/chat/createAsGroup/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                EventBus.$emit('INFOMESSAGE', {'message': 'Du hast die Gruppe ' + this.gruppenname + ' erfolgreich erstellt.', 'icon': 'SUCCESS'})
                this.closeNewChat()
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },
    }
};

</script>