name := "common-cache"

version := "1.0"

scalaVersion := "2.11.8"

organization := "com.lawsofnature.common"

libraryDependencies += "net.codingwell" % "scala-guice_2.11" % "4.1.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
libraryDependencies += "com.trueaccord.scalapb" % "scalapb-runtime_2.11" % "0.5.46"
libraryDependencies += "com.lawsofnature.common" % "common-redis_2.11" % "1.0"
libraryDependencies += "com.jxjxgo.edcenter" % "edclient_2.11" % "1.0"
