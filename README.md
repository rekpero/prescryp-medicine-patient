# Prescryp Medicines Patient - Beta

Health App for buying medicines from nearby medicine retail within 1 hour by just uploading your prescription. You can also view information about Medicines like Manufacture Name, Medicine Composition, Primary Usage, Medium of Usage, MRP, Package Contain. You can also add Pill Reminders in the App. You can see your prescription details and order details. This app supports various languages.

This app is connected to vendor app and will give realtime updates about the order placed simmilar to Zomato, Swiggy. The order is placed to the nearest active vendor and wait for the approval of the vendor and notify when the approval is done or rejected. There is a valet app that is also connected to this app when the order is out for delivery which will give you an estimated time of arrival and the details to the valet. Also there a OTP generated for the valet so there no issue with the medicine security.

This app stores data to a MySQL database using a PHP REST API to communicate with the database in the BlueHost Server. The app is using Volley Library for communicate with the REST API and get a json response back from the server.

## Stack Used

**Frontend** : Android, Java, XML

**Backend** : PHP Rest API

**DB** : MySQL

**Tools** : Android Studio, Sublime, BlueHost Server

## Screenshots

<img src="https://raw.githubusercontent.com/mmitrasish/prescryp-patient/master/screenshots/screenshot4.webp" width="200" height="400"> &nbsp;&nbsp;&nbsp; <img src="https://raw.githubusercontent.com/mmitrasish/prescryp-patient/master/screenshots/screenshot1.webp" width="200" height="400"> &nbsp;&nbsp;&nbsp; <img src="https://raw.githubusercontent.com/mmitrasish/prescryp-patient/master/screenshots/screenshot6.webp" width="200" height="400"> &nbsp;&nbsp;&nbsp; <img src="https://raw.githubusercontent.com/mmitrasish/prescryp-patient/master/screenshots/screenshot2.webp" width="200" height="400"> <br/><br/><img src="https://raw.githubusercontent.com/mmitrasish/prescryp-patient/master/screenshots/screenshot3.webp" width="200" height="400"> &nbsp;&nbsp;&nbsp; <img src="https://raw.githubusercontent.com/mmitrasish/prescryp-patient/master/screenshots/screenshot5.webp" width="200" height="400"> &nbsp;&nbsp;&nbsp; <img src="https://raw.githubusercontent.com/mmitrasish/prescryp-patient/master/screenshots/screenshot7.webp" width="200" height="400">
&nbsp;&nbsp;&nbsp; <img src="https://raw.githubusercontent.com/mmitrasish/prescryp-patient/master/screenshots/screenshot8.webp" width="200" height="400">

## Features

This app is built on Android (Java) which has various features:

- Authentication is done with both email/password and phone number as well as Google authentication and facebook authentication.
- Store data with PHP REST API in MySQL DB.
- Built a Reminder with a Job Scheduler that will give notification for Pill Reminder.
- Has the abilty for changing Language to Hindi, Bengali.
- Can view your Past Orders.
- Has the ability to add Address and get the location on Map using Google GeoLocation and store your LatLng for ordering.
- Can Upload Image(Prescription) and view it.
- Has Autocomplete abilty for Location and use of GPS to get the current location.
- Has Searching ability to search Medicines from 64k Medicine List.
- Has Add to Cart abilty for ordering medicines.
- Has the ability to realtime notify about the Acceptance of Order from the chemist and Delivery updates form delivery valet using Notification and UI.

## Other Modules

This app is depended on other two App which takes care of chemist order management and valet delivery management which give the process a realtime flavour. The links to both the App Github are:

> ###### [Prescryp Medicine Chemist](https://github.com/mmitrasish/prescryp-medicine-chemist)
> ###### [Prescryp Medicine Valet](https://github.com/mmitrasish/prescryp-medicine-valet)

## Links

> ###### [Privacy Policy](https://prescryp-medicines-p.flycricket.io/privacy.html)
> ###### [Play Store](https://play.google.com/store/apps/details?id=com.prescywallet.presdigi)
