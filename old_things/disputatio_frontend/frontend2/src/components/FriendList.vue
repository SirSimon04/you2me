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
                        >
                            <v-tab
                            href="#freunde"
                            >
                                Freunde
                            </v-tab>
                            <v-tab-item
                            value="freunde"
                            >
                                <v-card
                                max-width="600"
                                tile
                                rounded="true"
                                v-if="allFriends.friendList.length !== 0"
                                >
                                    <v-list 
                                        rounded
                                        :dark="getDarkmode()"
                                        min-height="400px"
                                        max-height="400px"
                                        min-width="628"
                                        class="overflow-y-auto"
                                        width="536px"
                                        >
                                        <v-list-item-group
                                        v-model="model"
                                        dark
                                        color="blue"
                                        >
                                            <v-list-item
                                            v-for="(item, i) in allFriends.friendList"
                                            :key="i"
                                            @contextmenu.stop="contextMenu($event, item.nutzer.benutzername);"
                                            >

                                                    <v-list-item-avatar>
                                                        <div v-if="item.nutzer.profilbild === undefined">
                                                            <v-img 
                                                            src='data:image/png;base64,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'
                                                            ></v-img>
                                                        </div>
                                                        <div v-else>
                                                            <v-img
                                                            :src="item.nutzer.profilbild.base64"
                                                            ></v-img>
                                                        </div>
                                                    </v-list-item-avatar>

                                                <v-list-item-content>
                                                    
                                                <v-list-item-title v-text="item.nutzer.benutzername" v-if="getDarkmode()"></v-list-item-title>
                                                <v-list-item-title v-text="item.nutzer.benutzername" v-else style="color:black"></v-list-item-title>
                                                <v-list-item-subtitle
                                                v-if="item.nutzer.isonline === false && getDarkmode()">Zuletzt online: {{  item.nutzer.lastOnlineString }}
                                                </v-list-item-subtitle>
                                                <v-list-item-subtitle
                                                v-if="item.nutzer.isonline === false && !getDarkmode()" style="color:black">Zuletzt online: {{  item.nutzer.lastOnlineString }}
                                                </v-list-item-subtitle>
                                                </v-list-item-content>
                                                <v-list-item-icon v-if="item.nutzer.isonline">
                                                    <v-tooltip bottom>
                                                    <template v-slot:activator="{ on, attrs }">
                                                        <v-img
                                                        v-bind="attrs"
                                                        v-on="on"
                                                        height="23.5px"
                                                        width="5px"
                                                        src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAJcwAACXMB+Yg9ogAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAPQSURBVFiFvZdbTFxVFIa/vc/AzMCAVDHQFAxFIChWVCiRaFSINQ2gNYBjTby2Wp8ankrRWpyoscADISY2RkyMJqbJQEmbKFpjbEIVsC9gtbZgA6gtpTatVotcZuYsH2CQ2zCn49D/6Zy91l7ff5Kz9kVhUVmdnkQ1ZS8X0aUKyQcygKTZ8J/AiEC/hq+VoT4b2LL7byt1VbiE3I63cwLo3cBWIM6i33+UcMAvunGouvbniAykeZudjhjfm0qkBrBZBC+WD1SL78pE/cgLnknLBjLbm7INLR0gd0QIXqxemwQqT1XtOR/WQFb7vruVVkeAm6MED+qsNo3ygepdJ0IamPly89tVgM+Z8Pn1xhF37VhwQAcfMj70OAxttq0iHCAt1mZ+muZtdi4xYEtyvgXkryIcAIGCONt0XfBdwVyrnSTyvz2k1Ax0sa76/Dp7xF07pgFm+zzq8OezCjmyacdyIVeMYdYDqKxOT6KadJzH+iJjSWXrcmne+Bh7+j7n4C8/LE0QxrVNrdVqyl4ebXhRcjpNhRW8c+qb5eEAinjxm2VaRJdGE56dmMy791Zy6Ncf2T/QvWKuoEv17MYSFaU4E2gtfoK+S+d4vf/L8BOU3KmB9dGAu2x2WouruTQ1Ts3xwwTEtDItUwOJoaI7c+8jLyklbJVYbfBecRUOw8aL3W1MBHxWfd+gV4re4lrDgQeeZkt6XsgcrRRNhRVkJtzI9u42/piesAoHwLjpyYdrCNEFX40OEhBhb/4mUp0ujv0+jCkLl5W6DaU8mn47L3W3MfjXxWuCA5c1MBwqKsD7g7283NPO5nW5fHz/UyTb4+fi27KLeCazgJ3fHaL/8ui1wgGGtEB/uKyuC0NUHf2IxBg7HSXPsWHNWsrSbqM27yH29n9B14WhSOAA36ucjoatAgesZMfZYmksKOPBlFvRSrH/dHfYXl9JgrhVntfjmjYcYyjiw0+Z2Vy2ZReREGOn5adjEcMRxmMDk6kKIOdgwwei2B55tUj4tJ6prNuhAfyiGwHLzRsFTWuRBpg9kMwcnVXL9aIraB6semVozgCA78pEPdB7HfA9piPOM8/Mf8rwNqXG2OQ4SPoq0UeV31806H7tXHBgwVI84q4d06auAM5Gn61+MzSb58OXGAAYqN51QvzmPUBXFOE9Pr8qOv143ZLTybKb0Rn3qxfFEfeIgjcQxv8HeVpE9onDWTL/LrDAWrgKGd6m1NkD5LNWF6sZ0+oThdkY/NtDKayBoPK8HpfPsJeLUiUId6FYz/zruTCMUn2i5KjdN9l50u25aqXuvwkzRBFUXRZyAAAAAElFTkSuQmCC"
                                                        >
                                                        </v-img>
                                                    </template>
                                                    <span
                                                    v-if="getDarkmode()"
                                                    >
                                                        {{ item.nutzer.benutzername }} ist gerade online
                                                    </span>
                                                    <span
                                                    v-else
                                                    style="color:black"
                                                    >
                                                        {{ item.nutzer.benutzername }} ist gerade online
                                                    </span>
                                                </v-tooltip>
                                                </v-list-item-icon>
                                                <v-spacer>
                                                </v-spacer>
                                                    
                                                

                                                

                                                    

                                                <v-list-item-action v-if="item.nutzer.hasChat">
                                                    <v-tooltip bottom>
                                                        <template v-slot:activator="{ on, attrs }">
                                                            <v-btn 
                                                            icon
                                                            v-bind="attrs"
                                                            v-on="on"
                                                            @click="openChat(item.nutzer.id, item.nutzer.benutzername);"
                                                            >
                                                                <v-icon color="grey lighten-1">mdi-message</v-icon>
                                                            </v-btn>
                                                        </template>
                                                        <span>
                                                            Öffne den Chat mit {{ item.nutzer.benutzername }}
                                                        </span> 
                                                    </v-tooltip>
                                                </v-list-item-action>

                                                <v-list-item-action v-else>
                                                    <v-tooltip bottom>
                                                        <template v-slot:activator="{ on, attrs }">
                                                            <v-btn 
                                                            icon
                                                            v-bind="attrs"
                                                            v-on="on"
                                                            @click="createChat(item.nutzer.benutzername, item.nutzer.id);"
                                                            >
                                                                <v-img
                                                                height="48px"
                                                                width="48px"
                                                                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAJcwAACXMB+Yg9ogAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAL2SURBVFiFxZdLSFRhFMd/55uZm1ppb1wUZGC0iB5GUUngo6KawkiUqBBpEW2iXbVJooKyRbSMFilSFhqRmIaUNgRWtDArokWQQS97LHpoOTr3nhbOhFmD906T87+b+73O78/l+849n+BSG1t3ZobDEhShCGWxGuaKMgVAhc/i8FJFukXoGCDQ0lly/pubuDLWhOKrFfMRPYjodiDDpd/vCJeMcapvbql/npCBVQ1l6RkT0o6huh/wuwSP1hBwxs7yV4UKawdcG1jXvCPXts1VgYUJgkfrvt9mW1vphXdjGljbVLHUwWkTZWaS4DG9RkywvaTucVwD65p35NqO6fwP8F8m7IB/eShY2xvrMLGXgtuVaY5jGv8jHGC2f8i+vqqhLP0PA76v9nGUxV6izUqfzqz06Z4cKLosw0o7FGsLRI+acZ7icbcfyNsDwKmuc55MAH12wJ8bCtb2Dn8B0YNe4QAm+iSgSSYSqQIwG1t3ZkaTzLhKlIr8pt2TTTgsQdxnuGRqYpqGNxkRilIAH5ZIkfG685MqZZFByEmZAZjnBzLjja7OzqN4Tn7c1QumzAPg8PJ9cee0v+rkbm9XvOGshM5QMiXF13Z9BGYksvhQ3l4ATnadTZT/waD0JLo6CXphVKQ7hQYeGRE6UkVXkXZjhweuA/0p4Pc74YEbJlTe2IdwebzpitSHyhv7DIAxTjXDBeR4adDY9kmIFiTR0vmM1yjvf3zi/Y9PnumKnL5VWv8CRtQAdpa/yvclsgZY6TZQzbMrnuGg9yZYU4/EWr8VpQUtldm+IfsB6JwEIrvRW/WZFR1b6t7EOn5LxaFgbS8im4HXyWfLK0E3jIT/YQCgvaTusTUYyAPuJA+u9+yAb8WtrRefjB7568/oRnnNR8uauh7kKP+WIwZRPWFZ0wpH3gVGaszLaUFLZbaJRKpEqQAmugT3I1yUiFMd2+3xNKaBX0Yayib5LCsIUiiwRCEHhq/nwGeBHtCHqNyODIVbQ+WNfW7i/gT/EPLBy30p5gAAAABJRU5ErkJggg=="
                                                                >
                                                                </v-img>
                                                            </v-btn>
                                                        </template>
                                                        <span>
                                                            Starte einen Chat mit {{ item.nutzer.benutzername }} 
                                                        </span> 
                                                    </v-tooltip>
                                                </v-list-item-action>


                                                

                                            </v-list-item>
                                        </v-list-item-group>
                                    </v-list>
                                </v-card>
                                <v-card
                                    v-else
                                    :dark="getDarkmode()"
                                    max-width="628"
                                    tile
                                    rounded="true"
                                    min-height="400px"
                                    max-height="400px"
                                    min-width="628"
                                    >
                                        <div
                                        style="height: 10 em; posiition: relative; display: flex; align-items: center; justify-content: center "
                                        >
                                            <p
                                            v-if="getDarkmode()"
                                            style="text-align:center; margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: white; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                            >
                                            Du hast bisher noch keine Freunde. Verschicke direkt ein paar Freundschaftsanfragen, um mit dem 
                                            Chatten zu beginnen.
                                            </p>
                                            <p
                                            v-else
                                            style="text-align:center; margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: black; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                            >
                                            Du hast bisher noch keine Freunde. Verschicke direkt ein paar Freundschaftsanfragen, um mit dem 
                                            Chatten zu beginnen.
                                            </p>

                                            <v-btn
                                            @click="openSearch();"
                                            color="blue"
                                            style=" margin: 0; position: absolute; top: 65%; transform: translate(0, -50%); color: white;"
                                            >
                                                Hier gehts zur Suche
                                            </v-btn>


                                        </div>
                                    </v-card>
                                <v-dialog
                                v-model="dialog"
                                max-width="290"
                                >
                                    <v-card
                                    :dark="getDarkmode()"
                                    >
                                        <v-card-title class="headline"
                                        v-if="getDarkmode()"
                                        >
                                        Möchtest du {{deletingUsername}} wirkich als Freund entfernen?
                                        </v-card-title>

                                        <v-card-title class="headline"
                                        v-else
                                        style="color:black"
                                        >
                                        Möchtest du {{deletingUsername}} wirkich als Freund entfernen?
                                        </v-card-title>

                                        <v-card-text
                                        v-if="getDarkmode()"
                                        >
                                        Diese Aktion ist unwiederrufbar. Alle Nachrichten in eurem Privatchat werden gelösscht!
                                        </v-card-text>

                                        <v-card-text
                                        v-else
                                        style="color:black"
                                        >
                                        Diese Aktion ist unwiederrufbar. Alle Nachrichten in eurem Privatchat werden gelösscht!
                                        </v-card-text>

                                        <v-card-actions>
                                        <v-spacer></v-spacer>

                                        <v-btn
                                            color="green darken-1"
                                            text
                                            @click="dialog = false"
                                        >
                                            Abbrechen
                                        </v-btn>

                                        <v-btn
                                            color="red darken-1"
                                            text
                                            @click="deleteFriend(deletingUsername)"
                                        >
                                            Bestätigen
                                        </v-btn>
                                        </v-card-actions>
                                    </v-card>
                                </v-dialog>
                            </v-tab-item>

                            <v-tab
                            href="#ausstehend"
                            >
                                Austehende Anfragen
                            </v-tab>
                            <v-tab-item
                            value="ausstehend"
                            >
                                <v-card
                                :dark="getDarkmode()"
                                max-width="300"
                                tile
                                rounded="true"
                                v-if="allFriends.pendingRequests.length !== 0"
                                >
                                    <v-list 
                                        rounded
                                        min-height="400px"
                                        max-height="400px"
                                        class="overflow-y-auto"
                                        width="628"
                                        :dark="getDarkmode()"
                                        >
                                    <v-list-item-group
                                        v-model="model"
                                        :dark="getDarkmode()"
                                        color="blue"
                                    >
                                        <v-tooltip bottom
                                        :dark="getDarkmode()"
                                        >
                                            <template v-slot:activator="{ on, attrs }">
                                                <v-list-item
                                                v-for="(item, i) in allFriends.pendingRequests"
                                                :key="i"
                                                v-bind="attrs"
                                                v-on="on"
                                                :dark="getDarkmode()"
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


                                            <v-list-item-title 
                                            v-if="getDarkmode()"
                                            v-bind="attrs"
                                            v-on="on"
                                            v-text="item.benutzername">
                                            </v-list-item-title>     
                                            
                                            <v-list-item-title 
                                            v-else
                                            style="color:black"
                                            v-bind="attrs"
                                            v-on="on"
                                            v-text="item.benutzername">
                                            </v-list-item-title>     

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

                                                

                                            

                                        </v-list-item>
                                            </template>
                                            <span>
                                            Warte auf die Antwort dieser Freundschaftsanfrage
                                        </span>
                                        </v-tooltip>
                                        

                                    </v-list-item-group>
                                    </v-list>
                                </v-card>
                                <v-card
                                    v-else
                                    max-width="628px"
                                    tile
                                    rounded="true"
                                    min-height="400px"
                                    :dark="getDarkmode()"
                                    max-height="400px"
                                    >
                                        <div
                                        style="height: 10 em; posiition: relative; display: flex; align-items: center; justify-content: center "
                                        >
                                            <p
                                            v-if="getDarkmode()"
                                            style="text-align:center; margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: white; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                            >
                                            Du hast gerade keinen austehenden Freundschaftsanfragen, kannst aber direkt neue verschicken.
                                            </p>

                                            <p
                                            v-else
                                            style="text-align:center; margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: black; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                            >
                                            Du hast gerade keinen austehenden Freundschaftsanfragen, kannst aber direkt neue verschicken.
                                            </p>

                                            <v-btn
                                            @click="openSearch();"
                                            color="blue"
                                            style="margin: 0; position: absolute; top: 60%; transform: translate(0, -50%); color: white;"
                                            >
                                                Hier gehts zur Suche
                                            </v-btn>


                                        </div>
                                    </v-card>
                            </v-tab-item>

                            <v-tab
                            href="#eingehend"
                            >
                                Eingehende Anfragen
                            </v-tab>
                            <v-tab-item
                            value="eingehend"
                            >
                                <v-card
                                :dark="getDarkmode()"
                                max-width="500"
                                tile
                                rounded="true"
                                v-if="allFriends.friendRequests.length !== 0"
                                >
                                    <v-list 
                                        rounded
                                        :dark="getDarkmode()"
                                        min-height="400px"
                                        max-height="400px"
                                        class="overflow-y-auto"
                                        width="628"
                                        >
                                    <v-list-item-group
                                        v-model="model"
                                        :dark="getDarkmode()"
                                        color="blue"
                                    >
                                        <v-list-item
                                        v-for="(item, i) in allFriends.friendRequests"
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
                                            <v-list-item-title v-text="item.benutzername" v-if="getDarkmode()"></v-list-item-title>
                                            <v-list-item-title v-text="item.benutzername" v-else></v-list-item-title>
                                            </v-list-item-content>

                                            <v-spacer>
                                            </v-spacer>
                                                
                                               

                                                

                                            <v-list-item-action>
                                                 <v-tooltip bottom>
                                                    <template v-slot:activator="{ on, attrs }">
                                                        <v-btn 
                                                        icon
                                                        v-bind="attrs"
                                                        v-on="on"
                                                        @click="sendFa(item.benutzername)"
                                                        >
                                                            <v-img
                                                            height="48px"
                                                            width="48px"
                                                            src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAJcwAACXMB+Yg9ogAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAPQSURBVFiFvZdbTFxVFIa/vc/AzMCAVDHQFAxFIChWVCiRaFSINQ2gNYBjTby2Wp8ankrRWpyoscADISY2RkyMJqbJQEmbKFpjbEIVsC9gtbZgA6gtpTatVotcZuYsH2CQ2zCn49D/6Zy91l7ff5Kz9kVhUVmdnkQ1ZS8X0aUKyQcygKTZ8J/AiEC/hq+VoT4b2LL7byt1VbiE3I63cwLo3cBWIM6i33+UcMAvunGouvbniAykeZudjhjfm0qkBrBZBC+WD1SL78pE/cgLnknLBjLbm7INLR0gd0QIXqxemwQqT1XtOR/WQFb7vruVVkeAm6MED+qsNo3ygepdJ0IamPly89tVgM+Z8Pn1xhF37VhwQAcfMj70OAxttq0iHCAt1mZ+muZtdi4xYEtyvgXkryIcAIGCONt0XfBdwVyrnSTyvz2k1Ax0sa76/Dp7xF07pgFm+zzq8OezCjmyacdyIVeMYdYDqKxOT6KadJzH+iJjSWXrcmne+Bh7+j7n4C8/LE0QxrVNrdVqyl4ebXhRcjpNhRW8c+qb5eEAinjxm2VaRJdGE56dmMy791Zy6Ncf2T/QvWKuoEv17MYSFaU4E2gtfoK+S+d4vf/L8BOU3KmB9dGAu2x2WouruTQ1Ts3xwwTEtDItUwOJoaI7c+8jLyklbJVYbfBecRUOw8aL3W1MBHxWfd+gV4re4lrDgQeeZkt6XsgcrRRNhRVkJtzI9u42/piesAoHwLjpyYdrCNEFX40OEhBhb/4mUp0ujv0+jCkLl5W6DaU8mn47L3W3MfjXxWuCA5c1MBwqKsD7g7283NPO5nW5fHz/UyTb4+fi27KLeCazgJ3fHaL/8ui1wgGGtEB/uKyuC0NUHf2IxBg7HSXPsWHNWsrSbqM27yH29n9B14WhSOAA36ucjoatAgesZMfZYmksKOPBlFvRSrH/dHfYXl9JgrhVntfjmjYcYyjiw0+Z2Vy2ZReREGOn5adjEcMRxmMDk6kKIOdgwwei2B55tUj4tJ6prNuhAfyiGwHLzRsFTWuRBpg9kMwcnVXL9aIraB6semVozgCA78pEPdB7HfA9piPOM8/Mf8rwNqXG2OQ4SPoq0UeV31806H7tXHBgwVI84q4d06auAM5Gn61+MzSb58OXGAAYqN51QvzmPUBXFOE9Pr8qOv143ZLTybKb0Rn3qxfFEfeIgjcQxv8HeVpE9onDWTL/LrDAWrgKGd6m1NkD5LNWF6sZ0+oThdkY/NtDKayBoPK8HpfPsJeLUiUId6FYz/zruTCMUn2i5KjdN9l50u25aqXuvwkzRBFUXRZyAAAAAElFTkSuQmCC"
                                                            >
                                                            </v-img>
                                                        </v-btn>
                                                    </template>
                                                    <span>
                                                        Freundschaftsanfrage
                                                        von {{ item.benutzername }}
                                                        annehmen
                                                    </span>
                                                </v-tooltip>
                                            </v-list-item-action>

                                            <v-list-item-action>
                                                 <v-tooltip bottom>
                                                    <template v-slot:activator="{ on, attrs }">
                                                        <v-btn 
                                                        icon
                                                        v-bind="attrs"
                                                        v-on="on"
                                                        @click="declineFa(item.benutzername)"
                                                        >
                                                            <v-img
                                                            height="48px"
                                                            width="48px"
                                                            src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAJcwAACXMB+Yg9ogAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAN7SURBVFiFtZdPTBxlGIefd2YW+bMgB032oCJtFinbCGrquTSNKSwxscnGhiZNA9FEW+Ox9kKM1igX05tNNIYaC4dNPOgCxT+FaEy5CTXbNhJBpdVtGgVldwMddl4P3SWbpbM7u7C/087M+33PM9lvvndG8JiF4z1Nm46ERTik0Ak8CTRnL68Cv6Eyp8IVo9Ycb//0yzUv80qpghsv97apIWdE9BhQ79E3LTDmYAx3jMYWKhJYjkTqklb6XUTfBCyP4MLYKpyv86WHWkdm1j0LXO/vC4o6XyDsrxBcmFk1Mkc7Pp/6q6RA/FjPM4YhU8CjuwTP5ZaoE24fu3zNVeB6f19QcH6sAnxLwrE5EIpOJHInjNyPpZMHa0WdaBXhAI8ZPmLLkUjdNoH1jYZzCJ1VhOfyXLIm+VbuQOD+o4ZJnMpXe5mRpGNrMBSdSBgAasiZYnDT30hDqMvz9PWhTkx/Y5EK9YtPhgCMheM9TdlNxjW1LXt5/Ow5mg+HS8KbD4d54ux7PNSyp2idoCduDrzYaL6+v+0oQlEB+26CzZW/CQycIpNcY/3XXx4M7z5CYOAUdy5eYG32h1KuNZrJzFvZvb1kVqenAAgMvgHAytdfbYcPnubOxQusfBPzMCOIcsjKNhZPcZOoBJ5VeNoCWssYsU1CbbtCOAB7LKCp3FGFEhXCAR42Ste4JW8Xl5Jd3TUW8B/wSDmD8v9z3bRdF6aH/GsBS+UIuC24CiUWLVTmED2wE3ipR9Q1wrylwhWBVyqF70RCVL6z1E7FxFefAhrcChtCXQQGT5MY+YjVb8ddJ1ydngLTInDyNTZu/0E6Pl+Mn8rYqUkBuNnf+4nCoFul6W+ktmUvqfhcybuC+81o4/dFMskiL8YqH+8bG3/VAHAwhgHbrTaTXPMMB0jH54vD4Z4pfADZF5KO0diCCuc9E3Ya4cO20fHFLQGAOl96CJitPl2vWv/o27mjLYHWkZl1x+YlgeUqwv80LDMSnJzc2CYAEIpOJFCnD7i122iBZUSOPPVZ7Hb++W29oH3s8jXL1meB73cPr1czNs/vuzTxc+GVBzajYHTyrrWiLyD6DpDaAfkeyvvWCt353wL5KdnG4pHegPhkSNATFNmsCpICvWRiDOdWu1s899F45KBfaurCgnQDXSit5H+eC0uq/GQo05nN9EQoOpP0Mu//wCJoeAiEHosAAAAASUVORK5CYII="
                                                            >
                                                            </v-img>
                                                        </v-btn>
                                                    </template>
                                                    <span>
                                                        Freundschaftsanfrage
                                                        von {{ item.benutzername }}
                                                        ablehnen
                                                    </span>
                                                </v-tooltip>

                                            </v-list-item-action>

                                        </v-list-item>
                                    </v-list-item-group>
                                    </v-list>
                                </v-card>
                                <v-card
                                    v-else
                                    :dark="getDarkmode()"
                                    max-width="628px"
                                    tile
                                    rounded="true"
                                    min-height="400px"
                                    max-height="400px"
                                    min-width="628"
                                    >
                                        <div
                                        style="height: 10 em; posiition: relative; display: flex; align-items: center; justify-content: center "
                                        >
                                            <p
                                            v-if="getDarkmode()"
                                            style="text-align:center; margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: white; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                            >
                                            Du hast gerade keine eingehenden Freundschftsanfragen.
                                            </p>

                                            <p
                                            v-else
                                            style="text-align:center; margin: 0; position: absolute; top: 45%; transform: translate(0, -50%); color: black; padding: 120px; margin-left: auto; margin-right: auto; width: 35em"
                                            >
                                            Du hast gerade keine eingehenden Freundschftsanfragen.
                                            </p>
                                        </div>
                                    </v-card>
                            </v-tab-item>

                            <v-tab
                            href="#search"
                            >
                                <v-icon color="grey lighten-1">mdi-account-search</v-icon>
                            </v-tab>
                            <v-tab-item
                            value="search"
                            >
                                <v-card
                                width="628"
                                min-height="400"
                                max-height="400"
                                :dark="getDarkmode()"
                                >
                                <v-card-title
                                >
                                    <span
                                    v-if="getDarkmode()"
                                    style="color:white"
                                    >
                                        Freunde hinzufügen
                                    </span>
                                    <span
                                    v-else
                                    style="color:black"
                                    >
                                        Freunde hinzufügen
                                    </span>
                                </v-card-title>
                                <v-card-text>
                                    <v-container>
                                    
                                        <v-text-field
                                        label="Nutzer suchen"
                                        color="primary"
                                        v-model="input"
                                        filled
                                        :dark="getDarkmode()"
                                        @keydown.enter="search();"
                                        >
                                    
                                        <template v-slot:append>
                                    
                                            <v-btn
                                            small
                                            color="primary"
                                            show
                                            @click="search();"
                                            @keydown.enter="search();"
                                            >
                                            
                                                <v-icon color="grey lighten-1">mdi-magnify</v-icon>
                                            </v-btn>
                                            
                                        </template>
                                            
                                        </v-text-field>
                                        
                                    </v-container>

                                    
                                    <v-list 
                                        rounded
                                        :dark="getDarkmode()"
                                        min-height="200px"
                                        max-height="200px"
                                        min-width="600"
                                        class="overflow-y-auto"
                                        width="536px"
                                        >
                                        <v-list-item-group
                                            v-model="model"
                                            :dark="getDarkmode()"
                                            color="blue"
                                        >
                                            <v-list-item
                                            v-for="(item, i) in searchResults.results"
                                            :key="i"
                                            >
                                                <v-list-item-avatar>
                                                    <div v-if="item.nutzer.profilbild === undefined">
                                                        <v-img 
                                                        src='data:image/png;base64,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'
                                                        ></v-img>
                                                    </div>
                                                    <div v-else>
                                                        <v-img
                                                        :src="item.nutzer.profilbild.base64"
                                                        ></v-img>
                                                    </div>
                                                </v-list-item-avatar>

                                                <v-list-item-content>
                                                <v-list-item-title v-text="item.nutzer.benutzername" v-if="getDarkmode()"></v-list-item-title>
                                                <v-list-item-title v-text="item.nutzer.benutzername" v-else style="color:black"></v-list-item-title>
                                                </v-list-item-content>
                                               
                                                <v-spacer>
                                                </v-spacer>
                                                    
                                                

                                                <v-list-item-action
                                                v-if="item.sentfa === undefined"
                                                >
                                                    <v-tooltip bottom>
                                                        <template v-slot:activator="{ on, attrs }">
                                                            <v-btn 
                                                            icon
                                                            v-bind="attrs"
                                                            v-on="on"
                                                            small
                                                            @click="sendFa(item.nutzer.benutzername)"
                                                            >
                                                                <v-img
                                                                height="48px"
                                                                width="48px"
                                                                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAJcwAACXMB+Yg9ogAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAL2SURBVFiFxZdLSFRhFMd/55uZm1ppb1wUZGC0iB5GUUngo6KawkiUqBBpEW2iXbVJooKyRbSMFilSFhqRmIaUNgRWtDArokWQQS97LHpoOTr3nhbOhFmD906T87+b+73O78/l+849n+BSG1t3ZobDEhShCGWxGuaKMgVAhc/i8FJFukXoGCDQ0lly/pubuDLWhOKrFfMRPYjodiDDpd/vCJeMcapvbql/npCBVQ1l6RkT0o6huh/wuwSP1hBwxs7yV4UKawdcG1jXvCPXts1VgYUJgkfrvt9mW1vphXdjGljbVLHUwWkTZWaS4DG9RkywvaTucVwD65p35NqO6fwP8F8m7IB/eShY2xvrMLGXgtuVaY5jGv8jHGC2f8i+vqqhLP0PA76v9nGUxV6izUqfzqz06Z4cKLosw0o7FGsLRI+acZ7icbcfyNsDwKmuc55MAH12wJ8bCtb2Dn8B0YNe4QAm+iSgSSYSqQIwG1t3ZkaTzLhKlIr8pt2TTTgsQdxnuGRqYpqGNxkRilIAH5ZIkfG685MqZZFByEmZAZjnBzLjja7OzqN4Tn7c1QumzAPg8PJ9cee0v+rkbm9XvOGshM5QMiXF13Z9BGYksvhQ3l4ATnadTZT/waD0JLo6CXphVKQ7hQYeGRE6UkVXkXZjhweuA/0p4Pc74YEbJlTe2IdwebzpitSHyhv7DIAxTjXDBeR4adDY9kmIFiTR0vmM1yjvf3zi/Y9PnumKnL5VWv8CRtQAdpa/yvclsgZY6TZQzbMrnuGg9yZYU4/EWr8VpQUtldm+IfsB6JwEIrvRW/WZFR1b6t7EOn5LxaFgbS8im4HXyWfLK0E3jIT/YQCgvaTusTUYyAPuJA+u9+yAb8WtrRefjB7568/oRnnNR8uauh7kKP+WIwZRPWFZ0wpH3gVGaszLaUFLZbaJRKpEqQAmugT3I1yUiFMd2+3xNKaBX0Yayib5LCsIUiiwRCEHhq/nwGeBHtCHqNyODIVbQ+WNfW7i/gT/EPLBy30p5gAAAABJRU5ErkJggg=="
                                                                >
                                                                </v-img>
                                                            </v-btn>
                                                        </template>
                                                        <span>
                                                            Freundschaftsanfrage an {{ item.nutzer.benutzername }} senden
                                                        </span> 
                                                    </v-tooltip>
                                                </v-list-item-action>
                                                <v-list-item-action v-else>
                                                    <v-tooltip bottom>
                                                        <template v-slot:activator="{ on, attrs }">
                                                            <v-btn 
                                                            icon
                                                            v-bind="attrs"
                                                            v-on="on"
                                                            @click="openChat(item.nutzer.id);"
                                                            >
                                                                <v-icon color="grey lighten-1">mdi-message</v-icon>
                                                            </v-btn>
                                                        </template>
                                                        <span>
                                                            Öffne den Chat mit {{ item.nutzer.benutzername }}
                                                        </span> 
                                                    </v-tooltip>
                                                </v-list-item-action>

                                            </v-list-item>
                                        </v-list-item-group>
                                    </v-list>
                                </v-card-text>
                                </v-card>
                            </v-tab-item>

                        </v-tabs>                       
                    </v-card>
            </v-row>
        </div>
    </v-container>
</template>

<script>
import axios from "axios";
import { ImageContainer } from './ImageContainer.js';
import { EventBus } from './EventBus.js';
  export default {
    

    mounted() {
        this.loadData();
        EventBus.$on("RELOAD", (payload) =>{
            this.loadData();
        });
    },
    methods: {

        methodThatForcesUpdate() {
            this.$forceUpdate()
        },

        loadData() {
            axios.get(window.IP_ADDRESS +"/GFOS/daten/nutzer/getFriendList/" + window.CURRENT_USER_ID + "/" + window.CURRENT_TOKEN)
            .then((resp)=>{
                this.allFriends = resp.data
                return resp.data
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },

        search() {
            if(this.input === undefined){
                EventBus.$emit("INFOMESSAGE",  {'message': 'Das Suchfeld darf nicht leer sein!', "icon": "WARNING"})
            }
            else{
                axios.get(window.IP_ADDRESS +"/GFOS/daten/nutzer/search/" + this.input + "/" + window.CURRENT_USER_ID + "/" + window.CURRENT_TOKEN)
                .then((resp)=>{
                this.searchResults = resp.data
                this.searchedOnce = false
                })
                .catch(error =>{
                    if(error.response.data === "Benutzername nicht vorhanden") {
                        EventBus.$emit("INFOMESSAGE",  {'message': 'Kein Nutzer gefunden!', "icon": "ERROR"})
                    }
                    if(error.response.data === "Kein gültiges Token"){
                        EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                        EventBus.$emit("LOGOUT", {})
                    }
                })
            
            }
        },

        openChat(id, benutzername) {
            axios.get(window.IP_ADDRESS + "/GFOS/daten/chat/" + window.CURRENT_USER_ID + "/" + id + "/" + window.CURRENT_TOKEN)
            .then((resp)=>{
                this.chatid = resp.data.id;
                //EventBus.$emit('CHANGEHEIGHT', {'height': 185});
                window.openChat(this.chatid, window.CURRENT_USER_ID)
                EventBus.$emit('OPENCHAT', {'chatid': this.chatid, 'userid': window.CURRENT_USER_ID, "name": benutzername});
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },

        createChat(benutzername, id) {
            EventBus.$emit("INFOMESSAGE", {'message': 'Dein Chat mit ' + benutzername + " wird erstellt"})
            const now = Date.now();
            const payload = {
                "eigeneId": window.CURRENT_USER_ID,
                "benutzername": benutzername,
                "datumuhrzeit": now
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/chat/createAsChat/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                this.loadData();
                this.openChat(id, benutzername);
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },

        sendFa(benutzername) {
            const payload = {
                "eigeneId": window.CURRENT_USER_ID,
                "andererNutzerName": benutzername,
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/nutzer/freundesAnfrage/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                EventBus.$emit("INFOMESSAGE", {'message': 'Deine Freundschaftsanfrage an ' + benutzername + " wurde erfolgreich gesendet"})
                this.loadData();
                this.search();
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },

        declineFa(benutzername){
            this.dialog = false
            const payload = {
                "eigeneId": window.CURRENT_USER_ID,
                "andererNutzerName": benutzername,
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/nutzer/loescheFreund/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                EventBus.$emit("INFOMESSAGE", {'message': 'Die Freundschaftsanfrage von ' + benutzername + " wird abgelehnt"})
                this.loadData();
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },

        deleteFriend(benutzername){
            this.dialog = false
            const payload = {
                "eigeneId": window.CURRENT_USER_ID,
                "andererNutzerName": benutzername,
            }
            const headers = {
                                'Accept': 'application/json', // Antwort von Server
                                'Content-Type': 'text/plain', // Wird an den Server gesendet
                            }
            axios.post(window.IP_ADDRESS +"/GFOS/daten/nutzer/loescheFreund/" + window.CURRENT_TOKEN , payload, { headers})
            .then((resp)=>{
                EventBus.$emit("INFOMESSAGE", {'message': 'Deine Freundschaft mit ' + benutzername + " wurde beendet."})
                this.loadData();
            })
            .catch(error =>{
                if(error.response.data === "Kein gültiges Token"){
                    EventBus.$emit("INFOMESSAGE", {"message": "Deine Sitzung ist abgelaufen. Bitte melde dich erneut an!", "icon": "ERROR"})
                    EventBus.$emit("LOGOUT", {})
                }
                
            })
        },

        contextMenu(e, name){
            this.dialog = true;
            this.deletingUsername = name
            e.preventDefault();
            this.showDelete = !this.showDelete
        },

        openSearch(){
            this.tab = "search"
        },

        getDarkmode(){
            return window.DARK_MODE
        },

        
    
    },

    data: () => ({
        model: 1,
        tab: "freunde",
      searchResults: {},
      allFriends:  {},
      isFetching: {},
      searchedOnce: false,
      searchIsEmpty: true,
      chatid: 0,
      showDelete: false,
      dialog: false,
      deletingUsername: "",

    }),

  }

</script>