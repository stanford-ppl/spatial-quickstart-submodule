val paradiseVersion = "2.1.0"

val common = Seq(
  scalaVersion := "2.12.7",
  addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full),
)
lazy val spatial = project in file("spatial/")
lazy val pirTest = project in file(".")
lazy val spatialApps = (project in file(".")).settings(
  common,
  scalaSource in Test := baseDirectory(_ / "src").value,
).dependsOn(pirTest % "compile->compile;test->test", spatial)

// For porting ML models
libraryDependencies += "org.jpmml" % "pmml-evaluator" % "1.4.8"
// For sparse LA verification
libraryDependencies += "org.scalanlp" %% "breeze" % "1.0"

