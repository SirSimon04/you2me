import 'dart:io';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/widgets/fill_outlined_button.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter_dispuatio/models/chat_model.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:flutter_slidable/flutter_slidable.dart';

class ChatListAppBarExtension extends StatelessWidget {
  ChatListAppBarExtension();

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.fromLTRB(
          kDefaultPadding, 0, kDefaultPadding, kDefaultPadding),
      color: Theme.of(context).scaffoldBackgroundColor,
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.only(top: 10),
            child: TextField(
              decoration: InputDecoration(
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(kCircularBorderRadius),
                ),
                hintText: "Suchen",
                hintStyle: TextStyle(
                  color: Colors.white,
                ),
                filled: true,
                prefixIcon: Icon(
                  FontAwesomeIcons.search,
                  color: Colors.white,
                ),
                prefixIconConstraints:
                    BoxConstraints(minWidth: 44, minHeight: 44),
                isDense: true,
                contentPadding: EdgeInsets.all(6),
              ),
            ),
          ),
          SizedBox(
            height: 20,
          ),
          /*
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              FillOutlineButton(
                false,
                "Archivierte Chats",
                () {},
              ),
              SizedBox(
                width: 20,
              ),
              FillOutlineButton(
                true,
                "Neue Gruppe",
                () {},
              ),
            ],
          ),
          */
        ],
      ),
    );
  }
}
