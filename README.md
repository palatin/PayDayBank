# PayDay Bank
The app is test project demonstating my vision of banking client.
It contains next features:
+ SignIn with fingerprint auth from android 6.0. User's password stores in the most security place - AndroidKeystore system with AES encryption. (Note: authenticate API not working, so the application return stub account data)
+ Fetching and displaying all transaction categories by given account with sorting by date's range.
+ Displaying transactions by given category

Project uses next libraries/technologies: Kotlin, Okhttp, Coroutines, Retrofit, Dagger2 with Hilt, Lifecycle, LiveData, ViewModel, Moshi, Glide, JUnit, Mockito, UI navigation

### Downloads
[apk](app-debug.apk)

<img src="/example.gif" alt="sample" title="sample" width="146" height="260" />

### TODOS/Improvements
+ resources don't contain big logo
+ icon logo which is presented in resources has shape. Icon shape should be generated by android studio tools.
+ smooth screen transaction/animations
+ displaying such errors as no network connection/servers offline
+ Showing data loading progress
+ caching data in rooms database
+ Create java docs
+ Make viewmodel unit tests/ ui tests
+ Using diffutils inside adapters instead of 'notifyDataSetChanged()'
+ make ui constraints, e.g. hanling long texts, different screen sizes
+ handle application state restoration
