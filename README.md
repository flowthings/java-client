## flowthings.io Java Client

#### Dependencies
The library is dependent on the following:

* Google gson >= 2.3
* Apache Commons Codec >= 1.6
* Jetty Websocket Client >= 9.2.11
* Google Guava >= 15.0

#### Instructions
More information on the flowthings.io platform is available at:

[https://flowthings.io/docs](https://flowthings.io/docs)

Create a free flowthings.io account and note down your `Master Token`.

Here is an example of how to use the client:

```java

import static com.flowthings.client.api.Flowthings.*

// Credentials
Credentials credentials = new Credentials("<account>", "<token>");
RestApi api = new RestApi(credentials);

// Create 2 Flows
String inputPath = "/" + accountName + "/input";
String outputPath = "/" + accountName + "/alerts";
Flow inputFlow = new Flow.Builder().setPath(inputPath).get();
Flow outputFlow = new Flow.Builder().setPath(outputPath).get();

// Send command to the platform
inputFlow = api.send(flow().create(inputFlow));
outputFlow = api.send(flow().create(outputFlow));

// Create a Track
String js = 
    "function (input) {"
        + "var temperature = input.elems.temperature.value;"
        + "if (temperature > 80){"
            + "return {"
                + "elems : {" 
                    + "category : \"HIGH_TEMPERATURE\""
                + "}"
            + "};"
        + "}"
        + "return null;"
        + "}";
 
Track alertTrack = new Track.Builder()
	.setSource(inputPath)
	.setDestination(outputPath)
	.setJs(js)
	.get();

// Track create
api.send(track().create(alertTrack));

// Subscribe to Websockets to get instant pushes of alerts
WebsocketApi wsApi = new WebsocketApi(credentials);
wsApi.send(drop(outputFlow.getId()).subscribe(
    new SubscriptionCallback<Drop>() {
      public void onMessage(Drop t) {
        System.out.println("I got the alert: " + t);
      }
    }));

// Send some test Drops
Drop d1 = new Drop.Builder().addElem("temperature", 60).get();
Drop d2 = new Drop.Builder().addElem("temperature", 85).get();

// The response will arrive at some time in the future
Future<Drop> f1 = api.send(drop(inputFlow.getId()).create(d1));
Future<Drop> f2 = api.send(drop(inputFlow.getId()).create(d2));

Thread.sleep(500);
```
