import 'package:cached_network_image/cached_network_image.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/login/login_screen.dart';
import 'package:flutter_dispuatio/screens/settings/screens/change_account/change_account.dart';
import 'package:flutter_dispuatio/screens/settings/screens/marked_messages/marked_messages.dart';
import 'package:flutter_dispuatio/screens/settings/screens/saved_overview/saved_overview.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:platform_list_tile/platform_list_tile.dart';

class Settings extends StatefulWidget {
  const Settings({Key? key}) : super(key: key);

  @override
  _SettingsState createState() => _SettingsState();
}

class _SettingsState extends State<Settings>
    with AutomaticKeepAliveClientMixin {
  bool notifications = false;
  bool profFilter = true;
  final _auth = FirebaseAuth.instance;
  final _firestore = FirebaseFirestore.instance;
  final _push = FirebaseMessaging.instance;

  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return Scaffold(
      appBar: AppBar(
        title: Text("Settings"),
        automaticallyImplyLeading: false,
      ),
      body: ListView(
        children: [
          SizedBox(
            height: 30,
          ),
          PlatformListTile(
            title: Text(_auth.currentUser?.displayName ?? ""),
            subtitle: Text("Hey there, I'm using Disputatio"),
            leading: CircleAvatar(
              radius: 30,
              backgroundImage:
                  CachedNetworkImageProvider(_auth.currentUser?.photoURL ?? ""),
            ),
            trailing: IconButton(
              icon: Icon(FontAwesomeIcons.userEdit),
              onPressed: () => Navigator.of(context).push(
                  CupertinoPageRoute(builder: (context) => ChangeAccount())),
            ),
            isElevatedM: true,
            onTap: () {
              //TODO: Implement Account page
            },
          ),
          SizedBox(
            height: 30,
          ),
          Card(
            elevation: 6,
            child: SwitchListTile(
              value: profFilter,
              onChanged: (value) => {setState(() => profFilter = value)},
              title: Text("Wortfilter"),
            ),
          ),
          Card(
            elevation: 6,
            child: SwitchListTile(
              value: notifications,
              onChanged: (value) => {setState(() => notifications = value)},
              title: Text("Benachrichtigungen"),
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
          // PlatformListTile(
          //   title: Text("Speicherübersicht"),
          //   leading: Icon(FontAwesomeIcons.solidSave),
          //   isElevatedM: true,
          //   onTap: () {
          //     Navigator.of(context).push(
          //         CupertinoPageRoute(builder: (context) => SavedOverview()));
          //   },
          // ),
        ],
      ),
    );
  }
}
