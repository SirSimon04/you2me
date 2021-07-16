import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';

class GeneralUserService {
  static FirebaseAuth _auth = FirebaseAuth.instance;

  static String getOtherUserFotoUrl(ChatModel chat) {
    // if (chat.members[0].split("|")[0] == _auth.currentUser?.uid) {
    //   return chat.fotoUrls[1];
    // } else if (chat.members[1].split("|")[0] == _auth.currentUser?.uid) {
    //   return chat.fotoUrls[1];
    // }
    // return "";

    if (chat.fotoUrls[1] == _auth.currentUser?.photoURL) {
      return chat.fotoUrls[0];
    } else {
      return chat.fotoUrls[1];
    }
  }
}
