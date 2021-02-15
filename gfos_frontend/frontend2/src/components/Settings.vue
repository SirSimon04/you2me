

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
            <v-subheader>Angemeldet:</v-subheader>
            <v-list-item>
              <v-list-item-content>
                <v-card
                          class="mx-auto"
                          max-width="344"
                        >
                          <v-img
                            src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
                            height="200px"
                          ></v-img>
                      
                          <v-card-title>
                            {{benutzername}}
                          </v-card-title>
                      
                          <v-card-subtitle>
                            {{nachname}}, {{vorname}}<br>
                            {{email}}
                          </v-card-subtitle>
                      
                          <v-card-actions>
                            
                      
                            <v-spacer></v-spacer>
                      
                            <v-btn
                              icon
                              @click="show = !show"
                            >
                              <v-icon>{{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
                            </v-btn>
                          </v-card-actions>
                      
                          <v-expand-transition>
                            <div v-show="show">
                              <v-divider></v-divider>
                      
                              <v-card-text>
                                {{info}}
                              </v-card-text>
                            </div>
                          </v-expand-transition>
                        </v-card>
                
              </v-list-item-content>
            </v-list-item> <!--TODO: Die Einsetellungen sollten noch so angepasst werden, dass sie ein schönes Erscheinungsbild besitzen. Ähnlich Windows 10?-->
            
          </v-list>
          <v-divider></v-divider>
          <v-list
            five-line
            subheader
          >
            <v-subheader>Einstellungen:</v-subheader>
            <v-list-item>
              <v-list-item-action>
                <v-btn
                  class="mx-2"
                  fab
                  dark
                  color="blue">
                    <v-icon dark>
                      mdi-format-list-bulleted-square
                    </v-icon>
                </v-btn>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-title>Benachrichtigungseinstellungen</v-list-item-title>
                <v-list-item-subtitle>Benachrichtigungstöne, besondere Benachrichtigugnen, blockierte Kontakte, ...</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <v-list-item>
              <v-list-item-action>
                <v-btn
                  class="mx-2"
                  fab
                  dark
                  color="blue">
                    <v-icon dark>
                      mdi-format-list-bulleted-square
                    </v-icon>
                </v-btn>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-title>Toneinstellungen</v-list-item-title>
                <v-list-item-subtitle>Töne, Stummschaltungen, Mikrofon/Lautsprecher</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <v-list-item>
              <v-list-item-action>
                <v-btn
                  class="mx-2"
                  fab
                  dark
                  color="blue">
                    <v-icon dark>
                      mdi-format-list-bulleted-square
                    </v-icon>
                </v-btn>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-title>Erscheindungsbild</v-list-item-title>
                <v-list-item-subtitle>Modus, Helligkeit, Farben, ...</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <v-list-item>
              <v-list-item-action>
                <v-btn
                  class="mx-2"
                  fab
                  dark
                  color="blue">
                    <v-icon dark>
                      mdi-format-list-bulleted-square
                    </v-icon>
                </v-btn>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-title>Account</v-list-item-title>
                <v-list-item-subtitle>Benutzereinstellungen, Datenschutz, Kontaktlisten, ...</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <v-list-item>
              <v-list-item-action>
                <v-btn
                  class="mx-2"
                  fab
                  dark
                  color="blue">
                    <v-icon dark>
                      mdi-format-list-bulleted-square
                    </v-icon>
                </v-btn>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-title>Informationen</v-list-item-title>
                <v-list-item-subtitle>Speicherplatz, Hilfe, Entwickleroptionen, ...</v-list-item-subtitle>
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
                    show: false,
                    benutzername: "SirSimon",
                    nachname: "Engel",
                    vorname: "Simon",
                    email: "simi@engelnet.de",
                    info: "Sei wer du bist, wenns schlecht läuft: es gibt Friedrich ^^",
                   
                }
    
    
  

                }
             }
             </script>
 