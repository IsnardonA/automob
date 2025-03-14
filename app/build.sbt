name := "main"

version := "1.0"

scalaVersion := "2.12.15"  // Assurez-vous que la version de Scala correspond à celle que vous avez installée

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.4.1",
  "org.apache.spark" %% "spark-sql" % "3.4.1",
  "org.postgresql" % "postgresql" % "42.5.4"
)
logLevel := Level.Error
mainClass in Compile := Some("main")