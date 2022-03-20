import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'GeneralUserService.dart';

class UserFirebaseService {
  UserFirebaseService();
  static final _auth = FirebaseAuth.instance;
  static final _firestore = FirebaseFirestore.instance;
  static final _fcm = FirebaseMessaging.instance;

  static Future<void> login({
    required String email,
    required String password,
  }) async {
    try {
      await _auth.signInWithEmailAndPassword(email: email, password: password);
      SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setString("email", "mail");

      String? fcmId = await _fcm.getToken();

      if (fcmId != null) {
        print("FCMID-NOT-NULL " + fcmId);
        addFcmId(fcmId);
        ChatFirebaseService.addFcmIdToAllChats(fcmId);
      }
    } on FirebaseAuthException catch (e) {
      String errorMessage = "";
      print(e.code);
      switch (e.code) {
        case "invalid-email":
          errorMessage = "Die Email-Adresse hat das falsche Format.";
          break;
        case "wrong-password":
          errorMessage = "Das Passwort ist falsch.";
          break;
        case "user-not-found":
          errorMessage = "Diese Email-Adresse existiert nicht.";
          break;
        case "user-disabled":
          errorMessage = "Der Nutzer mit dieser Email ist deaktiviert.";
          break;
        case "too-many-requests":
          errorMessage = "Zu viele Anfragen. Versuche es später nochmal";
          break;
        default:
          errorMessage = "Ein unbestimmter Fehler ist aufgetreten.";
      }
      throw errorMessage;
    } on Exception catch (e) {
      throw "Ein unbestimmter Fehler ist aufgetreten";
    }
  }

  static Future<void> loginForDelete({
    required String email,
    required String password,
  }) async {
    try {
      await _auth.signInWithEmailAndPassword(email: email, password: password);
    } on FirebaseAuthException catch (e) {
      String errorMessage = "";
      print(e.code);
      switch (e.code) {
        case "invalid-email":
          errorMessage = "Die Email-Adresse hat das falsche Format.";
          break;
        case "wrong-password":
          errorMessage = "Das Passwort ist falsch.";
          break;
        case "user-not-found":
          errorMessage = "Diese Email-Adresse existiert nicht.";
          break;
        case "user-disabled":
          errorMessage = "Der Nutzer mit dieser Email ist deaktiviert.";
          break;
        case "too-many-requests":
          errorMessage = "Zu viele Anfragen. Versuche es später nochmal";
          break;
        default:
          errorMessage = "Ein unbestimmter Fehler ist aufgetreten.";
      }
      throw errorMessage;
    }
  }

  static Future<void> logout() async {
    print("logged out called");

    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.remove("email");

    String? fcmId = await _fcm.getToken();

    if (fcmId != null) {
      print("fcm id is not null");
      await removeFcmId(fcmId);
      print("removed fcm id own");
      await ChatFirebaseService.removeFcmIdFromAllChats(fcmId);
      print("removed fcm id all");
    }
    await _auth.signOut();
  }

  static Future<void> setOnline() async {
    await _firestore.collection("user").doc(_auth.currentUser?.uid).update({
      "isonline": true,
    });
  }

  static Future<void> setWritingTrue(String chatUid) async {
    await _firestore.collection("chat").doc(chatUid).update({
      "writing": FieldValue.arrayUnion([_auth.currentUser?.uid]),
    });
  }

  static Future<void> setWritingFalse(String chatUid) async {
    await _firestore.collection("chat").doc(chatUid).update({
      "writing": FieldValue.arrayRemove([_auth.currentUser?.uid]),
    });
  }

  static Future<void> setOffline() async {
    await _firestore.collection("user").doc(_auth.currentUser?.uid).update({
      "isonline": false,
      "lastonline": Timestamp.now(),
    });
  }

  static Future<void> register(
      {required String email,
      required String password,
      required String username}) async {
    print("register");
    try {
      await FirebaseAuth.instance.createUserWithEmailAndPassword(
        email: email,
        password: password,
      );
    } on FirebaseAuthException catch (e) {
      String errorMessage = "";
      print(e.code);
      switch (e.code) {
        case "invalid-email":
          errorMessage = "Die Email-Adresse hat das falsche Format.";
          break;
        case "weak-password":
          errorMessage = "Das eingegebene Passwort ist zu schwach";
          break;
        case "email-already-in-use":
          errorMessage = "Diese E-Mail wird bereits verwendet";
          break;
        case "too-many-requests":
          errorMessage = "Zu viele Anfragen. Versuche es später nochmal";
          break;
        default:
          errorMessage = "Ein unbestimmter Fehler ist aufgetreten.";
      }
      throw errorMessage;
    }
    print("created firebaseuser");
    await _auth.currentUser?.updatePhotoURL(
        "https://firebasestorage.googleapis.com/v0/b/disputatio-a1039.appspot.com/o/user.png?alt=media&token=46927ec9-a8d4-431a-9fc1-60cbef1e4f2a");
    await _auth.currentUser?.updateDisplayName(username);

    print("updated firebase info");
    //old Auth version
    // await _auth.currentUser?.updateProfile(
    //   displayName: username,
    //   photoURL:
    //       "https://firebasestorage.googleapis.com/v0/b/disputatio-a1039.appspot.com/o/user.png?alt=media&token=46927ec9-a8d4-431a-9fc1-60cbef1e4f2a",
    // );

    List<String> splitList = username.split(" ");

    List<String> indexList = [];

    for (int i = 0; i < splitList.length; i++) {
      for (int j = 1; j < splitList[i].length + 1; j++) {
        indexList.add(splitList[i].substring(0, j).toLowerCase());
      }
    }
    print("indexlist " + indexList.toString());
    String fcmId = "";
    try {
      fcmId = await _fcm.getToken() ?? "";
    } catch (e) {}

    await _firestore.collection("user").doc(_auth.currentUser?.uid ?? "").set({
      "name": _auth.currentUser?.displayName,
      "friends": [],
      "isonline": true,
      "info": "Hey there! I'm using you2me",
      "searchIndex": indexList,
      "lastonline": Timestamp.now(),
      "hasChatWith": [],
      "fotourl":
          "https://firebasestorage.googleapis.com/v0/b/disputatio-a1039.appspot.com/o/user.png?alt=media&token=46927ec9-a8d4-431a-9fc1-60cbef1e4f2a",
      "fcmids": [fcmId],
    });
    print("added to firebase");
  }

  static Future<void> resetPassword(String mail) async {
    print(mail);
    try {
      await _auth.sendPasswordResetEmail(email: mail);
    } on FirebaseAuthException catch (e) {
      String errorMessage = "";
      print(e.code);
      switch (e.code) {
        case "invalid-email":
          errorMessage = "Die eingegebe E-Mail ist ungültig.";
          break;
        case "user-not-found":
          errorMessage =
              "Der Nutzer mit der eingebenen E-Mail wurde nicht gefunden.";
          break;
        default:
          errorMessage = "Ein unbestimmer Fehler ist aufgetreten.";
      }
      throw errorMessage;
    }
  }

  static Future<void> addFriend(String userUid) async {
    await _firestore.collection("user").doc(userUid).update({
      "friends": FieldValue.arrayUnion([_auth.currentUser?.uid]),
    });

    _firestore.collection("user").doc(_auth.currentUser?.uid).update({
      "friends": FieldValue.arrayUnion([userUid]),
    });
  }

  static Future<void> deleteFriend(String userUid) async {
    await _firestore.collection("user").doc(userUid).update({
      "friends": FieldValue.arrayRemove([_auth.currentUser?.uid]),
    });

    _firestore.collection("user").doc(_auth.currentUser?.uid).update({
      "friends": FieldValue.arrayRemove([userUid]),
    });
  }

  static Future<void> changeUserName(String newName) async {
    print("changing username");
    var snapshots = await _firestore
        .collection("chat")
        .where("members",
            arrayContains: ((_auth.currentUser?.uid ?? "") +
                "|" +
                (_auth.currentUser?.displayName ?? "")))
        .get();

    //After removing and inserting the member list order can be different, but it is important

    // print("length change " + snapshots.docs.length.toString());

    String oldName = _auth.currentUser?.displayName ?? "";

    for (var doc in snapshots.docs) {
      print("in while");
      if (doc.exists) {
        print("in if");
        Map<String, dynamic>? data = doc.data();
        var members = data["members"];

        var lastMessageSenderName = data["lastmessagesendername"];

        List newLastMessageSenderName = [];

        for (String name in lastMessageSenderName) {
          if (name == oldName) {
            newLastMessageSenderName.add(newName);
          } else {
            newLastMessageSenderName.add(name);
          }
        }

        int ownUidPos =
            GeneralUserService.getOwnUidPosInChatFromMemberList(members);

        members[ownUidPos] = (_auth.currentUser?.uid ?? "") + "|" + (newName);

        String chatName = doc["name"];
        print("chatname " + chatName);
        print("oldname " + oldName);
        print("newname " + newName);
        chatName = chatName.replaceAll(oldName, newName);

        await _firestore.collection("chat").doc(doc.id).update({
          "members": members,
          "name": chatName,
          "lastmessagesendername": newLastMessageSenderName,
        });
      }
    }

    List<String> splitList = newName.split(" ");

    List<String> indexList = [];

    for (int i = 0; i < splitList.length; i++) {
      for (int j = 1; j < splitList[i].length + 1; j++) {
        indexList.add(splitList[i].substring(0, j).toLowerCase());
      }
    }

    _firestore.collection("user").doc(_auth.currentUser?.uid).update({
      "name": newName,
      "searchIndex": indexList,
    });

    await _auth.currentUser
        ?.updateDisplayName(newName)
        .then((value) => print("name change success"));
  }

  static Future<void> changeMail(String newEmail) async {
    try {
      await _auth.currentUser?.updateEmail(newEmail);
    } on FirebaseAuthException catch (e) {
      String errorMessage = "";
      switch (e.code) {
        case "invalid-email":
          errorMessage = "Die E-Mailadresse ist ungültig";
          break;
        case "email-already-in-use":
          errorMessage = "Die E-Mailadresse wird schon benutzt";
          break;
      }
      throw errorMessage;
    }
  }

  static Future<void> changePassword(String newPassword) async {
    try {
      await _auth.currentUser?.updatePassword(newPassword);
    } on FirebaseAuthException catch (e) {
      String errorMessage = "";
      switch (e.code) {
        case "weak-password":
          errorMessage = "Das Passwort ist zu schwach. Bitte wähle ein Neues";
          break;
      }
      throw errorMessage;
    }
  }

  static Future<void> changeInfo(String newInfo) async {
    await _firestore
        .collection("user")
        .doc(_auth.currentUser?.uid ?? "")
        .update({
      "info": newInfo,
    });
  }

  static Future<void> deleteAccount() async {
    String uidName = (_auth.currentUser?.uid ?? "") +
        "|" +
        (_auth.currentUser?.displayName ?? "");
    String uid = (_auth.currentUser?.uid ?? "");

    var groupSnapshots = await _firestore.collection("chat").get();

    for (var doc in groupSnapshots.docs) {
      List members = doc.get("members");
      if (doc.get("isgroup") && members.contains(uid)) {
        int index = members.indexOf(uid);

        Map<String, dynamic>? data = doc.data();
        var lastMessageTexts = data["lastmessagetext"];
        var lastmessagesenderids = data["lastmessagesenderid"];
        var lastmessagesendernames = data["lastmessagesendername"];
        var lastmessagedates = data["lastmessagedate"];
        var newMembers = data["members"];
        var notarchivedby = data["notarchivedby"];
        var adminList = data["adminList"];

        lastmessagedates.removeAt(index);
        lastMessageTexts.removeAt(index);
        lastmessagesenderids.removeAt(index);
        lastmessagesendernames.removeAt(index);
        newMembers.remove(uid);
        notarchivedby.remove(uid);
        adminList.remove(uid);

        if (newMembers.length == 0) {
          await _firestore.collection("chat").doc(doc.id).delete();
          return;
        }

        if (adminList.length == 0) {
          print("no admins");
          adminList.add(newMembers[0]);
          print(adminList);
        }

        await _firestore.collection("chat").doc(doc.id).update({
          "lastmessagetext": lastMessageTexts,
          "lastmessagesenderid": lastmessagesenderids,
          "lastmessagedate": lastmessagedates,
          "lastmessagesendername": lastmessagesendernames,
          "members": newMembers,
          "notarchivedby": notarchivedby,
          "adminList": adminList,
        });

        var snapshots = await _firestore
            .collection("chat")
            .doc(doc.id)
            .collection("messages")
            .get();

        for (var doc in snapshots.docs) {
          await doc.reference.update({
            'canbeseenby': FieldValue.arrayRemove([uid]),
          });
        }
      }

      if (!doc.get("isgroup") && members.contains(uidName)) {
        await _firestore.collection("chat").doc(doc.id).delete();
      }
    }

    var userSnapshots = await _firestore.collection("user").get();

    for (var doc in userSnapshots.docs) {
      if (doc.get("friends").contains(uid)) {
        await _firestore.collection("user").doc(doc.id).update({
          "friends": FieldValue.arrayRemove([uid]),
        });
      }
      if (doc.get("hasChatWith").contains(uid)) {
        await _firestore.collection("user").doc(doc.id).update({
          "hasChatWith": FieldValue.arrayRemove([uid]),
        });
      }
    }

    await _firestore.collection("user").doc(uid).delete();
    await _auth.currentUser?.delete();
  }

  static Future<String> getFotoUrlByUid(String uid) async {
    String url =
        await _firestore.collection("user").doc(uid).get().then((value) {
      return value.get("fotourl");
    });
    return url;
  }

  static Future<void> changePhotoUrl(String url) async {
    _firestore.collection("user").doc(_auth.currentUser?.uid).update({
      "fotourl": url,
    });

    var snapshots = await _firestore
        .collection("chat")
        .where("members",
            arrayContains: ((_auth.currentUser?.uid ?? "") +
                "|" +
                (_auth.currentUser?.displayName ?? "")))
        .get();

    print((_auth.currentUser?.uid ?? "") +
        "|" +
        (_auth.currentUser?.displayName ?? "asdf"));
    print(snapshots.size.toString() + " ist die größe");

    for (var doc in snapshots.docs) {
      print("in for snapshots.docs");
      await doc.reference.update(({
        "fotourls": FieldValue.arrayRemove([_auth.currentUser?.photoURL]),
      }));

      await doc.reference.update(({
        "fotourls": FieldValue.arrayUnion([url]),
      }));
    }

    await _auth.currentUser?.updatePhotoURL(url);
  }

  static Future<List> getFcmIds(String uid) async {
    var doc = await _firestore.collection("user").doc(uid).get();

    List fcmIds = doc.get("fcmids");

    return fcmIds;
  }

  static Future<void> addFcmId(String fcmId) async {
    _firestore.collection("user").doc(_auth.currentUser?.uid).update({
      "fcmids": FieldValue.arrayUnion([fcmId])
    });
  }

  static Future<void> removeFcmId(String fcmId) async {
    _firestore.collection("user").doc(_auth.currentUser?.uid).update({
      "fcmids": FieldValue.arrayRemove([fcmId])
    });
  }
}
