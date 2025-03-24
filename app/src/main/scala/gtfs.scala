
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import java.sql.{Connection, DriverManager, Statement}
import utils._

class GTFS(spark: SparkSession, path: String, city: String) {

  // Chemins vers les fichiers GTFS
  private val cityPath = s"$path$city/"
  private val connectionString = config.DB_CS

  // Chargement des fichiers GTFS en DataFrames
  val agency: DataFrame = loadCSV(cityPath, "agency.txt")
  val calendarDates: DataFrame = loadCSV(cityPath, "calendar_dates.txt")
  val calendar: DataFrame = loadCSV(cityPath, "calendar.txt")
  val routes: DataFrame = loadCSV(cityPath, "routes.txt")
  val stopTimes: DataFrame = loadCSV(cityPath, "stop_times.txt")
  val stops: DataFrame = loadCSV(cityPath, "stops.txt")
  val transfers: DataFrame = loadCSV(cityPath, "transfers.txt")
  val trips: DataFrame = loadCSV(cityPath, "trips.txt")

  // Méthode privée pour charger un fichier CSV en DataFrame
  private def loadCSV(cityPath: String, file: String): DataFrame = {

    var result = spark.read
      .option("header", "true") // Première ligne comme en-tête
      .option("inferSchema", "true") // Déduction automatique des types
      .csv(cityPath + file)
    if (file == "stop_times.txt") {
      result = result
        .withColumn("stop_sequence", col("stop_sequence").cast(IntegerType))
        .withColumn("pickup_type", col("pickup_type").cast(IntegerType))
        .withColumn("drop_off_type", col("drop_off_type").cast(IntegerType))
        .withColumn(
          "arrival_time",
          to_timestamp(col("arrival_time"), "HH:mm:ss")
        )
        .withColumn(
          "departure_time",
          to_timestamp(col("departure_time"), "HH:mm:ss")
        )
    }
    if (file == "calendar.txt") {
      result = result
        .withColumn("start_date", to_date(col("start_date"), "yyyyMMdd"))
        .withColumn("end_date", to_date(col("end_date"), "yyyyMMdd"))

    }
    if (file == "calendar_dates") {
      result = result
        .withColumn("date", to_date(col("date"), "yyyyMMdd"))
    }

    return result

  }

  def insertGTFS(dt: DataFrame): Unit = {
    dt.write
      .format("jdbc")
      .option("url", connectionString)
      .option("dbtable", "agency")
      .option("driver", "org.postgresql.Driver")
      .mode("append")
      .save()
  }

  // Exemple : Obtenir la liste des arrêts actifs (sans doublons)
  def getUniqueStops(): DataFrame = {
    stops.select("stop_id", "stop_name").distinct()
  }

  // Exemple : Joindre les trajets avec leurs routes
  def getTripsWithRoutes(): DataFrame = {
    trips.join(routes, "route_id")
  }

  // Exemple : Compter le nombre de trajets par route
  def countTripsByRoute(): DataFrame = {
    trips.groupBy("route_id").count()
  }
}
