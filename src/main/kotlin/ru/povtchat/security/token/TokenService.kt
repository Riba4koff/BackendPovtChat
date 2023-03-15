package ru.povtchat.security.token

interface TokenService {
    fun generate(config: TokenConfig, vararg claims: TokenClaim): String
}