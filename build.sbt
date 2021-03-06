import com.typesafe.startscript.StartScriptPlugin

seq(StartScriptPlugin.startScriptForClassesSettings: _*)

name := "citation-search"

organization := "net.tqft"

version := "0.2-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions += "-target:jvm-1.7"

resolvers ++= Seq(
	"Java.net Maven2 Repository" at "http://download.java.net/maven/2/",
	"Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases",
	"Scala Snapshots" at "http://scala-tools.org/repo-snapshots/",
        "twitter-repo" at "http://maven.twttr.com"
)

// Project dependencies
libraryDependencies ++= Seq(
        "com.twitter" %% "finagle-core" % "6.1.0",
        "com.twitter" %% "finagle-http" % "6.1.0",
        "com.github.theon" %% "scala-uri" % "0.3.4",
	"org.apache.commons" % "commons-lang3" % "3.2.1",
	"commons-io" % "commons-io" % "2.4",
	"com.google.guava" % "guava" % "16.0.1",
	"com.google.code.findbugs" % "jsr305" % "2.0.2",
	"io.argonaut" %% "argonaut" % "6.0.1",
        "mysql" % "mysql-connector-java" % "5.1.24",
        "com.typesafe.slick" %% "slick" % "2.0.0-RC1",
	"org.mapdb" % "mapdb" % "0.9.9"
)

// Test dependencies
libraryDependencies ++= Seq(
	"junit" % "junit" % "4.8" % "test",
	"org.scalatest" % "scalatest_2.10" % "2.0" % "test"
)

publishTo := Some(Resolver.sftp("tqft.net", "tqft.net", "tqft.net/releases") as ("scottmorrison", new java.io.File("/Users/scott/.ssh/id_rsa")))

