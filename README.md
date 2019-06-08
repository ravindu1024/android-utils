# Android Utils
Helpers and simple implementations of frequently used utility classes

# Installation
Include this in the main project gradle file
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
		maven { url 'https://maven.google.com' }
	}
}
```
Include this in the app dependencies
```gradle
	dependencies {
	        compile 'com.github.ravindu1024:android-utils:v2.0.0'
	}

```

If you get a "library missing" error, just add "compile 'com.google.code.gson:gson:2.7'" to the module dependencies list.
