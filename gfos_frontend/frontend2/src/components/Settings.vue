

<template>
    <v-container>
        <v-card>

          <v-toolbar
            dark
            color="primary"
          >
            <v-btn
              icon
              dark
              @click="dialog = false"
            >
              <v-icon>mdi-close</v-icon>
            </v-btn>
            <v-toolbar-title>Einstellungen</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-toolbar-items>
              <v-btn
                dark
                text
                @click="dialog = false"
              >
                Speichern
              </v-btn>
            </v-toolbar-items>
          </v-toolbar>
          <v-list
            three-line
            subheader
          >
            <v-subheader>Benutzereinstellungen</v-subheader>
            <v-list-item>
              <v-list-item-content>
              
                <v-card
                    elevation="2"
                    
                    max-width= "400px"
                    height:= "50px"
                    outlined
                ><v-card-text>
                    <v-list-item-title class="display-5 text--primary">
                    Erscheinungsbild
                    </v-list-item-title>
                    <v-list-item-subtitle><v-btn><p>Ändern Sie das Design der Anwendung.</p></v-btn></v-list-item-subtitle>
                    </v-card-text>
                </v-card>
              
                
              </v-list-item-content>
            </v-list-item> <!--TODO: Die Einsetellungen sollten noch so angepasst werden, dass sie ein schönes Erscheinungsbild besitzen. Ähnlich Windows 10?-->
            <v-list-item>
              <v-list-item-content>
                <v-list-item-title>Account</v-list-item-title>
                <v-list-item-subtitle>Require password for purchase or use password to restrict purchase</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-list
            three-line
            subheader
          >
            <v-subheader>General</v-subheader>
            <v-list-item>
              <v-list-item-action>
                <v-checkbox v-model="notifications"></v-checkbox>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-title>Notifications</v-list-item-title>
                <v-list-item-subtitle>Notify me about updates to apps or games that I downloaded</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <v-list-item>
              <v-list-item-action>
                <v-checkbox v-model="sound"></v-checkbox>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-title>Sound</v-list-item-title>
                <v-list-item-subtitle>Auto-update apps at any time. Data charges may apply</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <v-list-item>
              <v-list-item-action>
                <v-checkbox v-model="widgets"></v-checkbox>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-title>Auto-add widgets</v-list-item-title>
                <v-list-item-subtitle>Automatically add home screen widgets</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-card>
    </v-container>
</template>
        <script>
            export default {
                name: 'Settings',
    
                data(){
                return {
                    dialog: false,
                   
                }
    
    
  

                }
             }
             </script>
 