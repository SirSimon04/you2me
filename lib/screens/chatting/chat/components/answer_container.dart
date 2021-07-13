import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';

Padding getAnswerContainer(var answerMessage, context) {
  return Padding(
    padding: const EdgeInsets.symmetric(vertical: 8.0),
    child: SizedBox(
      height: 60,
      child: Container(
        width: double.infinity,
        decoration: BoxDecoration(
          color: Colors.grey,
          borderRadius: BorderRadius.circular(kCircularBorderRadius * 0.5),
        ),
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                "Simon",
                style: TextStyle(color: Colors.redAccent),
              ),
              Text(
                answerMessage == null ? "" : answerMessage.text,
                overflow: TextOverflow.ellipsis,
              ),
            ],
          ),
        ),
      ),
    ),
  );
}
