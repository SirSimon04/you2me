<template>
  <v-container>
    <div v-if="darkmode === true">
      <v-card max-width="calc(50%)"  dark>
        <v-card-title style ="font-size: 20px; margin: 15px; position: relative; top: 13px;">Brightmode</v-card-title>
          <v-container>
            <v-row align ="end">
              <v-col cols="1" md="6">
                <v-btn style="height: 65px; width: 65px;" dark @click="darkmode= !darkmode">
                  <img src= "../assets/Sonnen_icon64.png" />
                </v-btn>
              </v-col>
              <v-spacer></v-spacer>
              <v-col cols="10" md="3">
                <v-card-text style="font-size: 12px;">
                  Aktivieren Sie den "Brightmode", um die Elemente der App im hellen Design anzuzeigen.
                </v-card-text>
              </v-col>
            </v-row>
          </v-container>
        </v-card>


      </div>
      <div v-else> <!--Hier alle hellen Elemente rein!-->
        <v-card style = "max-width: 400px">
        <v-card-title style ="font-size: 20px; margin: 15px; position: relative; top: 13px;">Darkmode</v-card-title>
          <v-container>
            <v-row align ="end">
              <v-col cols="1" md="6">
                <v-btn style="height: 65px; width: 65px;" @click="darkmode= !darkmode">
                  <img src= "../assets/Mond_icon64_heller.png" />
                </v-btn>
              </v-col>
              <v-spacer></v-spacer>
              <v-col cols="10" md="3">
                <v-card-text style="font-size: 12px;">
                  Aktivieren Sie den "Darkmode", um die Elemente der App im dunklen Design anzuzeigen.
                </v-card-text>
              </v-col>
            </v-row>
          </v-container>
        </v-card>
      </div>
      
    
  </v-container>
</template>
<script>
import { EventBus } from './EventBus.js';
  export default {
    
    name: 'Settings',
    
    data(){
      return {
        darkmode: true,
        schimpfwortfilter: true,
      }
    },
    
    
  }
</script>
