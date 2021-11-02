import 'package:flutter/material.dart';

class UserModel {
  String uid;
  String name;
  bool isOnline;
  String? fotoUrl;
  Widget? profilePic;
  List? fcmIds;

  UserModel({
    required this.uid,
    required this.name,
    required this.isOnline,
    this.fotoUrl,
    this.profilePic,
    this.fcmIds,
  });

  static UserModel getEmptyUser() {
    return UserModel(
      uid: "",
      name: "name",
      isOnline: false,
    );
  }
}
