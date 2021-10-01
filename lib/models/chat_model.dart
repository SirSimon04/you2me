import 'package:cloud_firestore/cloud_firestore.dart';

class ChatModel {
  final String name;
  final bool isGroup;
  final String uid;
  final String lastMessageText;
  final String lastMessageSender;
  final String lastMessageSenderId;
  final Timestamp lastMessageDate;
  bool isArchived;
  bool isPinned;
  final int userCount;
  final List<String> members;
  List<String> writing;
  final List<String> fotoUrls;
  final String? description;
  final List<String>? adminList;

  ChatModel({
    required this.name,
    required this.isGroup,
    required this.uid,
    required this.lastMessageSender,
    required this.lastMessageText,
    required this.lastMessageSenderId,
    required this.lastMessageDate,
    required this.userCount,
    required this.members,
    required this.writing,
    required this.isArchived,
    required this.isPinned,
    required this.fotoUrls,
    this.description,
    this.adminList,
  });

  static ChatModel getEmptyChat() {
    return ChatModel(
      userCount: 0,
      lastMessageText: "",
      isGroup: false,
      name: "",
      uid: "",
      members: [],
      writing: [],
      isArchived: false,
      isPinned: false,
      lastMessageDate: Timestamp.now(),
      lastMessageSenderId: "",
      lastMessageSender: "",
      fotoUrls: [],
    );
  }

  test() {
    lastMessageDate.millisecondsSinceEpoch;
  }
}
