<template>
  <v-container>
    <div v-if="darkmode === true">
       <v-card class="mx-auto" max-width="500px"  dark>
        <v-card-title style ="font-size: calc(100%); margin: 15px; position: relative; top: 13px;">
          <div style="position: relative; left: 5px;">Einstellungsübersicht</div>
        </v-card-title>
          <v-card-text>Hier finden Sie eine Übersicht Ihrer Einstellungen. Bitte speichern Sie diese nach allen Änderungen.</v-card-text>
        <v-divider dark></v-divider>
        <div v-if="darkmode === true">
          <v-alert border="left" dense outlined type="success">Darkmode: <div style="float: right">Aktiviert</div></v-alert>
        </div>
        <div v-else>
          <v-alert border="left" dense outlined type="error">Darkmode: <div style="float: right">Deaktiviert</div></v-alert>
        </div>
        <div v-if="schimpfwortfilter === true">
          <v-alert border="left" dense outlined type="success">Schimpfwortfilter: <div style="float: right">Aktiviert</div></v-alert>
        </div>
        <div v-else>
          <v-alert border="left" dense outlined type="error">Schimpfwortfilter: <div style="float: right">Deaktiviert</div></v-alert>
        </div>
        <div v-if="benachrichtigungen === true">
          <v-alert border="left" dense outlined type="success">Benachrichtigungen: <div style="float: right">Aktiviert</div></v-alert>
        </div>
        <div v-else>
          <v-alert border="left" dense outlined type="error">Benachrichtigungen: <div style="float: right">Deaktiviert</div></v-alert>
        </div>
        
        

        <v-divider dark></v-divider>
          <v-card-text></v-card-text>
          <v-btn dark style="position: relative; bottom: 20px left: 10px;">Speichern</v-btn>
      </v-card>

      <v-card class="mx-auto" max-width="500px"  dark>
        <v-card-title style ="font-size: calc(100%); margin: 15px; position: relative; top: 13px;">
          <v-btn style="width: 65px; height: 65px;" @click=" darkmode = !darkmode">
            <img src= "../assets/Sonnen_icon64.png" style =" position: relative; float: left; margin: 5px;" >
          </v-btn>
          <div style="position: relative; left: 5px;">Brightmode</div>
        </v-card-title>
        <v-divider dark></v-divider>
          <v-card-text>Aktivieren Sie den "Brightmode", um die Elemente der App im hellen Design anzuzeigen.</v-card-text>
      </v-card>

      <v-card class="mx-auto" max-width="500px"  dark>
        <v-card-title style ="font-size: calc(100%); margin: 15px; position: relative; top: 13px;">
          <v-btn style="width: 65px; height: 65px;" @click=" schimpfwortfilter = !schimpfwortfilter">
            <img src= "../assets/Schimpfwortfilter_darkmode-true_icon64.png" style =" position: relative; float: left; margin: 5px;" >
          </v-btn>
          <div style="position: relative; left: 5px;">Schimpfwortfilter</div>
        </v-card-title>
        <v-divider dark></v-divider>
          <v-card-text>Vorsicht! Wenn Sie den "Schimpfwortfilter" deaktivieren, werden Ihnen anstößige Inhalte unzensiert angezeigt.</v-card-text>
      </v-card>
      
     
      </div>
      <div v-else> <!--Hier alle hellen Elemente rein!-->
        <v-card class="mx-auto" max-width="500px">
        <v-card-title style ="font-size: calc(100%); margin: 5px; position: relative; top: 13px;">
          <v-btn style="width: 65px; height: 65px;" @click=" darkmode = !darkmode">
            <img src= "../assets/Sonne-Mond_icon64.png" style =" position: relative; float: left; margin: 5px;" >
          </v-btn>
          <div style="position: relative; left: 5px;">Darkmode</div>
        </v-card-title>
        <v-divider></v-divider>
          <v-card-text>Aktivieren Sie den "Darkmode", um die Elemente der App im dunklen Design anzuzeigen.</v-card-text>
      </v-card>

      <v-card class="mx-auto" max-width="500px">
        <v-card-title style ="font-size: calc(100%); margin: 5px; position: relative; top: 13px;">
          <v-btn style="width: 65px; height: 65px;" @click=" darkmode = !darkmode">
            <img src= "../assets/Schimpfwortfilter_brightmode-true_icon64.png" style =" position: relative; float: left; margin: 5px;" >
          </v-btn>
          <div style="position: relative; left: 5px;">Schimpfwortfilter</div>
        </v-card-title>
        <v-divider></v-divider>
          <v-card-text>Vorsicht! Wenn Sie den Schimpfwortfilter deaktivieren, werden Ihnen anstößige Inhalte unzensiert angezeigt.</v-card-text>
      </v-card>
      </div>
      
    
  </v-container>
</template>
<style>
  .v-card--reveal {
  bottom: 0;
  opacity: 1 !important;
  position: absolute;
  width: 100%;
  background-color: #17212B;
  }
</style>
<script>
import { EventBus } from './EventBus.js';
  export default {
    
    name: 'Settings',
    
    data(){
      return {
        darkmode: true,
        schimpfwortfilter: false,
        benachrichtigungen: false,
        
      }
    },
    mounted(){
      console.log(this.darkmode),
      console.log(this.schimpfwortfilter),
      console.log(this.benachrichtigungen)

    }
    
  }
</script>
