import 'dart:io';

import 'package:adaptive_action_sheet/adaptive_action_sheet.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chatinfo/chatinfo_screen.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:flutter_dispuatio/widgets/glassmorphism_container.dart';
import 'package:flutter_slidable/flutter_slidable.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

import 'chatlist_slidable_actions_more.dart';

List<Widget> getPrimaryActions(ChatModel chat, context) {
  return <Widget>[
    IconSlideAction(
      caption: chat.isArchived ? "Entarchivieren" : 'Archivieren',
      color: Colors.grey[600],
      iconWidget: Padding(
        padding: const EdgeInsets.all(4.0),
        child: chat.isArchived ? Icon(Icons.unarchive) : Icon(Icons.archive),
      ),
      onTap: () {
        if (chat.isArchived) {
          ChatFirebaseService.unarchiveChat(chat.uid);
        } else {
          ChatFirebaseService.archiveChat(chat.uid);
        }
      },
    ),
    IconSlideAction(
      caption: chat.isPinned ? "Entpinnen" : 'Anpinnen',
      color: Colors.grey[700],
      iconWidget: Padding(
        padding: const EdgeInsets.all(4.0),
        child: Icon(FontAwesomeIcons.mapPin),
      ),
      onTap: () {
        chat.isPinned
            ? ChatFirebaseService.unpinChat(chat.uid)
            : ChatFirebaseService.pinChat(chat.uid);
      },
    ),
  ];
}

List<Widget> getSecondaryActions(ChatModel data, context) {
  return <Widget>[
    IconSlideAction(
      caption: 'Info',
      color: Colors.grey[600],
      iconWidget: Padding(
        padding: const EdgeInsets.all(4.0),
        child: Icon(
          FontAwesomeIcons.infoCircle,
          color: Colors.white,
        ),
      ),
      onTap: () {
        Navigator.push(
          context,
          CupertinoPageRoute(
            builder: (context) => ChatInfoScreen(
              data,
            ),
          ),
        );
      },
    ),
    if (!data.isGroup)
      IconSlideAction(
        caption: 'Mehr',
        color: Colors.grey[700],
        iconWidget: Padding(
          padding: const EdgeInsets.all(3.0),
          child: Icon(FontAwesomeIcons.ellipsisH),
        ),
        onTap: () {
          if (Platform.isIOS) {
            showCupertinoModalPopup(
              context: context,
              builder: (context) => chatlistSlidableActionsMoreCupertino(
                context,
              ),
            );
          } else {
            showAdaptiveActionSheet(
              context: context,
              actions: chatlistSlidableActionsMoreMaterial(context, data),
              cancelAction: CancelAction(
                title: const Text('Schließen'),
              ),
            );
          }
        },
      ),
  ];
}

List<Widget> getGMPrimaryActions() {
  return [
    Padding(
      padding: const EdgeInsets.all(8.0),
      child: GlassMorphismContainer(
        height: 72,
        width: 64,
        child: Icon(FontAwesomeIcons.archive),
        color: Colors.blue.withOpacity(kColorOpacityValue),
      ),
    ),
    Padding(
      padding: const EdgeInsets.fromLTRB(8, 8, 8, 8),
      child: GlassMorphismContainer(
        height: 72,
        width: 64,
        child: Icon(FontAwesomeIcons.mapPin),
        color: Colors.blue.shade500.withOpacity(kColorOpacityValue),
      ),
    )
  ];
}

List<Widget> getGMSecondaryActions(data, context) {
  return [
    Padding(
      padding: const EdgeInsets.all(8.0),
      child: GlassMorphismContainer(
        height: 72,
        width: 64,
        child: Icon(FontAwesomeIcons.infoCircle),
        color: Colors.blue.withOpacity(kColorOpacityValue),
        onTap: () {
          Navigator.push(context,
              CupertinoPageRoute(builder: (context) => ChatInfoScreen(data)));
        },
      ),
    ),
    Padding(
      padding: const EdgeInsets.fromLTRB(8, 8, 8, 8),
      child: GlassMorphismContainer(
        height: 72,
        width: 64,
        child: Icon(FontAwesomeIcons.ellipsisH),
        color: Colors.black.withOpacity(kColorOpacityValue),
        onTap: () {
          showAdaptiveActionSheet(
            context: context,
            actions: <BottomSheetAction>[
              BottomSheetAction(
                title: const Text('Info'),
                onPressed: () {},
              ),
              BottomSheetAction(
                title: const Text(
                  'Löschen',
                  style: TextStyle(
                    color: Colors.red,
                  ),
                ),
                onPressed: () {
                  showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return AlertDialog(
                        title: Text(
                          'Wirklich löschen?',
                          style: TextStyle(
                            color: Colors.red,
                          ),
                        ),
                        content: Text(
                            'Bist du dir sicher, dass du diesen Chat löschen möchtest?'),
                        actions: <Widget>[
                          TextButton(
                              child: Text(
                                'Abbrechen',
                                style: TextStyle(
                                  color: Colors.blue,
                                ),
                              ),
                              onPressed: () {
                                // Hier passiert etwas
                                Navigator.of(context).pop();
                              }),
                          TextButton(
                            child: Text(
                              'Löschen',
                              style: TextStyle(
                                color: Colors.red,
                              ),
                            ),
                            onPressed: () {
                              // Hier passiert etwas anderes
                              Navigator.of(context).pop();
                            },
                          ),
                        ],
                      );
                    },
                  );
                },
              ),
            ],
            cancelAction: CancelAction(
              title: const Text('Abbrechen'),
            ),
          );
        },
      ),
    )
  ];
}
