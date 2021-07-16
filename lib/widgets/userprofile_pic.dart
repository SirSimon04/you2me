import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';

class UserProfilePic extends StatelessWidget {
  UserProfilePic({required this.isOnline, required this.url});

  final String url;
  final bool isOnline;

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        CircleAvatar(
          backgroundImage: CachedNetworkImageProvider(url),
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
