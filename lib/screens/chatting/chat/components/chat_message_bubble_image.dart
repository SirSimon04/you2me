import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chat/screens/image_fullscreen/image_fullscreen.dart';

class ChatMessageBubbleImage extends StatelessWidget {
  ChatMessageBubbleImage(this.msg);

  final MessageModel msg;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => Navigator.of(context)
          .push(CupertinoPageRoute(builder: (context) => ImageFullscreen(msg))),
      child: ClipRRect(
          borderRadius: BorderRadius.circular(kCircularBorderRadius * 0.5),
          child: CachedNetworkImage(
            imageUrl: msg.url ?? "",
          )),
    );
  }
}
