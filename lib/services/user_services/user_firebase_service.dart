import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:shared_preferences/shared_preferences.dart';

class UserFirebaseService {
  UserFirebaseService();
  static final _auth = FirebaseAuth.instance;
  static final _firestore = FirebaseFirestore.instance;

  static Future<void> login({
    required String email,
    required String password,
  }) async {
    await _auth.signInWithEmailAndPassword(email: email, password: password);
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setString("email", "mail");
  }

  static Future<void> logout() async {
    await _auth.signOut();
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.remove("email");
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
    print("here");
    UserCredential userCredential =
        await FirebaseAuth.instance.createUserWithEmailAndPassword(
      email: email,
      password: password,
    );
    //TODO: add exceptions
    await _auth.currentUser?.updatePhotoURL(
        "https://firebasestorage.googleapis.com/v0/b/disputatio-a1039.appspot.com/o/user.png?alt=media&token=46927ec9-a8d4-431a-9fc1-60cbef1e4f2a");
    await _auth.currentUser?.updateDisplayName(username);

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
    print(indexList);
    await _firestore.collection("user").doc(_auth.currentUser?.uid ?? "").set({
      "name": _auth.currentUser?.displayName,
      "friends": [],
      "isonline": true,
      "info": "Hey there! I'm using Disputatio",
      "searchIndex": indexList,
      "lastonline": Timestamp.now(),
      "bio": "Hallo, ich wünsche einen guten Tag!",
      "hasChatWith": [],
      "fotourl":
          "https://firebasestorage.googleapis.com/v0/b/disputatio-a1039.appspot.com/o/user.png?alt=media&token=46927ec9-a8d4-431a-9fc1-60cbef1e4f2a,"
    });
    print("added to firebase");
  }

  static Future<void> resetPassword(String mail) async {
    await _auth.sendPasswordResetEmail(email: mail);
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
    var snapshots = await _firestore
        .collection("chat")
        .where("members",
            arrayContains: ((_auth.currentUser?.uid ?? "") +
                "|" +
                (_auth.currentUser?.displayName ?? "")))
        .get();

    for (var doc in snapshots.docs) {
      await doc.reference.update({
        "members": FieldValue.arrayRemove(
          [
            ((_auth.currentUser?.uid ?? "") +
                "|" +
                (_auth.currentUser?.displayName ?? "")),
          ],
        ),
      });

      await doc.reference.update({
        "members": FieldValue.arrayUnion(
            [(_auth.currentUser?.uid ?? "") + "|" + (newName)])
      });
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
    await _auth.currentUser?.updateEmail(newEmail);
  }

  static Future<void> changePassword(String newPassword) async {
    await _auth.currentUser?.updatePassword(newPassword);
  }

  static Future<void> deleteAccount() async {
    // await _auth.currentUser?.delete();
    //TODO: iterate over all chats and remove own info
    var snapshots = await _firestore.collection("chat").get();

    String uidName =
        (_auth.currentUser?.uid ?? "") + (_auth.currentUser?.displayName ?? "");
    String uid = (_auth.currentUser?.uid ?? "");

    print("length of snapshots: " + snapshots.docs.length.toString());
    for (var doc in snapshots.docs) {
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
      }

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
}
