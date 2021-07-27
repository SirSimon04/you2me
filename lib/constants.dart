import 'package:bubble/bubble.dart';
import 'package:flutter/material.dart';

const double kDefaultPadding = 20;

const double kChatListTileHeight = 25;

const double kCircularBorderRadius = 30;

const kPrimaryColor = Color(0xFF00BFA6);
const kSecondaryColor = Color(0xFFFE9901);
const kContentColorLightTheme = Color(0xFF1D1D35);
const kContentColorDarkTheme = Color(0xFFF5FCF9);
const kWarningColor = Color(0xFFF3BB1C);
const kErrorColor = Color(0xFFF03738);
const kFocusedMenuHolderBGColor = Color(0xFF616161); //Colors.grey[700]

const kBackgroundDark = Color(0xFF1D1D35);

//Glassmorphism
const gmOn = false;
const kBlurEffectValue = 32.0;
const kColorOpacityValue = 0.25;

//Message-Bubble-Style

const kMyMessageColorDark = Colors.black54;
const kMyMessageColorLight = Colors.white70;

const kOtherMessageColorDark = Color(0xFF414141);
const kOtherMessageColorLight = Colors.white24;

const styleSomebody = BubbleStyle(
  nip: BubbleNip.leftBottom,
  color: Colors.blue,
  elevation: 4,
  margin: BubbleEdges.only(top: 8, right: 50),
  alignment: Alignment.topLeft,
);

const styleMe = BubbleStyle(
  nip: BubbleNip.rightBottom,
  color: Colors.blue,
  elevation: 4,
  margin: BubbleEdges.only(top: 8, left: 50),
  alignment: Alignment.topRight,
);

const kTextFieldDecoration = InputDecoration(
  hintStyle: TextStyle(
    color: Colors.black,
  ),
  hintText: '',
  contentPadding: EdgeInsets.symmetric(vertical: 10.0, horizontal: 20.0),
  border: OutlineInputBorder(
    borderRadius: BorderRadius.all(Radius.circular(32.0)),
  ),
  enabledBorder: OutlineInputBorder(
    borderSide: BorderSide(color: Colors.blueAccent, width: 1.0),
    borderRadius: BorderRadius.all(Radius.circular(32.0)),
  ),
  focusedBorder: OutlineInputBorder(
    borderSide: BorderSide(color: Colors.blueAccent, width: 2.0),
    borderRadius: BorderRadius.all(Radius.circular(32.0)),
  ),
);
