import 'package:cached_network_image/cached_network_image.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chatinfo/widgets/add_users_bottom_sheet.dart';
import 'package:flutter_dispuatio/screens/chatting/chatinfo/widgets/userlisttile.dart';
import 'package:flutter_dispuatio/screens/settings/screens/marked_messages/marked_messages.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:flutter_dispuatio/services/general_services/toast_service.dart';
import 'package:flutter_dispuatio/services/user_services/GeneralUserService.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:time_ago_provider/time_ago_provider.dart' as time_ago;

class ChatInfoScreen extends StatefulWidget {
  final ChatModel chat;
  final bool? comesFromFriends;
  final bool? isFriend;
  final String? userUid;
  final String? photoUrl;

  ChatInfoScreen(
    this.chat, {
    this.userUid,
    this.comesFromFriends,
    this.isFriend,
    this.photoUrl,
  });

  @override
  _ChatInfoScreenState createState() =>
      _ChatInfoScreenState(chat, isFriend: isFriend);
}

//TODO: Refactor into two multiple screens
class _ChatInfoScreenState extends State<ChatInfoScreen> {
  final _auth = FirebaseAuth.instance;

  final _firestore = FirebaseFirestore.instance;

  bool? isFriend;
  ChatModel chat;
  _ChatInfoScreenState(this.chat, {this.isFriend});

  /// return the uid of the other user
  String getUidForOtherUser(chat) {
    if (chat.members[0].split("|")[0] == _auth.currentUser?.uid) {
      return chat.members[1].split("|")[0];
    } else if (chat.members[1].split("|")[0] == _auth.currentUser?.uid) {
      return chat.members[0].split("|")[0];
    }
    return "";
  }

  List<String> orderMemberList(List<String> members) {
    members.remove(_auth.currentUser?.uid ?? "");
    members.insert(0, _auth.currentUser?.uid ?? "");
    print("members " + members.toString());
    return members;
  }

  Stream<DocumentSnapshot<Object?>>? getStream() {
    if (widget.comesFromFriends == true) {
      return _firestore.collection("user").doc(widget.userUid).snapshots();
    } else {
      return _firestore
          .collection("user")
          .doc(getUidForOtherUser(widget.chat))
          .snapshots();
    }
  }

  @override
  Widget build(BuildContext context) {
    print("adminList " + widget.chat.adminList.toString());
    time_ago.setLocale('de', time_ago.German());
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "Info",
          style: TextStyle(color: Colors.white),
        ),
        automaticallyImplyLeading: false,
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          color: Colors.white,
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: !widget.chat.isGroup
          ? StreamBuilder<DocumentSnapshot>(
              stream: getStream(),
              builder: (context, AsyncSnapshot snapshot) {
                if (!snapshot.hasData) {
                  return Container();
                } else {
                  return ListView(
                    children: [
                      Center(
                        child: Padding(
                          padding: const EdgeInsets.all(20.0),
                          child: Container(
                            height: 280,
                            width: 280,
                            decoration: BoxDecoration(
                              shape: BoxShape.circle,
                              image: DecorationImage(
                                fit: BoxFit.fill,
                                image: CachedNetworkImageProvider(
                                  widget.comesFromFriends == true
                                      ? widget.photoUrl ?? ""
                                      : GeneralUserService.getOtherUserFotoUrl(
                                          chat),
                                ),
                              ),
                            ),
                          ),
                        ),
                      ),
                      Center(
                        child: Text(
                          snapshot.data["name"],
                          style: TextStyle(
                            fontSize: 32,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Center(
                          child: Container(
                            width: MediaQuery.of(context).size.width * 0.8,
                            decoration: BoxDecoration(
                              borderRadius:
                                  BorderRadius.circular(kCircularBorderRadius),
                              color: Theme.of(context).cardColor,
                            ),
                            child: Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Text(
                                snapshot.data["isonline"]
                                    ? "Online"
                                    : time_ago.format(
                                        DateTime.fromMillisecondsSinceEpoch(
                                            snapshot.data["lastonline"]
                                                .millisecondsSinceEpoch),
                                        locale: "de"),
                                textAlign: TextAlign.center,
                              ),
                            ),
                          ),
                        ),
                      ),
                      SizedBox(
                        height: 30,
                      ),
                      PlatformListTile(
                        isElevatedM: true,
                        title: Text(
                          snapshot.data["info"],
                          style: Theme.of(context).textTheme.bodyText1,
                        ),
                        leading: Icon(FontAwesomeIcons.infoCircle),
                        trailing: Icon(
                          Icons.circle,
                          color: Colors.white.withOpacity(0),
                        ),
                      ),
                      SizedBox(
                        height: 70,
                      ),
                      // PlatformListTile(
                      //   isElevatedM: true,
                      //   title: Text(
                      //     "Fotos & Videos",
                      //     style: Theme.of(context).textTheme.bodyText1,
                      //   ),
                      //   leading: Icon(FontAwesomeIcons.photoVideo),
                      //   onTap: () {},
                      // ),
                      if (widget.comesFromFriends != true)
                        PlatformListTile(
                          isElevatedM: true,
                          title: Text(
                            "Mit Stern markiert",
                            style: Theme.of(context).textTheme.bodyText1,
                          ),
                          leading: Icon(FontAwesomeIcons.solidStar),
                          onTap: () {
                            Navigator.of(context).push(CupertinoPageRoute(
                              builder: (context) => MarkedMessages(
                                isInChat: true,
                                chatUid: widget.chat.uid,
                              ),
                            ));
                          },
                        ),
                      if (widget.comesFromFriends != true)
                        SizedBox(
                          height: 70,
                        ),
                      // PlatformListTile(
                      //   isElevatedM: true,
                      //   title: Text(
                      //     "Chat exportieren",
                      //     style: Theme.of(context).textTheme.bodyText1,
                      //   ),
                      //   leading: Icon(FontAwesomeIcons.fileExport),
                      //   onTap: () {},
                      // ),
                      if (widget.comesFromFriends != true)
                        PlatformListTile(
                          isElevatedM: true,
                          title: Text(
                            widget.chat.isArchived
                                ? "Chat entarchivieren"
                                : "Chat archivieren",
                            style: Theme.of(context).textTheme.bodyText1,
                          ),
                          leading: widget.chat.isArchived
                              ? Icon(
                                  Icons.unarchive,
                                )
                              : Icon(Icons.archive),
                          onTap: () async {
                            if (widget.chat.isArchived) {
                              await ChatFirebaseService.unarchiveChat(
                                      widget.chat.uid)
                                  .then((value) => ToastService.showLongToast(
                                      "Archivierung wurde aufgehoben"));
                            } else {
                              await ChatFirebaseService.archiveChat(
                                      widget.chat.uid)
                                  .then((value) => ToastService.showLongToast(
                                      "Chat wurde archiviert"));
                            }
                            setState(() {
                              chat.isArchived = !chat.isArchived;
                            });
                          },
                        ),
                      if (widget.comesFromFriends != true)
                        PlatformListTile(
                          isElevatedM: true,
                          title: Text(
                            "Chat leeren",
                            style: TextStyle(color: Colors.red),
                          ),
                          leading: Icon(FontAwesomeIcons.backspace),
                          onTap: () async {
                            await ChatFirebaseService.emptyChat(chat);
                            ToastService.showLongToast("Chat wurde geleert");
                          },
                        ),
                      if (widget.comesFromFriends != true)
                        SizedBox(
                          height: 70,
                        ),
                      if (widget.comesFromFriends != true)
                        PlatformListTile(
                          isElevatedM: true,
                          title: Text(
                            "Chat löschen",
                            style: TextStyle(color: Colors.red),
                          ),
                          leading: Icon(FontAwesomeIcons.trash),
                          onTap: () => showDialog(
                            context: context,
                            builder: (BuildContext context) {
                              return AlertDialog(
                                title: Text(
                                  'Wirklich löschen?',
                                  style: TextStyle(
                                    color: Colors.red,
                                  ),
                                ),
                                content: Column(
                                  mainAxisSize: MainAxisSize.min,
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Text(
                                      'Bist du dir sicher, dass du diesen Chat wirklich löschen möchtest?',
                                    ),
                                  ],
                                ),
                                actions: <Widget>[
                                  TextButton(
                                      child: Text(
                                        'Abbrechen',
                                        style: TextStyle(
                                          color: Colors.blue,
                                        ),
                                      ),
                                      onPressed: () {
                                        // Hier passiert etwas
                                        Navigator.of(context).pop();
                                      }),
                                  TextButton(
                                    child: Text(
                                      'Löschen',
                                      style: TextStyle(
                                        color: Colors.red,
                                      ),
                                    ),
                                    onPressed: () async {
                                      await ChatFirebaseService.deleteChat(chat)
                                          .then((value) {
                                        ToastService.showLongToast(
                                            "Chat wurde gelöscht");
                                        Navigator.of(context).pop();
                                        Navigator.of(context).pop();
                                        Navigator.of(context).pop();
                                      });
                                    },
                                  ),
                                ],
                              );
                            },
                          ),
                        ),
                      StreamBuilder<DocumentSnapshot>(
                        stream: _firestore
                            .collection("user")
                            .doc(_auth.currentUser?.uid)
                            .snapshots(),
                        builder: (context, AsyncSnapshot snapshot) {
                          if (!snapshot.hasData) {
                            return Container();
                          } else {
                            if (snapshot.data["friends"].contains(
                                widget.comesFromFriends == true
                                    ? widget.userUid
                                    : getUidForOtherUser(chat))) {
                              return PlatformListTile(
                                  isElevatedM: true,
                                  title: Text(
                                    "Freund entfernen",
                                    style: TextStyle(color: Colors.red),
                                  ),
                                  leading: Icon(FontAwesomeIcons.userSlash),
                                  onTap: () {
                                    UserFirebaseService.deleteFriend(widget
                                                    .comesFromFriends ==
                                                true
                                            ? widget.userUid ?? ""
                                            : getUidForOtherUser(widget.chat))
                                        .then((value) {
                                      ToastService.showLongToast(
                                          "Freund wurde entfernt");
                                      setState(() {
                                        isFriend = false;
                                      });
                                    });
                                  });
                            } else {
                              return PlatformListTile(
                                isElevatedM: true,
                                title: Text(
                                  "Freund hinzufügen",
                                  style: Theme.of(context).textTheme.bodyText1,
                                ),
                                leading: Icon(FontAwesomeIcons.solidUser),
                                onTap: () => UserFirebaseService.addFriend(
                                        widget.comesFromFriends == true
                                            ? widget.userUid ?? ""
                                            : getUidForOtherUser(widget.chat))
                                    .then((value) {
                                  ToastService.showLongToast(
                                      "Freund wurde hinzugefügt");
                                  setState(() {
                                    isFriend = true;
                                  });
                                }),
                              );
                            }
                          }
                        },
                      ),

                      SizedBox(
                        height: 30,
                      )
                    ],
                  );
                }
              },
            )
          : Container(
              child: ListView(
                children: [
                  Center(
                    child: Padding(
                      padding: const EdgeInsets.all(20.0),
                      child: Container(
                        height: 280,
                        width: 280,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          image: DecorationImage(
                            fit: BoxFit.fill,
                            image: CachedNetworkImageProvider(
                                widget.chat.fotoUrls.first),
                          ),
                        ),
                      ),
                    ),
                  ),
                  Center(
                    child: Text(
                      widget.chat.name,
                      style: TextStyle(
                        fontSize: 32,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  SizedBox(
                    height: 30,
                  ),
                  PlatformListTile(
                    isElevatedM: true,
                    title: Text(
                      widget.chat.description ?? "",
                      style: Theme.of(context).textTheme.bodyText1,
                    ),
                    leading: Icon(FontAwesomeIcons.infoCircle),
                    trailing: Icon(
                      Icons.circle,
                      color: Colors.white.withOpacity(0),
                    ),
                  ),
                  SizedBox(
                    height: 70,
                  ),
                  // PlatformListTile(
                  //   isElevatedM: true,
                  //   title: Text(
                  //     "Fotos & Videos",
                  //     style: Theme.of(context).textTheme.bodyText1,
                  //   ),
                  //   leading: Icon(FontAwesomeIcons.photoVideo),
                  //   onTap: () {},
                  // ),
                  PlatformListTile(
                    isElevatedM: true,
                    title: Text(
                      "Mit Stern markiert",
                      style: Theme.of(context).textTheme.bodyText1,
                    ),
                    leading: Icon(FontAwesomeIcons.solidStar),
                    onTap: () {
                      Navigator.of(context).push(CupertinoPageRoute(
                        builder: (context) => MarkedMessages(
                          isInChat: true,
                          chatUid: widget.chat.uid,
                        ),
                      ));
                    },
                  ),
                  SizedBox(
                    height: 70,
                  ),
                  for (String uid in orderMemberList(chat.members))
                    UserListTile(
                      uid: uid,
                      isAdmin: chat.adminList?.contains(uid) ?? false,
                      chat: widget.chat,
                    ),
                  SizedBox(
                    height: 30,
                  ),
                  if (widget.chat.adminList!
                      .contains(_auth.currentUser?.uid ?? null))
                    PlatformListTile(
                      isElevatedM: true,
                      title: Text(
                        "Nutzer hinzufügen",
                        style: Theme.of(context).textTheme.bodyText1,
                      ),
                      leading: Icon(FontAwesomeIcons.plus),
                      onTap: () => addUserBottomSheet(context, chat),
                    ),
                  SizedBox(
                    height: 30,
                  ),
                  // PlatformListTile(
                  //   isElevatedM: true,
                  //   title: Text(
                  //     "Chat exportieren",
                  //     style: Theme.of(context).textTheme.bodyText1,
                  //   ),
                  //   leading: Icon(FontAwesomeIcons.fileExport),
                  //   onTap: () {},
                  // ),
                  if (widget.comesFromFriends != true)
                    PlatformListTile(
                      isElevatedM: true,
                      title: Text(
                        widget.chat.isArchived
                            ? "Gruppe entarchivieren"
                            : "Gruppe archivieren",
                        style: Theme.of(context).textTheme.bodyText1,
                      ),
                      leading: widget.chat.isArchived
                          ? Icon(
                              Icons.unarchive,
                            )
                          : Icon(Icons.archive),
                      onTap: () async {
                        if (widget.chat.isArchived) {
                          await ChatFirebaseService.unarchiveChat(
                                  widget.chat.uid)
                              .then((value) => ToastService.showLongToast(
                                  "Archivierung wurde aufgehoben"));
                        } else {
                          await ChatFirebaseService.archiveChat(widget.chat.uid)
                              .then((value) => ToastService.showLongToast(
                                  "Gruppe wurde archiviert"));
                        }
                        setState(() {
                          chat.isArchived = !chat.isArchived;
                        });
                      },
                    ),
                  if (widget.comesFromFriends != true)
                    PlatformListTile(
                      isElevatedM: true,
                      title: Text(
                        "Chat leeren",
                        style: TextStyle(color: Colors.red),
                      ),
                      leading: Icon(FontAwesomeIcons.backspace),
                      onTap: () async {
                        await ChatFirebaseService.emptyChat(chat);
                        ToastService.showLongToast("Chat wurde geleert");
                      },
                    ),
                  if (widget.comesFromFriends != true)
                    SizedBox(
                      height: 70,
                    ),
                  if (widget.comesFromFriends != true)
                    PlatformListTile(
                      isElevatedM: true,
                      title: Text(
                        "Gruppe verlassen",
                        style: TextStyle(color: Colors.red),
                      ),
                      leading: Icon(FontAwesomeIcons.trash),
                      onTap: () =>
                          ChatFirebaseService.leaveGroup(chat, context),
                    ),
                ],
              ),
            ),
    );
  }
}
