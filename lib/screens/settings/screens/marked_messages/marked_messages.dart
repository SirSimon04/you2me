import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_dispuatio/models/Data.dart' as data;
import 'package:flutter_dispuatio/screens/chatting/chat/components/chat_message_bubble.dart';
import 'package:platform_list_tile/platform_list_tile.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

class MarkedMessages extends StatefulWidget {
  MarkedMessages({this.chatUid, required this.isInChat});

  final bool isInChat;
  final String? chatUid;

  @override
  _MarkedMessagesState createState() => _MarkedMessagesState();
}

class _MarkedMessagesState extends State<MarkedMessages> {
  final _auth = FirebaseAuth.instance;
  final _firestore = FirebaseFirestore.instance;
  final _push = FirebaseMessaging.instance;

  Future<List<MessageModel>> getMessages() async {
    List<MessageModel> messages = data.getMessages();
    return messages;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Mit Stern markiert"),
      ),
      body: Column(
        children: [
          Expanded(
            child: StreamBuilder<QuerySnapshot>(
              stream: _firestore
                  .collection("user")
                  .doc(_auth.currentUser?.uid)
                  .collection("markedmessages")
                  .orderBy("time")
                  .snapshots(),
              builder: (context, snapshot) {
                if (!snapshot.hasData) {
                  return Center(child: CircularProgressIndicator());
                } else {
                  final List<DocumentSnapshot> documents = snapshot.data!.docs;
                  return ListView(
                    children: documents.map((doc) {
                      if (widget.isInChat && doc["chatuid"] != widget.chatUid) {
                        return Container();
                      } else {
                        return Column(
                          children: [
                            PlatformListTile(
                              subtitle: Padding(
                                child: doc["sender"] ==
                                        _auth.currentUser?.displayName
                                    ? Text("Du")
                                    : Text(doc["sender"]),
                                padding: EdgeInsets.all(8.0),
                              ),
                              title: ChatMessageBubble(
                                allFav: true,
                                message: MessageModel(
                                  sender: doc["sender"],
                                  uid: doc.id,
                                  date: doc["time"],
                                  readByAll: false,
                                  isFav: true,
                                  isMy: doc["sender"] ==
                                      _auth.currentUser?.displayName,
                                  text: doc["text"],
                                  isImage: false,
                                ),
                                chat: ChatModel.getEmptyChat(),
                              ),
                            ),
                            Divider(
                              height: 5,
                              color: Colors.white,
                            )
                          ],
                        );
                      }
                    }).toList(),
                  );
                }
              },
            ),
          ),
        ],
      ),
    );
  }
}