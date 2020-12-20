package ca.allanwang.reddit.api.impl

import ca.allanwang.reddit.api.RedditConfig
import dagger.Binds
import dagger.Module
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditConfigImpl @Inject internal constructor() : RedditConfig {

    private val file = listOf("priv.properties", "../priv.properties")
        .map { File(it) }
        .first { it.isFile }

    private val props = Properties()

    init {
        FileInputStream(file).use { props.load(it) }
    }

    override val clientId: String = props.getProperty("client_id")!!
    override val clientSecret: String = props.getProperty("client_secret")!!
    override var accessToken: String?
        get() = props.getProperty("access_token")
        set(value) {
            props["access_token"] = value
        }
    override var expiration: Long?
        get() = props.getProperty("expiration")?.toLongOrNull()
        set(value) {
            props["expiration"] = value.toString()
        }

    override fun save() {
        FileOutputStream(file).use {
            props.store(it, null)
        }
    }
}

@Module
interface RedditConfigModule {
    @Binds
    fun to(config: RedditConfigImpl): RedditConfig
}