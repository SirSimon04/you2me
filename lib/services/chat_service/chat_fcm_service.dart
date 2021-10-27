import 'dart:convert';
import 'dart:io';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:http/http.dart' as http;
import 'package:http/http.dart';
import 'package:flutter/foundation.dart' show kIsWeb;

class ChatFcmService {
  ChatFcmService();
  static final _push = FirebaseMessaging.instance;

  static Future<void> subscribeToChat({
    required String chatUid,
  }) async {
    await _push.subscribeToTopic(chatUid);
  }

  static Future<void> unsubscribeFromChat({
    required String chatUid,
  }) async {
    await _push.unsubscribeFromTopic(chatUid);
  }

  static Future<void> sendMsgPrivate({
    required String chatUid,
    required String name,
    required String msgText,
    required isGroup,
    String? groupName,
  }) async {
    print("sendMsgPrivate");
    if (!kIsWeb) {
      await _push.unsubscribeFromTopic(chatUid);
      sleep(Duration(seconds: 1));
      print("unsubschribed");
    }
    Response r = await http.post(
      Uri.parse("https://fcm.googleapis.com/fcm/send"),
      headers: <String, String>{
        "Content-Type": "application/json",
        "Authorization":
            "key=AAAA-4KnGFI:APA91bHctUmcgHHPAAJHN0hrEuRM7xnxnt6YciQRbCisEhJZ1Vn1gIeOG80K9md2ltQ1NB8gzUwydJoNez9w6BACr-iLbFlSgkzNPZE0TVcq5pQNAryzaQPPdaY-p3vqrf7V2P_cFOFB"
      },
      body: jsonEncode(
        <String, dynamic>{
          "to": "/topics/$chatUid",
          "collapse_key": "Neue Nachricht",
          "notification": isGroup
              ? {"body": msgText, "title": name + " @ " + (groupName ?? "")}
              : {
                  "body": msgText,
                  "title": name,
                }
        },
      ),
    );
    print(r.body + " code: " + r.statusCode.toString());
    if (!kIsWeb) {
      sleep(Duration(seconds: 1));
      await _push.subscribeToTopic(chatUid);
      print("subscribed");
    }
  }

  static Future<String?> getToken() async {
    return await _push.getToken();
  }
}
