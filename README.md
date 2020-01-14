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

1. [Installation ](#installtion)

2. [Run the Sample App ](#run-the-sample-app)

3. [Screenshots ](#screenshots)

4. [Contribute](#contribute)



## Installtion

   Simply Clone the project from android-java-chat-app repository and open in Android Studio.
   Build the Demo App and it will be ready to Run

  ## v2 Apps

To run our open source app with CometChat Pro v2, follow these steps:

1. Check out to branch v2

-  modify *APP_ID* and *API_KEY* with your own **ApiKey** , **AppId** and **REGION**

        `public static final String APP_ID  = "XXXXXXXXX";`

       ` public static final String API_KEY = "XXXXXXXXX";`
       
        `public static final String REGION  = "XXXXXXXXX";`

2. Build and run the Sample App.

## v1 Apps

To run our open source app with CometChat Pro v1, follow these steps:

1. Check out to branch v1

-  modify *APP_ID* and *API_KEY* with your own **ApiKey** and **AppId**

       `public static final String APP_ID  = "XXXXXXXXX";`

       ` public static final String API_KEY = "XXXXXXXXX";`
       
2. Build and run the Sample App.

 - To Run the app you also need to include **google-services.json** from your firebase console ,To connect the app to     
    firebase follow the instruction below :
     
      [Add Firebase to your Android project](https://firebase.google.com/docs/android/setup)
      
  - To enable push notification in your app you also need to enable push notification extension in your app from CometChat       DashBoard.To enable push notification extension follow the below steps:
         
      * Go to your app in [CometChat Dashboard ](https://app.cometchat.io/)
      * Go to Extension section 
      * Add Push Notification Extension
      * Add **FCM Server Key** in Push Notification Extension    
      
  - To send location message Add your google API Key in `google_maps_api.xml`     


## Run the Sample App



   To Run to sample App you have to do the following changes by Adding **ApiKey** and **AppId**

   - Open the Project in Android Mode in Android Studio

   - Go to Under java --> com\inscripts\cometchatpulse\demo --> Contracts --> StringContract

   - Under class `StringContract.class`  go to static `class` named `AppDetails`

   -  modify *APP_ID* and *API_KEY* with your own **ApiKey** and **AppId**

        `public static final String APP_ID  = "XXXXXXXXX";`

       ` public static final String API_KEY = "XXXXXXXXX";`
       
   - add `google-service.json` file for push notification
       

## Note




   You can Obtain your  *APP_ID* and *API_KEY* from [CometChat-Pulse Dashboard](https://app.cometchat.com/)

   For more information read [CometChat-Pulse Android SDK](https://prodocs.cometchat.com/docs/android-quick-start) Documentation




  ![Studio Guide](https://github.com/CometChat-Pulse/android-java-chat-app/blob/master/ScreenShots/guide.png)                                    


## Push Notification

The Push Notification extension allows you to send push notifications to mobile apps and desktop browsers. 
	
For more information read [Android-Push-Notification](https://prodocs.cometchat.com/docs/android-extensions-push-notification) Documentation.
	





## Screenshots

   <img align="left" width="185" height="331" src="https://github.com/CometChat-Pulse/android-java-chat-app/blob/master/ScreenShots/gif_1.gif">


   <img align="left" width="185" height="331" src="https://github.com/CometChat-Pulse/android-java-chat-app/blob/master/ScreenShots/gif_2.gif">


   <img align="left" width="185" height="331" src="https://github.com/CometChat-Pulse/android-java-chat-app/blob/CP-31/ScreenShots/gif_3.gif">



   <img align="left" width="185" height="331" src="https://github.com/CometChat-Pulse/android-java-chat-app/blob/CP-31/ScreenShots/gif_4.gif">

`            `<br></br><br></br><br></br><br></br><br></br><br></br><br></br><br></br></br>


## Contribute
   
   
   Feel free to make Pull Request. 
   
