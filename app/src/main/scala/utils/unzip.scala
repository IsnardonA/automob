package utils

import java.io.{IOException, FileOutputStream, FileInputStream, File}
import java.util.zip.{ZipEntry, ZipInputStream}

object Unzip extends App {
  def unZipIt(zipFile: String, outputFolder: String): Unit = {

    val buffer = new Array[Byte](1024)

    try {

      // output directory
      val folder = new File(outputFolder);
      if (!folder.exists()) {
        folder.mkdir();
      }

      // zip file content
      val zis: ZipInputStream =
        new ZipInputStream(new FileInputStream(zipFile));
      // get the zipped file list entry
      var ze: ZipEntry = zis.getNextEntry();

      while (ze != null) {

        val fileName = ze.getName();
        val newFile = new File(outputFolder + File.separator + fileName);

        System.out.println("file unzip : " + newFile.getAbsoluteFile());

        // create folders
        new File(newFile.getParent()).mkdirs();

        val fos = new FileOutputStream(newFile);

        var len: Int = zis.read(buffer);

        while (len > 0) {

          fos.write(buffer, 0, len)
          len = zis.read(buffer)
        }

        fos.close()
        ze = zis.getNextEntry()
      }

      zis.closeEntry()
      zis.close()

    } catch {
      case e: IOException => println("exception caught: " + e.getMessage)
    }

  }

  def unzipGTFS(zipFilePath: String, outputFolder: String): Unit = {

    val buffer = new Array[Byte](1024) // Correifier la taille

    val zipStream = new ZipInputStream(new FileInputStream(zipFilePath))
    try {
      Stream.continually(zipStream.getNextEntry).takeWhile(_ != null).foreach {
        entry =>
          val newFile = new File(outputFolder, entry.getName)
          new File(newFile.getParent).mkdirs()

          val fos = new FileOutputStream(newFile)
          try {
            Stream
              .continually(zipStream.read(buffer))
              .takeWhile(_ != -1)
              .foreach(fos.write(buffer, 0, _))
          } finally {
            fos.close() // Fermeture propre du fichier extrait
          }
      }
    } finally {
      zipStream.close() // Fermeture du ZIP après traitement
    }
  }
}
