import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:flutter_dispuatio/widgets/userprofile_pic.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class UserListTile extends StatelessWidget {
  UserListTile({required this.uid});

  final _firestore = FirebaseFirestore.instance;
  final _auth = FirebaseAuth.instance;
  String uid;

  @override
  Widget build(BuildContext context) {
    if (uid == _auth.currentUser?.uid) {
      return PlatformListTile(
        title: Text(
          "Du",
          style: TextStyle(fontStyle: FontStyle.italic),
        ),
        isElevatedM: true,
        leading: UserProfilePic(
          isOnline: false,
          url: _auth.currentUser?.photoURL ?? "",
        ),
      );
    }

    return FutureBuilder(
      future: _firestore.collection("user").doc(uid).get(),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          print("here");
          print(snapshot.data.toString());
          var data = snapshot.data as DocumentSnapshot;
          return PlatformListTile(
            title: Text(data["name"].toString()),
            isElevatedM: true,
            leading: UserProfilePic(
              isOnline: false,
              url: data["fotourl"].toString(),
            ),
            trailing: Icon(
              FontAwesomeIcons.infoCircle,
            ),
          );
        } else {
          return PlatformListTile(
            isElevatedM: true,
            title: Text("Fehler"),
          );
        }
      },
    );
  }
}
