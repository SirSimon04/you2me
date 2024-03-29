import 'dart:async';

import 'package:badges/badges.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chat/chat_screen.dart';
import 'package:flutter_dispuatio/screens/chatting/chatinfo/chatinfo_screen.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/components/chatlist_tile/chatlist_slidable_actions.dart';
import 'package:flutter_dispuatio/services/user_services/GeneralUserService.dart';
import 'package:flutter_dispuatio/widgets/userprofile_pic.dart';
import 'package:flutter_slidable/flutter_slidable.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:time_ago_provider/time_ago_provider.dart' as time_ago;

class ChatListTile extends StatefulWidget {
  ChatListTile(this.chat);

  final ChatModel chat;

  @override
  _ChatListTileState createState() => _ChatListTileState();
}

class _ChatListTileState extends State<ChatListTile> {
  final _auth = FirebaseAuth.instance;

  final _firestore = FirebaseFirestore.instance;

  int newMessagesCount = 0;

  String getLastMessageText() {
    if (widget.chat.lastMessageSenderId == _auth.currentUser?.uid) {
      return "Du: " + widget.chat.lastMessageText;
    } else if (widget.chat.isGroup) {
      if (widget.chat.lastMessageText == "") {
        return "";
      } else {
        return widget.chat.lastMessageSender +
            ": " +
            widget.chat.lastMessageText;
      }
    } else {
      return widget.chat.lastMessageText;
    }
  }

  String? timeAgo = "";

  @override
  void initState() {
    super.initState();

    // sets first value
    // _now = DateTime.now().second.toString();
    timeAgo = time_ago.format(
        DateTime.fromMillisecondsSinceEpoch(
            widget.chat.lastMessageDate.millisecondsSinceEpoch),
        locale: "de_short");
    // defines a timer
    Timer.periodic(Duration(minutes: 1), (Timer t) {
      setState(() {
        // _now = DateTime.now().second.toString();
        timeAgo = time_ago.format(
            DateTime.fromMillisecondsSinceEpoch(
                widget.chat.lastMessageDate.millisecondsSinceEpoch),
            locale: "de_short");
        // print(_now);
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    time_ago.setLocale('de', time_ago.German());
    time_ago.setLocale('de_short', time_ago.German(shortForm: true));
    setState(() {
      // _now = DateTime.now().second.toString();
      timeAgo = time_ago.format(
          DateTime.fromMillisecondsSinceEpoch(
              widget.chat.lastMessageDate.millisecondsSinceEpoch),
          locale: "de_short");
      // print(_now);
    });
    return Slidable(
      child: ListTile(
        minVerticalPadding: kChatListTileHeight,
        enableFeedback: true,
        leading: UserProfilePic(
            isOnline: false,
            url: widget.chat.isGroup
                ? widget.chat.fotoUrls.first
                : GeneralUserService.getOtherUserFotoUrl(widget.chat)),
        title: Text(widget.chat.name),
        subtitle: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Flexible(
              child: Text(
                getLastMessageText(),
                overflow: TextOverflow.ellipsis,
                strutStyle: StrutStyle(fontSize: 12),
              ),
            ),
            // SizedBox(
            //   width: 3,
            // ),
            Spacer(),
            getTimeAndBadge(),
            if (widget.chat.isPinned)
              Icon(
                FontAwesomeIcons.mapPin,
              ),
          ],
        ),
        onTap: () {
          print(widget.chat.uid);
          Navigator.push(
              context,
              CupertinoPageRoute(
                  builder: (context) => ChatScreen(widget.chat)));
        },
        onLongPress: () {
          Navigator.push(
            context,
            CupertinoPageRoute(
              builder: (context) => ChatInfoScreen(widget.chat),
            ),
          );
        },
      ),
      actionPane: SlidableDrawerActionPane(),
      actions: getPrimaryActions(widget.chat, context),
      secondaryActions: getSecondaryActions(widget.chat, context),
    );
  }

  getTimeAndBadge() {
    return Row(
      children: [
        Text(
          timeAgo ?? "",
        ),
        SizedBox(
          width: 5,
        ),
        StreamBuilder<QuerySnapshot>(
          stream: _firestore
              .collection("chat")
              .doc(widget.chat.uid)
              .collection("messages")
              .snapshots(),
          builder: (context, snapshot) {
            if (!snapshot.hasData) {
              return Container();
            } else {
              final List<DocumentSnapshot> documents = snapshot.data!.docs;
              int allCount = documents.length;
              int readCount = 0;
              for (var doc in documents) {
                List readby = [];
                readby = doc.get("readby");
                if (readby.contains(_auth.currentUser?.uid)) {
                  readCount++;
                }
              }
              int unreadCount = allCount - readCount;
              return unreadCount == 0
                  ? Container()
                  : Badge(
                      badgeContent: Text(unreadCount.toString()),
                      animationType: BadgeAnimationType.slide,
                    );
            }
          },
        ),
      ],
    );
  }
}
