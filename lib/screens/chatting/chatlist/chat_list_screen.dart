import 'dart:io';
import 'dart:ui';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/components/chatlist_appbar.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/components/chatlist_appbar_extension.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/components/chatlist_divider.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/components/chatlist_streambuilder.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:flutter_dispuatio/widgets/fill_outlined_button.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_slidable/flutter_slidable.dart';
import 'package:implicitly_animated_reorderable_list/implicitly_animated_reorderable_list.dart';
import 'package:implicitly_animated_reorderable_list/transitions.dart';

class ChatList extends StatefulWidget {
  @override
  _ChatListState createState() => _ChatListState();
}

class _ChatListState extends State<ChatList>
    with AutomaticKeepAliveClientMixin {
  final _auth = FirebaseAuth.instance;
  final _firestore = FirebaseFirestore.instance;
  final _push = FirebaseMessaging.instance;

  bool isArchiveOpen = false;

  // void getToken() async {
  //   print("AAAAAAAAAAAAAAAAAAAAAAA");
  //   print(await _push.getToken());
  //   await _push.sendMessage(
  //     to: "fPIfg00WScSRH6SNy7ZFRt:APA91bGMr755qJjFiVoux9RuXLIcTrK8Wb1mYgaa3uMFuHJmuzBgWPZqAvBSezykM9MnswtLGKgrNWIKRCDlniGokwo78k7I4YZxBCqNao5BcM9UXPal9bgw_WWjkOXOBq1-iufP-rme",
  //     data: {
  //       "body": "Body of Your Notification",
  //       "title": "Title of Your Notification"
  //     },
  //     collapseKey: "NEW MESSAGE",
  //   );
  // }

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    super.initState();

    // getToken();
    // _push.sendMessage();
  }

  void test() async {
    String url =
        await UserFirebaseService.getFotoUrlbyUid(_auth.currentUser?.uid ?? "");
    print(url);
  }

  @override
  Widget build(BuildContext context) {
    test();
    super.build(context);
    // print("UID ist " + _auth.currentUser!.uid);
    // print(_auth.currentUser!.displayName);
    return Scaffold(
      appBar: getChatListAppBar(isArchiveOpen, () {
        setState(() {
          isArchiveOpen = !isArchiveOpen;
        });
      }),
      body: Container(
        decoration: BoxDecoration(
          image: gmOn
              ? DecorationImage(
                  image: NetworkImage(_auth.currentUser?.photoURL ?? ""),
                  fit: BoxFit.cover,
                )
              : null,
        ),
        child: Column(
          children: [
            ChatListAppBarExtension(),
            ChatListDivider(),
            ChatListStreamBuilder(isArchiveOpen: isArchiveOpen),
          ],
        ),
      ),
    );
  }
}
