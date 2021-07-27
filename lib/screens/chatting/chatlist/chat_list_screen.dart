import 'package:cached_network_image/cached_network_image.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/components/chatlist_appbar.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/components/chatlist_streambuilder.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';

class ChatList extends StatefulWidget {
  @override
  _ChatListState createState() => _ChatListState();
}

class _ChatListState extends State<ChatList>
    with AutomaticKeepAliveClientMixin {
  final _auth = FirebaseAuth.instance;

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
                  image: CachedNetworkImageProvider(
                      _auth.currentUser?.photoURL ?? ""),
                  fit: BoxFit.cover,
                )
              : null,
        ),
        child: Column(
          children: [
            // ChatListAppBarExtension(),
            // ChatListDivider(),
            ChatListStreamBuilder(isArchiveOpen: isArchiveOpen),
          ],
        ),
      ),
    );
  }
}
