import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_dispuatio/models/user_model.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_fcm_service.dart';
import 'package:flutter_dispuatio/services/user_services/GeneralUserService.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:flutter/foundation.dart' show kIsWeb;

class ChatFirebaseService {
  ChatFirebaseService();
  static final _auth = FirebaseAuth.instance;
  static final _firestore = FirebaseFirestore.instance;

  ///@param doc is the user from firestore
  static Future<void> createChat(var doc) async {
    String url1 =
        await UserFirebaseService.getFotoUrlbyUid(_auth.currentUser?.uid ?? "");
    String url2 = await UserFirebaseService.getFotoUrlbyUid(doc.id);
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
    int memberCount = addedUsers.length + 1;
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
    });
    if (!kIsWeb) {
      ChatFcmService.subscribeToChat(chatUid: docRef.id);
    }
  }

  static Future<void> leaveGroup(ChatModel chat) async {
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
    print("index " + index.toString());

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

  static Future<void> emptyChat(String chatUid, List members) async {
    final int ownUidPos =
        GeneralUserService.getOwnUidPosInGroupFromList(members);

    var docSnapshot = await _firestore.collection("chat").doc(chatUid).get();

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

      await _firestore.collection("chat").doc(chatUid).update({
        "lastmessagetext": lastMessageTexts,
        "lastmessagesenderid": lastmessagesenderids,
        "lastmessagedate": lastmessagedates,
        "lastmessagesendername": lastmessagesendernames
      });
    }

    await _firestore
        .collection("chat")
        .doc(chatUid)
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
    required chatUid,
    required msgUid,
    required List members,
  }) async {
    var snapshots = await _firestore
        .collection("chat")
        .doc(chatUid)
        .collection("messages")
        .where("canbeseenby", arrayContainsAny: [_auth.currentUser?.uid])
        .orderBy("time")
        .get();

    print("delete length " + snapshots.size.toString());

    var documents = snapshots.docs;

    final int ownUidPos =
        GeneralUserService.getOwnUidPosInGroupFromList(members);

    //check works
    if (documents[documents.length - 1].id == msgUid) {
      //checks if newest message
      print("this is after first if");
      if (documents.length == 1) {
        print("this is after second  if");
        //wenn nur eine Nachricht im Chat ist
        // await _firestore.collection("chat").doc(chatUid).update({
        //   "lastmessagetext": [],
        //   "lastmessagesenderid": [],
        //   "lastmessagedate": [],
        // });

        var docSnapshot =
            await _firestore.collection("chat").doc(chatUid).get();

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

          await _firestore.collection("chat").doc(chatUid).update({
            "lastmessagetext": lastMessageTexts,
            "lastmessagesenderid": lastmessagesenderids,
            "lastmessagedate": lastmessagedates,
            "lastmessagesendername": lastmessagesendernames
          });
        }
      } else {
        //replace msg text
        String lastMessageText = documents[documents.length - 2]["text"];

        String lastMessageSenderId =
            documents[documents.length - 2]["senderid"];

        Timestamp lastMessageDate = documents[documents.length - 2]["time"];

        String lastMessageSenderName =
            documents[documents.length - 2]["sender"];
        //get the chat

        var docSnapshot =
            await _firestore.collection("chat").doc(chatUid).get();

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

          await _firestore.collection("chat").doc(chatUid).update({
            "lastmessagetext": lastMessageTexts,
            "lastmessagesenderid": lastmessagesenderids,
            "lastmessagedate": lastmessagedates,
            "lastmessagesendername": lastmessagesendernames
          });
        }
      }
    } else {
      print("nicht letzte");
    }
    // _firestore.doc("chat/$chatUid/messages/${msg.uid}").update({
    //   "favby": FieldValue.arrayUnion([_auth.currentUser?.uid])
    // });
    _firestore
        .collection("chat")
        .doc(chatUid)
        .collection("messages")
        .doc(msgUid)
        .update(({
          "canbeseenby": FieldValue.arrayRemove([_auth.currentUser?.uid]),
        }));
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
}
