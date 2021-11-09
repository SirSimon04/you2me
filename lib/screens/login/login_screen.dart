import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart' show timeDilation;
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_dispuatio/home_page.dart';
import 'package:flutter_dispuatio/screens/homepages/homepage.dart';
import 'package:flutter_dispuatio/screens/login/signup_enter_name.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:flutter_login/flutter_login.dart';

import '../../main.dart';

class LoginScreen extends StatefulWidget {
  static const routeName = '/auth';

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  Duration get loginTime => Duration(milliseconds: timeDilation.ceil() * 2250);

  Future<String?> _loginUser(LoginData data) async {
    try {
      await UserFirebaseService.login(
          email: data.name, password: data.password);

      return null;
    } on FirebaseAuthException catch (e) {
      return (e.message);
    }
  }

  Future<String?> _signUp(LoginData data) async {
    final name = await Navigator.of(context)
        .push(CupertinoPageRoute(builder: (context) => SignUpEnterName()));
    if (name == null) {
      return "Kein Name gewählt";
    }
    try {
      await UserFirebaseService.register(
          email: data.name, password: data.password, username: name);
      return null;
    } on FirebaseAuthException catch (e) {
      return e.message;
    }
  }

  Future<String?> _recoverPassword(String mail) async {
    try {
      await UserFirebaseService.resetPassword(mail);
      return null;
    } catch (e) {
      return "Fehler";
    }
  }

  @override
  Widget build(BuildContext context) {
    return FlutterLogin(
      theme: LoginTheme(
          pageColorLight: kBackgroundDark,
          pageColorDark: kBackgroundDark,
          cardTheme: CardTheme(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(24.0),
            ),
          )),
      logo: "assets/logo.png",
      loginAfterSignUp: true,
      messages: LoginMessages(
        loginButton: "LOGIN",
        signupButton: "SIGNUP",
        recoverPasswordButton: "E-Mail senden",
        recoverPasswordDescription:
            "Dir wird eine E-Mail zugeschickt, mit der du dein Passwort zurücksetzen kannst",
        recoverPasswordIntro: "Hier kannst du dein Passwort zurücksetzen",
        recoverPasswordSuccess: "E-Mail wurde versendet",
        forgotPasswordButton: "Passwort vergessen?",
        goBackButton: "Zurück",
        passwordHint: "Passwort",
        userHint: "E-Mail",
        confirmPasswordHint: "Passwort bestätigen",
        signUpSuccess: "Erfolgreich angemeldet",
        flushbarTitleSuccess: "Erfolgreich",
        flushbarTitleError: "Fehler",
        confirmPasswordError: "Die Passwörter sind nicht gleich",
      ),
      navigateBackAfterRecovery: true,
      userValidator: (value) {
        if (!value!.contains('@')) {
          return "Email muss ein '@' enthalten";
        }
        return null;
      },
      passwordValidator: (value) {
        if (value!.isEmpty) {
          return "Passwort ist leer";
        }
        if (value.length < 6) {
          return "Passwort ist zu kurz";
        }
        return null;
      },
      onLogin: (loginData) {
        return _loginUser(loginData);
      },
      onSignup: (loginData) {
        return _signUp(loginData);
      },
      onSubmitAnimationCompleted: () {
        // Navigator.of(context).pushReplacement(FadePageRoute(
        //   builder: (context) => DashboardScreen(),
        // ));
        Navigator.of(context).pushReplacement(
          CupertinoPageRoute(
            builder: (context) => MyHomePage(),
          ),
        );
      },
      onRecoverPassword: (mail) {
        return _recoverPassword(mail);
        // Show new password dialog
      },
      showDebugButtons: false,
    );
  }
}

const mockUsers = {
  'dribbble@gmail.com': '12345',
  'hunter@gmail.com': 'hunter',
  'near.huscarl@gmail.com': 'subscribe to pewdiepie',
  '@.com': '.',
};

class Constants {
  static const String appName = 'ECORP';
  static const String logoTag = 'near.huscarl.loginsample.logo';
  static const String titleTag = 'near.huscarl.loginsample.title';
}

// logoTag: Constants.logoTag,
// titleTag: Constants.titleTag,
// loginProviders: [
//   LoginProvider(
//     icon: FontAwesomeIcons.google,
//     label: 'Google',
//     callback: () async {
//       print('start google sign in');
//       await Future.delayed(loginTime);
//       print('stop google sign in');
//       return '';
//     },
//   ),
//   LoginProvider(
//     icon: FontAwesomeIcons.linkedinIn,
//     label: 'LinkedIn',
//     callback: () async {
//       print('start linkdin sign in');
//       await Future.delayed(loginTime);
//       print('stop linkdin sign in');
//       return '';
//     },
//   ),
//   LoginProvider(
//     icon: FontAwesomeIcons.githubAlt,
//     callback: () async {
//       print('start github sign in');
//       await Future.delayed(loginTime);
//       print('stop github sign in');
//       return '';
//     },
//   ),
// ],
// hideProvidersTitle: false,
// loginAfterSignUp: false,
// hideForgotPasswordButton: true,
// hideSignUpButton: true,
// messages: LoginMessages(
//   userHint: 'User',
//   passwordHint: 'Pass',
//   confirmPasswordHint: 'Confirm',
//   loginButton: 'LOG IN',
//   signupButton: 'REGISTER',
//   forgotPasswordButton: 'Forgot huh?',
//   recoverPasswordButton: 'HELP ME',
//   goBackButton: 'GO BACK',
//   confirmPasswordError: 'Not match!',
//   recoverPasswordIntro: 'Don\'t feel bad. Happens all the time.',
//   recoverPasswordDescription: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry',
//   recoverPasswordSuccess: 'Password rescued successfully',
//   flushbarTitleError: 'Oh no!',
//   flushbarTitleSuccess: 'Succes!',
//   providersTitle: 'login with'
// ),
// theme: LoginTheme(
//   primaryColor: Colors.teal,
//   accentColor: Colors.yellow,
//   errorColor: Colors.deepOrange,
//   pageColorLight: Colors.indigo.shade300,
//   pageColorDark: Colors.indigo.shade500,
//   logoWidth: 0.80,
//   titleStyle: TextStyle(
//     color: Colors.greenAccent,
//     fontFamily: 'Quicksand',
//     letterSpacing: 4,
//   ),
//   // beforeHeroFontSize: 50,
//   // afterHeroFontSize: 20,
//   bodyStyle: TextStyle(
//     fontStyle: FontStyle.italic,
//     decoration: TextDecoration.underline,
//   ),
//   textFieldStyle: TextStyle(
//     color: Colors.orange,
//     shadows: [Shadow(color: Colors.yellow, blurRadius: 2)],
//   ),
//   buttonStyle: TextStyle(
//     fontWeight: FontWeight.w800,
//     color: Colors.yellow,
//   ),
//   cardTheme: CardTheme(
//     color: Colors.yellow.shade100,
//     elevation: 5,
//     margin: EdgeInsets.only(top: 15),
//     shape: ContinuousRectangleBorder(
//         borderRadius: BorderRadius.circular(100.0)),
//   ),
//   inputTheme: InputDecorationTheme(
//     filled: true,
//     fillColor: Colors.purple.withOpacity(.1),
//     contentPadding: EdgeInsets.zero,
//     errorStyle: TextStyle(
//       backgroundColor: Colors.orange,
//       color: Colors.white,
//     ),
//     labelStyle: TextStyle(fontSize: 12),
//     enabledBorder: UnderlineInputBorder(
//       borderSide: BorderSide(color: Colors.blue.shade700, width: 4),
//       borderRadius: inputBorder,
//     ),
//     focusedBorder: UnderlineInputBorder(
//       borderSide: BorderSide(color: Colors.blue.shade400, width: 5),
//       borderRadius: inputBorder,
//     ),
//     errorBorder: UnderlineInputBorder(
//       borderSide: BorderSide(color: Colors.red.shade700, width: 7),
//       borderRadius: inputBorder,
//     ),
//     focusedErrorBorder: UnderlineInputBorder(
//       borderSide: BorderSide(color: Colors.red.shade400, width: 8),
//       borderRadius: inputBorder,
//     ),
//     disabledBorder: UnderlineInputBorder(
//       borderSide: BorderSide(color: Colors.grey, width: 5),
//       borderRadius: inputBorder,
//     ),
//   ),
//   buttonTheme: LoginButtonTheme(
//     splashColor: Colors.purple,
//     backgroundColor: Colors.pinkAccent,
//     highlightColor: Colors.lightGreen,
//     elevation: 9.0,
//     highlightElevation: 6.0,
//     shape: BeveledRectangleBorder(
//       borderRadius: BorderRadius.circular(10),
//     ),
//     // shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(5)),
//     // shape: CircleBorder(side: BorderSide(color: Colors.green)),
//     // shape: ContinuousRectangleBorder(borderRadius: BorderRadius.circular(55.0)),
//   ),
// ),
