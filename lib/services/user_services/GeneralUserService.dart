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
    print("fotourls" + chat.fotoUrls.toString());
    if (chat.fotoUrls[1] == _auth.currentUser?.photoURL) {
      return chat.fotoUrls[0];
    } else if (chat.fotoUrls[0] == _auth.currentUser?.photoURL) {
      return chat.fotoUrls[1];
    } else {
      return "";
    }
  }

  static List<String> getUidsFromMembers(ChatModel chat) {
    List<String> uids = [];
    for (String nameUid in chat.members) {
      uids.add(nameUid.substring(0, nameUid.lastIndexOf("|")));
    }
    return uids;
  }

  static int getOwnUidPos(ChatModel chat) {
    for (String nameUid in chat.members) {
      if (nameUid ==
          ((_auth.currentUser?.uid ?? "") +
              "|" +
              (_auth.currentUser?.displayName ?? ""))) {
        return chat.members.indexOf(nameUid);
      }
    }
    return -1;
  }
}
