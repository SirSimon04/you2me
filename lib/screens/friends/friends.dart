import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chatinfo/chatinfo_screen.dart';
import 'package:flutter_dispuatio/screens/friends/components/friends_appbar.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:flutter_dispuatio/services/general_services/toast_service.dart';
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:flutter_dispuatio/widgets/userprofile_pic.dart';
import 'package:flutter_svg/svg.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

import '../../services/chat_service/chat_firebase_service.dart';

class FriendLists extends StatefulWidget {
  const FriendLists({Key? key}) : super(key: key);

  @override
  _FriendListsState createState() => _FriendListsState();
}

class _FriendListsState extends State<FriendLists>
    with AutomaticKeepAliveClientMixin {
  final _auth = FirebaseAuth.instance;
  final _firestore = FirebaseFirestore.instance;

  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) {
    super.build(context);
    print(_auth.currentUser?.uid);
    return Scaffold(
      appBar: getAppBar(context),
      body: Column(
        children: [
          Container(
            child: Expanded(
              child: StreamBuilder<QuerySnapshot>(
                  stream: _firestore.collection("user").where("friends",
                      arrayContainsAny: [_auth.currentUser?.uid]).snapshots(),
                  builder: (context, snapshot) {
                    if (snapshot.hasError) {
                      return Text("Error: ${snapshot.error}");
                    }
                    switch (snapshot.connectionState) {
                      case ConnectionState.waiting:
                        return Center(child: CircularProgressIndicator());
                      default:
                        if (snapshot.data!.docs.length == 0) {
                          return Column(
                            children: [
                              SingleChildScrollView(
                                child: Column(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Padding(
                                      padding: EdgeInsets.all(40),
                                      child: SvgPicture.asset(
                                        "assets/placeholders/friends.svg",
                                        height:
                                            MediaQuery.of(context).size.height *
                                                0.5,
                                        width:
                                            MediaQuery.of(context).size.width *
                                                0.7,
                                      ),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Text(
                                        "Du hast noch keine Freunde. Klicke auf die Lupe, um Freunde hinzuzfügen.",
                                        textAlign: TextAlign.center,
                                      ),
                                    )
                                  ],
                                ),
                              ),
                            ],
                          );
                        }

                        final List<DocumentSnapshot> documents =
                            snapshot.data!.docs;
                        return ListView(
                          children: documents.map((doc) {
                            return PlatformListTile(
                              title: Text(doc["name"]),
                              subtitle: Text(doc["info"]),
                              leading: UserProfilePic(
                                isOnline: doc["isonline"],
                                url: doc["fotourl"],
                              ),
                              trailing: Row(
                                mainAxisSize: MainAxisSize.min,
                                children: [
                                  IconButton(
                                    onPressed: () => Navigator.of(context).push(
                                      CupertinoPageRoute(
                                        builder: (context) => ChatInfoScreen(
                                          ChatModel.getEmptyChat(),
                                          comesFromFriends: true,
                                          userUid: doc.id,
                                          isFriend: doc["hasChatWith"]
                                              .contains(_auth.currentUser?.uid),
                                          photoUrl: doc["fotourl"],
                                        ),
                                      ),
                                    ),
                                    icon: Icon(FontAwesomeIcons.infoCircle),
                                  ),
                                  if (!doc["hasChatWith"]
                                      .contains(_auth.currentUser?.uid))
                                    IconButton(
                                      onPressed: () {
                                        ChatFirebaseService.createChat(doc)
                                            .then((value) =>
                                                ToastService.showLongToast(
                                                    "Dein Chat mit ${doc["name"]} wurde erfolgreich erstellt"))
                                            .onError((error, stackTrace) =>
                                                ToastService.showLongToast(
                                                    "Es ist ein Fehler aufgetreten"));
                                      },
                                      icon: Icon(FontAwesomeIcons.plusCircle),
                                    )
                                ],
                              ),
                            );
                          }).toList(),
                        );
                    }
                  }),
            ),
          )
        ],
      ),
    );
  }
}
