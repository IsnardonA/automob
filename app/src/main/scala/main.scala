import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions.col
import java.io.{IOException, File, FileInputStream, FileOutputStream}
import java.util.zip.{ZipEntry, ZipInputStream}
import utils._

object main {

  def main(args: Array[String]): Unit = {

    val connectionString =
      "jdbc:postgresql://localhost:5432/automob?user=admin&password=password"
    val postgresDriver = "org.postgresql.Driver"
    val fileUrl =
      "https://www.data.gouv.fr/fr/datasets/r/2ef043c8-3b10-4d87-af5f-65fead127407"
    val destinationPath = "data/TAM_MMM_GTFS.zip"
    val outputPath = "data/"
    val sqlDirectory = "scripts/init/"

    // Download the file
    // download.downloadFile(fileUrl, destinationPath)
    // Unzip.unzipGTFS(destinationPath, outputPath)
    // println(s"Décompression terminée dans $outputPath")
    //database.executeOrdoScripts(sqlDirectory, connectionString)

    database.initGTFS(outputPath, connectionString)

  }
}
