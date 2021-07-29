import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/user_model.dart';
import 'package:modal_bottom_sheet/modal_bottom_sheet.dart';

createGroupModalMaterialBottomSheet2(
    BuildContext context, List<UserModel> addedUsers) {
  showMaterialModalBottomSheet(
    context: context,
    backgroundColor: Theme.of(context).scaffoldBackgroundColor,
    builder: (context) => CreateGroup2(
      addedUsers: addedUsers,
    ),
  );
}

class CreateGroup2 extends StatefulWidget {
  CreateGroup2({required this.addedUsers});

  final List<UserModel> addedUsers;

  @override
  _CreateGroup2State createState() => _CreateGroup2State();
}

class _CreateGroup2State extends State<CreateGroup2> {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: MediaQuery.of(context).size.height * 0.9,
    );
  }
}
