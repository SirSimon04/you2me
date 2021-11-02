import 'dart:convert';
import 'dart:io';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:http/http.dart' as http;
import 'package:http/http.dart';
import 'package:flutter/foundation.dart' show kIsWeb;
import 'package:flutter_dispuatio/models/chat_model.dart';

class ChatFcmService {
  ChatFcmService();
  static final _push = FirebaseMessaging.instance;
  static final _auth = FirebaseAuth.instance;
  static final _firestore = FirebaseFirestore.instance;

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

  static Future<void> sendFcmMessage({
    required ChatModel chat,
    required String name,
    required String msgText,
    required isGroup,
    String? groupName,
  }) async {
    String ownUid = _auth.currentUser?.uid ?? "";

    List fcmIdsUids = chat.fcmIds;
    List idsToSendTo = [];
    for (String fcmIdUid in fcmIdsUids) {
      if (fcmIdUid.split("|")[1] != (ownUid)) {
        idsToSendTo.add(fcmIdUid.split("|")[0]);
      }
    }

    for (String fcmId in idsToSendTo) {
      Response r = await http.post(
        Uri.parse("https://fcm.googleapis.com/fcm/send"),
        headers: <String, String>{
          "Content-Type": "application/json",
          "Authorization":
              "key=AAAA-4KnGFI:APA91bHctUmcgHHPAAJHN0hrEuRM7xnxnt6YciQRbCisEhJZ1Vn1gIeOG80K9md2ltQ1NB8gzUwydJoNez9w6BACr-iLbFlSgkzNPZE0TVcq5pQNAryzaQPPdaY-p3vqrf7V2P_cFOFB"
        },
        body: jsonEncode(
          <String, dynamic>{
            "to": fcmId,
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
    }
  }

  static Future<void> sendMsg({
    required String chatUid,
    required String name,
    required String msgText,
    required isGroup,
    String? groupName,
  }) async {
    print("sendMsgPrivate");
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
  }

  static Future<String?> getToken() async {
    return await _push.getToken();
  }
}
