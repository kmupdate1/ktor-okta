# Kotlin + Ktor Example 

This example app demonstrates how to build a simple blogging service with Kotlin, Ktor, kotlinx.html, and Okta.

Please read [Create a Secure Ktor Application with Kotlin](https://developer.okta.com/blog/2020/10/19/ktor-kotlin) to see how it's built.

![Demo of the Nano Blogging Service](meta/nano-blogging-service-demo.gif)

**Prerequisites**

- [Java 11+](https://adoptopenjdk.net/) installed
- An [Okta Developer Account](https://developer.okta.com/)

> [Okta](https://developer.okta.com/) has Authentication and User Management APIs that reduce development time with instant-on, scalable user infrastructure. Okta's intuitive API and expert support make it easy for developers to authenticate, manage and secure users and roles in any application.

* [Getting Started](#getting-started)
* [Help](#help)
* [License](#license)

## Getting Started

To install this example application, run the following commands:

```bash
git clone https://github.com/oktadeveloper/okta-kotlin-ktor-example.git
cd okta-kotlin-ktor-example
```

This will get a copy of the project installed locally. To install all of its dependencies and start each app, follow the instructions below.

### Register an OIDC App in Okta

Login to the Okta developer console. On the top menu select **Applications** → **Add Application**.

Then, configure your Okta application. Don't worry, if you want to change anything it's always possible to return to this screen. At the very least, you need to set the following settings:
 
* **Name** - give it a meaningful name, for instance, `My Ktor nano Blogging Service`
* **Base URIs** - put `http://localhost:8080/` there. Multiple URI can be provided; you can add more URIs if needed.
* **Login redirect URIs** - set it to `http://localhost:8080/login/authorization-callback`. Upon successful login, the user will be redirected to URI provided with tokens in the query.
* **Logout redirect URIs** - value `http://localhost:8080` allows you to provide a redirect URL on successful logout.
 
Click **Done** to finish the initial setup.

Take note of the following three values.
 
* **Org URL**: Hover over **API** on the top menu bar, and select **Authorization Servers** menu item, copy the value from **Issuer URI**
 
* **Client ID** and **Client Secret** as below:

Create an `okta.env` file with the following code:
 
```shell
export OKTA_ORGURL=https://{yourOktaDomain}/oauth2/default
export OKTA_CLIENT_ID={yourClientId}
export OKTA_CLIENT_SECRET={yourClientSecret}
```
 
Next, run `source okta.env` before running your app. 
 
_If you're on Windows, name the file `okta.bat` and use `SET` instead of `export`._

Start your Ktor app:

```shell
./gradlew run 
```

Open your browser to `http://localhost:8080` and click **Login** from the top menu bar. You will see an Okta login screen. After you type your credentials you'll be redirected back to the app. Try to send some messages!

## Help

Visit our [Okta Developer Forums](https://devforum.okta.com/). You can also email [developers@okta.com](mailto:developers@okta.com) if would like to create a support ticket.

## License

Apache 2.0, see [LICENSE](LICENSE).
