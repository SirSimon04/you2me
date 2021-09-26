import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/Data.dart' as data;
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chat/components/chat_message_bubble.dart';
import 'package:flutter_svg/svg.dart';
import 'package:platform_list_tile/platform_list_tile.dart';

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

  Future<List<MessageModel>> getMessages() async {
    List<MessageModel> messages = data.getMessages();
    return messages;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "Mit Stern markiert",
          style: TextStyle(
            color: Colors.white,
          ),
        ),
        automaticallyImplyLeading: false,
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          color: Colors.white,
          onPressed: () => Navigator.of(context).pop(),
        ),
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
                  if (snapshot.data!.docs.length == 0) {
                    return SingleChildScrollView(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Padding(
                            padding: EdgeInsets.all(40),
                            child: SvgPicture.asset(
                              "assets/placeholders/marked_messages.svg",
                              height: MediaQuery.of(context).size.height * 0.5,
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(20.0),
                            child: Text(
                              widget.isInChat
                                  ? "Du hast in diesem Chat bisher noch keine Nachricht mit Stern markiert. Drücke lange auf eine Nachricht, um sie mit einem Stern markieren zu können."
                                  : "Du hast bisher noch keine Nachricht mit Stern markiert. Drücke lange auf eine Nachricht, um sie mit einem Stern markieren zu können.",
                              textAlign: TextAlign.center,
                            ),
                          )
                        ],
                      ),
                    );
                  }

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
