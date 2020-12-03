package com.okta.demo.ktor

import com.typesafe.config.Config
import io.ktor.auth.*
import io.ktor.config.*
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.util.*

data class OktaConfig (
    val orgUrl: String,
    val clientId: String,
    val clientSecret: String,
    val audience: String
) {
    val accessTokenUrl = "$orgUrl/vl/token"
    val authorizeUrl = "$orgUrl/vl/authorize"
    val logoutUrl = "$orgUrl/vl/logout"
}

@KtorExperimentalAPI
fun oktaConfigReader(config: Config): OktaConfig = OktaConfig(
    orgUrl = config.getString("okta.orgUrl"),
    clientId = config.getString("okta.clientId"),
    clientSecret = config.getString("okta.clientSecret"),
    audience = config.tryGetString("okta.audience")?: "api://default"
)

fun OktaConfig.asOAuth2Config(): OAuthServerSettings.OAuth2ServerSettings =
    OAuthServerSettings.OAuth2ServerSettings(
        name = "okta",
        authorizeUrl = authorizeUrl,
        accessTokenUrl = accessTokenUrl,
        clientId = clientId,
        clientSecret = clientSecret,
        defaultScopes = listOf("openid", "profile"),
        requestMethod = Post
    )
