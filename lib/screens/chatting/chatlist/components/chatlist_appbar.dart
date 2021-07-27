import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

getChatListAppBar(bool isArchiveOpen, VoidCallback onPressed) {
  return AppBar(
    automaticallyImplyLeading: isArchiveOpen,
    title: isArchiveOpen ? Text("Archivierte Chats") : Text("Chats"),
    actions: [
      !isArchiveOpen
          ? IconButton(
              onPressed: () {
                onPressed();
              },
              icon: Icon(FontAwesomeIcons.solidFolderOpen),
            )
          : Container(),
    ],
    leading: isArchiveOpen
        ? IconButton(
            icon: Icon(FontAwesomeIcons.arrowLeft),
            onPressed: onPressed,
          )
        : null,
  );
}
