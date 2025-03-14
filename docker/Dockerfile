# Utilisation de l'image Scala et SBT officielle
FROM openjdk:11
ARG SBT_VERSION=1.9.9
ARG SPARK_VERSION=3.5.5
ARG HADOOP_VERSION=3

# Installation des dépendances système
RUN apt-get update && apt-get install -y curl unzip git gnupg ca-certificates wget && apt-get clean

# Télécharger la clé GPG depuis le dépôt
#RUN curl -fsSL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | gpg --dearmor -o /usr/share/keyrings/sbt-keyring.gpg
RUN    curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb  
RUN    echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" >> /etc/apt/sources.list.d/sbt.list  
RUN    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add - 

# Installation de SBT
RUN    apt-get update && apt-get install -y sbt=$SBT_VERSION 
RUN    rm sbt-$SBT_VERSION.deb 

# Installation de Spark
RUN curl -L -o spark.tgz https://dlcdn.apache.org/spark/spark-$SPARK_VERSION/spark-$SPARK_VERSION-bin-hadoop$HADOOP_VERSION.tgz
RUN tar -xvzf spark.tgz
RUN mv spark-$SPARK_VERSION-bin-hadoop$HADOOP_VERSION /opt/spark && \
    rm spark.tgz

# Variables d'environnement
ENV PATH="$PATH:/opt/spark/bin:/opt/spark/sbin"
ENV SPARK_HOME="/opt/spark"

# Définition du dossier de travail
WORKDIR /app

# Copie du projet Scala
COPY app/ /app/

RUN cd /app && sbt sbtVersion

# Définition du point d'entrée
CMD ["sbt", "clean", "compile", "run"]
