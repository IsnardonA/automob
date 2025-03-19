package utils
import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import java.sql.{Connection, DriverManager, Statement}
import java.io.File
import java.nio.file.{Files, Paths}

object database {

// Paramètres PostgreSQL
  // val connectionString = "jdbc:postgresql://localhost:5432/automob?user=admin&password=password"

  def executeOrdoScripts(
      sqlDirectory: String,
      connectionString: String
  ): Unit = {

    try {
      // Lister les fichiers SQL triés alphabétiquement
      val sqlFiles = new File(sqlDirectory).listFiles
        .filter(
          _.getName.endsWith(".sql")
        ) // Filtrer uniquement les fichiers .sql
        .sorted // Trier par nom (ordre alphabétique)

      // Exécuter chaque fichier SQL
      sqlFiles.foreach { file =>
        println(s" Exécution de : ${file.getName}")
        val sql =
          new String(Files.readAllBytes(Paths.get(file.getAbsolutePath)))
        executeSQL(connectionString, sql)
      }
      println("Tous les scripts SQL ont été exécutés avec succès !")

    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  // Fonction pour exécuter un script SQL
  def executeSQL(connectionString: String, sql: String): Unit = {
    val connection = DriverManager.getConnection(connectionString)
    val statement: Statement = connection.createStatement()
    try {
      statement.execute(sql)
    } finally {
      statement.close()
      connection.close()
    }
  }

  def initGTFS(gtfsPath: String, connectionString: String): Unit = {

    val sqlDirectory = "scripts/init/"

    executeOrdoScripts(sqlDirectory, connectionString)

    // Créer la SparkSession
    val spark = SparkSession
      .builder()
      .appName("Init GTFS DB")
      .master("local[*]") // Mode local
      .getOrCreate()

    val agencyDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(gtfsPath + "agency.txt")

    agencyDF.write
      .format("jdbc")
      .option("url", connectionString)
      .option("dbtable", "agency")
      .option("driver", "org.postgresql.Driver")
      .mode("append") // Remplace la table si elle existe
      .save()

    val stopsDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(gtfsPath + "stops.txt")

    stopsDF.write
      .format("jdbc")
      .option("url", connectionString)
      .option("dbtable", "stops")
      .option("driver", "org.postgresql.Driver")
      .mode("append") // Remplace la table si elle existe
      .save()

    val routesDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(gtfsPath + "routes.txt")

    routesDF.write
      .format("jdbc")
      .option("url", connectionString)
      .option("dbtable", "routes")
      .option("driver", "org.postgresql.Driver")
      .mode("append") // Remplace la table si elle existe
      .save()

    val tripsDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(gtfsPath + "trips.txt")

    tripsDF.write
      .format("jdbc")
      .option("url", connectionString)
      .option("dbtable", "trips")
      .option("driver", "org.postgresql.Driver")
      .mode("append") // Remplace la table si elle existe
      .save()

    val stop_timesDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(gtfsPath + "stop_times.txt")

    // CAST des colonnes
    val stopTimesCastedDF = stop_timesDF
      .withColumn("stop_sequence", col("stop_sequence").cast(IntegerType))
      .withColumn("pickup_type", col("pickup_type").cast(IntegerType))
      .withColumn("drop_off_type", col("drop_off_type").cast(IntegerType))
      .withColumn("arrival_time", to_timestamp(col("arrival_time"), "HH:mm:ss"))
      .withColumn(
        "departure_time",
        to_timestamp(col("departure_time"), "HH:mm:ss")
      )

    stopTimesCastedDF.write
      .format("jdbc")
      .option("url", connectionString)
      .option("dbtable", "stop_times")
      .option("driver", "org.postgresql.Driver")
      .mode("append") // Remplace la table si elle existe
      .save()

    val calendarDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(gtfsPath + "calendar.txt")

    val calendarCastedDF = calendarDF
      .withColumn("start_date", to_date(col("start_date"), "yyyyMMdd"))
      .withColumn("end_date", to_date(col("end_date"), "yyyyMMdd"))

    calendarCastedDF.write
      .format("jdbc")
      .option("url", connectionString)
      .option("dbtable", "calendar")
      .option("driver", "org.postgresql.Driver")
      .mode("append") // Remplace la table si elle existe
      .save()

    val calendar_datesDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(gtfsPath + "calendar_dates.txt")

    val calendar_dates_castedDF = calendar_datesDF
    .withColumn("date", to_date(col("date"), "yyyyMMdd"))

    calendar_dates_castedDF.write
      .format("jdbc")
      .option("url", connectionString)
      .option("dbtable", "calendar_dates")
      .option("driver", "org.postgresql.Driver")
      .mode("append") // Remplace la table si elle existe
      .save()

    val transfersDF = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(gtfsPath + "transfers.txt")

    transfersDF.write
      .format("jdbc")
      .option("url", connectionString)
      .option("dbtable", "transfers")
      .option("driver", "org.postgresql.Driver")
      .mode("append") // Remplace la table si elle existe
      .save()

    // Arrêt de la session Spark
    spark.stop()

  }

}
