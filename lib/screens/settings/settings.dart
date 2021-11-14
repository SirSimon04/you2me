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
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:image_picker/image_picker.dart';

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

  final picker = ImagePicker();

  String fcmId = "";

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
      body: StreamBuilder<DocumentSnapshot>(
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
                  title: Text(_auth.currentUser?.displayName ?? ""),
                  subtitle: Text(data?.get("info")),
                  leading: CircleAvatar(
                    radius: 30,
                    backgroundImage: CachedNetworkImageProvider(
                        _auth.currentUser?.photoURL ?? ""),
                  ),
                  isElevatedM: true,
                ),
                SizedBox(
                  height: 50,
                ),
                Card(
                  elevation: 6,
                  child: SwitchListTile(
                    value: data?.get("fcmids").contains(fcmId),
                    onChanged: (value) async {
                      if (data?.get("fcmids").contains(fcmId)) {
                        await UserFirebaseService.removeFcmId(fcmId);
                        await ChatFirebaseService.removeFcmIdFromAllChats(
                            fcmId);
                        updateFcmId();
                      } else {
                        await UserFirebaseService.addFcmId(fcmId);
                        await ChatFirebaseService.addFcmIdToAllChats(fcmId);
                        updateFcmId();
                      }
                    },
                    title: Text("Benachrichtigungen"),
                    activeColor: Theme.of(context).primaryColor,
                  ),
                ),
                SizedBox(
                  height: 50,
                ),
                PlatformListTile(
                  title: Text("Mit Stern markierte"),
                  leading: Icon(FontAwesomeIcons.solidStar),
                  isElevatedM: true,
                  onTap: () {
                    Navigator.of(context).push(CupertinoPageRoute(
                        builder: (context) => MarkedMessages(
                              isInChat: false,
                            )));
                  },
                ),
                SizedBox(
                  height: 50,
                ),
                PlatformListTile(
                  title: Text("Account bearbeiten"),
                  leading: Icon(FontAwesomeIcons.userEdit),
                  isElevatedM: true,
                  onTap: () => Navigator.of(context).push(
                    CupertinoPageRoute(
                      builder: (context) => ChangeAccount(),
                    ),
                  ),
                )
              ],
            );
          }),
    );
  }
}
