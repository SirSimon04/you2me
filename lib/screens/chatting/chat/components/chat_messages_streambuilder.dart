import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:implicitly_animated_reorderable_list/implicitly_animated_reorderable_list.dart';
import 'package:implicitly_animated_reorderable_list/transitions.dart';

import 'chat_message_bubble.dart';

class ChatMessagesStreambuilder extends StatelessWidget {
  ChatMessagesStreambuilder({
    Key? key,
    required this.chat,
    required ScrollController controller,
  })  : _controller = controller,
        super(key: key);

  final ChatModel chat;
  final ScrollController _controller;
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;
  @override
  Widget build(BuildContext context) {
    return StreamBuilder<QuerySnapshot>(
      stream: _firestore
          .collection("chat")
          .doc(chat.uid)
          .collection("messages")
          .orderBy("time")
          .snapshots(),
      builder: (context, snapshot) {
        if (!snapshot.hasData) {
          return Container();
          // return Center(
          //   child: Center(
          //     child: CircularProgressIndicator(),
          //   ),
          // );
        } else {
          final List<DocumentSnapshot> documents = snapshot.data!.docs;

          return ImplicitlyAnimatedList<ChatMessageBubble>(
            controller: _controller,
            items: documents.map((doc) {
              List favBy = doc["favby"];
              List readBy = doc["readby"];

              try {
                //if message is an answer message
                return ChatMessageBubble(
                  message: MessageModel(
                    text: doc["text"],
                    isMy: doc["senderid"] == _auth.currentUser?.uid,
                    isFav: favBy.contains(_auth.currentUser?.uid),
                    date: doc["time"],
                    uid: doc.id,
                    readByAll: chat.userCount == readBy.length,
                    sender: doc["sender"],
                    isImage: doc["isimage"],
                    url: doc["isimage"] ? doc["url"] : "",
                  ),
                  chat: chat,
                  answerMessage: MessageModel(
                    uid: "",
                    text: doc["answertext"],
                    sender: doc["answersender"],
                    isMy: false,
                    isFav: false,
                    readByAll: false,
                    isImage: false,
                    date: Timestamp.now(),
                  ),
                );
              } catch (e) {
                return ChatMessageBubble(
                  message: MessageModel(
                    text: doc["text"],
                    isMy: doc["senderid"] == _auth.currentUser?.uid,
                    isFav: favBy.contains(_auth.currentUser?.uid),
                    date: doc["time"],
                    uid: doc.id,
                    readByAll: chat.userCount == readBy.length,
                    sender: doc["sender"],
                    isImage: doc["isimage"],
                    url: doc["isimage"] ? doc["url"] : "",
                  ),
                  chat: chat,
                );
              }
            }).toList(),
            itemBuilder: (context, animation, item, index) {
              // Specifiy a transition to be used by the ImplicitlyAnimatedList.
              // See the Transitions section on how to import this transition.
              return SizeFadeTransition(
                sizeFraction: 0.7,
                curve: Curves.easeInOut,
                animation: animation,
                child: item,
              );
            },
            areItemsTheSame: (a, b) => a.message.date == b.message.date,
          );
        }
      },
    );
  }
}
