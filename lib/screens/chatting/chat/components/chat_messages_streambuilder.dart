import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:implicitly_animated_reorderable_list/implicitly_animated_reorderable_list.dart';
import 'package:implicitly_animated_reorderable_list/transitions.dart';
import 'package:intl/intl.dart';
import 'chat_message_bubble.dart';

class ChatMessagesStreambuilder extends StatefulWidget {
  ChatMessagesStreambuilder({
    Key? key,
    required this.chat,
    required ScrollController controller,
  })  : _controller = controller,
        super(key: key);

  final ChatModel chat;
  final ScrollController _controller;

  @override
  State<ChatMessagesStreambuilder> createState() =>
      _ChatMessagesStreambuilderState();
}

class _ChatMessagesStreambuilderState extends State<ChatMessagesStreambuilder> {
  final FirebaseAuth _auth = FirebaseAuth.instance;

  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  @override
  Widget build(BuildContext context) {
    print("uid " + (_auth.currentUser?.uid ?? " leer"));
    return StreamBuilder<QuerySnapshot>(
      stream: _firestore
          .collection("chat")
          .doc(widget.chat.uid)
          .collection("messages")
          // .where("canbeseenby", whereIn: ["1Ob6BHEmqLUG83AJ0Dv6nKHZK4b2"])
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
          if (snapshot.data!.docs.length == 0) {
            return SingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Padding(
                    padding: EdgeInsets.all(40),
                    child: SvgPicture.asset(
                      "assets/placeholders/chat.svg",
                      height: MediaQuery.of(context).size.height * 0.5,
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: Text(
                      "Ihr habt bisher noch keine Nachrichten geschrieben. Fange direkt damit an.",
                      textAlign: TextAlign.center,
                    ),
                  )
                ],
              ),
            );
          }

          final List<DocumentSnapshot> allDocuments = snapshot.data!.docs;
          List<DocumentSnapshot> documents = [];
          for (DocumentSnapshot doc in allDocuments) {
            print(doc["time"]);
            if (doc["canbeseenby"].contains(_auth.currentUser?.uid)) {
              documents.add(doc);
            }
          }
          //TODO: smh add days
          //put documents.map in different function and after that method walk through and add dates
          return ImplicitlyAnimatedList<ChatMessageBubble>(
            controller: widget._controller,
            items: getListItems(documents),
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

  List<ChatMessageBubble> getListItems(List<DocumentSnapshot> documents) {
    List<ChatMessageBubble> msgList = documents.map((doc) {
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
            readByAll: widget.chat.userCount == readBy.length,
            sender: doc["sender"],
            isImage: doc["isimage"],
            url: doc["isimage"] ? doc["url"] : "",
          ),
          chat: widget.chat,
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
            readByAll: widget.chat.userCount == readBy.length,
            sender: doc["sender"],
            isImage: doc["isimage"],
            url: doc["isimage"] ? doc["url"] : "",
          ),
          chat: widget.chat,
        );
      }
    }).toList();

    var dateBubbles = new Map();

    int curDays = -1;

    for (ChatMessageBubble bubble in msgList) {
      // print("Tage " +
      //     (bubble.message.date.seconds / 86400).truncate().toString());
      if (curDays != (bubble.message.date.seconds / 86400).truncate()) {
        //we have new day
        // print("new day");
        curDays = (bubble.message.date.seconds / 86400).truncate(); //upd
        // ate days
        dateBubbles[msgList.indexOf(bubble)] = ChatMessageBubble(
          isDate: true,
          chat: ChatModel.getEmptyChat(),
          message: MessageModel.getEmptyMessage(),
          dateString: DateFormat('dd.MM. yyyy').format(
              DateTime.fromMillisecondsSinceEpoch(
                  bubble.message.date.seconds * 1000)),
        );
      }
      curDays = (bubble.message.date.seconds / 86400).truncate();
    }

    // print(dateBubbles);

    int i = 0;

    dateBubbles.forEach((key, value) {
      msgList.insert(key + i, value);
      i++;
    });

    // for (int i = 0; i < dateBubbles.length; i++) {
    //   msgList.insert(
    //       dateBubbles[i],
    //       ChatMessageBubble(
    //         isDate: true,
    //         chat: ChatModel.getEmptyChat(),
    //         message: MessageModel.getEmptyMessage(),
    //       ));
    // }

    // msgList.add(ChatMessageBubble(
    //   isDate: true,
    //   chat: ChatModel.getEmptyChat(),
    //   message: MessageModel.getEmptyMessage(),
    // ));

    return msgList;
  }
}
