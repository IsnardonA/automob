name := "main"

version := "1.0"

scalaVersion := "2.12.15"

addSbtPlugin("nl.gn0s1s" % "sbt-dotenv" % "3.1.1")

enablePlugins(FlywayPlugin)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.4.1",
  "org.apache.spark" %% "spark-sql" % "3.4.1",
  "org.postgresql" % "postgresql" % "42.5.4",
  "org.flywaydb" % "flyway-core" % "9.22.3",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.2",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.14.2",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.14.2",
  "com.typesafe" % "config" % "1.4.2"
)
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.2"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.14.2"
dependencyOverrides += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.14.2"

//logLevel := Level.Error
mainClass in Compile := Some("main")

flywayUrl := sys.env.getOrElse("DB_URL", "jdbc:postgresql://localhost:5432/default_db")
flywayUser :=  sys.env.getOrElse("DB_USER", "default_user")
flywayPassword := sys.env.getOrElse("DB_PASSWORD", "default_password")
flywayLocations := Seq("filesystem:src/main/resources/db/migration")
flywayBaselineOnMigrate := true 