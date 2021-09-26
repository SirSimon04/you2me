import 'package:flutter/material.dart';

class SavedOverview extends StatelessWidget {
  const SavedOverview({Key? key}) : super(key: key);

  List<DataColumn> getColumns() {
    return [
      DataColumn(
        label: Text(
          "Chat",
          style: TextStyle(fontStyle: FontStyle.italic),
        ),
      ),
      DataColumn(
        label: Text(
          "Speicher",
          style: TextStyle(fontStyle: FontStyle.italic),
        ),
      ),
      DataColumn(
        label: Text(
          "Nachrichten",
          style: TextStyle(fontStyle: FontStyle.italic),
        ),
      ),
    ];
  }

  List<DataRow> getRows() {
    List<DataRow> list = [];
    for (int x = 0; x < 50; x++) {
      list.add(
        DataRow(
          cells: getCells(x),
        ),
      );
    }
    return list;
  }

  List<DataCell> getCells(int x) {
    return [
      DataCell(
        Row(
          children: [
            CircleAvatar(
              radius: 25,
              backgroundImage: AssetImage("assets/user.png"),
            ),
            SizedBox(
              width: 10,
            ),
            Text("Jan"),
          ],
        ),
      ),
      DataCell(
        Text("${(x * 6.753).toStringAsFixed(1)} mb"),
      ),
      DataCell(
        Text("${x * 10}"),
      ),
    ];
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "Speicherübersicht",
          style: TextStyle(
            color: Colors.white,
          ),
        ),
        automaticallyImplyLeading: false,
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          color: Colors.white,
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: SingleChildScrollView(
        scrollDirection: Axis.vertical,
        child: DataTable(
          rows: getRows(),
          columns: getColumns(),
          dataRowHeight: 80.0,
          dividerThickness: 3,
        ),
      ),
    );
  }
}
