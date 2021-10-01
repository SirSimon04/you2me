import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chatinfo/chatinfo_screen.dart';
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:flutter_dispuatio/widgets/userprofile_pic.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class UserListTile extends StatelessWidget {
  UserListTile({
    required this.uid,
    required this.isAdmin,
  });

  final _firestore = FirebaseFirestore.instance;
  final _auth = FirebaseAuth.instance;
  final String uid;
  final bool isAdmin;

  @override
  Widget build(BuildContext context) {
    if (uid == _auth.currentUser?.uid) {
      return PlatformListTile(
        title: Row(
          children: [
            Text(
              "Du",
              style: TextStyle(fontStyle: FontStyle.italic),
            ),
            Spacer(),
            isAdmin
                ? Text(
                    "Admin",
                    style: TextStyle(
                      fontStyle: FontStyle.italic,
                    ),
                  )
                : Text(""),
          ],
        ),
        isElevatedM: true,
        leading: UserProfilePic(
          isOnline: false,
          url: _auth.currentUser?.photoURL ?? "",
        ),
      );
    }

    return StreamBuilder<DocumentSnapshot>(
      stream: _firestore.collection("user").doc(uid).snapshots(),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          print("snapshot " + snapshot.toString());
          print("data " + snapshot.data!.data().toString());
          var data = snapshot.data as DocumentSnapshot;
          // print("test2 " + data["name"]);
          // print("data2 " + data.data().toString());
          // return Container();
          return PlatformListTile(
            title: Row(
              children: [
                Text(data["name"].toString()),
                Spacer(),
                isAdmin
                    ? Text(
                        "Admin",
                        style: TextStyle(
                          fontStyle: FontStyle.italic,
                        ),
                      )
                    : Text(""),
              ],
            ),
            isElevatedM: true,
            leading: UserProfilePic(
              isOnline: false,
              url: data["fotourl"].toString(),
            ),
            trailing: IconButton(
              onPressed: () => Navigator.of(context).push(
                CupertinoPageRoute(
                  builder: (context) => ChatInfoScreen(
                    ChatModel.getEmptyChat(),
                    comesFromFriends: true,
                    userUid: data.id,
                    isFriend:
                        data["hasChatWith"].contains(_auth.currentUser?.uid),
                    photoUrl: data["fotourl"],
                  ),
                ),
              ),
              icon: Icon(FontAwesomeIcons.infoCircle),
            ),
          );
        } else {
          return Container();
        }
      },
    );
  }
}
