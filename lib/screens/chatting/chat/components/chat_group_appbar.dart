import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chatinfo/chatinfo_screen.dart';
import 'package:flutter_dispuatio/widgets/clickable_appbar.dart';
import 'package:flutter_dispuatio/widgets/userprofile_pic.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

getGroupAppBar(ChatModel chat, BuildContext context) {
  return ClickableAppBar(
    onTap: () => Navigator.of(context).push(
      CupertinoPageRoute(
        builder: (context) => ChatInfoScreen(chat),
      ),
    ),
    appBar: AppBar(
      actions: [
        IconButton(
          icon: Icon(FontAwesomeIcons.infoCircle),
          onPressed: () {
            Navigator.of(context).push(
              CupertinoPageRoute(
                builder: (context) => ChatInfoScreen(chat),
              ),
            );
          },
        ),
      ],
      title: Row(
        children: [
          UserProfilePic(
            url: chat.fotoUrls.first,
            isOnline: false,
          ),
          SizedBox(
            width: 20,
          ),
          Text(chat.name),
        ],
      ),
    ),
  );
}
