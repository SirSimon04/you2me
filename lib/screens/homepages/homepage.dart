import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/screens/homepages/responsive.dart';

import 'desktop_tablet_homepage.dart';
import 'mobile_homepage.dart';

class MyHomePage extends StatelessWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Responsive(
      desktop: TabletDesktopHomePage(),
      mobile: MobileHomePage(),
      tablet: TabletDesktopHomePage(),
    );
  }
}
