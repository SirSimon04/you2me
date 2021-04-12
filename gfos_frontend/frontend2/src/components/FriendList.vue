<template>
    <v-container>
        <div class="mx-auto" max-width="1000px" style="margin:100px">
            <v-row justify="center">
                <v-card
                max-width="300"
                tile
                rounded="true"
            >
                <v-list 
                    rounded
                    color="#202b36"
                    max-height="400px"
                    class="overflow-y-auto"
                    width="500px"
                    >
                <v-list-item-group
                    v-model="model"
                    dark
                    color="blue"
                >
                    <v-list-item
                    v-for="(item, i) in allFriends.friendList"
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
                        <v-list-item-title v-text="item.benutzername"></v-list-item-title>
                        <v-list-item-subtitle v-if="item.isonline === false">Zuletzt online: {{  times[i] }}</v-list-item-subtitle>
                        </v-list-item-content>

                        <v-spacer>
                        </v-spacer>
                            
                            <v-list-item-icon v-if="item.isonline">
                                <v-img
                                height="23.5px"
                                width="5px"
                                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAJcwAACXMB+Yg9ogAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAPQSURBVFiFvZdbTFxVFIa/vc/AzMCAVDHQFAxFIChWVCiRaFSINQ2gNYBjTby2Wp8ankrRWpyoscADISY2RkyMJqbJQEmbKFpjbEIVsC9gtbZgA6gtpTatVotcZuYsH2CQ2zCn49D/6Zy91l7ff5Kz9kVhUVmdnkQ1ZS8X0aUKyQcygKTZ8J/AiEC/hq+VoT4b2LL7byt1VbiE3I63cwLo3cBWIM6i33+UcMAvunGouvbniAykeZudjhjfm0qkBrBZBC+WD1SL78pE/cgLnknLBjLbm7INLR0gd0QIXqxemwQqT1XtOR/WQFb7vruVVkeAm6MED+qsNo3ygepdJ0IamPly89tVgM+Z8Pn1xhF37VhwQAcfMj70OAxttq0iHCAt1mZ+muZtdi4xYEtyvgXkryIcAIGCONt0XfBdwVyrnSTyvz2k1Ax0sa76/Dp7xF07pgFm+zzq8OezCjmyacdyIVeMYdYDqKxOT6KadJzH+iJjSWXrcmne+Bh7+j7n4C8/LE0QxrVNrdVqyl4ebXhRcjpNhRW8c+qb5eEAinjxm2VaRJdGE56dmMy791Zy6Ncf2T/QvWKuoEv17MYSFaU4E2gtfoK+S+d4vf/L8BOU3KmB9dGAu2x2WouruTQ1Ts3xwwTEtDItUwOJoaI7c+8jLyklbJVYbfBecRUOw8aL3W1MBHxWfd+gV4re4lrDgQeeZkt6XsgcrRRNhRVkJtzI9u42/piesAoHwLjpyYdrCNEFX40OEhBhb/4mUp0ujv0+jCkLl5W6DaU8mn47L3W3MfjXxWuCA5c1MBwqKsD7g7283NPO5nW5fHz/UyTb4+fi27KLeCazgJ3fHaL/8ui1wgGGtEB/uKyuC0NUHf2IxBg7HSXPsWHNWsrSbqM27yH29n9B14WhSOAA36ucjoatAgesZMfZYmksKOPBlFvRSrH/dHfYXl9JgrhVntfjmjYcYyjiw0+Z2Vy2ZReREGOn5adjEcMRxmMDk6kKIOdgwwei2B55tUj4tJ6prNuhAfyiGwHLzRsFTWuRBpg9kMwcnVXL9aIraB6semVozgCA78pEPdB7HfA9piPOM8/Mf8rwNqXG2OQ4SPoq0UeV31806H7tXHBgwVI84q4d06auAM5Gn61+MzSb58OXGAAYqN51QvzmPUBXFOE9Pr8qOv143ZLTybKb0Rn3qxfFEfeIgjcQxv8HeVpE9onDWTL/LrDAWrgKGd6m1NkD5LNWF6sZ0+oThdkY/NtDKayBoPK8HpfPsJeLUiUId6FYz/zruTCMUn2i5KjdN9l50u25aqXuvwkzRBFUXRZyAAAAAElFTkSuQmCC"
                                >
                                </v-img>
                            </v-list-item-icon>

                            

                        <v-list-item-action>
                          <v-btn icon>
                              <v-icon color="grey lighten-1">mdi-message</v-icon>
                              </v-btn>
                         </v-list-item-action>

                    </v-list-item>
                </v-list-item-group>
                </v-list>
            </v-card>
            </v-row>
        </div>
    </v-container>
</template>

<script>

import { ImageContainer } from './ImageContainer.js';

  export default {
    

    mounted() {
        var imageContainer = new ImageContainer();
        console.log("mounted");
        console.log(this.times)
        console.log(this.getTime());
    },
    methods() {
        function getFriends(){
            console.log("1");
            fetch(window.IP_ADDRESS + "/GFOS/daten/nutzer/getFriendList/2/1")
            .then(response => {
                response.clone();
                response.json.then(data => {
                    console.log("Hier");
                    console.log(data);
                })
                
            })
        }

        function writeTimes(){
            var times = [];
            var user;
            for(user in this.allFriends){
                times.push(user.lastonline)
            }
        }

        function getTime(i){
            /*
            console.log("hey");
            var date = new Date(this.allFriends.friendList[i]["lastonline"]);
            var now = new Date();
            var dateFormatted = fillStringZero(date.getHours(), 2) + ':' + fillStringZero(date.getMinutes(), 2);
            return dateFormatted;
            */
           //return this.allFriends.friendList[i]["lastonline"];
           return "1618265995952";
        }

        function fillStringZero(string, length) {
            string = string.toString();
            var zeroString = '';
            for (var i=length-string.length; i>0; i--) {
                zeroString += '0';
            }
            return zeroString + string;
        }
 
    
    },

    data: () => ({
        model: 1,
      times: [
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
          1618265995952,
      ],
      allFriends:  {
        "friendList": [
            {
                "id": 4,
                "benutzername": "NoSkiller",
                "email": "lb@mail.de",
                "lastonline": 1618265995952,
                "isonline": true,
                "mitgliedseit": 1618151073,
                "profilbild": {
                    "id": 2,
                    "base64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAA7AAAAOwBeShxvQAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAJBSURBVFiF7ZbNTxNBGIefd9payrbQ0gIlYIgYG2Pxg0BQgVi9eOHAgcSLEjHK/+Cf4M27N2M8GmICMTFqgvHmxZAQD3ARRKqJcqAR2O3ueLF1q2vTAlsu/E4z707meTJfWcGV2cXNaRF5AHTiT/Ii+v5Ef+JxqSClxvxSIW05xVUg5BO8FEsIHp84G/0KECxXi+YplAoB7O5afN7Ik2pL0BIzWF3fwLKKeyaKCD1dacLhEEBIO2YGqBQQEdG/2yuf1ph/ucBA/2nGLg7wbO41xeLeBQDGr+fIZvrKrDK31Hi+uHlFiyzsi1JjROvcxLnEWwDVCGC1BL2Kix+XmZ17xdCFLNdGh3n46AnFOs/A1dFhciODexPIZvpITk3SFm8l0hRm5tZk3YewPZmoaZynQCAQoDvdUe53pNrqgtcTT4G19TzvPyxxorebbOYkL968w7adqhP19nQxeP7MwQhEmppIJlqJGQZKKZLxOLZjV52oJWZU/W6EQAlsmTUIpJJxciND5f7YpYGqk9eSVLMQVLBl6or6oV/DI4EjgSOBhglo7V33fIj8yLefmj+/IYcgoARPgYZtQXuzkDb+NWjYCmyZGnWYW/Bj27vuq4BpORS2TRzXFVCwE40EvvguYNsO+e+FCnhAiY4Z4Xs3BjtWXEL+ZMe0veBT05c7n7rH+SbgfneUiI5Gwrf/hoNrC7TW3i/FPqNEtBENT93xgINrBULBY8uA5Qf87n/gFQLj2WheRM8A+YOBU4gagZvV4AC/ANekoKtCcBgNAAAAAElFTkSuQmCC"
                }
            },
            {
                "id": 4,
                "benutzername": "NoSkiller",
                "email": "lb@mail.de",
                "lastonline": 1618265995952,
                "isonline": false,
                "mitgliedseit": 1618151073,
                "profilbild": {
                    "id": 2,
                    "base64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAA7AAAAOwBeShxvQAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAJBSURBVFiF7ZbNTxNBGIefd9payrbQ0gIlYIgYG2Pxg0BQgVi9eOHAgcSLEjHK/+Cf4M27N2M8GmICMTFqgvHmxZAQD3ARRKqJcqAR2O3ueLF1q2vTAlsu/E4z707meTJfWcGV2cXNaRF5AHTiT/Ii+v5Ef+JxqSClxvxSIW05xVUg5BO8FEsIHp84G/0KECxXi+YplAoB7O5afN7Ik2pL0BIzWF3fwLKKeyaKCD1dacLhEEBIO2YGqBQQEdG/2yuf1ph/ucBA/2nGLg7wbO41xeLeBQDGr+fIZvrKrDK31Hi+uHlFiyzsi1JjROvcxLnEWwDVCGC1BL2Kix+XmZ17xdCFLNdGh3n46AnFOs/A1dFhciODexPIZvpITk3SFm8l0hRm5tZk3YewPZmoaZynQCAQoDvdUe53pNrqgtcTT4G19TzvPyxxorebbOYkL968w7adqhP19nQxeP7MwQhEmppIJlqJGQZKKZLxOLZjV52oJWZU/W6EQAlsmTUIpJJxciND5f7YpYGqk9eSVLMQVLBl6or6oV/DI4EjgSOBhglo7V33fIj8yLefmj+/IYcgoARPgYZtQXuzkDb+NWjYCmyZGnWYW/Bj27vuq4BpORS2TRzXFVCwE40EvvguYNsO+e+FCnhAiY4Z4Xs3BjtWXEL+ZMe0veBT05c7n7rH+SbgfneUiI5Gwrf/hoNrC7TW3i/FPqNEtBENT93xgINrBULBY8uA5Qf87n/gFQLj2WheRM8A+YOBU4gagZvV4AC/ANekoKtCcBgNAAAAAElFTkSuQmCC"
                }
            },
            {
                "id": 3,
                "benutzername": "Jet3141",
                "email": "jet@mail.de",
                "lastonline": 1618265995952,
                "isonline": false,
                "mitgliedseit": 1618151061
            },
            {
                "id": 4,
                "benutzername": "NoSkiller",
                "email": "lb@mail.de",
                "lastonline": 1618265995952,
                "isonline": true,
                "mitgliedseit": 1618151073,
                "profilbild": {
                    "id": 2,
                    "base64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAA7AAAAOwBeShxvQAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAJBSURBVFiF7ZbNTxNBGIefd9payrbQ0gIlYIgYG2Pxg0BQgVi9eOHAgcSLEjHK/+Cf4M27N2M8GmICMTFqgvHmxZAQD3ARRKqJcqAR2O3ueLF1q2vTAlsu/E4z707meTJfWcGV2cXNaRF5AHTiT/Ii+v5Ef+JxqSClxvxSIW05xVUg5BO8FEsIHp84G/0KECxXi+YplAoB7O5afN7Ik2pL0BIzWF3fwLKKeyaKCD1dacLhEEBIO2YGqBQQEdG/2yuf1ph/ucBA/2nGLg7wbO41xeLeBQDGr+fIZvrKrDK31Hi+uHlFiyzsi1JjROvcxLnEWwDVCGC1BL2Kix+XmZ17xdCFLNdGh3n46AnFOs/A1dFhciODexPIZvpITk3SFm8l0hRm5tZk3YewPZmoaZynQCAQoDvdUe53pNrqgtcTT4G19TzvPyxxorebbOYkL968w7adqhP19nQxeP7MwQhEmppIJlqJGQZKKZLxOLZjV52oJWZU/W6EQAlsmTUIpJJxciND5f7YpYGqk9eSVLMQVLBl6or6oV/DI4EjgSOBhglo7V33fIj8yLefmj+/IYcgoARPgYZtQXuzkDb+NWjYCmyZGnWYW/Bj27vuq4BpORS2TRzXFVCwE40EvvguYNsO+e+FCnhAiY4Z4Xs3BjtWXEL+ZMe0veBT05c7n7rH+SbgfneUiI5Gwrf/hoNrC7TW3i/FPqNEtBENT93xgINrBULBY8uA5Qf87n/gFQLj2WheRM8A+YOBU4gagZvV4AC/ANekoKtCcBgNAAAAAElFTkSuQmCC"
                }
            },
            {
                "id": 3,
                "benutzername": "Jet3141",
                "email": "jet@mail.de",
                "lastonline": 1618265995952,
                "isonline": true,
                "mitgliedseit": 1618151061
            },
            {
                "id": 4,
                "benutzername": "NoSkiller",
                "email": "lb@mail.de",
                "lastonline": 1618265995952,
                "isonline": false,
                "mitgliedseit": 1618151073,
                "profilbild": {
                    "id": 2,
                    "base64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAA7AAAAOwBeShxvQAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAJBSURBVFiF7ZbNTxNBGIefd9payrbQ0gIlYIgYG2Pxg0BQgVi9eOHAgcSLEjHK/+Cf4M27N2M8GmICMTFqgvHmxZAQD3ARRKqJcqAR2O3ueLF1q2vTAlsu/E4z707meTJfWcGV2cXNaRF5AHTiT/Ii+v5Ef+JxqSClxvxSIW05xVUg5BO8FEsIHp84G/0KECxXi+YplAoB7O5afN7Ik2pL0BIzWF3fwLKKeyaKCD1dacLhEEBIO2YGqBQQEdG/2yuf1ph/ucBA/2nGLg7wbO41xeLeBQDGr+fIZvrKrDK31Hi+uHlFiyzsi1JjROvcxLnEWwDVCGC1BL2Kix+XmZ17xdCFLNdGh3n46AnFOs/A1dFhciODexPIZvpITk3SFm8l0hRm5tZk3YewPZmoaZynQCAQoDvdUe53pNrqgtcTT4G19TzvPyxxorebbOYkL968w7adqhP19nQxeP7MwQhEmppIJlqJGQZKKZLxOLZjV52oJWZU/W6EQAlsmTUIpJJxciND5f7YpYGqk9eSVLMQVLBl6or6oV/DI4EjgSOBhglo7V33fIj8yLefmj+/IYcgoARPgYZtQXuzkDb+NWjYCmyZGnWYW/Bj27vuq4BpORS2TRzXFVCwE40EvvguYNsO+e+FCnhAiY4Z4Xs3BjtWXEL+ZMe0veBT05c7n7rH+SbgfneUiI5Gwrf/hoNrC7TW3i/FPqNEtBENT93xgINrBULBY8uA5Qf87n/gFQLj2WheRM8A+YOBU4gagZvV4AC/ANekoKtCcBgNAAAAAElFTkSuQmCC"
                }
            },
            {
                "id": 3,
                "benutzername": "Jet3141",
                "email": "jet@mail.de",
                "lastonline": 1618265995952,
                "isonline": true,
                "mitgliedseit": 1618151061
            },
            {
                "id": 4,
                "benutzername": "NoSkiller",
                "email": "lb@mail.de",
                "lastonline": 1618265995952,
                "isonline": false,
                "mitgliedseit": 1618151073,
                "profilbild": {
                    "id": 2,
                    "base64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAA7AAAAOwBeShxvQAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAJBSURBVFiF7ZbNTxNBGIefd9payrbQ0gIlYIgYG2Pxg0BQgVi9eOHAgcSLEjHK/+Cf4M27N2M8GmICMTFqgvHmxZAQD3ARRKqJcqAR2O3ueLF1q2vTAlsu/E4z707meTJfWcGV2cXNaRF5AHTiT/Ii+v5Ef+JxqSClxvxSIW05xVUg5BO8FEsIHp84G/0KECxXi+YplAoB7O5afN7Ik2pL0BIzWF3fwLKKeyaKCD1dacLhEEBIO2YGqBQQEdG/2yuf1ph/ucBA/2nGLg7wbO41xeLeBQDGr+fIZvrKrDK31Hi+uHlFiyzsi1JjROvcxLnEWwDVCGC1BL2Kix+XmZ17xdCFLNdGh3n46AnFOs/A1dFhciODexPIZvpITk3SFm8l0hRm5tZk3YewPZmoaZynQCAQoDvdUe53pNrqgtcTT4G19TzvPyxxorebbOYkL968w7adqhP19nQxeP7MwQhEmppIJlqJGQZKKZLxOLZjV52oJWZU/W6EQAlsmTUIpJJxciND5f7YpYGqk9eSVLMQVLBl6or6oV/DI4EjgSOBhglo7V33fIj8yLefmj+/IYcgoARPgYZtQXuzkDb+NWjYCmyZGnWYW/Bj27vuq4BpORS2TRzXFVCwE40EvvguYNsO+e+FCnhAiY4Z4Xs3BjtWXEL+ZMe0veBT05c7n7rH+SbgfneUiI5Gwrf/hoNrC7TW3i/FPqNEtBENT93xgINrBULBY8uA5Qf87n/gFQLj2WheRM8A+YOBU4gagZvV4AC/ANekoKtCcBgNAAAAAElFTkSuQmCC"
                }
            },
            {
                "id": 4,
                "benutzername": "NoSkiller",
                "email": "lb@mail.de",
                "lastonline": 1618265995952,
                "isonline": false,
                "mitgliedseit": 1618151073,
                "profilbild": {
                    "id": 2,
                    "base64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAA7AAAAOwBeShxvQAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAJBSURBVFiF7ZbNTxNBGIefd9payrbQ0gIlYIgYG2Pxg0BQgVi9eOHAgcSLEjHK/+Cf4M27N2M8GmICMTFqgvHmxZAQD3ARRKqJcqAR2O3ueLF1q2vTAlsu/E4z707meTJfWcGV2cXNaRF5AHTiT/Ii+v5Ef+JxqSClxvxSIW05xVUg5BO8FEsIHp84G/0KECxXi+YplAoB7O5afN7Ik2pL0BIzWF3fwLKKeyaKCD1dacLhEEBIO2YGqBQQEdG/2yuf1ph/ucBA/2nGLg7wbO41xeLeBQDGr+fIZvrKrDK31Hi+uHlFiyzsi1JjROvcxLnEWwDVCGC1BL2Kix+XmZ17xdCFLNdGh3n46AnFOs/A1dFhciODexPIZvpITk3SFm8l0hRm5tZk3YewPZmoaZynQCAQoDvdUe53pNrqgtcTT4G19TzvPyxxorebbOYkL968w7adqhP19nQxeP7MwQhEmppIJlqJGQZKKZLxOLZjV52oJWZU/W6EQAlsmTUIpJJxciND5f7YpYGqk9eSVLMQVLBl6or6oV/DI4EjgSOBhglo7V33fIj8yLefmj+/IYcgoARPgYZtQXuzkDb+NWjYCmyZGnWYW/Bj27vuq4BpORS2TRzXFVCwE40EvvguYNsO+e+FCnhAiY4Z4Xs3BjtWXEL+ZMe0veBT05c7n7rH+SbgfneUiI5Gwrf/hoNrC7TW3i/FPqNEtBENT93xgINrBULBY8uA5Qf87n/gFQLj2WheRM8A+YOBU4gagZvV4AC/ANekoKtCcBgNAAAAAElFTkSuQmCC"
                }
            },
            {
                "id": 3,
                "benutzername": "Jet3141",
                "email": "jet@mail.de",
                "lastonline": 1618265995952,
                "isonline": false,
                "mitgliedseit": 1618151061
            },
            {
                "id": 4,
                "benutzername": "NoSkiller",
                "email": "lb@mail.de",
                "lastonline": 1618265995952,
                "isonline": false,
                "mitgliedseit": 1618151073,
                "profilbild": {
                    "id": 2,
                    "base64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAA7AAAAOwBeShxvQAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAJBSURBVFiF7ZbNTxNBGIefd9payrbQ0gIlYIgYG2Pxg0BQgVi9eOHAgcSLEjHK/+Cf4M27N2M8GmICMTFqgvHmxZAQD3ARRKqJcqAR2O3ueLF1q2vTAlsu/E4z707meTJfWcGV2cXNaRF5AHTiT/Ii+v5Ef+JxqSClxvxSIW05xVUg5BO8FEsIHp84G/0KECxXi+YplAoB7O5afN7Ik2pL0BIzWF3fwLKKeyaKCD1dacLhEEBIO2YGqBQQEdG/2yuf1ph/ucBA/2nGLg7wbO41xeLeBQDGr+fIZvrKrDK31Hi+uHlFiyzsi1JjROvcxLnEWwDVCGC1BL2Kix+XmZ17xdCFLNdGh3n46AnFOs/A1dFhciODexPIZvpITk3SFm8l0hRm5tZk3YewPZmoaZynQCAQoDvdUe53pNrqgtcTT4G19TzvPyxxorebbOYkL968w7adqhP19nQxeP7MwQhEmppIJlqJGQZKKZLxOLZjV52oJWZU/W6EQAlsmTUIpJJxciND5f7YpYGqk9eSVLMQVLBl6or6oV/DI4EjgSOBhglo7V33fIj8yLefmj+/IYcgoARPgYZtQXuzkDb+NWjYCmyZGnWYW/Bj27vuq4BpORS2TRzXFVCwE40EvvguYNsO+e+FCnhAiY4Z4Xs3BjtWXEL+ZMe0veBT05c7n7rH+SbgfneUiI5Gwrf/hoNrC7TW3i/FPqNEtBENT93xgINrBULBY8uA5Qf87n/gFQLj2WheRM8A+YOBU4gagZvV4AC/ANekoKtCcBgNAAAAAElFTkSuQmCC"
                }
            },
            {
                "id": 3,
                "benutzername": "Jet3141",
                "email": "jet@mail.de",
                "lastonline": 1618265995952,
                "isonline": false,
                "mitgliedseit": 1618151061
            },
            {
                "id": 4,
                "benutzername": "NoSkiller",
                "email": "lb@mail.de",
                "lastonline": 1618265995952,
                "isonline": false,
                "mitgliedseit": 1618151073,
                "profilbild": {
                    "id": 2,
                    "base64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAA7AAAAOwBeShxvQAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAJBSURBVFiF7ZbNTxNBGIefd9payrbQ0gIlYIgYG2Pxg0BQgVi9eOHAgcSLEjHK/+Cf4M27N2M8GmICMTFqgvHmxZAQD3ARRKqJcqAR2O3ueLF1q2vTAlsu/E4z707meTJfWcGV2cXNaRF5AHTiT/Ii+v5Ef+JxqSClxvxSIW05xVUg5BO8FEsIHp84G/0KECxXi+YplAoB7O5afN7Ik2pL0BIzWF3fwLKKeyaKCD1dacLhEEBIO2YGqBQQEdG/2yuf1ph/ucBA/2nGLg7wbO41xeLeBQDGr+fIZvrKrDK31Hi+uHlFiyzsi1JjROvcxLnEWwDVCGC1BL2Kix+XmZ17xdCFLNdGh3n46AnFOs/A1dFhciODexPIZvpITk3SFm8l0hRm5tZk3YewPZmoaZynQCAQoDvdUe53pNrqgtcTT4G19TzvPyxxorebbOYkL968w7adqhP19nQxeP7MwQhEmppIJlqJGQZKKZLxOLZjV52oJWZU/W6EQAlsmTUIpJJxciND5f7YpYGqk9eSVLMQVLBl6or6oV/DI4EjgSOBhglo7V33fIj8yLefmj+/IYcgoARPgYZtQXuzkDb+NWjYCmyZGnWYW/Bj27vuq4BpORS2TRzXFVCwE40EvvguYNsO+e+FCnhAiY4Z4Xs3BjtWXEL+ZMe0veBT05c7n7rH+SbgfneUiI5Gwrf/hoNrC7TW3i/FPqNEtBENT93xgINrBULBY8uA5Qf87n/gFQLj2WheRM8A+YOBU4gagZvV4AC/ANekoKtCcBgNAAAAAElFTkSuQmCC"
                }
            },
            {
                "id": 3,
                "benutzername": "Jet3141",
                "email": "jet@mail.de",
                "lastonline": 1618265995952,
                "isonline": false,
                "mitgliedseit": 1618151061
            },
            
        ],
        "friendRequests": [],
        "pendingRequests": [],
        "onlineFriends": [],
        "blockedUser": []
        }
    }),

  }
</script>