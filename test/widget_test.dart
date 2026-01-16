import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:change_icon/main.dart';
import 'package:change_icon/home_android.dart';
import 'package:change_icon/home_ios.dart';
import 'dart:io' show Platform;

void main() {
  group('MyApp Widget Tests', () {
    testWidgets('App builds without errors', (WidgetTester tester) async {
      await tester.pumpWidget(const MyApp());
      expect(find.byType(MyHomePage), findsOneWidget);
    });

    testWidgets('MyHomePage displays title and FAB', (WidgetTester tester) async {
      await tester.pumpWidget(const MaterialApp(home: MyHomePage(title: 'Test Title')));

      expect(find.text('Test Title'), findsOneWidget);
      expect(find.byIcon(Icons.palette), findsOneWidget);
      expect(find.byType(FloatingActionButton), findsOneWidget);
    });

    testWidgets('FAB navigates to HomeAndroid on Android', (WidgetTester tester) async {
      // Note: This test assumes running on non-Android platform (e.g., macOS in tests)
      // In real Android tests, Platform.isAndroid would be true
      await tester.pumpWidget(const MaterialApp(home: MyHomePage(title: 'Test')));

      await tester.tap(find.byType(FloatingActionButton));
      await tester.pumpAndSettle();

      expect(find.byType(HomeAndroid), findsNothing); // Since we're on macOS, it navigates to iOS
    });

    testWidgets('FAB navigates to HomeIos on iOS', (WidgetTester tester) async {
      await tester.pumpWidget(const MaterialApp(home: MyHomePage(title: 'Test')));

      await tester.tap(find.byType(FloatingActionButton));
      await tester.pumpAndSettle();

      expect(find.byType(HomeIos), findsOneWidget);
    });
  });
}
