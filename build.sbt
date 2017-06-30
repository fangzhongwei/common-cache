name := "common-cache"

version := "1.1"

scalaVersion := "2.12.2"

organization := "com.jxjxgo.common"

libraryDependencies += "net.codingwell" % "scala-guice_2.12" % "4.1.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
libraryDependencies += "com.trueaccord.scalapb" % "scalapb-runtime_2.12" % "0.6.0-pre5"
libraryDependencies += "com.jxjxgo.common" % "common-redis_2.12" % "1.1"
libraryDependencies += "com.jxjxgo.edcenter" % "edclient_2.12" % "1.0"