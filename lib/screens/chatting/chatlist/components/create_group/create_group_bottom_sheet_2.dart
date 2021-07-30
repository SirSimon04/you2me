import 'dart:io';

import 'package:adaptive_action_sheet/adaptive_action_sheet.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/models/user_model.dart';
import 'package:flutter_dispuatio/widgets/chip.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:image_picker/image_picker.dart';
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
  _CreateGroup2State createState() => _CreateGroup2State(addedUsers);
}

class _CreateGroup2State extends State<CreateGroup2> {
  _CreateGroup2State(this.addedUsers);

  final List<UserModel> addedUsers;

  final nameController = TextEditingController();
  final desController = TextEditingController();

  Future getImageFromCam() async {
    File imageFile = File(await ImagePicker()
        .getImage(source: ImageSource.camera)
        .then((pickedFile) => pickedFile!.path));

    Navigator.of(context).pop();
  }

  Future getImageFromGallery() async {
    File imageFile = File(await ImagePicker()
        .getImage(source: ImageSource.gallery)
        .then((pickedFile) => pickedFile!.path));
    Navigator.of(context).pop();
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    print(addedUsers);
    return GestureDetector(
      onTap: () => FocusScope.of(context).requestFocus(new FocusNode()),
      child: Container(
        height: MediaQuery.of(context).size.height * 0.9,
        child: Scaffold(
          appBar: AppBar(
            title: Text(
              "Neue Gruppe erstellen",
              style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 30,
              ),
            ),
            automaticallyImplyLeading: false,
            elevation: 0,
          ),
          body: SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                SizedBox(
                  height: kDefaultPadding,
                ),
                Stack(
                  children: [
                    CircleAvatar(
                      backgroundImage: AssetImage("assets/user.png"),
                      radius: MediaQuery.of(context).size.width * 0.25,
                    ),
                    Positioned(
                      right: 0,
                      bottom: 0,
                      child: Container(
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          color: Theme.of(context).scaffoldBackgroundColor,
                          border: Border.all(
                            color: Theme.of(context).scaffoldBackgroundColor,
                            width: 3,
                          ),
                        ),
                        child: Padding(
                          padding: EdgeInsets.all(
                              MediaQuery.of(context).size.width * 0.01),
                          child: IconButton(
                            splashColor: Colors.transparent,
                            highlightColor: Colors.transparent,
                            icon: Icon(
                              Icons.add_a_photo,
                              size: MediaQuery.of(context).size.width * 0.1,
                            ),
                            onPressed: () {
                              if (Platform.isIOS) {
                                showCupertinoModalPopup(
                                  context: context,
                                  builder: (context) => CupertinoActionSheet(),
                                );
                              } else {
                                showAdaptiveActionSheet(
                                  context: context,
                                  actions: [
                                    BottomSheetAction(
                                      title: Text(
                                        "Fotomediathek",
                                        textAlign: TextAlign.center,
                                      ),
                                      onPressed: getImageFromGallery,
                                      trailing:
                                          Icon(FontAwesomeIcons.solidImages),
                                    ),
                                    BottomSheetAction(
                                        title: Text(
                                          "Foto aufnehmen",
                                          textAlign: TextAlign.center,
                                        ),
                                        onPressed: getImageFromCam,
                                        trailing:
                                            Icon(FontAwesomeIcons.camera)),
                                  ],
                                  cancelAction: CancelAction(
                                    title: const Text('Schließen'),
                                  ),
                                );
                              }
                            },
                          ),
                        ),
                      ),
                    )
                  ],
                ),
                SizedBox(
                  height: kDefaultPadding * 2,
                ),
                Center(
                  child: Container(
                    width: MediaQuery.of(context).size.width * 0.8,
                    child: TextField(
                      keyboardType: TextInputType.multiline,
                      cursorColor: Theme.of(context).primaryColor,
                      decoration: InputDecoration(
                        hintText: "Gruppenname",
                        focusedBorder: OutlineInputBorder(
                          borderRadius:
                              BorderRadius.circular(kCircularBorderRadius),
                          borderSide: BorderSide(
                            color: Theme.of(context).primaryColor,
                            width: 3,
                          ),
                        ),
                        border: OutlineInputBorder(
                          borderRadius:
                              BorderRadius.circular(kCircularBorderRadius),
                        ),
                        filled: true,
                        isDense: true,
                        contentPadding: EdgeInsets.all(14),
                      ),
                      minLines: 1,
                      maxLines: 2,
                      controller: nameController,
                    ),
                  ),
                ),
                SizedBox(
                  height: kDefaultPadding,
                ),
                Center(
                  child: Container(
                    width: MediaQuery.of(context).size.width * 0.8,
                    child: TextField(
                      keyboardType: TextInputType.multiline,
                      cursorColor: Theme.of(context).primaryColor,
                      decoration: InputDecoration(
                        hintText: "Gruppenbeschreibung",
                        focusedBorder: OutlineInputBorder(
                          borderRadius:
                              BorderRadius.circular(kCircularBorderRadius),
                          borderSide: BorderSide(
                            color: Theme.of(context).primaryColor,
                            width: 3,
                          ),
                        ),
                        border: OutlineInputBorder(
                          borderRadius:
                              BorderRadius.circular(kCircularBorderRadius),
                        ),
                        filled: true,
                        isDense: true,
                        contentPadding: EdgeInsets.all(14),
                      ),
                      minLines: 1,
                      maxLines: 2,
                      controller: nameController,
                    ),
                  ),
                ),
                SizedBox(
                  height: kDefaultPadding,
                ),
                SizedBox(
                  height: MediaQuery.of(context).size.height * 0.18,
                  child: SingleChildScrollView(
                    scrollDirection: Axis.vertical,
                    child: Wrap(
                      direction: Axis.horizontal,
                      spacing: 8.0,
                      runSpacing: 6.0,
                      children: addedUsers
                          .map((e) => customColorChip(
                              label: e.name,
                              avatar: e.profilePic ?? Container()))
                          .toList(),
                    ),
                  ),
                ),
                SizedBox(
                  height: kDefaultPadding,
                ),
                Padding(
                  padding: EdgeInsets.symmetric(vertical: 16.0),
                  child: Material(
                    elevation: 5.0,
                    borderRadius: BorderRadius.circular(30.0),
                    color: nameController.text.trim().length == 0
                        ? Colors.grey
                        : Theme.of(context).primaryColor,
                    child: MaterialButton(
                      onPressed: nameController.text.trim().length == 0
                          ? null
                          : () {
                              Navigator.of(context).pop();
                              Navigator.of(context).pop();
                            },
                      minWidth: MediaQuery.of(context).size.width * 0.6,
                      height: MediaQuery.of(context).size.height * 0.07,
                      child: Text(
                        "Gruppe erstellen",
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 20.0,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
