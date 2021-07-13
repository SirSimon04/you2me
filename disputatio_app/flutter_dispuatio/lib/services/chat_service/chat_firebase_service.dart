import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/models/message_model.dart';

class ChatFirebaseService {
  ChatFirebaseService();
  static final _auth = FirebaseAuth.instance;
  static final _firestore = FirebaseFirestore.instance;

  ///@param doc is the user from firestore
  static Future<void> createChat(var doc) async {
    await _firestore.collection("chat").add({
      "isgroup": false,
      "lastmessagedate": DateTime.now(),
      "lastmessagesenderid": "",
      "lastmessagesendername": "",
      "lastmessagetext": "",
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
    });

    await _firestore.collection("user").doc(_auth.currentUser?.uid).update({
      "hasChatWith": FieldValue.arrayUnion([doc.id]),
    });
    await _firestore.collection("user").doc(doc.id).update({
      "hasChatWith": FieldValue.arrayUnion([_auth.currentUser?.uid]),
    });
  }

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

  static Future<void> emptyChat(String chatUid) async {
    await _firestore
        .collection("chat")
        .doc(chatUid)
        .collection("messages")
        .get()
        .then((snapshot) => {
              for (DocumentSnapshot ds in snapshot.docs) {ds.reference.delete()}
            });
  }

  static Future<void> sendMessage(
      {required MessageModel ansMsg,
      required String text,
      required String chatUid}) async {
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
      });
    }
  }

  static Future<void> deleteMessage({required chatUid, required msgUid}) async {
    _firestore
        .collection("chat")
        .doc(chatUid)
        .collection("messages")
        .doc(msgUid)
        .delete();
  }

  static Future<void> sendImage(
      {required String chatUid, required String url}) async {
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
    });
  }

  static Future<void> updateChatAfterSend(
      {required String chatUid, required String text}) async {
    await _firestore.doc("chat/$chatUid").update({
      "lastmessagedate": DateTime.now(),
      "lastmessagesenderid": _auth.currentUser?.uid,
      "lastmessagesendername": _auth.currentUser?.displayName,
      "lastmessagetext": text,
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

  static Future<void> markMessage(
      {required String chatUid, required MessageModel msg}) async {
    _firestore.doc("chat/${chatUid}/messages/${msg.uid}").update({
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
