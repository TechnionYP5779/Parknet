import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
export const helloWorld = functions.https.onCall((data, context) => {
    // Checking that the user is authenticated.
    if (!context.auth) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
        'while authenticated.');
    }
    return {
        text: "Hello World"
    }
  });

  const calculateDistance = ((lat1:number,lat2:number,long1:number,long2:number) =>{
    const p = 0.017453292519943295;    // Math.PI / 180
    const c = Math.cos;
    const a = 0.5 - c((lat1-lat2) * p) / 2 + c(lat2 * p) *c((lat1) * p) * (1 - c(((long1- long2) * p))) / 2;
    const dis = (12742 * Math.asin(Math.sqrt(a))); // 2 * R; R = 6371 km
    return dis;
  });
  
  export const queryTest = functions.https.onCall((data, context) => {
    // Checking that the user is authenticated.
    if (!context.auth) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
        'while authenticated.');
    }

    const lat = data.lat
    const long = data.long

    const MILLISECS_TO_HOURS = 3600000
    var refRes = {}

    const offersRef = admin.database().ref("offers/").once('value', snap => {
        var qLocation = snap.child("address").val()
        if(calculateDistance(qLocation.latitude, lat, qLocation.longtitude, long) < 10){
            refRes[snap.key] = snap.ref
        }
    })
    
    return refRes
  });

