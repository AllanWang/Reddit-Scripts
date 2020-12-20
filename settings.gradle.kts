rootProject.name = "Reddit Scripts"

include("buildSrc")
findProject(":buildSrc")?.name = "gradle-plugin"

include("api")
include("giveaway")
