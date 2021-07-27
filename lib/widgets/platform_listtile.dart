import 'dart:io';

import 'package:cupertino_list_tile/cupertino_list_tile.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class PlatformListTile extends StatelessWidget {
  PlatformListTile({
    this.leading,
    this.title,
    this.subtitle,
    this.trailing,
    this.isThreeLine = false,
    this.dense,
    this.contentPadding,
    this.enabled = true,
    this.onTap,
    this.onLongPress,
    this.selected = false,
    this.focusColor,
    this.hoverColor,
    this.focusNode,
    this.autofocus = false,
    this.tileColorM,
    this.selectedTileColorM,
    this.enableFeedbackM,
    this.horizontalTitleGapM,
    this.minVerticalPaddingM,
    this.minLeadingWidthM,
    this.isElevatedM = false,
    this.shapeM,
    this.visualDensitiyM,
    this.elevation = 6.0,
  });

  final Widget? leading;
  final Widget? title;
  final Widget? subtitle;
  final Widget? trailing;
  final bool isElevatedM;
  final bool enabled;
  final bool isThreeLine;
  final bool? dense;
  final EdgeInsetsGeometry? contentPadding;
  final GestureTapCallback? onTap;
  final GestureLongPressCallback? onLongPress;
  final bool selected;
  final Color? focusColor;
  final Color? hoverColor;
  final FocusNode? focusNode;
  final bool autofocus;
  final Color? tileColorM;
  final Color? selectedTileColorM;
  final bool? enableFeedbackM;
  final double? horizontalTitleGapM;
  final double? minVerticalPaddingM;
  final double? minLeadingWidthM;
  final ShapeBorder? shapeM;
  final VisualDensity? visualDensitiyM;
  final double elevation;

  @override
  Widget build(BuildContext context) {
    if (Platform.isIOS) {
      return CupertinoListTile(
        isThreeLine: isThreeLine,
        dense: dense,
        contentPadding: contentPadding,
        onTap: onTap,
        onLongPress: onLongPress,
        selected: selected,
        focusColor: focusColor,
        hoverColor: hoverColor,
        focusNode: focusNode,
        autofocus: autofocus,
        enabled: enabled,
        leading: leading,
        title: title,
        subtitle: subtitle,
        trailing: trailing,
      );
    } else if (isElevatedM) {
      return Card(
        elevation: elevation,
        child: MaterialListTile(
          title: title,
          leading: leading,
          subtitle: subtitle,
          trailing: trailing,
          enabled: enabled,
          isThreeLine: isThreeLine,
          dense: dense,
          contentPadding: contentPadding,
          onTap: onTap,
          onLongPress: onLongPress,
          selected: selected,
          focusColor: focusColor,
          hoverColor: hoverColor,
          focusNode: focusNode,
          autofocus: autofocus,
          tileColor: tileColorM,
          selectedTileColor: selectedTileColorM,
          enableFeedback: enableFeedbackM,
          horizontalTitleGap: horizontalTitleGapM,
          minVerticalPadding: minVerticalPaddingM,
          minLeadingWidth: minLeadingWidthM,
          shape: shapeM,
          visualDensity: visualDensitiyM,
        ),
      );
    } else {
      return MaterialListTile(
        title: title,
        leading: leading,
        subtitle: subtitle,
        trailing: trailing,
        enabled: enabled,
        isThreeLine: isThreeLine,
        dense: dense,
        contentPadding: contentPadding,
        onTap: onTap,
        onLongPress: onLongPress,
        selected: selected,
        focusColor: focusColor,
        hoverColor: hoverColor,
        focusNode: focusNode,
        autofocus: autofocus,
        tileColor: tileColorM,
        selectedTileColor: selectedTileColorM,
        enableFeedback: enableFeedbackM,
        horizontalTitleGap: horizontalTitleGapM,
        minVerticalPadding: minVerticalPaddingM,
        minLeadingWidth: minLeadingWidthM,
        shape: shapeM,
        visualDensity: visualDensitiyM,
      );
    }
  }
}

class MaterialListTile extends StatelessWidget {
  MaterialListTile({
    this.leading,
    this.title,
    this.subtitle,
    this.trailing,
    this.isThreeLine = false,
    this.dense,
    this.contentPadding,
    this.enabled = true,
    this.onTap,
    this.onLongPress,
    this.selected = false,
    this.focusColor,
    this.hoverColor,
    this.focusNode,
    this.autofocus = false,
    this.tileColor,
    this.selectedTileColor,
    this.enableFeedback,
    this.horizontalTitleGap,
    this.minVerticalPadding,
    this.minLeadingWidth,
    this.isElevated = false,
    this.shape,
    this.visualDensity,
    this.elevation = 6.0,
  });

  final Widget? leading;
  final Widget? title;
  final Widget? subtitle;
  final Widget? trailing;
  final bool isElevated;
  final bool enabled;
  final bool isThreeLine;
  final bool? dense;
  final EdgeInsetsGeometry? contentPadding;
  final GestureTapCallback? onTap;
  final GestureLongPressCallback? onLongPress;
  final bool selected;
  final Color? focusColor;
  final Color? hoverColor;
  final FocusNode? focusNode;
  final bool autofocus;
  final Color? tileColor;
  final Color? selectedTileColor;
  final bool? enableFeedback;
  final double? horizontalTitleGap;
  final double? minVerticalPadding;
  final double? minLeadingWidth;
  final ShapeBorder? shape;
  final VisualDensity? visualDensity;
  final double? elevation;

  @override
  Widget build(BuildContext context) {
    return isElevated
        ? Card(
            elevation: elevation,
            child: ListTile(
              title: title,
              leading: leading,
              subtitle: subtitle,
              trailing: trailing,
              enabled: enabled,
              isThreeLine: isThreeLine,
              dense: dense,
              contentPadding: contentPadding,
              onTap: onTap,
              onLongPress: onLongPress,
              selected: selected,
              focusColor: focusColor,
              hoverColor: hoverColor,
              focusNode: focusNode,
              autofocus: autofocus,
              tileColor: tileColor,
              selectedTileColor: selectedTileColor,
              enableFeedback: enableFeedback,
              horizontalTitleGap: horizontalTitleGap,
              minVerticalPadding: minVerticalPadding,
              minLeadingWidth: minLeadingWidth,
              shape: shape,
              visualDensity: visualDensity,
            ),
          )
        : ListTile(
            title: title,
            leading: leading,
            subtitle: subtitle,
            trailing: trailing,
            enabled: enabled,
            isThreeLine: isThreeLine,
            dense: dense,
            contentPadding: contentPadding,
            onTap: onTap,
            onLongPress: onLongPress,
            selected: selected,
            focusColor: focusColor,
            hoverColor: hoverColor,
            focusNode: focusNode,
            autofocus: autofocus,
            tileColor: tileColor,
            selectedTileColor: selectedTileColor,
            enableFeedback: enableFeedback,
            horizontalTitleGap: horizontalTitleGap,
            minVerticalPadding: minVerticalPadding,
            minLeadingWidth: minLeadingWidth,
            shape: shape,
            visualDensity: visualDensity,
          );
  }
}
