import 'package:flutter/material.dart';

class UserModel {
  final String name;
  final bool isOnline;
  final String? fotoUrl;
  final Widget? profilePic;
  bool? isSelected;

  UserModel({
    required this.name,
    required this.isOnline,
    required this.fotoUrl,
    required this.profilePic,
    required this.isSelected,
  });
}
