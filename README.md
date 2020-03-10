# Sage Business Cloud Java API Sample application (deprecated)

**Please note that this Sample Application is not updated anymore.** To get an overview of all current sample applications
for the Sage Accounting API, please visit https://developer.sage.com/api/accounting/guides/sample_apps/.

<details><summary>Application Setup</summary>
<p>

### *FOR changes required for v3* please refer to the [v3 branch](https://github.com/Sage/sageone_api_java_sample/tree/v3)

##### Note: Request signing and noncing (the X-Signature and X-Nonce headers) is no longer checked in v3. The related code will soon be removed from this repo.

Sample application that integrates with Sage Business Cloud Accounting via the Sage API.

Update the [SageoneConstants.java](src/org/sage_one_sample/sageone/SageoneConstants.java) file with your application's client_id, client_secret, signing_secret and callback_url.

Authentication with Sage is handled as follows:

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
</p>
</details>

## License

This sample application is available as open source under the terms of the
[MIT licence](LICENSE).

Copyright (c) 2020 Sage Group Plc. All rights reserved.
