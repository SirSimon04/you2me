import 'package:flutter/material.dart';

class ListDivider extends StatelessWidget {
  const ListDivider({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(
        vertical: 30.0,
        horizontal: 40.0,
      ),
      child: SizedBox(
        width: 150,
        height: 10.0,
        child: Divider(
          color: Colors.black,
        ),
      ),
    );
  }
}
