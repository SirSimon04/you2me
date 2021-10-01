import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:flutter_dispuatio/services/general_services/toast_service.dart';
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:flutter_dispuatio/widgets/userprofile_pic.dart';
import 'package:flutter_svg/svg.dart';
import 'package:modal_bottom_sheet/modal_bottom_sheet.dart';

addUserBottomSheet(BuildContext context, ChatModel chat) {
  showMaterialModalBottomSheet(
    context: context,
    backgroundColor: Theme.of(context).scaffoldBackgroundColor,
    builder: (context) => AddUsers(chat),
  );
}

class AddUsers extends StatefulWidget {
  AddUsers(this.chat);

  final ChatModel chat;

  @override
  _AddUsersState createState() => _AddUsersState();
}

class _AddUsersState extends State<AddUsers> {
  final _auth = FirebaseAuth.instance;
  final _firestore = FirebaseFirestore.instance;

  @override
  Widget build(BuildContext context) {
    return Container(
      height: MediaQuery.of(context).size.height * 0.9,
      child: Scaffold(
        appBar: AppBar(
          title: Text(
            "Nutzer hinzufügen",
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 30,
              color: Colors.white,
            ),
            textAlign: TextAlign.center,
          ),
          automaticallyImplyLeading: false,
          elevation: 0,
        ),
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SizedBox(
              height: 50,
            ),
            Center(
              child: Text(
                "Wähle den neuen Nutzer aus",
                textAlign: TextAlign.center,
              ),
            ),
            SizedBox(
              height: 50,
            ),
            Expanded(
              child: FutureBuilder<QuerySnapshot>(
                future: _firestore.collection("user").where("friends",
                    arrayContainsAny: [_auth.currentUser?.uid]).get(),
                builder: (context, snapshot) {
                  print(snapshot);
                  switch (snapshot.connectionState) {
                    case ConnectionState.waiting:
                      return Center(child: CircularProgressIndicator());
                    case ConnectionState.done:
                      final List<DocumentSnapshot> documents =
                          snapshot.data!.docs;

                      List<dynamic> filteredDocs = [];

                      for (var doc in documents) {
                        print("docid " + doc.id);
                        if (!widget.chat.members.contains(doc.id)) {
                          filteredDocs.add(doc);
                        }
                      }

                      if (filteredDocs.length == 0) {
                        return Column(
                          children: [
                            SingleChildScrollView(
                              controller: ModalScrollController.of(context),
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Padding(
                                    padding: EdgeInsets.all(40),
                                    child: SvgPicture.asset(
                                      "assets/placeholders/friends.svg",
                                      height:
                                          MediaQuery.of(context).size.height *
                                              0.3,
                                      width: MediaQuery.of(context).size.width *
                                          0.7,
                                    ),
                                  ),
                                  Padding(
                                    padding: const EdgeInsets.all(8.0),
                                    child: Text(
                                      "Entweder sind alle deine Freunde bereits in dieser Gruppe oder du hast keine Freunde.",
                                      textAlign: TextAlign.center,
                                    ),
                                  )
                                ],
                              ),
                            ),
                          ],
                        ); // Placeholder
                      }

                      return ListView(
                        children: filteredDocs
                            .map(
                              (doc) => PlatformListTile(
                                title: Text(doc["name"]),
                                leading: UserProfilePic(
                                  url: doc["fotourl"],
                                  isOnline: doc["isonline"],
                                ),
                                isElevatedM: true,
                                onTap: () async {
                                  await ChatFirebaseService.addToGroup(
                                      uid: doc.id, chat: widget.chat);
                                  Navigator.of(context).pop();
                                  Navigator.of(context).pop();
                                  Navigator.of(context).pop();
                                  ToastService.showLongToast(doc["name"]
                                          .toString() +
                                      " wurde zur Gruppe ${widget.chat.name} hinzugefügt.");
                                },
                              ),
                            )
                            .toList(),
                      );

                    default:
                      return Container();
                  }
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
