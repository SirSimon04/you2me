import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';

class UserProfilePic extends StatelessWidget {
  UserProfilePic({required this.isOnline, required this.url});

  final String url;
  final bool isOnline;

  @override
  Widget build(BuildContext context) {
    print("url here " + url);
    return Stack(
      children: [
        CircleAvatar(
          backgroundImage: url ==
                  "https://firebasestorage.googleapis.com/v0/b/disputatio-a1039.appspot.com/o/user.png?alt=media&token=46927ec9-a8d4-431a-9fc1-60cbef1e4f2a"
              ? Image.asset("assets/user.png").image
              : CachedNetworkImageProvider(url),
        ),
        if (isOnline)
          Positioned(
            right: 0,
            bottom: 0,
            child: Container(
              height: 16,
              width: 16,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: Colors.greenAccent,
                border: Border.all(
                  color: Theme.of(context).scaffoldBackgroundColor,
                  width: 3,
                ),
              ),
            ),
          )
      ],
    );
  }
}
