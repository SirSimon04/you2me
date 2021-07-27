import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chat/components/focused_messagemenu_holder.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_fcm_service.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

import 'components/chat_appbar.dart';
import 'components/chat_input_row.dart';
import 'components/chat_messages_streambuilder.dart';

class ChatScreen extends StatefulWidget {
  ChatScreen(this.chat);
  final ChatModel chat;

  @override
  _ChatScreenState createState() => _ChatScreenState(chat);
}

class _ChatScreenState extends State<ChatScreen> {
  final _auth = FirebaseAuth.instance;

  _ChatScreenState(this.chat);
  ChatModel chat;

  bool isAnswerOpen = false;

  MessageModel answerMessage = MessageModel(
    text: "text",
    isMy: false,
    isFav: false,
    readByAll: false,
    date: Timestamp.now(),
    uid: "n.a.",
    sender: "sender",
    isImage: false,
  );

  final _controller = ScrollController();

  @override
  void initState() {
    super.initState();
    ChatFirebaseService.readMessages(chatUid: widget.chat.uid);
    ChatFcmService.subscribeToChat(chatUid: widget.chat.uid);
  }

  @override
  void dispose() {
    super.dispose();
    _controller.dispose();
    ChatFirebaseService.readMessages(chatUid: "chatUid");
    UserFirebaseService.setWritingFalse(widget.chat.uid);
  }

  @override
  Widget build(BuildContext context) {
    return NotificationListener<OpenAnswer>(
      onNotification: (msg) {
        setState(() {
          isAnswerOpen = true;
          answerMessage = msg.msg;
        });
        return true;
      },
      child: NotificationListener<SendMessage>(
        onNotification: (val) {
          setState(() {
            isAnswerOpen = false;
            answerMessage = MessageModel(
              text: "text",
              isMy: false,
              isFav: false,
              readByAll: false,
              date: Timestamp.now(),
              uid: "n.a.",
              sender: "sender",
              isImage: false,
            );
          });

          return true;
        },
        child: Scaffold(
          backgroundColor: Theme.of(context).canvasColor,
          appBar: getChatAppBar(chat, context),
          body: Column(
            children: [
              Expanded(
                child: ChatMessagesStreambuilder(
                  chat: chat,
                  controller: _controller,
                ),
              ),
              answerViewMessage,
              ChatInputRow(
                chat: chat,
                scrollController: _controller,
                answerMessage: answerMessage,
              ),
            ],
          ),
        ),
      ),
    );
  }

  Container get answerViewMessage {
    return !isAnswerOpen
        ? Container()
        : Container(
            color: Colors.black26,
            height: 70,
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: SizedBox(
                height: 60,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    Expanded(
                      flex: 11,
                      child: Container(
                        decoration: BoxDecoration(
                          color: Colors.grey,
                          borderRadius: BorderRadius.circular(
                              kCircularBorderRadius * 0.5),
                        ),
                        child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.spaceAround,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                answerMessage.sender !=
                                        _auth.currentUser?.displayName
                                    ? answerMessage.sender
                                    : "Du",
                                style: TextStyle(color: Colors.redAccent),
                              ),
                              Text(
                                answerMessage.text,
                                overflow: TextOverflow.ellipsis,
                              ),
                            ],
                          ),
                        ),
                      ),
                    ),
                    IconButton(
                      onPressed: () {
                        setState(() {
                          print("closed answer");
                          isAnswerOpen = false;
                          answerMessage = MessageModel(
                            text: "text",
                            isMy: false,
                            isFav: false,
                            readByAll: false,
                            date: Timestamp.now(),
                            uid: "n.a.",
                            sender: "sender",
                            isImage: false,
                          );
                        });
                      },
                      icon: Icon(
                        FontAwesomeIcons.solidTimesCircle,
                        size: 28,
                      ),
                    )
                  ],
                ),
              ),
            ),
          );
  }
}
