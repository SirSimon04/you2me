import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';

class FillOutlineButton extends StatelessWidget {
  final bool isFilled;
  final VoidCallback onPress;
  final String text;

  FillOutlineButton(this.isFilled, this.text, this.onPress);

  @override
  Widget build(BuildContext context) {
    return MaterialButton(
      onPressed: onPress,
      shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(kCircularBorderRadius),
          side: BorderSide(color: Colors.white)),
      color: isFilled ? Colors.white : Colors.transparent,
      child: Text(
        text,
        style: TextStyle(
          color: isFilled ? Colors.black : Colors.white,
          fontSize: 12,
        ),
      ),
      elevation: isFilled ? 2 : 0,
    );
  }
}
