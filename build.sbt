name := """exo-recapitulatif"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  evolutions
)


libraryDependencies += "mysql" % "mysql-connector-java" % "6.0.5"

libraryDependencies += "org.springframework" % "spring-jdbc" % "4.3.6.RELEASE"
