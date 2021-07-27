import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';

class GlassMorphismContainer extends StatelessWidget {
  GlassMorphismContainer({
    required this.height,
    required this.child,
    required this.width,
    required this.color,
    this.onTap,
  });

  final double height;
  final double width;
  final Widget child;
  final Color color;
  final Function()? onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(boxShadow: [
          BoxShadow(
            blurRadius: 24,
            spreadRadius: 16,
            color: Colors.black.withOpacity(0.2),
          )
        ]),
        child: ClipRect(
          child: BackdropFilter(
            filter: ImageFilter.blur(
              sigmaX: kBlurEffectValue,
              sigmaY: kBlurEffectValue,
            ),
            child: Container(
              height: height,
              width: width,
              decoration: BoxDecoration(
                color: color,
                borderRadius: BorderRadius.circular(kCircularBorderRadius / 2),
                border: Border.all(
                  width: 1.5,
                  color: Colors.white.withOpacity(kColorOpacityValue),
                ),
              ),
              child: Center(child: child),
            ),
          ),
        ),
      ),
    );
  }
}
