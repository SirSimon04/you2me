import 'package:adaptive_action_sheet/adaptive_action_sheet.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chatinfo/chatinfo_screen.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:flutter_dispuatio/widgets/userprofile_pic.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class UserListTile extends StatefulWidget {
  UserListTile({
    required this.uid,
    required this.isAdmin,
    required this.chat,
  });

  final String uid;
  final bool isAdmin;
  final ChatModel chat;

  @override
  State<UserListTile> createState() => _UserListTileState(isAdmin);
}

class _UserListTileState extends State<UserListTile> {
  _UserListTileState(this.isAdmin);

  final _firestore = FirebaseFirestore.instance;

  final _auth = FirebaseAuth.instance;

  bool isAdmin;

  @override
  Widget build(BuildContext context) {
    if (widget.uid == _auth.currentUser?.uid) {
      return PlatformListTile(
        title: Row(
          children: [
            Text(
              "Du",
              style: TextStyle(fontStyle: FontStyle.italic),
            ),
            Spacer(),
            widget.isAdmin
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
      stream: _firestore.collection("user").doc(widget.uid).snapshots(),
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
            onTap: (widget.chat.adminList!
                    .contains(_auth.currentUser?.uid ?? ""))
                ? () {
                    showAdaptiveActionSheet(
                      context: context,
                      actions: [
                        BottomSheetAction(
                          title: Text("Entfernen"),
                          onPressed: () => ChatFirebaseService.kickOutOfGroup(
                            uid: data.id,
                            chat: widget.chat,
                          ),
                        ),
                        BottomSheetAction(
                          title: isAdmin
                              ? Text("Admin entfernen")
                              : Text("Zum Admin machen"),
                          onPressed: isAdmin
                              ? () async {
                                  await ChatFirebaseService.removeAdmin(
                                    userUid: data.id,
                                    chatUid: widget.chat.uid,
                                  );
                                  setState(() {
                                    isAdmin = false;
                                  });
                                  Navigator.of(context).pop();
                                }
                              : () async {
                                  await ChatFirebaseService.makeAdmin(
                                    userUid: data.id,
                                    chatUid: widget.chat.uid,
                                  );
                                  setState(() {
                                    isAdmin = true;
                                  });
                                  Navigator.of(context).pop();
                                },
                        ),
                      ],
                      cancelAction: CancelAction(
                        title: const Text('Schließen'),
                      ),
                    );
                  }
                : null,
          );
        } else {
          return Container();
        }
      },
    );
  }
}
