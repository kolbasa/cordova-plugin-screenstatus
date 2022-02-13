Cordova ScreenStatus Plugin
=========================

Cordova plugin to monitor status changes of the screen.
Works even if the app is not in the foreground.

# Installation

### Cordova

    cordova plugin add cordova-plugin-screenstatus

### Ionic + Cordova

    ionic cordova plugin add cordova-plugin-screenstatus

### Capacitor

    npm install cordova-plugin-screenstatus

# API

### subscribe()

```js
ScreenStatus
    .subscribe(function(isScreenOn) {
        // true, false
    })
    .then(function () {
        // subscribed
    })
    .catch(function (err) {
        // something broke
    });
```

### unsubscribe()

```js
ScreenStatus
    .unsubscribe()
    .then(function () {
        // unsubscribed
    })
    .catch(function (err) {
        // something broke
    });
```