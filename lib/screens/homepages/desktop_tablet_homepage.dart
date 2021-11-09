import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/chatting/chat/screens/placeholder/placeholder.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/chat_list_screen.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';

class TabletDesktopHomePage extends StatefulWidget {
  const TabletDesktopHomePage({Key? key}) : super(key: key);

  @override
  _TabletDesktopHomePageState createState() => _TabletDesktopHomePageState();
}

class _TabletDesktopHomePageState extends State<TabletDesktopHomePage>
    with WidgetsBindingObserver {
  @override
  void initState() {
    UserFirebaseService.setOnline();
    super.initState();
    WidgetsBinding.instance?.addObserver(this);
  }

  @override
  void dispose() {
    WidgetsBinding.instance?.removeObserver(this);
    UserFirebaseService.setOffline();
    super.dispose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    switch (state) {
      case AppLifecycleState.resumed:
        UserFirebaseService.setOnline();
        break;
      case AppLifecycleState.inactive:
        UserFirebaseService.setOffline();
        break;
      case AppLifecycleState.paused:
        UserFirebaseService.setOffline();
        break;
      case AppLifecycleState.detached:
        UserFirebaseService.setOffline();
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
            flex: 3,
            child: ChatList(),
          ),
          Expanded(
            flex: 8,
            child: ChatPlaceholder(),
          )
        ],
      ),
    );
  }
}
