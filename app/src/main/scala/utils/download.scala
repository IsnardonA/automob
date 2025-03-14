package utils

import java.io._
import java.net._
import java.nio.file.{Files, Paths}
import java.util.zip._

object download {

  def downloadFile(url: String, destinationPath: String): Unit = {
    // Étape 1: Créer une connexion à l'URL pour télécharger le fichier
    val connection = new URL(url).openConnection()

    // Étape 2: Ajouter un en-tête 'User-Agent' pour éviter que certains serveurs bloquent la connexion
    // Ce header est souvent nécessaire pour simuler un navigateur et accéder à des fichiers via HTTP.
    connection.setRequestProperty("User-Agent", "Mozilla/5.0")

    // Étape 3: Ouvrir un flux d'entrée pour lire les données du fichier à partir de l'URL
    val inputStream = connection.getInputStream

    // Étape 4: Créer un flux de sortie pour écrire les données dans le fichier de destination
    val outputStream = new FileOutputStream(destinationPath)

    // Étape 5: Créer un tableau de bytes (buffer) pour lire les données en morceaux
    // Le buffer doit avoir une taille spécifiée. Par exemple, 1024 octets.
    val buffer = new Array[Byte](1024) // Taill 1024 octets (1 Ko).
    var bytesRead =
      0 // Variable qui stocke le nombre d'octets lus à chaque itération.

    // Étape 6: Lire les données depuis l'inputStream et les écrire dans le fichier de destination
    // Tant qu'il reste des données à lire (c'est-à-dire tant que bytesRead != -1),
    // la boucle continue à lire et écrire.
    while ({ bytesRead = inputStream.read(buffer); bytesRead != -1 }) {
      outputStream.write(
        buffer,
        0,
        bytesRead
      ) // Écrire les données lues dans le fichier
    }

    // Étape 7: Fermer les flux une fois le téléchargement terminé
    inputStream.close() // Fermer le flux d'entrée pour libérer les ressources
    outputStream
      .close() // Fermer le flux de sortie pour finaliser l'écriture du fichier

    // Étape 8: Afficher un message indiquant que le fichier a été téléchargé avec succès
    println(s"File downloaded successfully to: $destinationPath")
  }
}
