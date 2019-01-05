 - Used dialogflow.com to convert the human conversations declaring the intents and entities that need to be listened in the conversation.
 - I have created a fulfillment engine(web hook) that responds back with an appropriate response for the requested input over voice.
 - Details of this fulfillment engine
    - The application is built on Java 1.8 and uses gradle for dependency management.
    - Used Spring boot framework for dependency injection and auto configuration of java spring resources.
    - Maintains Redis cache as a persistent data storage for the health check status by experience.
    - Multiple endpoints exposed to read, write, update and delete the health check data in redis cache.
    - One more endpoint that builds a response in a human readable format when the REST service is invoked for the input voice request.
 
 - Platform used to run this backend fulfillment engine : Public PWS - Pivotal Web Services. I used it as a PoC.
 - The fulfillment engine endpoint is integrated into dialogflow as a web hook that fulfills a request.
 
Below is the flow.
 
- User talks to dialogflow application interface that understands the human conversation and parses it into intents and entities.
- This request will be sent to this fulfillment web hook with the intents and entities parsed.
- The fulfillment engine looks at the request, talks to the backend redis data source and fetches the required information.
- The response will be framed in a way that dialogflow interface can understand to display and read out the part of the response that is intended for the user.
