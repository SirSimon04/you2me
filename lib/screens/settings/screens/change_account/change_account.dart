import 'dart:io';

import 'package:adaptive_action_sheet/adaptive_action_sheet.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/login/login_screen.dart';
import 'package:flutter_dispuatio/services/general_services/firebase_storage_service.dart';
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
                                    builder: (context) =>
                                        CupertinoActionSheet(),
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
                title: Text(_auth.currentUser?.displayName ?? ""),
                subtitle: Text("Nutzername ändern"),
                leading: Icon(FontAwesomeIcons.solidUser),
                isElevatedM: true,
                onTap: () => showDialog(
                  context: context,
                  builder: (context) => AlertDialog(
                    title: Text("Benutzernamen ändern"),
                    content: TextField(
                      controller: _nameController,
                      maxLines: 1,
                      decoration: InputDecoration(
                        hintText: "Neuer Benutzername",
                      ),
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
                        onPressed: () {
                          Navigator.of(context).pop();
                          setState(() {
                            _isLoading = true;
                          });
                          UserFirebaseService.changeUserName(
                                  _nameController.text)
                              .then(
                            (value) {
                              setState(() {
                                _isLoading = false;
                                _nameController.clear();
                              });
                            },
                          );
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
                title: Text(_auth.currentUser?.email ?? ""),
                subtitle: Text("E-Mail ändern"),
                leading: Icon(FontAwesomeIcons.mailBulk),
                isElevatedM: true,
                onTap: () => showDialog(
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
                        onPressed: () {
                          Navigator.of(context).pop();
                          setState(() {
                            _isLoading = true;
                          });

                          UserFirebaseService.login(
                                  email: _oldMail.text.toString().trim(),
                                  password: _oldPassword.text)
                              .then((value) {
                            UserFirebaseService.changeMail(
                              _mailController.text.toString().trim(),
                            ).then(
                              (value) {
                                setState(() {
                                  _isLoading = false;
                                  _mailController.clear();
                                  _oldPassword.clear();
                                  _oldMail.clear();
                                });
                              },
                            );
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
                title: Text("********"),
                subtitle: Text("Passwort ändern"),
                leading: Icon(FontAwesomeIcons.lock),
                isElevatedM: true,
                onTap: () => showDialog(
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
                            hintText: "Alte E-Mail",
                          ),
                          keyboardType: TextInputType.emailAddress,
                        ),
                        TextField(
                          controller: _oldPassword,
                          obscureText: true,
                          maxLines: 1,
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
                        onPressed: () {
                          Navigator.of(context).pop();
                          setState(() {
                            _isLoading = true;
                          });

                          UserFirebaseService.login(
                                  email: _oldMail.text.toString().trim(),
                                  password: _oldPassword.text)
                              .then(
                            (value) => {
                              UserFirebaseService.changePassword(
                                      _passwordController.text)
                                  .then((value) {
                                setState(() {
                                  _isLoading = false;
                                });
                              }),
                            },
                          );
                        },
                      ),
                    ],
                  ),
                ),
              ),
              Spacer(),
              PlatformListTile(
                title: Text("Mehr"),
                isElevatedM: true,
                leading: Icon(FontAwesomeIcons.ellipsisH),
                onTap: () {
                  if (Platform.isIOS) {
                    showCupertinoModalPopup(
                      context: context,
                      builder: (context) => CupertinoActionSheet(),
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
                                (value) => Navigator.of(context)
                                    .pushReplacement(CupertinoPageRoute(
                                        builder: (context) => LoginScreen())))),
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
                                        children: [
                                          Text(
                                            'Bist du dir sicher, dass du deinen Acount wirklich löschen möchtest? Diese Aktion ist nicht zu widerrufen!',
                                          ),
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
                                          onPressed: () {
                                            UserFirebaseService.deleteAccount()
                                                .then((value) => Navigator.of(
                                                        context)
                                                    .pushReplacement(
                                                        CupertinoPageRoute(
                                                            builder: (context) =>
                                                                LoginScreen())));
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
