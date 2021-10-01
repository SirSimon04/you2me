import 'package:device_preview/device_preview.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/chat_list_screen.dart';
import 'package:flutter_dispuatio/screens/login/login_screen.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:flutter_dispuatio/themes.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'screens/friends/friends.dart';
import 'screens/settings/settings.dart';

// void main() {
//   runApp(
//     DevicePreview(
//       enabled: !kReleaseMode,
//       builder: (context) => MyApp(),
//     ),
//   );
// }

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  SharedPreferences prefs = await SharedPreferences.getInstance();
  var email = prefs.getString("email");
  runApp(MyApp(
    loggedIn: email != null,
  ));
}

class MyApp extends StatelessWidget {
  MyApp({required this.loggedIn});

  final bool loggedIn;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      locale: DevicePreview.locale(context),
      builder: DevicePreview.appBuilder,
      title: 'Flutter Demo',
      theme: lightThemeData(context),
      darkTheme: darkThemeData(context),
      // home: WelcomeScreen(),
      home: loggedIn ? MyHomePage() : LoginScreen(),
      debugShowCheckedModeBanner: false,
      routes: {},
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> with WidgetsBindingObserver {
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
        bottomNavigationBar: BottomNavigationBar(
          items: [
            BottomNavigationBarItem(
                icon: Padding(
                  padding: const EdgeInsets.only(bottom: 8.0),
                  child: Icon(FontAwesomeIcons.userFriends),
                ),
                label: "Freunde"),
            BottomNavigationBarItem(
                icon: Padding(
                  padding: const EdgeInsets.only(bottom: 8.0),
                  child: Icon(FontAwesomeIcons.solidComment),
                ),
                label: "Chats"),
            BottomNavigationBarItem(
                icon: Padding(
                  padding: const EdgeInsets.only(bottom: 8),
                  child: Icon(FontAwesomeIcons.userCog),
                ),
                label: "Einstellungen"),
          ],
          onTap: onItemTapped,
          currentIndex: currentPageIndex,
          backgroundColor: Theme.of(context).bottomAppBarColor,
          selectedFontSize: 14,
        ),
        // bottomNavigationBar: CurvedNavigationBar(
        //   items: [
        //     Padding(
        //       padding: const EdgeInsets.fromLTRB(2, 4, 7, 4),
        //       child: Icon(
        //         FontAwesomeIcons.userFriends,
        //         size: 30,
        //       ),
        //     ),
        //     Padding(
        //       padding: const EdgeInsets.all(8.0),
        //       child: Icon(
        //         FontAwesomeIcons.solidComment,
        //         size: 40,
        //       ),
        //     ),
        //     Padding(
        //       padding: const EdgeInsets.fromLTRB(2, 4, 7, 4),
        //       child: Icon(
        //         FontAwesomeIcons.userCog,
        //         size: 30,
        //       ),
        //     ),
        //   ],
        //   onTap: onItemTapped,
        //   backgroundColor: Theme.of(context).scaffoldBackgroundColor,
        //   color: Colors.grey.shade700,
        //   height: 60.0,
        //   index: 1,
        // ),
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
