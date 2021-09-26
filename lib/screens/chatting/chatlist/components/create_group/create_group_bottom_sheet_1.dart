import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/models/user_model.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/components/create_group/create_group_bottom_sheet_2.dart';
import 'package:flutter_dispuatio/widgets/userprofile_pic.dart';
import 'package:flutter_svg/svg.dart';
import 'package:modal_bottom_sheet/modal_bottom_sheet.dart';

createGroupModalMaterialBottomSheet1(BuildContext context) {
  showMaterialModalBottomSheet(
    context: context,
    backgroundColor: Theme.of(context).scaffoldBackgroundColor,
    builder: (context) => CreateGroup1(),
  );
}

class CreateGroup1 extends StatefulWidget {
  const CreateGroup1({Key? key}) : super(key: key);

  @override
  _CreateGroup1State createState() => _CreateGroup1State();
}

class _CreateGroup1State extends State<CreateGroup1> {
  final _auth = FirebaseAuth.instance;
  final _firestore = FirebaseFirestore.instance;

  final textController = TextEditingController();

  Map<String, bool> usersChecked = {};

  Map<String, UserModel> usersSelected = {};

  void buildMap() async {
    var documents = await _firestore
        .collection("user")
        .where("friends", arrayContainsAny: [_auth.currentUser?.uid]).get();
    print(documents.docs[0]["name"]);
    for (var doc in documents.docs) {
      usersChecked[doc["name"]] = false;
    }
    print(usersChecked);
  }

  Future<QuerySnapshot<Object?>>? _future;

  @override
  void initState() {
    super.initState();
    buildMap();
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
                                    value: usersChecked[doc["name"]],
                                    selected:
                                        usersChecked[doc["name"]] ?? false,
                                    onChanged: (bool? newVal) {
                                      print("newVal " + newVal.toString());
                                      setState(() {
                                        usersChecked[doc["name"]] =
                                            newVal ?? false;
                                      });

                                      //add to selected if not added before
                                      if (newVal == true) {
                                        usersSelected[doc["name"]] = UserModel(
                                          uid: doc.id,
                                          name: doc["name"],
                                          isOnline: doc["isonline"],
                                          profilePic: UserProfilePic(
                                            url: doc["fotourl"],
                                            isOnline: doc["isonline"],
                                          ),
                                        );
                                      }
                                      if (newVal == false) {
                                        usersSelected.remove(doc["name"]);
                                      }

                                      print("usersSelected " +
                                          usersSelected.toString());
                                    },
                                    title: Text(doc["name"]),
                                    secondary: UserProfilePic(
                                      url: doc["fotourl"],
                                      isOnline: doc["isonline"],
                                    ),
                                    activeColor: Theme.of(context).primaryColor,
                                    selectedTileColor: Theme.of(context)
                                        .primaryColor
                                        .withOpacity(0.05),
                                    // Theme.of(context).primaryColor,,
                                    // checkColor: Theme.of(context).primaryColor,
                                  ))
                              .toList());
                    default:
                      return Container();
                  }
                },
              ),
            ),
            Padding(
              padding: EdgeInsets.symmetric(vertical: 16.0),
              child: Material(
                elevation: 5.0,
                borderRadius: BorderRadius.circular(30.0),
                color: usersSelected.length != 0
                    ? Theme.of(context).primaryColor
                    : Colors.grey,
                child: MaterialButton(
                  onPressed: usersSelected.length != 0
                      ? () {
                          List<UserModel> addedUsers = [];

                          usersSelected.forEach((key, value) {
                            addedUsers.add(value);
                          });

                          createGroupModalMaterialBottomSheet2(
                              context, addedUsers);
                        }
                      : null,
                  minWidth: MediaQuery.of(context).size.width * 0.6,
                  height: MediaQuery.of(context).size.height * 0.07,
                  child: Text(
                    "Weiter",
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
    );
  }
}
