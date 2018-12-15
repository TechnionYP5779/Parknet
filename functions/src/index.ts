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

  
  export const queryTest = functions.https.onCall((data, context) => {
    // Checking that the user is authenticated.
    if (!context.auth) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
        'while authenticated.');
    }

    const startDate = new Date(data.startDate)
    const endDate = new Date(data.endDate)
    const address = new String(data.address)

    const MILLISECS_TO_HOURS = 3600000

    const offersRef = admin.database().ref("offers/availabilty").once('value').then(function(snap) {
        var array = snap.val()
        var currDate1 = startDate;
        var currDate2 = new Date();
        var i = 0
        for(i = 0; i <= (endDate.getTime() - startDate.getTime())/MILLISECS_TO_HOURS; i++){
            currDate2.setTime(currDate1.getTime() + MILLISECS_TO_HOURS)
            var temp = {
                startTime: currDate1,
                endTime: currDate2,
                available: true
            }
        }
    })
    
    return offersRef.
  });
