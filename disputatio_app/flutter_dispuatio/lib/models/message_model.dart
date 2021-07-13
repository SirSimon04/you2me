import 'package:cloud_firestore/cloud_firestore.dart';

class MessageModel {
  final String text;
  final bool isMy;
  final bool isFav;
  final Timestamp date;
  final bool readByAll;
  final String uid;
  final String sender;
  final bool isImage;
  final String? url;
  MessageModel({
    required this.text,
    required this.isMy,
    required this.isFav,
    required this.readByAll,
    required this.date,
    required this.uid,
    required this.sender,
    required this.isImage,
    this.url,
  });

  @override
  String toString() {
    return super.toString();
  }
}
