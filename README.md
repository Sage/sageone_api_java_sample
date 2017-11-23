# Sage One Java API Sample application

Sample application that integrates with Sage One Accounting via the Sage One API.

##### Note: Request signing and noncing (the X-Signature and X-Nonce headers) is no longer checked in v3. The related code will soon be removed from this repo.

Update the [SageoneConstants.java](src/org/sage_one_sample/sageone/SageoneConstants.java) file with your application's client_id, client_secret, signing_secret and callback_url.

Authentication with Sage One is handled as follows:

* Auth redirect is handled in [SageoneAuth.java](src/org/sage_one_sample/sageone/SageoneAuth.java)
* Token exchange is handled in [SageoneToken.java](src/org/sage_one_sample/sageone/SageoneToken.java)

An example API call (including signing) can be seen in [SageoneData.java](src/org/sage_one_sample/sageone/SageoneData.java).

## Run the app locally

Clone the repo:

`git clone git@github.com:Sage/sageone_api_java_sample.git`

and import the project into Eclipse.

Update the [SageoneConstants.java](src/org/sage_one_sample/sageone/SageoneConstants.java) file with your application's `client_id`, `client_secret`, `signing_secret` and `callback_url`.

Run your application using Eclipse / Tomcat.

Access the [home page](http://localhost:8080/SageOneSampleApp), authorize and make an API call.
