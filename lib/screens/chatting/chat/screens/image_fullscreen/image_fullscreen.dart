import 'dart:typed_data';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_dispuatio/models/message_model.dart';
import 'package:flutter_dispuatio/services/general_services/toast_service.dart';
import 'package:image_gallery_saver/image_gallery_saver.dart';
import 'package:intl/intl.dart';

class ImageFullscreen extends StatefulWidget {
  ImageFullscreen(this.msg);

  final MessageModel msg;

  @override
  _ImageFullscreenState createState() => _ImageFullscreenState();
}

class _ImageFullscreenState extends State<ImageFullscreen> {
  final _auth = FirebaseAuth.instance;

  void saveImage() async {
    ToastService.showLongToast("Das Bild wird heruntergeladen");
    try {
      Uint8List imageBytes =
          (await NetworkAssetBundle(Uri.parse(widget.msg.url ?? ""))
                  .load(widget.msg.url ?? ""))
              .buffer
              .asUint8List();
      await ImageGallerySaver.saveImage(imageBytes);
      ToastService.showLongToast("Das Bild wurde erfolgreich heruntergeladen");
    } catch (e) {
      ToastService.showLongToast(
          "Beim Herunterladen des Bildes ist ein Fehler aufgetreten.");
    }
  }

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
        actions: [
          IconButton(
            onPressed: saveImage,
            icon: Icon(
              Icons.file_download,
            ),
            color: Colors.white,
          ),
        ],
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
