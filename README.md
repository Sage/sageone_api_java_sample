# Sage One Java API Sample application

### *FOR changes required for v3* please refer to the [v3 branch](https://github.com/Sage/sageone_api_java_sample/tree/v3)
##### NOTE: v3 is currently under development. It is only available in Canada [Beta]

Sample application that integrates with Sage One Accounting via the Sage One API.

Make a copy from the file [application-sample.properties](/src/main/resources/application-sample.properties) to "application.properties" in the 
same directory (project directory) and update with your application's `client_id`, `client_secret`, `signing_secret` and `callback_url`.

Authentication with Sage One is handled as follows:

* Auth redirect is handled in [SageoneAuth.java](src/main/java/org/sage_one_sample/sageone/SageoneAuth.java)
* Token exchange is handled in [SageoneToken.java](src/main/java/org/sage_one_sample/sageone/SageoneToken.java)

An example API call (including signing) can be seen in [SageoneData.java](src/main/java/org/sage_one_sample/sageone/SageoneData.java).

## Run the app locally

Clone the repo:

`git clone git@github.com:Sage/sageone_api_java_sample.git`

and import the project into Eclipse.

Update your application.properties file with your application's `client_id`, `client_secret`, `signing_secret` and `callback_url`.

You can run your application in different ways:
* Main class [JettyRunner.java](src/test/java/org/sage_one_sample/sageone/JettyRunner.java)
* Eclipe / Tomcat plugins
* Maven: `mvn jetty:run`
* Gradle: `gradle appRun`



Access the [home page](http://localhost:8080/SageOneSampleApp), authorize and make an API call.
