import 'package:adaptive_action_sheet/adaptive_action_sheet.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/chatting/chatinfo/chatinfo_screen.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';

List<BottomSheetAction> chatlistSlidableActionsMoreMaterial(context, chat) {
  return [
    BottomSheetAction(
      title: const Text('Info'),
      onPressed: () {
        Navigator.of(context).push(
          CupertinoPageRoute(
            builder: (context) => ChatInfoScreen(chat),
          ),
        );
      },
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
                    ChatFirebaseService.deleteChat(chat);
                    Navigator.of(context).pop();
                    Navigator.of(context).pop();
                  },
                ),
              ],
            );
          },
        );
      },
    ),
  ];
}

CupertinoActionSheet chatlistSlidableActionsMoreCupertino(context, chat) {
  return CupertinoActionSheet(
    actions: [
      CupertinoActionSheetAction(
        onPressed: () {
          Navigator.of(context).push(
            CupertinoPageRoute(
              builder: (context) => ChatInfoScreen(chat),
            ),
          );
        },
        child: Text("Info"),
      ),
      CupertinoActionSheetAction(
        onPressed: () => showCupertinoDialog(
          context: context,
          builder: (context) => CupertinoAlertDialog(
            title: Text(
              'Wirklich löschen?',
              style: TextStyle(
                color: Colors.red,
              ),
            ),
            content: Text(
                'Bist du dir sicher, dass du diesen Chat löschen möchtest?'),
            actions: [
              CupertinoDialogAction(
                child: Text("Abbrechen"),
                onPressed: () => Navigator.of(context).pop(),
              ),
              CupertinoDialogAction(
                child: Text("Löschen"),
                onPressed: () {
                  ChatFirebaseService.deleteChat(chat);
                  Navigator.of(context).pop();
                  Navigator.of(context).pop();
                },
              ),
            ],
          ),
        ),
        child: Text("Löschen"),
        isDestructiveAction: true,
      ),
    ],
    cancelButton: CupertinoActionSheetAction(
      child: Text("Schließen"),
      onPressed: () {
        Navigator.of(context).pop();
      },
    ),
  );
}
