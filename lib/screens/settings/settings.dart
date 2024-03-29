import 'dart:io';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/settings/screens/change_account/change_account.dart';
import 'package:flutter_dispuatio/screens/settings/screens/marked_messages/marked_messages.dart';
import 'package:flutter_dispuatio/services/chat_service/chat_firebase_service.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:flutter_dispuatio/widgets/loader.dart';
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:image_picker/image_picker.dart';
import 'package:url_launcher/url_launcher.dart';

class Settings extends StatefulWidget {
  const Settings({Key? key}) : super(key: key);

  @override
  _SettingsState createState() => _SettingsState();
}

class _SettingsState extends State<Settings>
    with AutomaticKeepAliveClientMixin {
  final _auth = FirebaseAuth.instance;
  final _firestore = FirebaseFirestore.instance;
  final _fcm = FirebaseMessaging.instance;

  final TextEditingController _infoController = TextEditingController();

  final picker = ImagePicker();

  String fcmId = "";

  final String _url = "https://sirsimon04.github.io/you2me-data-policy/";

  bool _isLoading = false;

  updateFcmId() async {
    String newFcmId = await _fcm.getToken() ?? "";
    setState(() {
      fcmId = newFcmId;
    });
    print("FCMID " + newFcmId + "   " + fcmId);
  }

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    super.initState();
    updateFcmId();
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return Scaffold(
      appBar: AppBar(
        title: Text("Settings", style: TextStyle(color: Colors.white)),
        automaticallyImplyLeading: false,
      ),
      body: Stack(
        children: [
          StreamBuilder<DocumentSnapshot>(
              stream: _firestore
                  .collection("user")
                  .doc(_auth.currentUser?.uid ?? "")
                  .snapshots(),
              builder: (context, snapshot) {
                switch (snapshot.connectionState) {
                  case ConnectionState.none:
                    return Center(child: CircularProgressIndicator());
                  case ConnectionState.waiting:
                    return Center(child: CircularProgressIndicator());
                  case ConnectionState.active:
                  case ConnectionState.done:
                }

                var data = snapshot.data;

                return ListView(
                  children: [
                    SizedBox(
                      height: 30,
                    ),
                    PlatformListTile(
                      title: Text(
                        _auth.currentUser?.displayName ?? "",
                        style: Theme.of(context).textTheme.headline6,
                      ),
                      subtitle: Text(
                        data?.get("info"),
                        style: Theme.of(context).textTheme.bodyText1,
                      ),
                      leading: CircleAvatar(
                        radius: 30,
                        backgroundImage: CachedNetworkImageProvider(
                            data?.get("fotourl") ?? ""),
                      ),
                      isElevatedM: true,
                      trailing: IconButton(
                        icon: Icon(FontAwesomeIcons.pencilAlt),
                        onPressed: () {
                          _infoController.text = data?.get("info");
                          if (Platform.isIOS) {
                            showCupertinoDialog(
                              context: context,
                              builder: (context) => CupertinoAlertDialog(
                                title: Text("Info ändern"),
                                content: Column(
                                  children: [
                                    SizedBox(
                                      height: 10,
                                    ),
                                    CupertinoTextField(
                                      controller: _infoController,
                                      keyboardType: TextInputType.multiline,
                                      maxLines: null,
                                      style:
                                          Theme.of(context).textTheme.bodyText1,
                                    ),
                                  ],
                                ),
                                actions: [
                                  CupertinoDialogAction(
                                    child: Text("Schließen"),
                                    onPressed: () =>
                                        Navigator.of(context).pop(),
                                  ),
                                  CupertinoDialogAction(
                                    child: Text(
                                      "Ändern",
                                    ),
                                    onPressed: () async {
                                      Navigator.of(context).pop();
                                      setState(() {
                                        _isLoading = true;
                                      });

                                      await UserFirebaseService.changeInfo(
                                          _infoController.text);

                                      setState(() {
                                        _isLoading = false;
                                      });
                                    },
                                  )
                                ],
                              ),
                            );
                          } else {
                            showDialog(
                              context: context,
                              builder: (context) => AlertDialog(
                                title: Text("Info ändern"),
                                content: TextField(
                                  controller: _infoController,
                                  keyboardType: TextInputType.multiline,
                                  maxLines: null,
                                  decoration: InputDecoration(
                                    suffixIcon: IconButton(
                                      onPressed: _infoController.clear,
                                      icon: Icon(Icons.clear),
                                    ),
                                  ),
                                ),
                                actions: [
                                  TextButton(
                                    child: Text(
                                      'Ändern',
                                      style: TextStyle(
                                        color: Colors.green,
                                      ),
                                    ),
                                    onPressed: () async {
                                      Navigator.of(context).pop();
                                      setState(() {
                                        _isLoading = true;
                                      });

                                      await UserFirebaseService.changeInfo(
                                          _infoController.text);

                                      setState(() {
                                        _isLoading = false;
                                      });
                                    },
                                  )
                                ],
                              ),
                            );
                          }
                        },
                      ),
                    ),
                    SizedBox(
                      height: 50,
                    ),
                    PlatformListTile(
                      isElevatedM: true,
                      title: Text(
                        "Benachrichtigungen",
                        style: Theme.of(context).textTheme.bodyText1,
                      ),
                      leading: Icon(FontAwesomeIcons.solidEnvelope),
                      trailing: Platform.isIOS
                          ? CupertinoSwitch(
                              value: data?.get("fcmids").contains(fcmId),
                              onChanged: (value) async {
                                if (data?.get("fcmids").contains(fcmId)) {
                                  await UserFirebaseService.removeFcmId(fcmId);
                                  await ChatFirebaseService
                                      .removeFcmIdFromAllChats(fcmId);
                                  updateFcmId();
                                } else {
                                  await UserFirebaseService.addFcmId(fcmId);
                                  await ChatFirebaseService.addFcmIdToAllChats(
                                      fcmId);
                                  updateFcmId();
                                }
                              },
                            )
                          : Switch(
                              value: data?.get("fcmids").contains(fcmId),
                              onChanged: (value) async {
                                if (data?.get("fcmids").contains(fcmId)) {
                                  await UserFirebaseService.removeFcmId(fcmId);
                                  await ChatFirebaseService
                                      .removeFcmIdFromAllChats(fcmId);
                                  updateFcmId();
                                } else {
                                  await UserFirebaseService.addFcmId(fcmId);
                                  await ChatFirebaseService.addFcmIdToAllChats(
                                      fcmId);
                                  updateFcmId();
                                }
                              },
                            ),
                    ),
                    SizedBox(
                      height: 50,
                    ),
                    PlatformListTile(
                      title: Text(
                        "Mit Stern markierte",
                        style: Theme.of(context).textTheme.bodyText1,
                      ),
                      leading: Icon(FontAwesomeIcons.solidStar),
                      isElevatedM: true,
                      onTap: () {
                        Navigator.of(context)
                            .push(CupertinoPageRoute(
                                builder: (context) => MarkedMessages(
                                      isInChat: false,
                                    )))
                            .then((value) => setState(() {}));
                      },
                    ),
                    SizedBox(
                      height: 50,
                    ),
                    PlatformListTile(
                      title: Text(
                        "Account bearbeiten",
                        style: Theme.of(context).textTheme.bodyText1,
                      ),
                      leading: Icon(FontAwesomeIcons.userEdit),
                      isElevatedM: true,
                      onTap: () => Navigator.of(context).push(
                        CupertinoPageRoute(
                          builder: (context) => ChangeAccount(),
                        ),
                      ),
                    ),
                    SizedBox(
                      height: 50,
                    ),
                    PlatformListTile(
                      title: Text(
                        "Datenschutzerklärung",
                        style: Theme.of(context).textTheme.bodyText1,
                      ),
                      leading: Icon(FontAwesomeIcons.lock),
                      isElevatedM: true,
                      onTap: () => _launchURL(),
                    )
                  ],
                );
              }),
          Container(
            child: _isLoading
                ? Loader(
                    loadingTxt: "Aktualisiert...",
                  )
                : Container(),
          ),
        ],
      ),
    );
  }

  void _launchURL() async => await canLaunch(_url)
      ? await launch(_url)
      : print('Could not launch $_url');
}
