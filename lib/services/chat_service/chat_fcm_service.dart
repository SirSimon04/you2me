import 'dart:convert';

import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:http/http.dart' as http;

class ChatFcmService {
  ChatFcmService();
  static final _push = FirebaseMessaging.instance;

  static Future<void> subscribeToChat({required String chatUid}) async {
    await _push.subscribeToTopic(chatUid);
  }

  static Future<void> sendMsgPrivate({
    required String chatUid,
    required String name,
    required String msgText,
  }) async {
    http.post(
      Uri.parse("https://fcm.googleapis.com/fcm/send"),
      headers: <String, String>{
        "Content-Type": "application/json",
        "Authorization":
            "key=AAAA-4KnGFI:APA91bHctUmcgHHPAAJHN0hrEuRM7xnxnt6YciQRbCisEhJZ1Vn1gIeOG80K9md2ltQ1NB8gzUwydJoNez9w6BACr-iLbFlSgkzNPZE0TVcq5pQNAryzaQPPdaY-p3vqrf7V2P_cFOFB"
      },
      body: jsonEncode(
        <String, dynamic>{
          "to": "/topics/wAOg0eQSox4YBBEhAGJi",
          "collapse_key": "Neue Nachricht",
          "notification": {
            "body": msgText,
            "title": name,
          }
        },
      ),
    );
  }

  static Future<String?> getToken() async {
    return await _push.getToken();
  }
}
