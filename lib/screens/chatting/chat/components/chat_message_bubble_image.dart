import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';

class ChatMessageBubbleImage extends StatefulWidget {
  ChatMessageBubbleImage(this.url);

  final String url;

  @override
  _ChatMessageBubbleImageState createState() => _ChatMessageBubbleImageState();
}

class _ChatMessageBubbleImageState extends State<ChatMessageBubbleImage> {
  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(kCircularBorderRadius * 0.5),
      child: Image.network(
        widget.url,
      ),
    );
  }
}
