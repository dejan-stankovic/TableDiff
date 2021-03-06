import sbt._

organization := "org.suecarter"

name := "tablediff"

crossScalaVersions := Seq("2.12.1", "2.11.8", "2.10.6")

version := "1.0.2"

//resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-lang3"   % "3.1",
  "org.scalatest"     %% "scalatest"       % {if (scalaVersion.value.startsWith("2.9.")) "2.0.M5b" else "3.0.1"} % "test",
  "com.novocode"       % "junit-interface" % "0.11" % Test
) ++ {if (scalaVersion.value.startsWith("2.11.") || scalaVersion.value.startsWith("2.12.")) {
  Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.5")
} else Nil}

// Want to keep testing the SampleApp and this ups the Java integration coverage
unmanagedSourceDirectories in Test += baseDirectory.value / "SampleApp/src/test/java"

scalacOptions in Compile ++= Seq(
  "-encoding", "UTF-8", "-target:jvm-1.6", "-feature", "-deprecation",
  //"-Xfatal-warnings", // @unchecked seems to be ignored
  "-language:postfixOps", "-language:implicitConversions"
)

javacOptions in (Compile, compile) ++= Seq (
  "-source", "1.6", "-target", "1.6"//, "-Xlint:all", "-Werror"
)

javacOptions in doc ++= Seq("-source", "1.6")

maxErrors := 1

fork := true

javaOptions ++= Seq("-XX:MaxPermSize=256m", "-Xmx2g", "-XX:+UseConcMarkSweepGC")

coverageMinimum := 90
coverageFailOnMinimum := true
coverageHighlighting := false

licenses := Seq(
  "MIT" -> url("http://opensource.org/licenses/MIT")
)

homepage := Some(url("http://github.com/smootoo/TableDiff"))

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.contains("SNAP")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else                    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

credentials += Credentials(
  "Sonatype Nexus Repository Manager", "oss.sonatype.org",
  sys.env.get("SONATYPE_USERNAME").getOrElse(""),
  sys.env.get("SONATYPE_PASSWORD").getOrElse("")
)

pomExtra :=
<scm>
  <url>git@github.com:smootoo/TableDiff.git</url>
  <connection>scm:git:git@github.com:smootoo/TableDiff.git</connection>
</scm>
<developers>
   <developer>
      <id>smootoo</id>
      <name>Sue Carter</name>
   </developer>
</developers>
