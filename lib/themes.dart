import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'constants.dart';

ThemeData lightThemeData(BuildContext context) {
  return ThemeData.light().copyWith(
    primaryColor: kPrimaryColor,
    scaffoldBackgroundColor: Colors.white,
    iconTheme: IconThemeData(color: kContentColorLightTheme),
    bottomNavigationBarTheme: BottomNavigationBarThemeData(
      backgroundColor: Colors.white,
      selectedItemColor: kContentColorLightTheme.withOpacity(0.7),
      unselectedItemColor: kContentColorLightTheme.withOpacity(0.32),
      selectedIconTheme: IconThemeData(color: kPrimaryColor),
    ),
    colorScheme: ColorScheme.light().copyWith(
      secondary: kMyMessageColorLight, //MessageColors
      secondaryVariant: kOtherMessageColorLight,
    ),
  );
}

ThemeData darkThemeData(BuildContext context) {
  return ThemeData.dark().copyWith(
    primaryColorDark: kPrimaryColor,
    appBarTheme: AppBarTheme(
      backgroundColor: kBackgroundDark,
    ),
    textTheme: TextTheme(
      bodyText1: TextStyle(
        color: Colors.white,
      ),
    ),
    colorScheme: ColorScheme.dark().copyWith(
      secondary: kMyMessageColorDark, //MessageColors
      secondaryVariant: kOtherMessageColorDark,
      onSurface: kFocusedMenuHolderBGColorDark,
    ),
    primaryColor: kPrimaryColor,
    scaffoldBackgroundColor: kBackgroundDark,
    iconTheme: IconThemeData(color: kContentColorDarkTheme),
    bottomNavigationBarTheme: BottomNavigationBarThemeData(
      selectedItemColor: Colors.white70,
      unselectedItemColor: kContentColorDarkTheme.withOpacity(0.6),
      selectedIconTheme: IconThemeData(color: kPrimaryColor),
    ),
    cardTheme: CardTheme(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20.0)),
      margin: EdgeInsets.all(4),
      elevation: 12,
      color: ThemeData.dark().cardColor,
    ),
    bottomAppBarColor: Colors.grey[800],
  );
}
