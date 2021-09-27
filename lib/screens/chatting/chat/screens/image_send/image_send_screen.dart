import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:flutter_dispuatio/services/general_services/firebase_storage_service.dart';
import 'package:flutter_dispuatio/services/user_services/GeneralUserService.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:modal_progress_hud_nsn/modal_progress_hud_nsn.dart';

class ImageSendScreen extends StatefulWidget {
  ImageSendScreen({required this.image, required this.chat});

  final File image;
  final ChatModel chat;

  @override
  _ImageSendScreenState createState() => _ImageSendScreenState();
}

class _ImageSendScreenState extends State<ImageSendScreen> {
  bool showSpinner = false;

  @override
  Widget build(BuildContext context) {
    return ModalProgressHUD(
      inAsyncCall: showSpinner,
      child: Scaffold(
        appBar: AppBar(
          title: Text("Bild versenden",
              style: TextStyle(
                color: Colors.white,
              )),
          automaticallyImplyLeading: false,
          leading: IconButton(
            icon: Icon(Icons.arrow_back),
            color: Colors.white,
            onPressed: () => Navigator.of(context).pop(),
          ),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () async {
            setState(() {
              showSpinner = true;
            });
            await ChatFirebaseService.updateChatAfterSend(
                chatUid: widget.chat.uid,
                text: "Foto",
                memberCount: widget.chat.members.length);
            String url = await FireBaseStorageService.uploadImage(widget.image);
            await ChatFirebaseService.sendImage(
              chatUid: widget.chat.uid,
              url: url,
              usersUid: GeneralUserService.getUidsFromMembers(widget.chat),
            );

            setState(() {
              showSpinner = false;
            });
            Navigator.of(context).pop();
          },
          child: Icon(
            FontAwesomeIcons.solidPaperPlane,
            color: Colors.white,
          ),
          backgroundColor: Colors.grey[600],
        ),
        body: Container(
          decoration: BoxDecoration(
            image: DecorationImage(
              image: FileImage(widget.image),
            ),
          ),
          child: null /* add child content here */,
        ),
      ),
    );
  }
}
