import 'package:curved_navigation_bar/curved_navigation_bar.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/chat_list_screen.dart';
import 'package:flutter_dispuatio/screens/friends/friends.dart';
import 'package:flutter_dispuatio/screens/settings/settings.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class MobileHomePage extends StatefulWidget {
  @override
  _MobileHomePageState createState() => _MobileHomePageState();
}

class _MobileHomePageState extends State<MobileHomePage>
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

  var currentPageIndex = 1;
  List<Widget> children = [FriendLists(), ChatList(), Settings()];
  PageController _pageController = PageController(initialPage: 1);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        bottomNavigationBar: CurvedNavigationBar(
          items: [
            Padding(
              padding: const EdgeInsets.fromLTRB(8, 8, 12, 8),
              child: Icon(
                FontAwesomeIcons.userFriends,
                size: 30,
              ),
            ),
            Padding(
              padding: const EdgeInsets.fromLTRB(12, 8, 8, 8),
              child: Icon(
                FontAwesomeIcons.solidComment,
                size: 30,
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Icon(
                FontAwesomeIcons.userCog,
                size: 30,
              ),
            ),
          ],
          backgroundColor: Theme.of(context).scaffoldBackgroundColor,
          color: Theme.of(context).bottomAppBarColor,
          height: 60,
          index: currentPageIndex,
          onTap: onItemTapped,
        ),
        body: PageView(
          controller: _pageController,
          physics: NeverScrollableScrollPhysics(),
          children: children,
        ));
  }

  void onItemTapped(int index) {
    setState(() {
      currentPageIndex = index;
      _pageController.jumpToPage(index);
    });
  }
}