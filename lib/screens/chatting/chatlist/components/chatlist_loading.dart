import 'package:flutter/material.dart';
import 'package:skeleton_text/skeleton_text.dart';

class ChatListLoading extends StatefulWidget {
  const ChatListLoading({Key? key}) : super(key: key);

  @override
  _ChatListLoadingState createState() => _ChatListLoadingState();
}

class _ChatListLoadingState extends State<ChatListLoading> {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: 90,
      width: double.infinity,
      child: Row(
        children: [
          SkeletonAnimation(
            child: Container(
              margin: EdgeInsets.all(10.0),
              height: 50,
              width: 50,
              decoration: BoxDecoration(
                color: Colors.grey,
              ),
            ),
          ),
          Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SkeletonAnimation(
                child: Container(
                  margin: EdgeInsets.all(10.0),
                  height: 10,
                  width: 100,
                  decoration: BoxDecoration(
                    color: Colors.grey,
                  ),
                ),
              ),
              SkeletonAnimation(
                child: Container(
                  margin: EdgeInsets.all(10.0),
                  height: 10,
                  width: 200,
                  decoration: BoxDecoration(
                    color: Colors.grey,
                  ),
                ),
              ),
            ],
          )
        ],
      ),
    );
  }
}
