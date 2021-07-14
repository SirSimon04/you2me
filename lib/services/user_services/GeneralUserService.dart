import 'package:firebase_auth/firebase_auth.dart';

class GeneralUserService {
  static FirebaseAuth _auth = FirebaseAuth.instance;

  static String getOtherUserFotoUrl(chat) {
    if (chat.members[0].split("|")[0] == _auth.currentUser?.uid) {
      return chat.fotoUrls[1];
    } else if (chat.members[1].split("|")[0] == _auth.currentUser?.uid) {
      return chat.fotoUrls[1];
    }
    return "";
  }
}
