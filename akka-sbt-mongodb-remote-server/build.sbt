name          := "akka-sbt-mongodb-remote-server"
organization  := "reyes.akka"
version       := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(

  //  A K K A
  "com.typesafe.akka" % "akka-actor_2.11"   % "2.4.17",
  "com.typesafe.akka" % "akka-remote_2.11"  % "2.4.17",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.17",

  "org.mongodb"       % "mongo-java-driver" % "3.4.1",
  "junit"             % "junit"             % "4.12"  % "test",
  "com.novocode"      % "junit-interface"   % "0.11"  % "test",
  "org.slf4j"         % "slf4j-simple"      % "1.7.25"
)

mappings in (Compile, packageBin) ~= { _.filterNot { case (_, name) =>
  Seq("application.conf").contains(name)
}}
