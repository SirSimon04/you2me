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
      automaticallyImplyLeading: false,
      leading: IconButton(
        icon: Icon(Icons.arrow_back),
        color: Colors.white,
        onPressed: () => Navigator.of(context).pop(),
      ),
      actions: [
        IconButton(
          icon: Icon(
            FontAwesomeIcons.infoCircle,
            color: Colors.white,
          ),
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
          Text(
            chat.name,
            style: TextStyle(
              color: Colors.white,
            ),
          ),
        ],
      ),
    ),
  );
}
