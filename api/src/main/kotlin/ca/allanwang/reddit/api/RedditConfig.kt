package ca.allanwang.reddit.api

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

class RedditConfig @Inject internal constructor() {

    private val file = listOf("priv.properties", "../priv.properties")
        .map { File(it) }
        .first { it.isFile }

    private val props = Properties()

    init {
        FileInputStream(file).use { props.load(it) }
    }

    val clientId: String = props.getProperty("client_id")!!
    val clientSecret: String = props.getProperty("client_secret")!!
    val submission: String? = props.getProperty("submission")

    var uuid: UUID?
        get() = props.getProperty("uuid")?.let { UUID.fromString(it) }
        set(value) {
            props["uuid"] = value.toString()
        }

    fun save() {
        FileOutputStream(file).use {
            props.store(it, null)
        }
    }
}
