import 'dart:math';

import 'package:flutter/material.dart';

Widget customColorChip({
  required String label,
  required Widget avatar,
}) {
  List colors = [
    Colors.red,
    Colors.green,
    Colors.green,
    Colors.teal,
    Colors.purple,
    Colors.blue,
    Colors.brown,
    Colors.indigo,
    Colors.deepPurple,
  ];
  Random random = new Random();

  return Chip(
    labelPadding: EdgeInsets.all(5.0),
    avatar: avatar,
    label: Text(
      label,
      style: TextStyle(
        color: Colors.white,
      ),
    ),
    backgroundColor: colors[random.nextInt(colors.length)],
    elevation: 6.0,
    shadowColor: Colors.grey[60],
    padding: EdgeInsets.all(6.0),
  );
}
