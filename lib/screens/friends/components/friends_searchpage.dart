import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/services/general_services/toast_service.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class FriendsSearchPage extends StatefulWidget {
  const FriendsSearchPage({Key? key}) : super(key: key);

  @override
  _FriendsSearchPageState createState() => _FriendsSearchPageState();
}

class _FriendsSearchPageState extends State<FriendsSearchPage> {
  final textController = TextEditingController();
  final _auth = FirebaseAuth.instance;
  final _firestore = FirebaseFirestore.instance;

  String text = "";

  @override
  void dispose() {
    textController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Nach Nutzern suchen"),
      ),
      body: Column(
        children: [
          Container(
            padding: EdgeInsets.fromLTRB(
                kDefaultPadding, 0, kDefaultPadding, kDefaultPadding),
            color: Theme.of(context).scaffoldBackgroundColor,
            child: Column(
              children: [
                Padding(
                  padding: const EdgeInsets.only(top: 10),
                  child: TextField(
                    autofocus: true,
                    onChanged: (value) {
                      setState(() {
                        text = value;
                      });
                    },
                    controller: textController,
                    decoration: InputDecoration(
                      border: OutlineInputBorder(
                        borderRadius:
                            BorderRadius.circular(kCircularBorderRadius),
                      ),
                      hintText: "Suchen",
                      hintStyle: TextStyle(
                        color: Colors.white,
                      ),
                      filled: true,
                      prefixIcon: Icon(
                        FontAwesomeIcons.search,
                        color: Colors.white,
                      ),
                      prefixIconConstraints:
                          BoxConstraints(minWidth: 44, minHeight: 44),
                      isDense: true,
                      contentPadding: EdgeInsets.all(6),
                    ),
                  ),
                ),
                SizedBox(
                  height: 20,
                ),
              ],
            ),
          ), //Searchfield
          Expanded(
              child: StreamBuilder<QuerySnapshot>(
                  stream: _firestore
                      .collection("user")
                      .where("searchIndex", arrayContains: textController.text)
                      .snapshots(),
                  builder: (context, snapshot) {
                    if (snapshot.hasError) {
                      return Text("Error: ${snapshot.error}");
                    }
                    switch (snapshot.connectionState) {
                      case ConnectionState.waiting:
                        return Center(child: CircularProgressIndicator());
                      default:
                        final List<DocumentSnapshot> documents =
                            snapshot.data!.docs;
                        return ListView(
                          children: documents.map((doc) {
                            return PlatformListTile(
                              title: Text(doc["name"]),
                              subtitle: Text(
                                doc["info"] + doc["info"],
                                overflow: TextOverflow.ellipsis,
                              ),
                              leading: CircleAvatar(
                                backgroundImage: AssetImage("assets/user.png"),
                              ),
                              trailing: doc["friends"]
                                      .contains(_auth.currentUser?.uid)
                                  ? IconButton(
                                      onPressed: () {
                                        UserFirebaseService.deleteFriend(doc.id)
                                            .then(
                                          (value) => ToastService.showLongToast(
                                              "${doc["name"]} wurde als Freund entfernt"),
                                        );
                                      },
                                      icon: Icon(FontAwesomeIcons.userMinus))
                                  : IconButton(
                                      onPressed: () {
                                        UserFirebaseService.addFriend(doc.id)
                                            .then(
                                          (value) => ToastService.showLongToast(
                                              "${doc["name"]} wurde als Freund hinzugefügt"),
                                        );
                                      },
                                      icon: Icon(FontAwesomeIcons.userPlus)),
                            );
                          }).toList(),
                        );
                    }
                  }))
        ],
      ),
    );
  }
}
