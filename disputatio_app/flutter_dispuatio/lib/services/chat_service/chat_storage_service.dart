import 'dart:io';

import 'package:firebase_storage/firebase_storage.dart' as firebase_storage;
import 'package:path/path.dart' as Path;

class ChatStorageService {
  static firebase_storage.FirebaseStorage storage =
      firebase_storage.FirebaseStorage.instance;

  static Future<String> uploadImage(File image) async {
    String filename = Path.basename(image.path);
    firebase_storage.Reference ref = storage.ref().child("upload/$filename");
    // firebase_storage.UploadTask uploadTask = ref.putFile(image);
    String url = "test";
    await ref.putFile(image).then((value) async {
      url = await value.ref.getDownloadURL();
    });
    return url;
  }
}
