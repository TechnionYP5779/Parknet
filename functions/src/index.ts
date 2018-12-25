import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
admin.initializeApp(functions.config().firebase);

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
  const calculateDistance = ((lat1:number,lat2:number,long1:number,long2:number) =>{
    const p = 0.017453292519943295    // Math.PI / 180
    const c = Math.cos
    const c1 = (c((lat1-lat2) * p) / 2)
    const c2 = (c(lat2 * p))
    const c3 = (c((lat1) * p))
    const temp = (long1- long2) * p
    const c4 = c(temp)
    const c5 = ((1 - c4) / 2)
    const a = 0.5 - c1 + c2 * c3* c5
    const dis = (12742 * Math.asin(Math.sqrt(a))) // 2 * R; R = 6371 km
    return dis
  });
  
  export const availableNearby = functions.https.onCall((data, context) => {
    // Checking that the user is authenticated.
    if (!context.auth) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
        'while authenticated.');
    }

    const lat = data.lat
    const long = data.long

    const res = []
    return admin.firestore().collection("offers").get().then((snap) => {
        snap.forEach((offer) => {
            const availability = offer.get("availability")
            const qLocation = offer.get("address")
            const id = offer.id
            if(calculateDistance(qLocation.latitude, lat, qLocation.longitude, long) < 10){
                let cnt = 0
                availability.forEach(item => {
                    if (item.available === true && cnt === 0){
                        res.push({id: id, data: offer.data()})
                        cnt = cnt + 1
                    }
                })
            }
        })
        return res
    }).catch(err => console.log("fail"))
  });

