import 'package:flutter/material.dart';

class UserModel {
  String uid;
  String name;
  bool isOnline;
  String? fotoUrl;
  Widget? profilePic;

  UserModel({
    required this.uid,
    required this.name,
    required this.isOnline,
    this.fotoUrl,
    this.profilePic,
  });

  static UserModel getEmptyUser() {
    return UserModel(
      uid: "",
      name: "name",
      isOnline: false,
    );
  }
}
