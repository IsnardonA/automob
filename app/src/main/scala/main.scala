import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions.col
import java.io.{IOException, File, FileInputStream, FileOutputStream}
import java.util.zip.{ZipEntry, ZipInputStream}
import utils._

object main {

  def main(args: Array[String]): Unit = {


    // Download the file
    download.downloadFile(config.GTFS_MTP_URL, config.GTFS_PATH + "MTP/" + config.GTFS_MTP_FILENAME)
    Unzip.unzipGTFS( config.GTFS_PATH + "MTP/" + config.GTFS_MTP_FILENAME, config.GTFS_PATH + "MTP/")
    // println(s"Décompression terminée dans $outputPath")
    //database.executeOrdoScripts(sqlDirectory, connectionString)
    val spark = SparkSession
      .builder()
      .appName("Init GTFS DB")
      .master("local[*]") // Mode local
      .getOrCreate()

    val datas = new GTFS(spark, config.GTFS_PATH, "MTP")

    datas.agency.show()


    //database.initGTFS(outputPath, connectionString)

  }
}
