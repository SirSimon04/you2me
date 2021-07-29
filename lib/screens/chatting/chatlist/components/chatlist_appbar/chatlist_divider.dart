import 'package:flutter/material.dart';

class ChatListDivider extends StatelessWidget {
  const ChatListDivider({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 1,
      width: double.infinity,
      child: Divider(
        color: Colors.grey[500],
      ),
    );
  }
}
