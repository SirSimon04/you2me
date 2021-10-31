import 'package:flutter/material.dart';
import 'package:flutter_dispuatio/constants.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class SignUpEnterName extends StatefulWidget {
  @override
  _SignUpEnterNameState createState() => _SignUpEnterNameState();
}

class _SignUpEnterNameState extends State<SignUpEnterName> {
  final myController = TextEditingController();
  bool isButtonEnabled = false;
  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    myController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false,
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          color: Colors.white,
          onPressed: () => Navigator.of(context).pop(),
        ),
        title: Text("Wähle einen Nutzernamen",
            style: TextStyle(color: Colors.white)),
      ),
      body: Container(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Center(
              child: Container(
                width: MediaQuery.of(context).size.width * 0.8,
                child: Card(
                  child: Padding(
                    padding: const EdgeInsets.all(24.0),
                    child: Column(
                      children: [
                        Text(
                          "Nutzernamen wählen",
                          style: Theme.of(context).textTheme.headline6,
                        ),
                        Padding(
                          padding: const EdgeInsets.symmetric(vertical: 20),
                          child: Text(
                            "Dieser ist für andere Nutzer sichtbar",
                            style: Theme.of(context).textTheme.subtitle1,
                            textAlign: TextAlign.center,
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.symmetric(
                              vertical: kDefaultPadding),
                          child: TextField(
                            controller: myController,
                            onChanged: (String x) {
                              if (x.trim().length > 0) {
                                setState(() {
                                  isButtonEnabled = true;
                                });
                              } else {
                                setState(() {
                                  isButtonEnabled = false;
                                });
                              }
                            },
                            decoration: InputDecoration(
                              filled: true,
                              prefixIcon:
                                  Icon(FontAwesomeIcons.solidUserCircle),
                              fillColor: Color.alphaBlend(
                                Colors.grey.shade200.withOpacity(.07),
                                Colors.grey.withOpacity(.04),
                              ),
                              contentPadding:
                                  EdgeInsets.symmetric(vertical: 4.0),
                              enabledBorder: OutlineInputBorder(
                                borderSide:
                                    BorderSide(color: Colors.transparent),
                                borderRadius: BorderRadius.circular(100),
                              ),
                              focusedBorder: OutlineInputBorder(
                                borderSide: BorderSide(width: 1.5),
                                borderRadius: BorderRadius.circular(100),
                              ),
                              errorBorder: OutlineInputBorder(
                                borderSide: BorderSide(color: kErrorColor),
                                borderRadius: BorderRadius.circular(100),
                              ),
                            ),
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(top: kDefaultPadding),
                          child: TextButton(
                            onPressed: !isButtonEnabled
                                ? null
                                : () {
                                    Navigator.pop(context, myController.text);
                                  },
                            child: Text(
                              "SIGNUP",
                              style: TextStyle(
                                color: Colors.white,
                                fontSize: 16,
                              ),
                            ),
                            style: ButtonStyle(
                              elevation: MaterialStateProperty.all<double>(8),
                              padding: MaterialStateProperty.all<EdgeInsets>(
                                EdgeInsets.symmetric(
                                  horizontal: kDefaultPadding * 1.5,
                                ),
                              ),
                              backgroundColor: myController.text.isEmpty
                                  ? MaterialStateProperty.all<Color>(
                                      Colors.grey)
                                  : MaterialStateProperty.all<Color>(
                                      Colors.blue),
                              shadowColor: myController.text.isEmpty
                                  ? MaterialStateProperty.all<Color>(
                                      Colors.grey)
                                  : MaterialStateProperty.all<Color>(
                                      Colors.blue),
                              shape: MaterialStateProperty.all<
                                  RoundedRectangleBorder>(
                                RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(100),
                                ),
                              ),
                            ),
                          ),
                        )
                      ],
                    ),
                  ),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
