package utils
import com.typesafe.config.ConfigFactory

object config {
  private val config = ConfigFactory.load()

  final val DB_URL: String = sys.env.getOrElse("DB_URL", "jdbc:postgresql://localhost:5432/default_db")
  final val DB_USER: String = sys.env.getOrElse("DB_USER", "default_user")
  final val DB_PASSWORD: String = sys.env.getOrElse("DB_PASSWORD", "default_password")
  final val DB_CS: String = sys.env.getOrElse("DB_CS", "jdbc:postgresql://localhost:5432/default_db")

  final val GTFS_MTP_URL = "https://www.data.gouv.fr/fr/datasets/r/2ef043c8-3b10-4d87-af5f-65fead127407"
  final val GTFS_MTP_DEST = "data/MTP/"
  final val GTFS_MTP_FILENAME = "TAM_MMM_GTFS.zip"
}
