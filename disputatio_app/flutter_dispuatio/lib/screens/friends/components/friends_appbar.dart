import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'friends_searchpage.dart';

AppBar getAppBar(BuildContext context) {
  return AppBar(
    title: Text("Freunde"),
    actions: [
      Padding(
          padding: EdgeInsets.only(right: 20.0),
          child: GestureDetector(
            onTap: () {
              print("pressed");
              Navigator.of(context).push(CupertinoPageRoute(
                  builder: (context) => FriendsSearchPage()));
            },
            child: Icon(
              Icons.search,
              size: 26.0,
            ),
          )),
    ],
    automaticallyImplyLeading: false,
  );
}
