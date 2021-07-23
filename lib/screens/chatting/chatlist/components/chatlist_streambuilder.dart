import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:implicitly_animated_reorderable_list/implicitly_animated_reorderable_list.dart';
import 'package:implicitly_animated_reorderable_list/transitions.dart';

import 'chatlist_tile.dart';

class ChatListStreamBuilder extends StatelessWidget {
  ChatListStreamBuilder({required this.isArchiveOpen});

  final bool isArchiveOpen;

  final _auth = FirebaseAuth.instance;
  final _firestore = FirebaseFirestore.instance;

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: StreamBuilder<QuerySnapshot>(
        stream: isArchiveOpen
            ? _firestore
                .collection("chat")
                .where("archivedby", arrayContains: _auth.currentUser?.uid)
                .snapshots()
            : _firestore
                .collection("chat")
                .where("notarchivedby", arrayContains: _auth.currentUser!.uid)
                //.where("members",
                //    arrayContains: "1Ob6BHEmqLUG83AJ0Dv6nKHZK4b2|simi")
                .snapshots(),
        builder: (context, snapshot) {
          if (!snapshot.hasData) {
            return CupertinoActivityIndicator();
            // return ListView.builder(
            //   shrinkWrap: true,
            //   itemBuilder: (context, index) => ChatListLoading(),
            //   itemCount: 10,
            // );
          } else {
            List<DocumentSnapshot> allChatsUnsorted = snapshot.data!.docs;

            List<DocumentSnapshot> pinned = [];
            List<DocumentSnapshot> notPinned = [];

            List<DocumentSnapshot> allChatsSorted = [];

            // print("pin debug:::;");
            for (DocumentSnapshot doc in allChatsUnsorted) {
              //   print(doc["pinnedby"].contains(_auth.currentUser?.uid));
              if (doc["pinnedby"].contains(_auth.currentUser?.uid)) {
                pinned.add(doc);
              } else {
                notPinned.add(doc);
              }
            }
            allChatsSorted.addAll(pinned);
            allChatsSorted.addAll(notPinned);

            return ImplicitlyAnimatedList<ChatListTile>(
              items: allChatsSorted.map((doc) {
                List users = doc["members"];
                return ChatListTile(
                  ChatModel(
                      name: doc["isgroup"]
                          ? doc["name"]
                          : getName(doc["members"]),
                      isGroup: doc["isgroup"],
                      uid: doc.id,
                      userCount: users.length,
                      lastMessageSender: doc["lastmessagesendername"],
                      lastMessageSenderId: doc["lastmessagesenderid"],
                      lastMessageDate: doc["lastmessagedate"],
                      lastMessageText: doc["lastmessagetext"],
                      members: List<String>.from(doc["members"]),
                      writing: List<String>.from(doc["writing"]),
                      isArchived: isArchiveOpen,
                      isPinned:
                          doc["pinnedby"].contains(_auth.currentUser?.uid),
                      fotoUrls: List<String>.from(
                        doc["fotourls"],
                      )),
                );
              }).toList(),
              areItemsTheSame: (a, b) => a.chat.uid == b.chat.uid,
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
            );
          }
        },
      ),
    );
  }

  String getName(doc) {
    print(doc);
    if (doc[0] ==
        (_auth.currentUser?.uid ?? "") +
            "|" +
            (_auth.currentUser?.displayName ?? "")) {
      return (doc[1]).toString().split("|")[1];
    } else {
      return (doc[0]).toString().split("|")[1];
    }
  }
}