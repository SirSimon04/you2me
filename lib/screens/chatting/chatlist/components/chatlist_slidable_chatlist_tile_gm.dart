import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chat/chat_screen.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/components/chatlist_slidable_actions.dart';
import 'package:flutter_dispuatio/widgets/glassmorphism_container.dart';
import 'package:flutter_slidable/flutter_slidable.dart';

class GMChatListTile extends StatelessWidget {
  GMChatListTile(this.chat);

  final ChatModel chat;

  @override
  Widget build(BuildContext context) {
    return Slidable(
      child: Container(
        child: Padding(
          padding: const EdgeInsets.symmetric(
            vertical: 16,
            horizontal: 32,
          ),
          child: GlassMorphismContainer(
            height: 72,
            width: double.infinity,
            color: Colors.white.withOpacity(kColorOpacityValue),
            child: Center(
              child: Text("Moin"),
            ),
            onTap: () {
              Navigator.push(context,
                  CupertinoPageRoute(builder: (context) => ChatScreen(chat)));
            },
          ),
        ),
      ),
      actionPane: SlidableDrawerActionPane(),
      actions: getGMPrimaryActions(),
      secondaryActions: getGMSecondaryActions(chat, context),
    );
  }
}
