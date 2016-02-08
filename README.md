# Sage One Java API Sample application

Sample application that integrates with Sage One Accounting via the Sage One API.

Update the [SageoneConstants.java](src/org/sage_one_sample/sageone/SageoneConstants.java) file with your application's client_id, client_secret and signing_secret.

Authentication with Sage One is handled as follows:

* Auth redirect is handled in [SageoneAuth.java](src/org/sage_one_sample/sageone/SageoneAuth.java)
* Token exchange is handled in [SageoneToken.java](src/org/sage_one_sample/sageone/SageoneToken.java)

An example API call (including signing) can be seen in [SageoneData.java](src/org/sage_one_sample/sageone/SageoneData.java).
