import 'package:flutter/material.dart';

class ChatPlaceholder extends StatelessWidget {
  const ChatPlaceholder({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Theme.of(context).canvasColor,
      body: Center(
        child: Text("Öffne einen Chat"),
      ),
    );
  }
}
