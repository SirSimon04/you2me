import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:focused_menu/modals.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

List<FocusedMenuItem> getRightContextMenuActions({
  required VoidCallback normalPressed,
  required VoidCallback importantPressed,
  required VoidCallback plannedPressed,
}) {
  return [
    FocusedMenuItem(
      title: Text(
        "Normal senden",
      ),
      onPressed: normalPressed,
      backgroundColor: kFocusedMenuHolderBGColorDark,
    ),
    FocusedMenuItem(
      title: Text(
        "Wichtige Nachricht",
      ),
      onPressed: importantPressed,
      backgroundColor: kFocusedMenuHolderBGColorDark,
      trailingIcon: Icon(FontAwesomeIcons.exclamation),
    ),
    FocusedMenuItem(
      title: Text("Geplant senden"),
      onPressed: plannedPressed,
      backgroundColor: kFocusedMenuHolderBGColorDark,
      trailingIcon: Icon(FontAwesomeIcons.solidCalendar),
    ),
  ];
}

List<FocusedMenuItem> getLeftContextMenuActions(
    {required VoidCallback camera, required VoidCallback pic}) {
  return [
    FocusedMenuItem(
      title: Text(
        "Kamera",
      ),
      onPressed: camera,
      backgroundColor: kFocusedMenuHolderBGColorDark,
      trailingIcon: Icon(FontAwesomeIcons.camera),
    ),
    FocusedMenuItem(
      title: Text(
        "Foto- und Videomediathek",
      ),
      onPressed: pic,
      backgroundColor: kFocusedMenuHolderBGColorDark,
      trailingIcon: Icon(FontAwesomeIcons.solidImages),
    ),
    // FocusedMenuItem(
    //   title: Text(
    //     "Dokument",
    //   ),
    //   onPressed: () {},
    //   backgroundColor: kFocusedMenuHolderBGColor,
    //   trailingIcon: Icon(FontAwesomeIcons.fileUpload),
    // ),
  ];
}
