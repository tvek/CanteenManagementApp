# CanteenManagementApp
A Canteen Management App

## Tested On
Tested on API 34, Pixel6

## Features
- Used Firebase to store restaurant data, Applied MVC pattern design style for client side & server side.
- Provided sign in/sign up function, menu and food list loading, orders management process, and intro sliders for new users.

## Database Structure
Import database JSON file from database/firebase-database-export.json

## Code Download & Build
1. Install Git-bash and Android Studio on your OS(Windows/Linux/MacOSX)
2. Open the folder in which you want to download the code (Let's name is PATH_FOOD_APP_ROOT)
3. Right click > "Open in Git-bash"
4. Download the code using "git clone https://github.com/tvek/CanteenManagementApp.git"
5. Wait for the cloning to complete
6. Open Android Studio
7. File > Open > select "<PATH_FOOD_APP_ROOT>/FoodManagementApp" from the window
8. Click on "Run" button to start the build

## Database Setup
1. Visit "https://console.firebase.google.com/"
2. "Add Project"
3. Add package name as that is mentioned in "FoodManagementApp/app/src/main/AndroidManifest.xml"
4. Click on "All Products" in Firebase and tap on "Realtime Database"
5. Setup the realtime database and import the JSON file (database/firebase-database-export.json)
6. Click > Project > App Name > Settings > Download the google-service.json
7. Place the downloaded google-service.json in "FoodManagementApp/app/" folder
8. Trigger the final build and test the application on the mobile

## Application Demo View
<img src="https://user-images.githubusercontent.com/31734493/67163069-4d548f80-f388-11e9-9f0f-c18d2ac90744.png" width="200" height="400" />
<br/>
More screenshots are present in demo_images folder

## Developer Note
<h5>As I am a android learner myself, all help & contribution is very well appreciated. If you find this idea or the underlying code useful, feel free to reuse it in your own projects.</h5>

