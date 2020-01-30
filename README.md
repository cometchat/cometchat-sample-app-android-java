<div style="width:100%">
<div style="width:100%">
	<div style="width:50%; display:inline-block">
		<p align="center">
		<img align="center" width="180" height="180" alt="" src="https://github.com/cometchat-pro/ios-swift-chat-app/blob/master/Screenshots/CometChat%20Logo.png">	
		</p>	
	</div>	
</div>
</br>
</br>
</div>

CometChat Android Demo app (built using **CometChat Pro**) is a fully functional messaging app capable of **one-on-one** (private) and **group** messaging. The app enables users to send **text** and **multimedia messages like audio, video, images, documents.**

[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](#)      [![Platform](https://img.shields.io/badge/Language-Java-yellowgreen.svg)](#)

## Table of Contents

1. [Set up and Run](#set-up-and-run)

2. [Screenshots ](#screenshots)

2. [Contribute](#contribute)



## Set up and Run

   Simply Clone the project from android-java-chat-app repository and open in Android Studio.
   Build the Demo App and it will be ready to Run


   To run our open source app with CometChat Pro, follow these steps:


1. modify *APP_ID* and *API_KEY* with your own **ApiKey** , **AppId** and **REGION**

   - Open the Project in Android Mode in Android Studio

   - Go to Under java --> com\inscripts\cometchatpulse\demo --> Contracts --> StringContract

   - Under class `StringContract.class`  go to static `class` named `AppDetails`

        `public static final String APP_ID  = "XXXXXXXXX";`

       ` public static final String API_KEY = "XXXXXXXXX";`
       
        `public static final String REGION  = "XXXXXXXXX";`

2. Add Push Notifications to the Sample App.

       Add your own `google-services.json` to start receiving Push Notification.
       
       For more deatail follow the below steps:

     
      [Add Push Notification to your Android project](https://prodocs.cometchat.com/docs/android-extensions-push-notification)
      
  - To enable push notification in your app you also need to enable push notification extension in your app from CometChat       DashBoard.To enable push notification extension follow the below steps:
         
      * Go to your app in [CometChat Dashboard ](https://app.cometchat.io/)
      * Go to Extension section 
      * Add Push Notification Extension
      * Add **FCM Server Key** in Push Notification Extension   
      
      
       

     
## Note


   You can Obtain your  *APP_ID* and *API_KEY* from [CometChat-Pro Dashboard](https://app.cometchat.com/)

   For more information read [CometChat-Pro Android SDK](https://prodocs.cometchat.com/docs/android-quick-start) Documentation




  ![Studio Guide](https://github.com/CometChat-Pulse/android-java-chat-app/blob/master/ScreenShots/guide.png)                                    


## Push Notification

The Push Notification extension allows you to send push notifications to mobile apps and desktop browsers. 
	
For more information read [Android-Push-Notification](https://prodocs.cometchat.com/docs/android-extensions-push-notification) Documentation.
	





## Screenshots

   <img align="left" width="185" height="331" src="https://github.com/CometChat-Pulse/android-java-chat-app/blob/master/ScreenShots/gif_1.gif">


   <img align="left" width="185" height="331" src="https://github.com/CometChat-Pulse/android-java-chat-app/blob/master/ScreenShots/gif_2.gif">


   <img align="left" width="185" height="331" src="https://github.com/cometchat-pro-samples/android-java-chat-app/blob/master/ScreenShots/gif3.gif">



   <img align="left" width="185" height="331" src="https://github.com/cometchat-pro-samples/android-java-chat-app/blob/master/ScreenShots/gif_4.gif">

`            `<br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br></br>


## Contribute
   
   
   Feel free to make Pull Request. 
   
