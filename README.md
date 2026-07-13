MALL APP – Smart Mall Navigation & Shopping Android App
=========================================================

Semester Project | Mobile Application Development
Android Studio | Java | XML

Repository: https://github.com/syeda-iram/Mall-App


ABOUT THE PROJECT
------------------
This is my semester project for the Mobile Application Development course.
The idea is a mall app that helps users find their way around a shopping
mall — like locating stores, checking parking, seeing ongoing offers, and
browsing restaurants, all in one app. Built using Java and XML in Android
Studio as part of the coursework.


FEATURES
--------
- User Sign Up / Login with account creation
- Home screen with quick access to Parking, 3D Map, Offers, and Restaurants
- Hot Offers screen showing store-wise deals and discounts
- Interactive 3D indoor map with:
    * Floor selection (F1, F2, F3)
    * Live user location on the map
    * Nearest store / parking / food court distance info
    * Navigation with real-time "remaining distance" tracking
    * "Locate Store" and "Navigate" actions
- Parking slot booking with confirmation
- Restaurant listing with ratings, cuisine type, and active discounts
- Store & product search with category-wise suggested stores
- User Profile screen with:
    * Account details
    * Quick access to Services (Parking, 3D Map, Offers, Restaurants)
    * Recent activity / purchase history
- Bottom navigation bar: Home, Search, Favourites, Profile


TECH STACK
----------
- Language      : Java
- UI            : XML (Android Views/Layouts)
- IDE           : Android Studio
- Build System  : Gradle (Kotlin DSL - build.gradle.kts)
- Min SDK       : 24
- Target SDK    : 36
- Backend       : Firebase (Authentication, Realtime Database)
- Maps/Location : Google Maps, Google Play Services Location


PROJECT STRUCTURE
------------------
Mall-App/

 |-- app/                    -> Main application module (Java classes, layouts, resources)

 |-- gradle/                 -> Gradle wrapper files
 
 |-- build.gradle.kts        -> Project-level build configuration
 
 |-- settings.gradle.kts     -> Project settings
 
 |-- gradle.properties       -> Gradle configuration properties


HOW TO RUN
-----------
1. Clone this repository:
   git clone https://github.com/syeda-iram/Mall-App.git
2. Open the project in Android Studio
3. Let Gradle sync complete
4. Run the app on an emulator or physical device (API level 24 or above)


DEMO
-----
A demo video (demo.mp4) is included showing the complete user flow:

Sign Up -> Home -> Hot Offers -> 3D Map Navigation -> Parking -> Restaurants
-> Profile.


TEAM / CREDITS
---------------
Developed by: Tasmiya Iram

Course: Mobile Application Development


NOTE
----
This project was developed for academic purposes as part of the
university semester coursework.
