import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_dispuatio/models/story.dart';
import 'package:flutter_dispuatio/screens/chatting/chat/screens/image_send/image_send_screen.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:focused_menu/focused_menu.dart';
import 'package:focused_menu/modals.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter/foundation.dart' show kIsWeb;
import 'package:image_picker/image_picker.dart';
import 'chat_contextmenu_actions.dart';
import 'dart:io';

class SendMessage extends Notification {
  SendMessage();
}

class ChatInputRow extends StatefulWidget {
  final ChatModel chat;
  final ScrollController scrollController;
  final MessageModel answerMessage;
  ChatInputRow({
    required this.chat,
    required this.scrollController,
    required this.answerMessage,
  });

  @override
  _ChatInputRowState createState() => _ChatInputRowState(chat);
}

class _ChatInputRowState extends State<ChatInputRow> {
  ChatModel chat;
  _ChatInputRowState(this.chat);

  final textController = TextEditingController();

  Widget sendIconButton = Container();

  File _image = File("");
  final picker = ImagePicker();

  Future getImageFromCam() async {
    File imageFile = File(await ImagePicker()
        .getImage(source: ImageSource.camera)
        .then((pickedFile) => pickedFile!.path));

    Navigator.of(context).push(
      CupertinoPageRoute(
        builder: (context) => ImageSendScreen(image: imageFile, chat: chat),
      ),
    );
  }

  Future getImageFromGallery() async {
    File imageFile = File(await ImagePicker()
        .getImage(source: ImageSource.gallery)
        .then((pickedFile) => pickedFile!.path));

    Navigator.of(context).push(
      CupertinoPageRoute(
        builder: (context) => ImageSendScreen(image: imageFile, chat: chat),
      ),
    );
  }

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    textController.dispose();
    super.dispose();
  }

  Icon _rightContextButton = Icon(FontAwesomeIcons.ellipsisH);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 60,
      color: Colors.black26,
      child: Column(
        children: [
          SizedBox(
            height: kDefaultPadding / 2,
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              SizedBox(
                width: kDefaultPadding / 2,
              ),
              SizedBox(
                height: 24,
                width: 24,
                child: FocusedMenuHolder(
                  menuItems: getLeftContextMenuActions(
                    camera: getImageFromCam,
                    pic: getImageFromGallery,
                  ),
                  onPressed: () {},
                  openWithTap: true,
                  animateMenuItems: false,
                  bottomOffsetHeight: 10.0,
                  menuOffset: 10.0,
                  child: Icon(
                    FontAwesomeIcons.plus,
                  ),
                ),
              ),
              SizedBox(
                width: kDefaultPadding / 2,
              ),
              Expanded(
                child: TextField(
                  decoration: InputDecoration(
                    border: OutlineInputBorder(
                      borderRadius:
                          BorderRadius.circular(kCircularBorderRadius),
                    ),
                    filled: true,
                    isDense: true,
                    contentPadding: EdgeInsets.all(12),
                  ),
                  maxLines: null,
                  controller: textController,
                  onChanged: (text) {
                    setState(() {
                      checkUserInput();
                    });
                  },
                ),
              ),
              SizedBox(
                width: kDefaultPadding / 2,
              ),
              // SizedBox(
              //   height: 24,
              //   width: 24,
              //   child: FocusedMenuHolder(
              //     menuItems: getRightContextMenuActions(
              //       normalPressed: () {
              //         setState(() {
              //           _rightContextButton = Icon(FontAwesomeIcons.ellipsisH);
              //         });
              //       },
              //       importantPressed: () {
              //         setState(() {
              //           _rightContextButton =
              //               Icon(FontAwesomeIcons.exclamation);
              //         });
              //       },
              //       plannedPressed: () {
              //         setState(() {
              //           _rightContextButton =
              //               Icon(FontAwesomeIcons.solidCalendar);
              //         });
              //       },
              //     ),
              //     onPressed: () {},
              //     openWithTap: true,
              //     animateMenuItems: false,
              //     bottomOffsetHeight: 10.0,
              //     menuOffset: 10.0,
              //     child: _rightContextButton,
              //   ),
              // ),
              SizedBox(
                width: kDefaultPadding / 2,
              ),
              sendIconButton,
            ],
          ),
        ],
      ),
    );
  }

  checkUserInput() {
    if (textController.text.isNotEmpty) {
      UserFirebaseService.setWritingTrue(chat.uid);
      sendIconButton = Row(
        children: [
          SizedBox(
            height: 24,
            width: 24,
            child: IconButton(
              icon: Icon(
                FontAwesomeIcons.solidPaperPlane,
              ),
              padding: EdgeInsets.all(0),
              onPressed: () async {
                await ChatFirebaseService.sendMessage(
                  ansMsg: widget.answerMessage,
                  text: textController.text,
                  chatUid: widget.chat.uid,
                );

                await ChatFirebaseService.updateChatAfterSend(
                  chatUid: widget.chat.uid,
                  text: textController.text,
                );

                await UserFirebaseService.setWritingFalse(chat.uid);

                SendMessage().dispatch(context); //Notify parent msg got send

                setState(() {
                  // Reset textfield and scroll to bottom
                  textController.clear();
                  widget.scrollController.animateTo(
                    widget.scrollController.position.maxScrollExtent,
                    duration: Duration(milliseconds: 500),
                    curve: Curves.fastOutSlowIn,
                  );
                });
              },
            ),
          ),
          SizedBox(
            width: kDefaultPadding / 2,
          ),
        ],
      );
    } else {
      UserFirebaseService.setWritingFalse(chat.uid);
      sendIconButton = Container();
    }
  }
}
