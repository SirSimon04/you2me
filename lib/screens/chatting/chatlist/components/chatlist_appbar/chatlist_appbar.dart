import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/components/create_group/create_group_bottom_sheet_1.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

getChatListAppBar({
  required bool isArchiveOpen,
  required VoidCallback onArchivePress,
  required BuildContext context,
}) {
  return AppBar(
    automaticallyImplyLeading: isArchiveOpen,
    title: isArchiveOpen ? Text("Archivierte Chats") : Text("Chats"),
    actions: [
      !isArchiveOpen
          ? IconButton(
              onPressed: () {
                onArchivePress();
              },
              icon: Icon(FontAwesomeIcons.solidFolderOpen),
            )
          : Container(),
      Padding(
        padding: const EdgeInsets.only(right: 8.0),
        child: IconButton(
            onPressed: () => createGroupModalMaterialBottomSheet1(context),
            icon: Icon(FontAwesomeIcons.users)),
      )
    ],
    leading: isArchiveOpen
        ? IconButton(
            icon: Icon(FontAwesomeIcons.arrowLeft),
            onPressed: onArchivePress,
          )
        : null,
  );
}
