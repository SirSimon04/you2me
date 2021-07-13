import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:shared_preferences/shared_preferences.dart';

final _auth = FirebaseAuth.instance;
final _firestore = FirebaseFirestore.instance;

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
    final newUser = await _auth.createUserWithEmailAndPassword(
        email: email, password: password);

    var users = await _auth.signInWithEmailAndPassword(
        email: email, password: password);

    var user = _auth.currentUser;

    if (user != null) {
      print("here");

      await _auth.currentUser?.updateProfile(
        displayName: username,
        photoURL:
            "https://firebasestorage.googleapis.com/v0/b/disputatio-a1039.appspot.com/o/user.png?alt=media&token=46927ec9-a8d4-431a-9fc1-60cbef1e4f2a",
      );

      List<String> splitList = username.split(" ");

      List<String> indexList = [];

      for (int i = 0; i < splitList.length; i++) {
        for (int j = 1; j < splitList[i].length + 1; j++) {
          indexList.add(splitList[i].substring(0, j).toLowerCase());
        }
      }

      _firestore.collection("user").doc(user.uid).set({
        "name": _auth.currentUser?.displayName,
        "friends": [],
        "isonline": true,
        "info": "Hey there! I'm using Disputatio",
        "searchIndex": indexList,
        "isonline": true,
        "lastonline": Timestamp.now(),
        "bio": "Hallo, ich wünsche einen guten Tag!",
        "hasChatWith": [],
      });
    }
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
    await _auth.currentUser?.updateDisplayName(newName);
  }

  static Future<void> changeMail(String newEmail) async {
    await _auth.currentUser?.updateEmail(newEmail);
  }

  static Future<void> changePassword(String newPassword) async {
    await _auth.currentUser?.updatePassword(newPassword);
  }

  static Future<void> deleteAccount() async {
    await _auth.currentUser?.delete();
  }
}
