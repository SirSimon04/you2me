import 'package:clipboard/clipboard.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:focused_menu/focused_menu.dart';
import 'package:focused_menu/modals.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class OpenAnswer extends Notification {
  final MessageModel msg;
  OpenAnswer(this.msg);
}

class FocusedMessageMenu extends StatelessWidget {
  FocusedMessageMenu({
    required this.child,
    required this.message,
    required this.chat,
    required this.isMy,
  });

  final Widget child;
  final MessageModel message;
  final ChatModel chat;
  final bool isMy;

  @override
  Widget build(BuildContext context) {
    return FocusedMenuHolder(
      onPressed: () {},
      menuBoxDecoration: BoxDecoration(
        color: Colors.black26,
        shape: BoxShape.circle,
      ),
      blurBackgroundColor: Colors.black12,
      menuWidth: MediaQuery.of(context).size.width * 0.6,
      menuOffset: 15.0,
      animateMenuItems: false,
      menuItems: getFocusedMenuItems(context),
      child: child,
    );
  }

  getFocusedMenuItems(context) {
    return isMy
        ? [
            FocusedMenuItem(
              title: message.isFav
                  ? Text("Stern entfernen")
                  : Text("Mit Stern markieren"),
              trailingIcon: message.isFav
                  ? Icon(FontAwesomeIcons.star)
                  : Icon(FontAwesomeIcons.solidStar),
              onPressed: () {
                print(message.toString());
                if (message.isFav) {
                  ChatFirebaseService.unmarkMessage(
                      chatUid: chat.uid, msg: message);
                } else {
                  ChatFirebaseService.markMessage(
                      chatUid: chat.uid, msg: message);
                }
              },
              backgroundColor:
                  Theme.of(context).brightness == ThemeData.dark().brightness
                      ? kFocusedMenuHolderBGColorDark
                      : kFocusedMenuHolderBGColorLight,
            ),
            FocusedMenuItem(
              title: Text("Antworten"),
              trailingIcon: Icon(FontAwesomeIcons.reply),
              onPressed: () {
                print("focused message menu pressed");
                OpenAnswer(message).dispatch(context);
              },
              backgroundColor:
                  Theme.of(context).brightness == ThemeData.dark().brightness
                      ? kFocusedMenuHolderBGColorDark
                      : kFocusedMenuHolderBGColorLight,
            ),
            // FocusedMenuItem(
            //   title: Text("Info"),
            //   trailingIcon: Icon(FontAwesomeIcons.infoCircle),
            //   onPressed: () {},
            //   backgroundColor: kFocusedMenuHolderBGColor,
            // ),
            FocusedMenuItem(
              title: Text("Bearbeiten"),
              onPressed: () {},
              backgroundColor:
                  Theme.of(context).brightness == ThemeData.dark().brightness
                      ? kFocusedMenuHolderBGColorDark
                      : kFocusedMenuHolderBGColorLight,
              trailingIcon: Icon(FontAwesomeIcons.solidEdit),
            ),
            if (!message.isImage)
              FocusedMenuItem(
                onPressed: () {
                  FlutterClipboard.copy(message.text);
                },
                title: Text("Kopieren"),
                trailingIcon: Icon(FontAwesomeIcons.solidCopy),
                backgroundColor:
                    Theme.of(context).brightness == ThemeData.dark().brightness
                        ? kFocusedMenuHolderBGColorDark
                        : kFocusedMenuHolderBGColorLight,
              ),
            FocusedMenuItem(
              title: Text("Löschen"),
              trailingIcon: Icon(FontAwesomeIcons.trash),
              onPressed: () {
                print("deleted");
                ChatFirebaseService.deleteMessage(
                  chatUid: chat.uid,
                  msgUid: message.uid,
                  members: chat.members,
                );
              },
              backgroundColor: Colors.redAccent[700],
            ),
          ]
        : [
            FocusedMenuItem(
              title: message.isFav
                  ? Text("Stern entfernen")
                  : Text("Mit Stern markieren"),
              trailingIcon: message.isFav
                  ? Icon(FontAwesomeIcons.star)
                  : Icon(FontAwesomeIcons.solidStar),
              onPressed: () {
                print(message.toString());
                if (message.isFav) {
                  ChatFirebaseService.unmarkMessage(
                      chatUid: chat.uid, msg: message);
                } else {
                  ChatFirebaseService.markMessage(
                      chatUid: chat.uid, msg: message);
                }
              },
              backgroundColor:
                  Theme.of(context).brightness == ThemeData.dark().brightness
                      ? kFocusedMenuHolderBGColorDark
                      : kFocusedMenuHolderBGColorLight,
            ),
            FocusedMenuItem(
              title: Text("Antworten"),
              trailingIcon: Icon(FontAwesomeIcons.reply),
              onPressed: () {
                OpenAnswer(message).dispatch(context);
              },
              backgroundColor:
                  Theme.of(context).brightness == ThemeData.dark().brightness
                      ? kFocusedMenuHolderBGColorDark
                      : kFocusedMenuHolderBGColorLight,
            ),
            FocusedMenuItem(
              onPressed: () {
                FlutterClipboard.copy(message.text);
              },
              title: Text("Kopieren"),
              trailingIcon: Icon(FontAwesomeIcons.solidCopy),
              backgroundColor:
                  Theme.of(context).brightness == ThemeData.dark().brightness
                      ? kFocusedMenuHolderBGColorDark
                      : kFocusedMenuHolderBGColorLight,
            ),
          ];
  }
}
