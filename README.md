## About

This is small project to study Android MVP architecture and the testability of it.

Application uses LocationManager to gain location of the device and Foursquare REST API through OKHttp3 library to search venues matching users criteria.

Model and presenter implement application business logic and are tested using Mockito 2 framework.

View is as thin as possible, tested on device with Espresso framework and Mockito-android.
