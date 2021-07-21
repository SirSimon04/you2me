import 'dart:io';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/widgets/fill_outlined_button.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_slidable/flutter_slidable.dart';

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
