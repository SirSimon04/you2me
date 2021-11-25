import 'dart:io';
import 'package:adaptive_action_sheet/adaptive_action_sheet.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/login/login_screen.dart';
import 'package:flutter_dispuatio/services/general_services/firebase_storage_service.dart';
import 'package:flutter_dispuatio/services/general_services/toast_service.dart';
import 'package:flutter_dispuatio/services/user_services/user_firebase_service.dart';
import 'package:flutter_dispuatio/widgets/loader.dart';
import 'package:flutter_dispuatio/widgets/platform_listtile.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:image_picker/image_picker.dart';

class ChangeAccount extends StatefulWidget {
  const ChangeAccount({Key? key}) : super(key: key);

  @override
  _ChangeAccountState createState() => _ChangeAccountState();
}

class _ChangeAccountState extends State<ChangeAccount> {
  TextEditingController _nameController = TextEditingController();
  TextEditingController _mailController = TextEditingController();
  TextEditingController _passwordController = TextEditingController();

  TextEditingController _oldMail = TextEditingController();
  TextEditingController _oldPassword = TextEditingController();

  final _auth = FirebaseAuth.instance;

  bool _isLoading = false;

  final picker = ImagePicker();

  Future getImageFromCam() async {
    File imageFile = File(await ImagePicker()
        .getImage(source: ImageSource.camera)
        .then((pickedFile) => pickedFile!.path));

    Navigator.of(context).pop();

    setState(() {
      _isLoading = true;
    });

    String url = await FireBaseStorageService.uploadImage(imageFile);

    UserFirebaseService.changePhotoUrl(url).then((value) {
      setState(() {
        _isLoading = false;
      });
    });
  }

  Future getImageFromGallery() async {
    File imageFile = File(await ImagePicker()
        .getImage(source: ImageSource.gallery)
        .then((pickedFile) => pickedFile!.path));
    Navigator.of(context).pop();

    setState(() {
      _isLoading = true;
    });

    String url = await FireBaseStorageService.uploadImage(imageFile);

    UserFirebaseService.changePhotoUrl(url).then((value) {
      setState(() {
        _isLoading = false;
      });
    });
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
        title: Text("Account bearbeiten",
            style: TextStyle(
              color: Colors.white,
            )),
        automaticallyImplyLeading: false,
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          color: Colors.white,
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: Stack(
        children: [
          ListView(
            children: [
              SizedBox(
                height: 30,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Stack(
                    children: [
                      CircleAvatar(
                        backgroundImage: CachedNetworkImageProvider(
                            _auth.currentUser?.photoURL ?? ""),
                        radius: MediaQuery.of(context).size.width * 0.25,
                      ),
                      Positioned(
                        right: 0,
                        bottom: 0,
                        child: Container(
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            color: Theme.of(context).scaffoldBackgroundColor,
                            border: Border.all(
                              color: Theme.of(context).scaffoldBackgroundColor,
                              width: 3,
                            ),
                          ),
                          child: Padding(
                            padding: EdgeInsets.all(
                                MediaQuery.of(context).size.width * 0.01),
                            child: IconButton(
                              splashColor: Colors.transparent,
                              highlightColor: Colors.transparent,
                              icon: Icon(
                                Icons.add_a_photo,
                                size: MediaQuery.of(context).size.width * 0.1,
                              ),
                              onPressed: () {
                                if (Platform.isIOS) {
                                  showCupertinoModalPopup(
                                    context: context,
                                    builder: (context) => CupertinoActionSheet(
                                      actions: [
                                        CupertinoActionSheetAction(
                                          child: Text("Fotomediathek"),
                                          onPressed: getImageFromGallery,
                                        ),
                                        CupertinoActionSheetAction(
                                          child: Text("Foto aufnehmen"),
                                          onPressed: getImageFromCam,
                                        )
                                      ],
                                    ),
                                  );
                                } else {
                                  showAdaptiveActionSheet(
                                    context: context,
                                    actions: [
                                      BottomSheetAction(
                                        title: Text(
                                          "Fotomediathek",
                                          textAlign: TextAlign.center,
                                        ),
                                        onPressed: getImageFromGallery,
                                        trailing:
                                            Icon(FontAwesomeIcons.solidImages),
                                      ),
                                      BottomSheetAction(
                                          title: Text(
                                            "Foto aufnehmen",
                                            textAlign: TextAlign.center,
                                          ),
                                          onPressed: getImageFromCam,
                                          trailing:
                                              Icon(FontAwesomeIcons.camera)),
                                    ],
                                    cancelAction: CancelAction(
                                      title: const Text('Schließen'),
                                    ),
                                  );
                                }
                              },
                            ),
                          ),
                        ),
                      )
                    ],
                  ),
                ],
              ),
              SizedBox(
                height: 30,
              ),
              PlatformListTile(
                title: Text(
                  _auth.currentUser?.displayName ?? "",
                  style: Theme.of(context).textTheme.bodyText1,
                ),
                subtitle: Text(
                  "Nutzername ändern",
                  style: Theme.of(context).textTheme.subtitle1,
                ),
                leading: Icon(FontAwesomeIcons.solidUser),
                isElevatedM: true,
                onTap: () => Platform.isIOS
                    ? showCupertinoDialog(
                        context: context,
                        builder: (context) => CupertinoAlertDialog(
                          title: Text("Benutzernamen ändern"),
                          content: Column(
                            children: [
                              Text(
                                "Nach dieser Aktion wirst du ausgeloggt",
                                textAlign: TextAlign.center,
                              ),
                              SizedBox(
                                height: 10,
                              ),
                              CupertinoTextField(
                                controller: _nameController,
                                maxLines: 1,
                                placeholder: "Neuer Benutzername",
                              ),
                            ],
                          ),
                          actions: [
                            CupertinoDialogAction(
                              child: Text("Abbrechen"),
                              onPressed: () => Navigator.of(context).pop(),
                            ),
                            CupertinoDialogAction(
                              child: Text("Ändern"),
                              onPressed: () async {
                                Navigator.of(context).pop();
                                setState(() {
                                  _isLoading = true;
                                });
                                await UserFirebaseService.changeUserName(
                                    _nameController.text);
                                print("changed name from flutter");
                                UserFirebaseService.logout();
                                Navigator.of(context).pushReplacement(
                                  CupertinoPageRoute(
                                    builder: (context) => LoginScreen(),
                                  ),
                                );
                              },
                            ),
                          ],
                        ),
                      )
                    : showDialog(
                        context: context,
                        builder: (context2) => AlertDialog(
                          title: Text("Benutzernamen ändern"),
                          content: Column(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Text(
                                "Nach dieser Aktion wirst du ausgeloggt",
                                textAlign: TextAlign.center,
                              ),
                              TextField(
                                controller: _nameController,
                                maxLines: 1,
                                decoration: InputDecoration(
                                  hintText: "Neuer Benutzername",
                                ),
                              ),
                            ],
                          ),
                          actions: [
                            TextButton(
                              child: Text(
                                'Abbrechen',
                                style: TextStyle(
                                  color: Colors.blue,
                                ),
                              ),
                              onPressed: () {
                                // Hier passiert etwas
                                Navigator.of(context).pop();
                              },
                            ),
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
                                  await UserFirebaseService.changeUserName(
                                      _nameController.text);
                                  print("changed name from flutter");
                                  UserFirebaseService.logout();
                                  Navigator.of(context).pushReplacement(
                                    CupertinoPageRoute(
                                      builder: (context) => LoginScreen(),
                                    ),
                                  );
                                }),
                          ],
                        ),
                      ),
              ),
              SizedBox(
                height: 30,
              ),
              PlatformListTile(
                title: Text(
                  _auth.currentUser?.email ?? "",
                  style: Theme.of(context).textTheme.bodyText1,
                ),
                subtitle: Text(
                  "E-Mail ändern",
                  style: Theme.of(context).textTheme.subtitle1,
                ),
                leading: Icon(FontAwesomeIcons.mailBulk),
                isElevatedM: true,
                onTap: () => Platform.isIOS
                    ? showCupertinoDialog(
                        context: context,
                        builder: (context) => CupertinoAlertDialog(
                          title: Text("E-Mail ändern"),
                          content: Column(
                            children: [
                              SizedBox(
                                height: 10,
                              ),
                              CupertinoTextField(
                                controller: _mailController,
                                maxLines: 1,
                                placeholder: "Neue Email",
                                keyboardType: TextInputType.emailAddress,
                              ),
                              SizedBox(
                                height: 10,
                              ),
                              CupertinoTextField(
                                controller: _oldMail,
                                maxLines: 1,
                                placeholder: "Alte Email",
                                keyboardType: TextInputType.emailAddress,
                              ),
                              SizedBox(
                                height: 10,
                              ),
                              CupertinoTextField(
                                controller: _oldPassword,
                                maxLines: 1,
                                obscureText: true,
                                placeholder: "Passwort",
                              ),
                            ],
                          ),
                          actions: [
                            CupertinoDialogAction(
                              child: Text("SChließen"),
                              onPressed: () => Navigator.of(context).pop(),
                            ),
                            CupertinoDialogAction(
                              child: Text("Ändern"),
                              onPressed: () async {
                                Navigator.of(context).pop();
                                setState(() {
                                  _isLoading = true;
                                });

                                try {
                                  await UserFirebaseService.loginForDelete(
                                      email: _oldMail.text.toString().trim(),
                                      password: _oldPassword.text);

                                  await UserFirebaseService.changeMail(
                                    _mailController.text.toString().trim(),
                                  );

                                  ToastService.showLongToast(
                                      "Dein Passwort wurde erfolgreich geändert");
                                } catch (error) {
                                  ToastService.showLongToast(error.toString());
                                }

                                setState(() {
                                  _isLoading = false;
                                  _passwordController.clear();
                                  _oldPassword.clear();
                                  _oldMail.clear();
                                });
                              },
                            )
                          ],
                        ),
                      )
                    : showDialog(
                        context: context,
                        builder: (context) => AlertDialog(
                          title: Text("E-Mail ändern ändern"),
                          content: Column(
                            mainAxisSize: MainAxisSize.min,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              TextField(
                                  controller: _mailController,
                                  maxLines: 1,
                                  decoration: InputDecoration(
                                    hintText: "Neue E-Mail",
                                  ),
                                  keyboardType: TextInputType.emailAddress),
                              TextField(
                                controller: _oldMail,
                                maxLines: 1,
                                decoration: InputDecoration(
                                  hintText: "Alte E-Mail",
                                ),
                                keyboardType: TextInputType.emailAddress,
                              ),
                              TextField(
                                controller: _oldPassword,
                                maxLines: 1,
                                obscureText: true,
                                decoration: InputDecoration(
                                  hintText: "Passwort",
                                ),
                              ),
                            ],
                          ),
                          actions: [
                            TextButton(
                              child: Text(
                                'Abbrechen',
                                style: TextStyle(
                                  color: Colors.blue,
                                ),
                              ),
                              onPressed: () {
                                // Hier passiert etwas
                                Navigator.of(context).pop();
                              },
                            ),
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

                                try {
                                  await UserFirebaseService.loginForDelete(
                                      email: _oldMail.text.toString().trim(),
                                      password: _oldPassword.text);

                                  await UserFirebaseService.changeMail(
                                    _mailController.text.toString().trim(),
                                  );

                                  ToastService.showLongToast(
                                      "Dein Passwort wurde erfolgreich geändert");
                                } catch (error) {
                                  ToastService.showLongToast(error.toString());
                                }

                                setState(() {
                                  _isLoading = false;
                                  _passwordController.clear();
                                  _oldPassword.clear();
                                  _oldMail.clear();
                                });
                              },
                            ),
                          ],
                        ),
                      ),
              ),
              SizedBox(
                height: 30,
              ),
              PlatformListTile(
                title: Text(
                  "********",
                  style: Theme.of(context).textTheme.bodyText1,
                ),
                subtitle: Text(
                  "Passwort ändern",
                  style: Theme.of(context).textTheme.subtitle1,
                ),
                leading: Icon(
                  FontAwesomeIcons.lock,
                ),
                isElevatedM: true,
                onTap: () => Platform.isIOS
                    ? showCupertinoDialog(
                        context: context,
                        builder: (context) => CupertinoAlertDialog(
                          title: Text("Passwort ändern"),
                          content: Column(
                            children: [
                              SizedBox(
                                height: 10,
                              ),
                              CupertinoTextField(
                                controller: _passwordController,
                                maxLines: 1,
                                placeholder: "Neues Passwort",
                              ),
                              SizedBox(
                                height: 10,
                              ),
                              CupertinoTextField(
                                controller: _oldMail,
                                maxLines: 1,
                                placeholder: "E-Mail",
                                keyboardType: TextInputType.emailAddress,
                              ),
                              SizedBox(
                                height: 10,
                              ),
                              CupertinoTextField(
                                controller: _oldPassword,
                                obscureText: true,
                                maxLines: 1,
                                placeholder: "Altes Passwort",
                              ),
                            ],
                          ),
                          actions: [
                            CupertinoDialogAction(
                              child: Text("Abbrechen"),
                              onPressed: () => Navigator.of(context).pop(),
                            ),
                            CupertinoDialogAction(
                              child: Text("Ändern"),
                              onPressed: () async {
                                Navigator.of(context).pop();
                                setState(() {
                                  _isLoading = true;
                                });

                                try {
                                  await UserFirebaseService.loginForDelete(
                                      email: _oldMail.text.toString().trim(),
                                      password: _oldPassword.text);

                                  await UserFirebaseService.changePassword(
                                    _passwordController.text.toString().trim(),
                                  );

                                  ToastService.showLongToast(
                                      "Dein Passwort wurde erfolgreich geändert");
                                } catch (error) {
                                  ToastService.showLongToast(error.toString());
                                }

                                setState(() {
                                  _isLoading = false;
                                  _passwordController.clear();
                                  _oldPassword.clear();
                                  _oldMail.clear();
                                });
                              },
                            ),
                          ],
                        ),
                      )
                    : showDialog(
                        context: context,
                        builder: (context) => AlertDialog(
                          title: Text("Passwort ändern"),
                          content: Column(
                            mainAxisSize: MainAxisSize.min,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              TextField(
                                controller: _passwordController,
                                maxLines: 1,
                                decoration: InputDecoration(
                                  hintText: "Neues Passwort",
                                ),
                              ),
                              TextField(
                                controller: _oldMail,
                                maxLines: 1,
                                decoration: InputDecoration(
                                  hintText: "E-Mail",
                                ),
                                keyboardType: TextInputType.emailAddress,
                              ),
                              TextField(
                                controller: _oldPassword,
                                obscureText: true,
                                maxLines: 1,
                                decoration: InputDecoration(
                                  hintText: "Altes Passwort",
                                ),
                              ),
                            ],
                          ),
                          actions: [
                            TextButton(
                              child: Text(
                                'Abbrechen',
                                style: TextStyle(
                                  color: Colors.blue,
                                ),
                              ),
                              onPressed: () {
                                // Hier passiert etwas
                                Navigator.of(context).pop();
                              },
                            ),
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

                                try {
                                  await UserFirebaseService.loginForDelete(
                                      email: _oldMail.text.toString().trim(),
                                      password: _oldPassword.text);

                                  await UserFirebaseService.changePassword(
                                    _passwordController.text.toString().trim(),
                                  );

                                  ToastService.showLongToast(
                                      "Dein Passwort wurde erfolgreich geändert");
                                } catch (error) {
                                  ToastService.showLongToast(error.toString());
                                }

                                setState(() {
                                  _isLoading = false;
                                  _passwordController.clear();
                                  _oldPassword.clear();
                                  _oldMail.clear();
                                });
                              },
                            ),
                          ],
                        ),
                      ),
              ),
              Spacer(),
              PlatformListTile(
                title: Text(
                  "Mehr",
                  style: Theme.of(context).textTheme.bodyText1,
                ),
                isElevatedM: true,
                leading: Icon(FontAwesomeIcons.ellipsisH),
                onTap: () {
                  if (Platform.isIOS) {
                    showCupertinoModalPopup(
                      context: context,
                      builder: (context) => CupertinoActionSheet(
                        actions: [
                          CupertinoActionSheetAction(
                              onPressed: () => UserFirebaseService.logout()
                                  .then((value) => Navigator.of(context)
                                      .pushReplacement(CupertinoPageRoute(
                                          builder: (context) =>
                                              LoginScreen()))),
                              child: Text(
                                "Abmelden",
                                style: TextStyle(color: Colors.red),
                              )),
                          CupertinoActionSheetAction(
                            onPressed: () => showDialog<void>(
                              context: context,
                              builder: (context) => CupertinoAlertDialog(
                                title: Text(
                                  "Wirklich löschen?",
                                  style: TextStyle(
                                    color: Colors.red,
                                  ),
                                ),
                                content: Column(
                                  mainAxisSize: MainAxisSize.min,
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Text(
                                      'Bist du dir sicher, dass du deinen Acount wirklich löschen möchtest? Diese Aktion ist nicht zu widerrufen!',
                                    ),
                                    SizedBox(height: 10),
                                    CupertinoTextField(
                                      controller: _oldMail,
                                      maxLines: 1,
                                      placeholder: "E-Mail",
                                      keyboardType: TextInputType.emailAddress,
                                    ),
                                    SizedBox(height: 10),
                                    CupertinoTextField(
                                      controller: _oldPassword,
                                      maxLines: 1,
                                      placeholder: "Passwort",
                                      obscureText: true,
                                    )
                                  ],
                                ),
                                actions: [
                                  CupertinoDialogAction(
                                    child: Text("Abbrechen"),
                                    onPressed: () {
                                      Navigator.of(context).pop();
                                      Navigator.of(context).pop();
                                    },
                                  ),
                                  CupertinoDialogAction(
                                    child: Text(
                                      "Löschen",
                                      style: TextStyle(
                                        color: Colors.red,
                                      ),
                                    ),
                                    onPressed: () async {
                                      // UserFirebaseService.deleteAccount();
                                      await UserFirebaseService.loginForDelete(
                                              email: _oldMail.text,
                                              password: _oldPassword.text)
                                          .onError((error, stackTrace) =>
                                              ToastService.showLongToast(
                                                  error.toString()))
                                          .then((value) {
                                        UserFirebaseService.deleteAccount()
                                            .then((value) => Navigator.of(
                                                    context)
                                                .pushReplacement(
                                                    CupertinoPageRoute(
                                                        builder: (context) =>
                                                            LoginScreen())));
                                      });
                                    },
                                  )
                                ],
                              ),
                            ),
                            child: Text(
                              "Account löschen",
                              style: TextStyle(color: Colors.red),
                            ),
                          ),
                        ],
                        cancelButton: CupertinoActionSheetAction(
                          onPressed: () => Navigator.of(context).pop(),
                          child: Text("Schließen"),
                        ),
                      ),
                    );
                  } else {
                    showAdaptiveActionSheet(
                      context: context,
                      actions: [
                        BottomSheetAction(
                          title: Text(
                            "Abmelden",
                            style: TextStyle(color: Colors.red),
                          ),
                          onPressed: () => UserFirebaseService.logout().then(
                            (value) => Navigator.of(context).pushReplacement(
                              CupertinoPageRoute(
                                builder: (context) => LoginScreen(),
                              ),
                            ),
                          ),
                        ),
                        BottomSheetAction(
                            title: Text(
                              "Account löschen",
                              style: TextStyle(color: Colors.red),
                            ),
                            onPressed: () => showDialog(
                                  context: context,
                                  builder: (BuildContext context) {
                                    return AlertDialog(
                                      title: Text(
                                        'Wirklich löschen?',
                                        style: TextStyle(
                                          color: Colors.red,
                                        ),
                                      ),
                                      content: Column(
                                        mainAxisSize: MainAxisSize.min,
                                        mainAxisAlignment:
                                            MainAxisAlignment.center,
                                        children: [
                                          Text(
                                            'Bist du dir sicher, dass du deinen Acount wirklich löschen möchtest? Diese Aktion ist nicht zu widerrufen!',
                                          ),
                                          SizedBox(height: 10),
                                          TextField(
                                            controller: _oldMail,
                                            maxLines: 1,
                                            decoration: InputDecoration(
                                              hintText: "E-Mail",
                                            ),
                                            keyboardType:
                                                TextInputType.emailAddress,
                                          ),
                                          TextField(
                                            controller: _oldPassword,
                                            maxLines: 1,
                                            decoration: InputDecoration(
                                              hintText: "Passwort",
                                            ),
                                            obscureText: true,
                                          )
                                        ],
                                      ),
                                      actions: <Widget>[
                                        TextButton(
                                            child: Text(
                                              'Abbrechen',
                                              style: TextStyle(
                                                color: Colors.blue,
                                              ),
                                            ),
                                            onPressed: () {
                                              // Hier passiert etwas
                                              Navigator.of(context).pop();
                                            }),
                                        TextButton(
                                          child: Text(
                                            'Löschen',
                                            style: TextStyle(
                                              color: Colors.red,
                                            ),
                                          ),
                                          onPressed: () async {
                                            // UserFirebaseService.deleteAccount();
                                            await UserFirebaseService
                                                    .loginForDelete(
                                                        email: _oldMail.text,
                                                        password:
                                                            _oldPassword.text)
                                                .onError((error, stackTrace) =>
                                                    ToastService.showLongToast(
                                                        error.toString()))
                                                .then((value) {
                                              UserFirebaseService
                                                      .deleteAccount()
                                                  .then((value) => Navigator.of(
                                                          context)
                                                      .pushReplacement(
                                                          CupertinoPageRoute(
                                                              builder: (context) =>
                                                                  LoginScreen())));
                                            });
                                          },
                                        ),
                                      ],
                                    );
                                  },
                                )),
                      ],
                      cancelAction: CancelAction(
                        title: const Text('Schließen'),
                      ),
                    );
                  }
                },
              ),
              SizedBox(
                height: 30,
              ),
              // PlatformListTile(
              //   title: Text("Ausloggen"),
              //   leading: Icon(FontAwesomeIcons.signOutAlt),
              //   isElevatedM: true,
              //   onTap: () async {
              //     await UserFirebaseService.logout();
              //     Navigator.of(context).pushReplacement(
              //         CupertinoPageRoute(builder: (context) => LoginScreen()));
              //   },
              //   focusColor: Colors.red,
              //   tileColorM: Colors.red.withOpacity(0.6),
              // ),
              // PlatformListTile(
              //   title: Text("Account löschen"),
              //   leading: Icon(FontAwesomeIcons.signOutAlt),
              //   isElevatedM: true,
              //   onTap: () async {
              //     await UserFirebaseService.logout();
              //     Navigator.of(context).pushReplacement(
              //         CupertinoPageRoute(builder: (context) => LoginScreen()));
              //   },
              //   focusColor: Colors.red,
              //   tileColorM: Colors.red.withOpacity(0.6),
              // ),
            ],
          ),
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
}
