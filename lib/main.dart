import 'package:curved_navigation_bar/curved_navigation_bar.dart';
import 'package:device_preview/device_preview.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_dispuatio/home_page.dart';
import 'package:flutter_dispuatio/screens/chatting/chatlist/chat_list_screen.dart';
import 'package:flutter_dispuatio/screens/login/login_screen.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:flutter_dispuatio/themes.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:device_preview/plugins.dart';
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

  // runApp(
  //   DevicePreview(
  //     enabled: !kReleaseMode,
  //     builder: (context) => MyApp(
  //       loggedIn: email != null,
  //     ),
  //     plugins: [
  //       const ScreenshotPlugin(),
  //       const FileExplorerPlugin(),
  //       const SharedPreferencesExplorerPlugin(),
  //     ],
  //   ),
  // );
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
      // theme: lightThemeData(context),
      // darkTheme: darkThemeData(context),
      theme: lightThemeData(context),
      darkTheme: darkThemeData(context),
      // home: WelcomeScreen(),
      home: loggedIn ? MyHomePage() : LoginScreen(),
      debugShowCheckedModeBanner: false,
      routes: {},
    );
  }
}
