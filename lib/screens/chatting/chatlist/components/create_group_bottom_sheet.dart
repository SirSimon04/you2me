import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/widgets/userprofile_pic.dart';
import 'package:flutter_svg/svg.dart';
import 'package:modal_bottom_sheet/modal_bottom_sheet.dart';

createGroupModalMaterialBottomSheet(BuildContext context) {
  showMaterialModalBottomSheet(
    context: context,
    backgroundColor: Theme.of(context).scaffoldBackgroundColor,
    builder: (context) => CreateGroup(),
  );
}

class CreateGroup extends StatefulWidget {
  const CreateGroup({Key? key}) : super(key: key);

  @override
  _CreateGroupState createState() => _CreateGroupState();
}

class _CreateGroupState extends State<CreateGroup> {
  final _auth = FirebaseAuth.instance;
  final _firestore = FirebaseFirestore.instance;

  bool _checked = false;

  Future<QuerySnapshot<Map<String, dynamic>>> getDocuments() async {
    var documents = await _firestore
        .collection("user")
        .where("friends", arrayContainsAny: [_auth.currentUser?.uid]).get();
    print(documents.docs[0]["name"]);
    return documents;
  }

  Future<QuerySnapshot<Object?>>? _future;

  @override
  void initState() {
    super.initState();
    _future = _firestore
        .collection("user")
        .where("friends", arrayContainsAny: [_auth.currentUser?.uid]).get();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
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
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Padding(
              padding: const EdgeInsets.all(50.0),
              child: Text(
                "Wähle die neuen Gruppenmitglieder aus",
                textAlign: TextAlign.center,
              ),
            ),
            Expanded(
              child: FutureBuilder<QuerySnapshot>(
                future: _future,
                builder: (BuildContext context, snapshot) {
                  print(snapshot);
                  switch (snapshot.connectionState) {
                    case ConnectionState.waiting:
                      return Center(child: CircularProgressIndicator());
                    case ConnectionState.done:
                      if (snapshot.data!.docs.length == 0) {
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
                                      "Du hast noch keine Freunde, um sie zu einer Gruppe hinzuzufügen",
                                      textAlign: TextAlign.center,
                                    ),
                                  )
                                ],
                              ),
                            ),
                          ],
                        ); // Placeholder
                      }

                      final List<DocumentSnapshot> documents =
                          snapshot.data!.docs;
                      return ListView(
                          children: documents
                              .map((doc) => CheckboxListTile(
                                    value: _checked,
                                    onChanged: (bool? newVal) {
                                      setState(() {
                                        _checked = newVal ?? false;
                                      });
                                    },
                                    title: Text(doc["name"]),
                                    secondary: UserProfilePic(
                                      url: doc["fotourl"],
                                      isOnline: doc["isonline"],
                                    ),
                                  ))
                              .toList());
                    default:
                      return Container();
                  }
                  return Text("");
                },
              ),
              // child: StreamBuilder<QuerySnapshot>(
              //   stream: _firestore.collection("user").where("friends",
              //       arrayContainsAny: [_auth.currentUser?.uid]).snapshots(),
              //   builder: (context, snapshot) {
              //     switch (snapshot.connectionState) {
              //       case ConnectionState.waiting:
              //         return Center(child: CircularProgressIndicator());
              //       default:
              //         if (snapshot.data!.docs.length == 0) {
              //           return Column(
              //             children: [
              //               SingleChildScrollView(
              //                 controller: ModalScrollController.of(context),
              //                 child: Column(
              //                   mainAxisAlignment: MainAxisAlignment.center,
              //                   children: [
              //                     Padding(
              //                       padding: EdgeInsets.all(40),
              //                       child: SvgPicture.asset(
              //                         "assets/placeholders/friends.svg",
              //                         height:
              //                             MediaQuery.of(context).size.height *
              //                                 0.3,
              //                         width: MediaQuery.of(context).size.width *
              //                             0.7,
              //                       ),
              //                     ),
              //                     Padding(
              //                       padding: const EdgeInsets.all(8.0),
              //                       child: Text(
              //                         "Du hast noch keine Freunde, um sie zu einer Gruppe hinzuzufügen",
              //                         textAlign: TextAlign.center,
              //                       ),
              //                     )
              //                   ],
              //                 ),
              //               ),
              //             ],
              //           ); // Placeholder
              //         }
              //
              //         final List<DocumentSnapshot> documents =
              //             snapshot.data!.docs;
              //         return ListView(
              //           children: documents
              //               .map(
              //                 (doc) => CheckboxListTile(
              //                   value: _checked,
              //                   onChanged: (bool? newV) {
              //                     setState(
              //                       () {
              //                         _checked = newV ?? false;
              //                       },
              //                     );
              //                   },
              //                 ),
              //               )
              //               .toList(),
              //           // PlatformListTile(
              //           //       title: Text(doc["name"]),
              //           //       subtitle: Text(doc["info"]),
              //           //       leading: UserProfilePic(
              //           //         isOnline: doc["isonline"],
              //           //         url: doc["fotourl"],
              //           //       ),
              //           //       isElevatedM: true,
              //           //     ))
              //           // .toList(),
              //         );
              //     }
              //   },
              // ),
            ),
          ],
        ),
      ),
    );
  }
}
