dependencies {
    api(reddit.Dependencies.jraw)
    implementation(reddit.Dependencies.retrofit)
    implementation(reddit.Dependencies.retrofitConverter("moshi"))
    implementation(reddit.Dependencies.moshi)
    implementation(reddit.Dependencies.moshi("kotlin"))
    implementation(reddit.Dependencies.moshi("adapters"))
}
