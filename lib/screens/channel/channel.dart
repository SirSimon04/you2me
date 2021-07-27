import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/data/users.dart';
import 'package:flutter_dispuatio/screens/channel/story_page_view.dart';

// class Channel extends StatefulWidget {
//   const Channel({Key? key}) : super(key: key);
//
//   @override
//   _ChannelState createState() => _ChannelState();
// }
//
// class _ChannelState extends State<Channel> {
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//         appBar: AppBar(
//           title: Text("Channel"),
//         ),
//         body: Padding(
//           padding: EdgeInsets.all(8),
//           child: ListView(
//             children: users
//                 .map((user) => ListTile(
//                       title: Text(user.name),
//                       leading: CircleAvatar(
//                         backgroundImage: AssetImage(user.imgUrl),
//                       ),
//                       onTap: () {
//                         Navigator.of(context).push(
//                           CupertinoPageRoute(
//                             builder: (context) => StoryPageView(user: user),
//                           ),
//                         );
//                       },
//                     ))
//                 .toList(),
//           ),
//         ));
//   }
// }

class Channel extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Test"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          children: [
            Divider(
              color: Colors.white38,
            ),
            SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: Row(
                children: users
                    .map(
                      (user) => GestureDetector(
                        onTap: () {
                          Navigator.of(context).push(
                            CupertinoPageRoute(
                              builder: (context) => StoryPage(user: user),
                            ),
                          );
                        },
                        child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Column(
                            children: [
                              CircleAvatar(
                                radius: 32,
                                backgroundImage: AssetImage(user.imgUrl),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(4.0),
                                child: Text(user.name),
                              )
                            ],
                          ),
                        ),
                      ),
                    )
                    .toList(),
              ),
            ),
            Divider(
              color: Colors.white38,
            ),
          ],
        ),
      ),
    );
  }
}
