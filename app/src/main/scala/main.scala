import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions.col
import java.io.{IOException, File, FileInputStream, FileOutputStream}
import java.util.zip.{ZipEntry, ZipInputStream}
import utils._

object main {

  def main(args: Array[String]): Unit = {


    // Download the file
    download.downloadFile(config.GTFS_MTP_URL, config.GTFS_MTP_DEST + config.GTFS_MTP_FILENAME)
    Unzip.unzipGTFS( config.GTFS_MTP_DEST + config.GTFS_MTP_FILENAME, config.GTFS_MTP_DEST)
    // println(s"Décompression terminée dans $outputPath")
    //database.executeOrdoScripts(sqlDirectory, connectionString)



    //database.initGTFS(outputPath, connectionString)

  }
}
