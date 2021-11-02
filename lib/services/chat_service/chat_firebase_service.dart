import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_dispuatio/models/user_model.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_fcm_service.dart';
import 'package:flutter_dispuatio/services/general_services/toast_service.dart';
import 'package:flutter_dispuatio/services/user_services/GeneralUserService.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:flutter/foundation.dart' show kIsWeb;

class ChatFirebaseService {
  ChatFirebaseService();
  static final _auth = FirebaseAuth.instance;
  static final _firestore = FirebaseFirestore.instance;
  static final _fcm = FirebaseMessaging.instance;

  ///@param doc is the user from firestore
  static Future<void> createChat(var doc) async {
    String url1 =
        await UserFirebaseService.getFotoUrlByUid(_auth.currentUser?.uid ?? "");
    String url2 = await UserFirebaseService.getFotoUrlByUid(doc.id);

    //TODO:get all own fcm ids

    List fcmIdsUids = [];

    List ownFcmIds =
        await UserFirebaseService.getFcmIds(_auth.currentUser?.uid ?? "");

    String ownUid = _auth.currentUser?.uid ?? "";

    for (String fcmId in ownFcmIds) {
      String ownFcmIdUid = fcmId + "|" + ownUid;
      fcmIdsUids.add(ownFcmIdUid);
    }

    List otherUserFcmIds = doc["fcmids"];

    print("otherUserFcmIds " + otherUserFcmIds.toString());

    for (var fcmId in otherUserFcmIds) {
      fcmIdsUids.add((fcmId + "|" + doc.id));
    }

    DocumentReference docRef = await _firestore.collection("chat").add({
      "isgroup": false,
      "lastmessagedate": [for (int i = 0; i < 2; i++) DateTime.now()],
      "lastmessagesenderid": [for (int i = 0; i < 2; i++) ""],
      "lastmessagesendername": [for (int i = 0; i < 2; i++) ""],
      "lastmessagetext": [for (int i = 0; i < 2; i++) ""],
      "members": [
        (doc.id + "|" + doc["name"]),
        ((_auth.currentUser?.uid ?? "") +
            "|" +
            (_auth.currentUser?.displayName ?? ""))
      ],
      "archivedby": [],
      "notarchivedby": [
        _auth.currentUser?.uid,
        doc.id,
      ],
      "pinnedby": [],
      "name": "${doc["name"]} + ${_auth.currentUser?.displayName}",
      "writing": [],
      "fotourls": [url1, url2],
      "fcmids": fcmIdsUids,
    });

    await _firestore.collection("user").doc(_auth.currentUser?.uid).update({
      "hasChatWith": FieldValue.arrayUnion([doc.id]),
    });
    await _firestore.collection("user").doc(doc.id).update({
      "hasChatWith": FieldValue.arrayUnion([_auth.currentUser?.uid]),
    });
    ChatFcmService.subscribeToChat(chatUid: docRef.id);
  }

  static Map<dynamic, dynamic> getMemberMap(List<UserModel> addedUsers) {
    Map<dynamic, dynamic> users = {};
    for (UserModel user in addedUsers) {
      users["name"] = user.name;
      users["photourl"] = user.fotoUrl;
    }
    return users;
  }

  // static List<String> getNotArchivedBy(List<UserModel> addedUsers) {
  //   List<String> names = [];
  //   for (UserModel user in addedUsers) {
  //     names.add(user.uid);
  //   }
  //   names.add(_auth.currentUser?.uid ?? "");
  //   return names;
  // }

  static List<String> getMemberUids(List<UserModel> addedUsers) {
    List<String> uid = [];
    for (UserModel user in addedUsers) {
      uid.add(user.uid);
    }
    uid.add(_auth.currentUser?.uid ?? "");
    return uid;
  }

  static Future<void> createGroup({
    required List<UserModel> addedUsers,
    required String fotoUrl,
    required String name,
    String? info,
  }) async {
    print("addedUsers: " + addedUsers.toString());

    int memberCount = addedUsers.length + 1;

    //TODO:get all own fcm ids

    List<String> fcmIdsUids = [];

    List ownFcmIds =
        await UserFirebaseService.getFcmIds(_auth.currentUser?.uid ?? "");

    String ownUid = _auth.currentUser?.uid ?? "";

    for (String fcmId in ownFcmIds) {
      String ownFcmIdUid = fcmId + "|" + ownUid;
      fcmIdsUids.add(ownFcmIdUid);
    }

    for (UserModel user in addedUsers) {
      List fcmIds = user.fcmIds ?? [];
      for (String fcmId in fcmIds) {
        fcmIdsUids.add((fcmId + "|" + user.uid));
      }
    }

    DocumentReference docRef = await _firestore.collection("chat").add({
      "isgroup": true,
      "lastmessagedate": [for (int i = 0; i < memberCount; i++) DateTime.now()],
      "lastmessagesenderid": [for (int i = 0; i < memberCount; i++) ""],
      "lastmessagesendername": [for (int i = 0; i < memberCount; i++) ""],
      "lastmessagetext": [for (int i = 0; i < memberCount; i++) ""],
      "members": getMemberUids(addedUsers),
      "archivedby": [],
      "notarchivedby": getMemberUids(addedUsers),
      "pinnedby": [],
      "name": name,
      "info": info ?? "",
      "writing": [],
      "fotourls": [(fotoUrl)],
      "groupinfo": info ?? "Das ist die Gruppenbeschreibung",
      "adminList": [_auth.currentUser?.uid],
      "fcmids": fcmIdsUids,
    });
    if (!kIsWeb) {
      ChatFcmService.subscribeToChat(chatUid: docRef.id);
    }
  }

  static Future<void> leaveGroup(ChatModel chat, dynamic context) async {
    if (chat.members.length == 1) {
      await _firestore.collection("chat").doc(chat.uid).delete();
      ToastService.showLongToast("Gruppe wurde verlassen");
      Navigator.of(context).pop();
      Navigator.of(context).pop();
    } else if (chat.adminList!.length == 1 &&
        chat.adminList!.contains(_auth.currentUser?.uid ?? "")) {
      ToastService.showLongToast(
          "Du bist der einzige Admin und kannst die Gruppe erst verlassen, wenn du einen anderen Nutzer zum Admin ernannt hast");
    } else {
      await _firestore.collection("chat").doc(chat.uid).update({
        "members": FieldValue.arrayRemove(
          [_auth.currentUser?.uid],
        )
      });

      await _firestore.collection("chat").doc(chat.uid).update({
        "notarchivedby": FieldValue.arrayRemove(
          [_auth.currentUser?.uid],
        )
      });
      await _firestore.collection("chat").doc(chat.uid).update({
        "pinnedby": FieldValue.arrayRemove(
          [_auth.currentUser?.uid],
        )
      });
      await _firestore.collection("chat").doc(chat.uid).update({
        "writing": FieldValue.arrayRemove(
          [_auth.currentUser?.uid],
        )
      });

      var snapshots = await _firestore
          .collection("chat")
          .doc(chat.uid)
          .collection("messages")
          .get();

      for (var doc in snapshots.docs) {
        await doc.reference.update({
          'canbeseenby': FieldValue.arrayRemove([_auth.currentUser?.uid ?? ""]),
        });
      }
      ToastService.showLongToast("Gruppe wurde verlassen");
      Navigator.of(context).pop();
      Navigator.of(context).pop();
    }
  }

  static Future<void> addToGroup({
    required String uid,
    required ChatModel chat,
  }) async {
    var docSnapshot = await _firestore.collection("chat").doc(chat.uid).get();

    if (docSnapshot.exists) {
      Map<String, dynamic>? data = docSnapshot.data();
      var lastMessageTexts = data?["lastmessagetext"];
      var lastmessagesenderids = data?["lastmessagesenderid"];
      var lastmessagesendernames = data?["lastmessagesendername"];
      var lastmessagedates = data?["lastmessagedate"];
      var members = data?["members"];
      var notarchivedby = data?["notarchivedby"];

      lastmessagedates.add(DateTime.now());
      lastMessageTexts.add("");
      lastmessagesenderids.add("");
      lastmessagesendernames.add("");
      members.add(uid);
      notarchivedby.add(uid);

      await _firestore.collection("chat").doc(chat.uid).update({
        "lastmessagetext": lastMessageTexts,
        "lastmessagesenderid": lastmessagesenderids,
        "lastmessagedate": lastmessagedates,
        "lastmessagesendername": lastmessagesendernames,
        "members": members,
        "notarchivedby": notarchivedby,
      });
    }
  }

  static Future<void> kickOutOfGroup({
    required String uid,
    required ChatModel chat,
  }) async {
    int index = chat.members.indexOf(uid);

    var docSnapshot = await _firestore.collection("chat").doc(chat.uid).get();

    if (docSnapshot.exists) {
      Map<String, dynamic>? data = docSnapshot.data();
      var lastMessageTexts = data?["lastmessagetext"];
      var lastmessagesenderids = data?["lastmessagesenderid"];
      var lastmessagesendernames = data?["lastmessagesendername"];
      var lastmessagedates = data?["lastmessagedate"];
      var members = data?["members"];
      var notarchivedby = data?["notarchivedby"];
      var adminList = data?["adminList"];

      lastmessagedates.removeAt(index);
      lastMessageTexts.removeAt(index);
      lastmessagesenderids.removeAt(index);
      lastmessagesendernames.removeAt(index);
      members.remove(uid);
      notarchivedby.remove(uid);
      adminList.remove(uid);

      await _firestore.collection("chat").doc(chat.uid).update({
        "lastmessagetext": lastMessageTexts,
        "lastmessagesenderid": lastmessagesenderids,
        "lastmessagedate": lastmessagedates,
        "lastmessagesendername": lastmessagesendernames,
        "members": members,
        "notarchivedby": notarchivedby,
        "adminList": adminList,
      });
    }

    var snapshots = await _firestore
        .collection("chat")
        .doc(chat.uid)
        .collection("messages")
        .get();

    for (var doc in snapshots.docs) {
      await doc.reference.update({
        'canbeseenby': FieldValue.arrayRemove([uid]),
      });
    }
    // var docSnapshot = await _firestore.collection("chat").doc(chatUid).get();
    //
    // if (docSnapshot.exists) {
    //   Map<String, dynamic>? data = docSnapshot.data();
    //   var lastMessageTexts = data?["lastmessagetext"];
    //   var lastmessagesenderids = data?["lastmessagesenderid"];
    //   var lastmessagesendernames = data?["lastmessagesendername"];
    //   var lastmessagedates = data?["lastmessagedate"];
    //
    //   lastMessageTexts[ownUidPos] = "";
    //   lastmessagedates[ownUidPos] = Timestamp.now();
    //   lastmessagesenderids[ownUidPos] = "";
    //   lastmessagesendernames[ownUidPos] = "";
    //
    //   await _firestore.collection("chat").doc(chatUid).update({
    //     "lastmessagetext": lastMessageTexts,
    //     "lastmessagesenderid": lastmessagesenderids,
    //     "lastmessagedate": lastmessagedates,
    //     "lastmessagesendername": lastmessagesendernames
    //   });
    // }
  }

  static Future<void> makeAdmin(
      {required String userUid, required String chatUid}) async {
    await _firestore.collection("chat").doc(chatUid).update({
      "adminList": FieldValue.arrayUnion([userUid]),
    });
  }

  static Future<void> removeAdmin(
      {required String userUid, required String chatUid}) async {
    await _firestore.collection("chat").doc(chatUid).update({
      "adminList": FieldValue.arrayRemove([userUid]),
    });
  }

  static Future<void> archiveChat(String chatUid) async {
    await _firestore.collection("chat").doc(chatUid).update({
      "archivedby": FieldValue.arrayUnion(
        [_auth.currentUser?.uid],
      )
    });
    await _firestore.collection("chat").doc(chatUid).update({
      "notarchivedby": FieldValue.arrayRemove(
        [_auth.currentUser?.uid],
      )
    });
  }

  static Future<void> unarchiveChat(String chatUid) async {
    await _firestore.collection("chat").doc(chatUid).update({
      "archivedby": FieldValue.arrayRemove(
        [_auth.currentUser?.uid],
      )
    });
    await _firestore.collection("chat").doc(chatUid).update({
      "notarchivedby": FieldValue.arrayUnion(
        [_auth.currentUser?.uid],
      )
    });
  }

  static Future<void> pinChat(String chatUid) async {
    await _firestore.collection("chat").doc(chatUid).update({
      "pinnedby": FieldValue.arrayUnion(
        [_auth.currentUser?.uid],
      )
    });
  }

  static Future<void> unpinChat(String chatUid) async {
    await _firestore.collection("chat").doc(chatUid).update({
      "pinnedby": FieldValue.arrayRemove(
        [_auth.currentUser?.uid],
      )
    });
  }

  static Future<void> deleteChat(ChatModel chat) async {
    await _firestore.collection("chat").doc(chat.uid).delete();

    /*
    static Future<void> archiveChat(String chatUid) async {
    await _firestore.collection("chat").doc(chatUid).update({
      "archivedby": FieldValue.arrayUnion(
        [_auth.currentUser?.uid],
      )
    });
    await _firestore.collection("chat").doc(chatUid).update({
      "notarchivedby": FieldValue.arrayUnion(
        [_auth.currentUser?.uid],
      )
    });
     */

    await _firestore
        .collection("user")
        .doc(getUserUidFromMemberEntry(chat.members[0]))
        .update(
      {
        "hasChatWith": FieldValue.arrayRemove(
            [getUserUidFromMemberEntry(chat.members[1])]),
      },
    );
    await _firestore
        .collection("user")
        .doc(getUserUidFromMemberEntry(chat.members[1]))
        .update(
      {
        "hasChatWith": FieldValue.arrayRemove(
            [getUserUidFromMemberEntry(chat.members[0])]),
      },
    );

    // for (String userUid in chat.members) {
    //   for (String removingUserUid in chat.members) {
    //     await _firestore.collection("user").doc(userUid).update({
    //       "hasChatWith": FieldValue.arrayRemove([removingUserUid]),
    //     });
    //   }
    // }
  }

  static String getUserUidFromMemberEntry(String entry) {
    return entry.split("|").first;
  }

  static Future<void> emptyChat(ChatModel chat) async {
    int ownUidPos = -1;
    if (chat.isGroup) {
      ownUidPos = GeneralUserService.getOwnUidPosInGroupFromList(chat.members);
    } else {
      ownUidPos = GeneralUserService.getOwnUidPosInChatFromChatModel(chat);
    }

    var docSnapshot = await _firestore.collection("chat").doc(chat.uid).get();

    if (docSnapshot.exists) {
      Map<String, dynamic>? data = docSnapshot.data();
      var lastMessageTexts = data?["lastmessagetext"];
      var lastmessagesenderids = data?["lastmessagesenderid"];
      var lastmessagesendernames = data?["lastmessagesendername"];
      var lastmessagedates = data?["lastmessagedate"];

      lastMessageTexts[ownUidPos] = "";
      lastmessagedates[ownUidPos] = Timestamp.now();
      lastmessagesenderids[ownUidPos] = "";
      lastmessagesendernames[ownUidPos] = "";

      await _firestore.collection("chat").doc(chat.uid).update({
        "lastmessagetext": lastMessageTexts,
        "lastmessagesenderid": lastmessagesenderids,
        "lastmessagedate": lastmessagedates,
        "lastmessagesendername": lastmessagesendernames
      });
    }

    await _firestore
        .collection("chat")
        .doc(chat.uid)
        .collection("messages")
        .get()
        .then((snapshot) => {
              for (DocumentSnapshot ds in snapshot.docs)
                {
                  ds.reference.update(
                    ({
                      "canbeseenby":
                          FieldValue.arrayRemove([_auth.currentUser?.uid]),
                    }),
                  ),
                }
            });
  }

  static Future<void> sendMessage({
    required MessageModel ansMsg,
    required String text,
    required String chatUid,
    required List<String> usersUid,
    required int memberCount,
  }) async {
    if (ansMsg.uid == "n.a.") {
      await _firestore
          .collection("chat")
          .doc(chatUid)
          .collection("messages")
          .add({
        "text": text,
        "time": DateTime.now(),
        "sender": _auth.currentUser?.displayName,
        "senderid": _auth.currentUser?.uid,
        "readby": [_auth.currentUser?.uid],
        "favby": [],
        "isimage": false,
        "canbeseenby": usersUid,
      });
    } else {
      //set answermessage
      await _firestore
          .collection("chat")
          .doc(chatUid)
          .collection("messages")
          .add({
        "text": text,
        "time": DateTime.now(),
        "sender": _auth.currentUser?.displayName,
        "senderid": _auth.currentUser?.uid,
        "readby": [_auth.currentUser?.uid],
        "favby": [],
        "answersender": ansMsg.sender,
        "answertext": ansMsg.text,
        "isimage": false,
        "canbeseenby": usersUid,
      });
    }
  }

  static Future<void> deleteMessage({
    required ChatModel chat,
    required msgUid,
  }) async {
    var snapshots = await _firestore
        .collection("chat")
        .doc(chat.uid)
        .collection("messages")
        .where("canbeseenby", arrayContainsAny: [_auth.currentUser?.uid])
        .orderBy("time")
        .get();

    print("delete length " + snapshots.size.toString());

    var documents = snapshots.docs;

    final int ownUidPos = chat.isGroup
        ? GeneralUserService.getOwnUidPosInGroupFromList(chat.members)
        : GeneralUserService.getOwnUidPosInChatFromChatModel(chat);

    //check works
    if (documents[documents.length - 1].id == msgUid) {
      //checks if newest message
      print("the message to delete is the newest msg in chat");
      if (documents.length == 1) {
        print("the message to delelte is the only message in chat");
        //wenn nur eine Nachricht im Chat ist
        // await _firestore.collection("chat").doc(chatUid).update({
        //   "lastmessagetext": [],
        //   "lastmessagesenderid": [],
        //   "lastmessagedate": [],
        // });

        var docSnapshot =
            await _firestore.collection("chat").doc(chat.uid).get();

        if (docSnapshot.exists) {
          Map<String, dynamic>? data = docSnapshot.data();
          var lastMessageTexts = data?["lastmessagetext"];
          var lastmessagesenderids = data?["lastmessagesenderid"];
          var lastmessagesendernames = data?["lastmessagesendername"];
          var lastmessagedates = data?["lastmessagedate"];

          lastMessageTexts[ownUidPos] = "";
          lastmessagedates[ownUidPos] = Timestamp.now();
          lastmessagesenderids[ownUidPos] = "";
          lastmessagesendernames[ownUidPos] = "";

          await _firestore.collection("chat").doc(chat.uid).update({
            "lastmessagetext": lastMessageTexts,
            "lastmessagesenderid": lastmessagesenderids,
            "lastmessagedate": lastmessagedates,
            "lastmessagesendername": lastmessagesendernames
          });
        }
      } else {
        //replace msg text
        print("the message to delete is not only one in chat");
        String lastMessageText = documents[documents.length - 2]["text"];

        String lastMessageSenderId =
            documents[documents.length - 2]["senderid"];

        Timestamp lastMessageDate = documents[documents.length - 2]["time"];

        String lastMessageSenderName =
            documents[documents.length - 2]["sender"];
        //get the chat

        var docSnapshot =
            await _firestore.collection("chat").doc(chat.uid).get();

        if (docSnapshot.exists) {
          Map<String, dynamic>? data = docSnapshot.data();
          var lastMessageTexts = data?["lastmessagetext"];
          var lastmessagesenderids = data?["lastmessagesenderid"];
          var lastmessagesendernames = data?["lastmessagesendername"];
          var lastmessagedates = data?["lastmessagedate"];

          lastMessageTexts[ownUidPos] = lastMessageText;
          lastmessagedates[ownUidPos] = lastMessageDate;
          lastmessagesenderids[ownUidPos] = lastMessageSenderId;
          lastmessagesendernames[ownUidPos] = lastMessageSenderName;

          await _firestore.collection("chat").doc(chat.uid).update({
            "lastmessagetext": lastMessageTexts,
            "lastmessagesenderid": lastmessagesenderids,
            "lastmessagedate": lastmessagedates,
            "lastmessagesendername": lastmessagesendernames
          });
        }
      }
    } else {
      print("the message to delete is not the newest one and not the only one");
    }
    // _firestore.doc("chat/$chatUid/messages/${msg.uid}").update({
    //   "favby": FieldValue.arrayUnion([_auth.currentUser?.uid])
    // });
    print("before remove");
    await _firestore
        .collection("chat")
        .doc(chat.uid)
        .collection("messages")
        .doc(msgUid)
        .update(({
          "canbeseenby": FieldValue.arrayRemove([_auth.currentUser?.uid]),
        }));
    print("after remove");
  }

  static Future<void> sendImage(
      {required String chatUid,
      required String url,
      required List<String> usersUid,
      required memberCount}) async {
    await _firestore
        .collection("chat")
        .doc(chatUid)
        .collection("messages")
        .add({
      "text": "Foto",
      "time": DateTime.now(),
      "sender": _auth.currentUser?.displayName,
      "senderid": _auth.currentUser?.uid,
      "readby": [_auth.currentUser?.uid],
      "favby": [],
      "isimage": true,
      "url": url,
      "canbeseenby": usersUid,
    });
  }

  static Future<void> updateChatAfterSend({
    required String chatUid,
    required String text,
    required int memberCount,
  }) async {
    await _firestore.doc("chat/$chatUid").update({
      "lastmessagedate": [for (int i = 0; i < memberCount; i++) DateTime.now()],
      "lastmessagesenderid": [
        for (int i = 0; i < memberCount; i++) _auth.currentUser?.uid
      ],
      "lastmessagesendername": [
        for (int i = 0; i < memberCount; i++) _auth.currentUser?.displayName
      ],
      "lastmessagetext": [for (int i = 0; i < memberCount; i++) text],
    });
  }

  static Future<void> readMessages({required String chatUid}) async {
    var snapshots = _firestore
        .collection("chat")
        .doc(chatUid)
        .collection("messages")
        .snapshots();

    await for (var snapshot in snapshots) {
      List<DocumentSnapshot> docs = snapshot.docs;

      for (var doc in docs) {
        await doc.reference.update({
          "readby": FieldValue.arrayUnion([_auth.currentUser?.uid]),
        });
      }
      break;
    }
  }

  static Future<void> markMessage({
    required String chatUid,
    required MessageModel msg,
  }) async {
    _firestore.doc("chat/$chatUid/messages/${msg.uid}").update({
      "favby": FieldValue.arrayUnion([_auth.currentUser?.uid])
    });
    _firestore
        .collection("user")
        .doc(_auth.currentUser?.uid)
        .collection("markedmessages")
        .doc(msg.uid)
        .set({
      "sender": msg.sender,
      "text": msg.text,
      "time": msg.date,
      "chatuid": "$chatUid",
    });
  }

  static Future<void> unmarkMessage(
      {required String chatUid, required MessageModel msg}) async {
    print("unmark in service");
    print(msg.uid);

    _firestore.doc("chat/$chatUid/messages/${msg.uid}").update({
      "favby": FieldValue.arrayRemove([_auth.currentUser?.uid])
    });
    _firestore
        .collection("user")
        .doc(_auth.currentUser?.uid)
        .collection("markedmessages")
        .doc(msg.uid)
        .delete();
  }

  static Future<void> unmarkMessageFromFavPage({required msg}) async {
    _firestore
        .collection("user")
        .doc(_auth.currentUser?.uid)
        .collection("markedmessages")
        .doc(msg.uid)
        .get()
        .then((snapshot) {
      print(snapshot.get("chatuid"));
      String chatUid = snapshot.get("chatuid");
      _firestore.doc("chat/$chatUid/messages/${msg.uid}").update({
        "favby": FieldValue.arrayRemove([_auth.currentUser?.uid])
      });
      _firestore
          .collection("user")
          .doc(_auth.currentUser?.uid)
          .collection("markedmessages")
          .doc(msg.uid)
          .delete();
    });
  }

  static Future<void> addFcmIdToAllChats(String fcmId) async {
    List test = [
      (_auth.currentUser?.uid ?? "").toString(),
      ((_auth.currentUser?.uid ?? "FEHLER") +
              "|" +
              (_auth.currentUser?.displayName ?? "FEHLER"))
          .toString()
    ];

    print("QUERY-LIST: " + test.toString());
    print("UID " + (_auth.currentUser?.uid ?? ""));

    var snapshots = _firestore
        .collection("chat")
        .where("members", arrayContainsAny: test)
        .snapshots();

    await for (var snapshot in snapshots) {
      List<DocumentSnapshot> docs = snapshot.docs;
      print("DOC-LENGTH " + docs.length.toString());
      for (var doc in docs) {
        await doc.reference.update({
          "fcmids": FieldValue.arrayUnion(
              [(fcmId) + "|" + (_auth.currentUser?.uid ?? "FEHLER")])
        });
      }
    }
  }

  static Future<void> removeFcmIdFromAllChats(String fcmId) async {
    var snapshots =
        _firestore.collection("user").where("members", arrayContainsAny: [
      (_auth.currentUser?.uid),
      ((_auth.currentUser?.uid ?? "FEHLER") +
          "|" +
          (_auth.currentUser?.displayName ?? "FEHLER"))
    ]).snapshots();

    await for (var snapshot in snapshots) {
      List<DocumentSnapshot> docs = snapshot.docs;

      for (var doc in docs) {
        await doc.reference.update({
          "fcmids": FieldValue.arrayRemove(
              [(fcmId) + "|" + (_auth.currentUser?.uid ?? "FEHLER")])
        });
      }
    }
  }
}
