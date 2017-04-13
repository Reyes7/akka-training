name := """akka-sbt-mongodb-remote-client"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "reyes.akka"              %% "akka-sbt-mongodb-remote-server" % "1.0.0-SNAPSHOT",
  "org.scala-lang.modules"  % "scala-java8-compat_2.11"         % "0.8.0",
  "junit"                   % "junit"                           % "4.12"  % "test",
  "com.novocode"            % "junit-interface"                 % "0.11"  % "test"
)