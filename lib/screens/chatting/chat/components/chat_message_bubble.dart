import 'package:bubble/bubble.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:focused_menu/focused_menu.dart';
import 'package:focused_menu/modals.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:intl/intl.dart';

import 'answer_container.dart';
import 'chat_message_bubble_image.dart';
import 'focused_messagemenu_holder.dart';

class ChatMessageBubble extends StatefulWidget {
  ChatMessageBubble({
    required this.message,
    required this.chat,
    this.answerMessage,
    this.allFav = false,
    this.isDate = false,
    this.isFirstDate = false,
    this.dateString,
  });

  final MessageModel message;
  final ChatModel chat;
  final MessageModel? answerMessage;
  final bool allFav;
  final bool isDate;
  final String? dateString;
  final bool isFirstDate;

  @override
  _ChatMessageBubbleState createState() => _ChatMessageBubbleState();
}

class _ChatMessageBubbleState extends State<ChatMessageBubble> {
  @override
  Widget build(BuildContext context) {
    if (widget.isDate) {
      if (widget.isFirstDate) {
        return Padding(
          padding: const EdgeInsets.only(
            top: 12,
          ),
          child: Bubble(
            color: Colors.blueGrey,
            child: Text(
              widget.dateString ?? "",
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 11.0,
                color: Colors.white,
              ),
            ),
            alignment: Alignment.center,
          ),
        );
      }
      return Bubble(
        color: Colors.blueGrey,
        child: Text(
          widget.dateString ?? "",
          textAlign: TextAlign.center,
          style: TextStyle(
            fontSize: 11.0,
            color: Colors.white,
          ),
        ),
        alignment: Alignment.center,
      );
    }
    return !widget.allFav
        ? FocusedMessageMenu(
            isMy: widget.message.isMy,
            message: widget.message,
            chat: widget.chat,
            child: Bubble(
              color: widget.message.isMy
                  ? Theme.of(context).colorScheme.secondary
                  : Theme.of(context).colorScheme.secondaryVariant,
              margin: BubbleEdges.only(top: 5, bottom: 5),
              style: widget.message.isMy ? styleMe : styleSomebody,
              radius: Radius.circular(kCircularBorderRadius * 0.5),
              child: getMessage(),
            ),
          )
        : FocusedMenuHolder(
            menuBoxDecoration: BoxDecoration(
              color: Colors.black26,
              shape: BoxShape.circle,
            ),
            blurBackgroundColor: Colors.black12,
            menuWidth: MediaQuery.of(context).size.width * 0.6,
            menuOffset: 15.0,
            animateMenuItems: false,
            onPressed: () {},
            menuItems: [
              FocusedMenuItem(
                title: Text("Stern entfernen"),
                trailingIcon: Icon(FontAwesomeIcons.star),
                onPressed: () {
                  print("pressed in unmark");

                  ChatFirebaseService.unmarkMessageFromFavPage(
                    msg: widget.message,
                  );
                },
                backgroundColor: kFocusedMenuHolderBGColorDark,
              ),
            ],
            child: Bubble(
              color: widget.message.isMy
                  ? Theme.of(context).colorScheme.secondary
                  : Theme.of(context).colorScheme.secondaryVariant,
              margin: BubbleEdges.only(top: 5, bottom: 5),
              style: widget.message.isMy ? styleMe : styleSomebody,
              radius: Radius.circular(kCircularBorderRadius * 0.5),
              child: getMessage(),
            ),
          );
  }

  Widget getMessage() {
    //Name anzeigen wenn Gruppe und nicht meine Nachricht
    if (!widget.message.isMy && widget.chat.isGroup) {
      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            widget.chat.lastMessageSender,
            style: TextStyle(
              color: kSecondaryColor,
            ),
          ),
          Visibility(
            visible: widget.answerMessage != null,
            child: getAnswerContainer(widget.answerMessage, context),
          ),
          Stack(
            children: [
              !widget.message.isImage
                  ? Text(
                      widget.message.text + "                  ",
                    )
                  : ChatMessageBubbleImage(widget.message),
              Positioned(
                bottom: 0,
                right: 0,
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Visibility(
                      visible: widget.allFav || widget.message.isFav,
                      child: Icon(
                        FontAwesomeIcons.solidStar,
                        size: 8,
                        color: Colors.yellow,
                      ),
                    ),
                    Visibility(
                      visible: widget.allFav || widget.message.isFav,
                      child: SizedBox(
                        width: 3,
                      ),
                    ),
                    Text(
                      DateFormat("HH:mm").format(
                          DateTime.fromMillisecondsSinceEpoch(
                              widget.message.date.millisecondsSinceEpoch)),
                      style: TextStyle(color: Colors.grey, fontSize: 12),
                    ),
                  ],
                ),
              )
            ],
          ),
        ],
      );
    } else {
      //keinen namen bei der Nachricht anzeigen
      return Column(
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          Visibility(
            visible: widget.answerMessage != null,
            child: getAnswerContainer(widget.answerMessage, context),
          ),
          Stack(
            children: [
              !widget.message.isImage
                  ? Text(
                      widget.message.text + "                  ",
                    )
                  : ChatMessageBubbleImage(widget.message),
              Positioned(
                bottom: 0,
                right: 0,
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Visibility(
                      visible: widget.allFav || widget.message.isFav,
                      child: Icon(
                        FontAwesomeIcons.solidStar,
                        size: 8,
                        color: Colors.yellow,
                      ),
                    ),
                    Visibility(
                      visible: widget.allFav || widget.message.isFav,
                      child: SizedBox(
                        width: 5,
                      ),
                    ),
                    Text(
                      DateFormat("HH:mm").format(
                          DateTime.fromMillisecondsSinceEpoch(
                              widget.message.date.millisecondsSinceEpoch)),
                      style: TextStyle(
                        color: Colors.grey,
                        fontSize: 12,
                      ),
                    ),
                    Visibility(
                      child: SizedBox(
                        width: 5,
                      ),
                      visible: widget.message.readByAll ? true : false,
                    ),
                    Visibility(
                      child: Icon(
                        FontAwesomeIcons.solidEye,
                        size: 12,
                      ),
                      visible: widget.message.readByAll,
                    )
                  ],
                ),
              )
            ],
          ),
        ],
      );
    }
  }
}
