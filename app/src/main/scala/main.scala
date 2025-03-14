import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions.col
import java.io.{IOException, File, FileInputStream, FileOutputStream}
import java.util.zip.{ZipEntry, ZipInputStream}
import utils._

object main {

  def main(args: Array[String]): Unit = {

    val fileUrl =
      "https://www.data.gouv.fr/fr/datasets/r/2ef043c8-3b10-4d87-af5f-65fead127407"
    val destinationPath = "data/TAM_MMM_GTFS.zip"
    val outputPath = "data/"

    // Download the file
    download.downloadFile(fileUrl, destinationPath)
    Unzip.unzipGTFS(destinationPath, outputPath)
    println(s"Décompression terminée dans $outputPath")

    // Créer la SparkSession
    val spark = SparkSession
      .builder()
      .appName("Lire GTFS avec Spark")
      .master("local[*]") // Mode local
      .getOrCreate()

// Lire le fichier stops.txt
    val stopsDF: DataFrame = spark.read
      .option("header", "true") // Première ligne = noms de colonnes
      .option("inferSchema", "true") // Détecte les types des colonnes
      .csv("data/stops.txt")

// Lire le fichier routes.txt
    val routesDF: DataFrame = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data/routes.txt")
//
    val stopTimesDF =
      spark.read.option("header", "true").csv("data/stop_times.txt")
    val tripsDF = spark.read.option("header", "true").csv("data/trips.txt")

// Joindre stop_times et trips pour voir les horaires des voyages
    val joinedDF = stopTimesDF.join(tripsDF, "trip_id")

// Afficher quelques données
//stopsDF.show(5)
//routesDF.show(5)
    joinedDF.show(5)
    // Arrêt de la session Spark
    spark.stop()
  }
}
