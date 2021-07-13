import 'package:firebase_messaging/firebase_messaging.dart';

class ChatFcmService {
  ChatFcmService();
  static final _push = FirebaseMessaging.instance;
  static Future<void> subscribeToChat({required String chatUid}) async {
    await _push.subscribeToTopic(chatUid);
  }
}
