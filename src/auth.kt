package com.okta.demo.ktor

import com.okta.jwt.JwtVerifiers
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import java.time.Duration

@KtorExperimentalAPI
fun Application.setupAuth() {
    val oktaConfig = oktaConfigReader(ConfigFactory.load()?: throw Exception("Could not load config"))
    val accessTokenVerifier = JwtVerifiers.accessTokenVerifierBuilder()
        .setAudience(oktaConfig.audience)
        .setIssuer(oktaConfig.orgUrl)
        .setConnectionTimeout(Duration.ofSeconds(1))
        .build()
    val idVerifier = JwtVerifiers.idTokenVerifierBuilder()
        .setClientId(oktaConfig.clientId)
        .setIssuer(oktaConfig.orgUrl)
        .setConnectionTimeout(Duration.ofSeconds(1))
        .build()

    install(Authentication) {
        oauth {
            urlProvider = { "http://localhost:8080/login/authorization-callback" }
            providerLookup = { oktaConfig.asOAuth2Config() }
            client = HttpClient()
        }
    }

    routing {
        authenticate {
            // Okta calls this endpoint providing accessToken along with requested idToken
            get("/login/authorization-callback") {
                // Get a principal from OAuth2 token
                val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                    ?: throw Exception("No principal was given")

                // Parse and verify access token with Okta.JwtVerifier
                val accessToken = accessTokenVerifier.decode(principal.accessToken)

                // Get idTokenString, parse and verify id token
                val idTokenString = principal.extraParameters["id_token"]
                    ?: throw Exception("id_token wasn't returned")

                val idToken = idVerifier.decode(idTokenString, null)

                // try to get handle from the id token, of fail back to subject field in access token
                val fullName = (idToken.claims["name"]?: accessToken.claims["sub"]?: "UNKNOWN_NAME").toString()
                println("User $fullName logged in successfully")

                // Create a session object with "slugified" username
                val session = UserSession(
                    username = fullName.replace("[^a-zA-Z0-9]".toRegex(), ""),
                    idToken = idTokenString
                )

                call.sessions.set(session)
                call.respondRedirect("/")
            }

            //When guest accessing '/login' it automatically redirects to okta login page
            get("/login") {
                call.respondRedirect("/")
            }
        }

        // Perform logout by cleaning cookies
        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/")
        }
    }
}
