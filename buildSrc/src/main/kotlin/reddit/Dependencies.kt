package reddit

object Dependencies {
    val flogger = "com.google.flogger:flogger:${Versions.flogger}"
    fun flogger(type: String) =
        "com.google.flogger:flogger-${type}:${Versions.flogger}"

    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    fun retrofitConverter(type: String) =
        "com.squareup.retrofit2:converter-${type}:${Versions.retrofit}"

    val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    fun moshi(type: String) =
        "com.squareup.moshi:moshi-${type}:${Versions.moshi}"

    const val jraw = "net.dean.jraw:JRAW:${Versions.jraw}"

    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerKapt =
        "com.google.dagger:dagger-compiler:${Versions.dagger}"

    // https://github.com/Kotlin/kotlinx.coroutines/releases
    val coroutines = coroutines("core")
    fun coroutines(type: String) =
        "org.jetbrains.kotlinx:kotlinx-coroutines-${type}:${Versions.coroutines}"

    fun junit(type: String) =
        "org.junit.jupiter:junit-jupiter-${type}:${Versions.junit}"

    const val hamkrest = "com.natpryce:hamkrest:${Versions.hamkrest}"
}