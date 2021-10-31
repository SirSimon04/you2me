import 'package:cached_network_image/cached_network_image.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/services/general_services/toast_service.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:flutter_svg/svg.dart';
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
        title: Text("Nach Nutzern suchen",
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
                    cursorColor: Theme.of(context).primaryColor,
                    controller: textController,
                    decoration: InputDecoration(
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
                        borderSide: BorderSide(
                          color: Theme.of(context).primaryColor,
                          width: 3,
                        ),
                      ),
                      hintText: "Suchen",
                      filled: true,
                      prefixIcon: Icon(
                        FontAwesomeIcons.search,
                        color: kPrimaryColor,
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
                        if (snapshot.data!.docs.length == 0 &&
                            textController.text.trim().isEmpty) {
                          return SingleChildScrollView(
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.start,
                              children: [
                                Padding(
                                  padding: EdgeInsets.all(40),
                                  child: SvgPicture.asset(
                                    "assets/placeholders/add_friend.svg",
                                    height: MediaQuery.of(context).size.height *
                                        0.25,
                                  ),
                                ),
                                Text(
                                  "Suche nach Nutzern, um sie als Freunde hinzuzufügen.",
                                  textAlign: TextAlign.center,
                                )
                              ],
                            ),
                          );
                        }

                        if (snapshot.data!.docs.length == 0) {
                          return SingleChildScrollView(
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Padding(
                                  padding: EdgeInsets.all(40),
                                  child: SvgPicture.asset(
                                    "assets/placeholders/no_results.svg",
                                    height: MediaQuery.of(context).size.height *
                                        0.25,
                                  ),
                                ),
                                Text(
                                  "Der gesuchte Nutzer wurde nicht gefunden.",
                                  textAlign: TextAlign.center,
                                )
                              ],
                            ),
                          );
                        }

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
                                backgroundImage:
                                    CachedNetworkImageProvider(doc["fotourl"]),
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
