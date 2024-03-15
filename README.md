# Android Sample App by CometChat

This is a reference application showcasing the integration of [**CometChat's Android UI Kit**](https://www.cometchat.com/docs/v4/android-uikit/overview) within an Android framework. It provides developers with examples of implementing real-time messaging and voice and video calling features in their own Android-based applications.

## Prerequisites

- Android Studio 
- Android Device or emulator with Android version 6.0 or above.
- Java or above.
- Sign up for a [CometChat](https://app.cometchat.com/) account to get your app credentials: _`App ID`_, _`Region`_, and _`Auth Key`_


## Installation
1. Clone the repository:
    ```
    git clone https://github.com/cometchat/cometchat-sample-app-android-java.git
    ```
2. In Android Studio, open the project cometchat-sample-app-android-java

3. Enter your CometChat _`App ID`_, _`Region`_, and _`Auth Key`_ in the [app/src/main/java/com/cometchat/javasampleapp/AppConstants.java](https://github.com/cometchat/cometchat-sample-app-android-java/blob/v4/app/src/main/java/com/cometchat/javasampleapp/AppConstants.java) file:
    ```java
    public static final String APP_ID = "XXXXXXXXXXXXXXXXX";
    public static final String AUTH_KEY = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String REGION = "XX";
    ```
4. Run the app on a device or emulator.
![image (2)](https://github.com/cometchat/cometchat-sample-app-android-java/assets/2210730/5f78442e-11f1-438f-9344-d379a865a529)

## Help and Support
For issues running the project or integrating with our UI Kits, consult our [documentation](https://www.cometchat.com/docs/android-uikit/integration) or create a [support ticket](https://help.cometchat.com/hc/en-us) or seek real-time support via the [CometChat Dashboard](http://app.cometchat.com/).
