import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chatinfo/chatinfo_screen.dart';
import 'package:flutter_dispuatio/services/user_services/GeneralUserService.dart';
import 'package:flutter_dispuatio/widgets/clickable_appbar.dart';
import 'package:flutter_dispuatio/widgets/userprofile_pic.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:time_ago_provider/time_ago_provider.dart' as time_ago;

final _auth = FirebaseAuth.instance;
final _firestore = FirebaseFirestore.instance;

String getChatAppBarUid(chat) {
  if (chat.members[0].split("|")[0] == _auth.currentUser?.uid) {
    return chat.members[1].split("|")[0];
  } else if (chat.members[1].split("|")[0] == _auth.currentUser?.uid) {
    return chat.members[0].split("|")[0];
  }
  return "";
}

getChatAppBar(chat, context) {
  time_ago.setLocale('de', time_ago.German());
  time_ago.setLocale('de_short', time_ago.German(shortForm: true));
  return ClickableAppBar(
    onTap: () => Navigator.of(context).push(
      CupertinoPageRoute(
        builder: (context) => ChatInfoScreen(chat),
      ),
    ),
    appBar: AppBar(
      actions: [
        IconButton(
          icon: Icon(FontAwesomeIcons.infoCircle),
          onPressed: () {
            Navigator.of(context).push(
              CupertinoPageRoute(
                builder: (context) => ChatInfoScreen(chat),
              ),
            );
          },
        ),
      ],
      title: StreamBuilder<DocumentSnapshot>(
          stream: _firestore
              .collection("user")
              .doc(getChatAppBarUid(chat))
              .snapshots(),
          builder: (context, AsyncSnapshot snapshot) {
            if (!snapshot.hasData) {
              return Container();
            } else {
              return Row(
                children: [
                  UserProfilePic(
                      isOnline: snapshot.data["isonline"],
                      url: GeneralUserService.getOtherUserFotoUrl(chat)),
                  SizedBox(
                    width: 20,
                  ),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        chat.name,
                        overflow: TextOverflow.ellipsis,
                        style: TextStyle(
                          fontSize: 15,
                        ),
                      ),
                      SizedBox(
                        height: 3,
                      ),
                      snapshot.data["isonline"]
                          ? getTextIfOnline(chat, context)
                          : Text(
                              !snapshot.hasData
                                  ? ""
                                  : time_ago.format(
                                      DateTime.fromMillisecondsSinceEpoch(
                                        snapshot.data["lastonline"]
                                            .millisecondsSinceEpoch,
                                      ),
                                      locale: "de"),
                              style: TextStyle(
                                fontSize: 12,
                              ),
                            ),
                      // snapshot.data["isonline"]
                      //     ?
                    ],
                  )
                ],
              );
            }
          }),
    ),
  );
}

StreamBuilder getTextIfOnline(ChatModel chat, context) {
  return StreamBuilder<DocumentSnapshot>(
    stream: _firestore.collection("chat").doc(chat.uid).snapshots(),
    builder: (context, AsyncSnapshot snapshot) {
      if (!snapshot.hasData) {
        return Text("");
      } else {
        if (snapshot.data["writing"].contains(getChatAppBarUid(chat))) {
          return Text(
            "Schreibt...",
            style: TextStyle(
              fontStyle: FontStyle.italic,
              fontSize: 12,
            ),
          );
        } else {
          return Text(
            "Online",
            style: TextStyle(
              fontSize: 12,
            ),
          );
        }
      }
    },
  );
}
