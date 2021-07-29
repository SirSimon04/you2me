import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/data/users.dart';
import 'package:flutter_dispuatio/models/user_old.dart';
import 'package:flutter_dispuatio/screens/channel/profile_widget.dart';
import 'package:story_view/story_view.dart';

class StoryWidget extends StatefulWidget {
  final User user;
  final PageController controller;

  const StoryWidget({
    required this.user,
    required this.controller,
  });

  @override
  _StoryWidgetState createState() => _StoryWidgetState();
}

class _StoryWidgetState extends State<StoryWidget> {
  var storyItems = <StoryItem>[];
  StoryController controller = StoryController();
  String date = '';

  void addStoryItems() {
    storyItems.add(StoryItem.text(title: "HEy", backgroundColor: Colors.red));
    storyItems.add(StoryItem.text(title: "HEy", backgroundColor: Colors.red));
    storyItems.add(StoryItem.text(title: "HEy", backgroundColor: Colors.red));
    storyItems.add(StoryItem.text(title: "HEy", backgroundColor: Colors.red));

    // for (final story in widget.user.stories) {
    //   switch (story.mediaType) {
    //     case MediaType.image:
    //       storyItems.add(
    //         StoryItem.pageImage(
    //           url: story.url,
    //           controller: controller,
    //           caption: story.caption,
    //           duration: Duration(
    //             milliseconds: (story.duration * 1000).toInt(),
    //           ),
    //         ),
    //       );
    //       break;
    //     case MediaType.text:
    //       storyItems.add(
    //         StoryItem.text(
    //           title: story.caption,
    //           backgroundColor: story.color,
    //           duration: Duration(
    //             milliseconds: (story.duration * 1000).toInt(),
    //           ),
    //         ),
    //       );
    //       break;
    //   }
    // }
  }

  @override
  void initState() {
    addStoryItems();

    controller = StoryController();

    date = widget.user.stories[0].date;

    super.initState();
  }

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  void handleCompleted() {
    widget.controller.nextPage(
      duration: Duration(milliseconds: 300),
      curve: Curves.easeIn,
    );

    final currentIndex = users.indexOf(widget.user);
    final isLastPage = users.length - 1 == currentIndex;

    if (isLastPage) {
      Navigator.of(context).pop();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[
        Material(
          type: MaterialType.transparency,
          child: StoryView(
            repeat: false,
            progressPosition: ProgressPosition.top,
            inline: false,
            storyItems: [
              StoryItem.text(title: "Hey", backgroundColor: Colors.red),
              StoryItem.text(title: "Hey", backgroundColor: Colors.blue),
              StoryItem.text(title: "Hey", backgroundColor: Colors.yellow),
              StoryItem.text(title: "Hey", backgroundColor: Colors.green),
            ],
            controller: controller,
            onComplete: handleCompleted,
            onVerticalSwipeComplete: (direction) {
              if (direction == Direction.down) {
                Navigator.pop(context);
              }
            },
            onStoryShow: (storyItem) {
              final index = storyItems.indexOf(storyItem);

              if (index > 0) {
                setState(() {
                  date = widget.user.stories[index].date;
                });
              }
            },
          ),
        ),
        ProfileWidget(
          user: widget.user,
          date: date,
        ),
      ],
    );
  }
}
