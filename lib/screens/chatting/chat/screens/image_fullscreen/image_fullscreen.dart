import 'package:cached_network_image/cached_network_image.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:intl/intl.dart';

class ImageFullscreen extends StatefulWidget {
  ImageFullscreen(this.msg);

  final MessageModel msg;

  @override
  _ImageFullscreenState createState() => _ImageFullscreenState();
}

class _ImageFullscreenState extends State<ImageFullscreen> {
  final _auth = FirebaseAuth.instance;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              "Foto von ${widget.msg.sender == _auth.currentUser?.displayName ? "Dir" : widget.msg.sender}",
              style: TextStyle(color: Colors.white),
            ),
            Text(
              DateFormat('dd.MM. yyyy').format(
                  DateTime.fromMillisecondsSinceEpoch(
                      widget.msg.date.seconds * 1000)),
              style: TextStyle(
                fontSize: 12,
              ),
            )
          ],
        ),
        automaticallyImplyLeading: false,
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          color: Colors.white,
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: Center(
        child: InteractiveViewer(
          clipBehavior: Clip.none,
          child: CachedNetworkImage(
            imageUrl: widget.msg.url ?? "",
          ),
        ),
      ),
    );
  }
}
