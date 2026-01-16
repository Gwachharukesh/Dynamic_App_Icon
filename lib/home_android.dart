import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HomeAndroid extends StatefulWidget {
  const HomeAndroid({super.key});

  @override
  State<HomeAndroid> createState() => _HomeAndroidState();
}

class _HomeAndroidState extends State<HomeAndroid> {
  final MethodChannel platformChannel =
      MethodChannel('com.example.change_icon/change_icon');
  final List<String> icons = [
    'AppIcon',
    'AppIconNewYear',
  ];

  Future<void> _changeIcon(String iconName) async {
    try {
      final result = await platformChannel.invokeMethod('changeIcon', iconName);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: const Text('Icon changed! Restarting app...'),
            duration: const Duration(seconds: 2),
          ),
        );
        // Small delay to show the snackbar before restart
        await Future.delayed(const Duration(seconds: 2));
        // Attempt programmatic restart (Android only)
        await platformChannel.invokeMethod('restartApp');
      }
    } on PlatformException catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error changing icon: ${e.message}')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Change App Icon (Android)'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          children: [
            const Text(
              'Select an icon. On Android, changes take effect after app restart.',
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
           ListTile(
                onTap: () => _changeIcon('AppIcon'),
                title: const Text('Default Icon'),
                leading: Image.asset('assets/AppIcon.png'),
              ),
           

              ListTile(
                onTap: () => _changeIcon('AppIconNewYear'),
                title: const Text('New Year Icon'),
                leading: Image.asset('assets/new_year.png'),
              ),
          ],
        ),
      ),
    );
  }
}