import 'package:cached_network_image/cached_network_image.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/settings/screens/change_account/change_account.dart';
import 'package:flutter_dispuatio/screens/settings/screens/marked_messages/marked_messages.dart';
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
  bool notifications = false;

  final _auth = FirebaseAuth.instance;

  final picker = ImagePicker();

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
            subtitle: Text("Hier muss noch die Info hin"),
            leading: CircleAvatar(
              radius: 30,
              backgroundImage:
                  CachedNetworkImageProvider(_auth.currentUser?.photoURL ?? ""),
            ),
            isElevatedM: true,
          ),
          SizedBox(
            height: 50,
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
      ),
    );
  }
}
