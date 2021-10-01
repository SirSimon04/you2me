import 'package:cached_network_image/cached_network_image.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/settings/screens/change_account/change_account.dart';
import 'package:flutter_dispuatio/screens/settings/screens/marked_messages/marked_messages.dart';
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

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

  @override
  bool get wantKeepAlive => true;

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return Scaffold(
      appBar: AppBar(
        title: Text("Settings", style: TextStyle(color: Colors.white)),
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
              activeColor: Theme.of(context).primaryColor,
            ),
          ),
          Card(
            elevation: 6,
            child: SwitchListTile(
              value: notifications,
              onChanged: (value) => {setState(() => notifications = value)},
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
