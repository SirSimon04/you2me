import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/components/create_group/create_group_bottom_sheet_1.dart';
import 'package:flutter_dispuatio/screens/homepages/responsive.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

getChatListAppBar({
  required bool isArchiveOpen,
  required VoidCallback onArchivePress,
  required BuildContext context,
}) {
  return AppBar(
      automaticallyImplyLeading: isArchiveOpen,
      title: isArchiveOpen
          ? Text(
              "Archivierte Chats",
              style: TextStyle(color: Colors.white),
            )
          : Text(
              "Chats",
              style: TextStyle(
                color: Colors.white,
              ),
            ),
      actions: [
        !isArchiveOpen
            ? IconButton(
                onPressed: () {
                  onArchivePress();
                },
                icon: Icon(
                  FontAwesomeIcons.solidFolderOpen,
                  color: Colors.white,
                ),
              )
            : Container(),
        Padding(
          padding: const EdgeInsets.only(right: 8.0),
          child: IconButton(
              onPressed: () => createGroupModalMaterialBottomSheet1(context),
              icon: Icon(
                FontAwesomeIcons.users,
                color: Colors.white,
              )),
        )
      ],
      leading: (Responsive.isDesktop(context) ||
              Responsive.isTablet(context) ||
              isArchiveOpen)
          ? getLeadingRow(context, isArchiveOpen, onArchivePress)
          : null);
}

getLeadingRow(context, isArchiveOpen, onArchivePress) {
  return Row(
    children: [
      if (Responsive.isDesktop(context) || Responsive.isTablet(context))
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Icon(
            FontAwesomeIcons.hamburger,
            color: Colors.white,
          ),
        )
      else
        Container(),
      isArchiveOpen
          ? IconButton(
              icon: Icon(
                FontAwesomeIcons.arrowLeft,
                color: Colors.white,
              ),
              onPressed: onArchivePress,
            )
          : Container(),
    ],
  );
}
