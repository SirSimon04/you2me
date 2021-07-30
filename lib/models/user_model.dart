import 'package:flutter/material.dart';

class UserModel {
  String name;
  bool isOnline;
  String? fotoUrl;
  Widget? profilePic;

  UserModel({
    required this.name,
    required this.isOnline,
    this.fotoUrl,
    this.profilePic,
  });

  static UserModel getEmptyUser() {
    return UserModel(
      name: "name",
      isOnline: false,
    );
  }
}
