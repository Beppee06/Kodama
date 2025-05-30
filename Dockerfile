# Fase 1: Build dell'applicazione con Maven
# Usa un'immagine Maven con una versione JDK compatibile con il tuo progetto (21 nel pom.xml modificato)
FROM maven:3.9-eclipse-temurin-21 AS build
# Imposta la directory di lavoro all'interno del container
WORKDIR /app
# Copia prima il pom.xml per sfruttare la cache di Docker per le dipendenze
COPY pom.xml .
# Scarica le dipendenze (se pom.xml non cambia, questo strato verrà riutilizzato)
RUN mvn dependency:go-offline -B
# Copia il resto del codice sorgente
COPY src ./src
# Compila l'applicazione e crea il JAR eseguibile
RUN mvn package -DskipTests

# Fase 2: Creazione dell'immagine finale ottimizzata
# Usa un'immagine JRE (Java Runtime Environment) snella, compatibile con il tuo JDK
FROM eclipse-temurin:21-jre-jammy
# Imposta la directory di lavoro
WORKDIR /app
# Copia solo il JAR creato dalla fase di build precedente
# Assicurati che il nome del JAR corrisponda a quello generato da Maven (artifactId-version.jar)
# Potrebbe essere Kodama-1.0-SNAPSHOT.jar. Controlla nella tua cartella 'target'.
# Usiamo un nome generico 'app.jar' per semplicità.
COPY --from=build /app/target/Kodama-1.0-SNAPSHOT.jar app.jar
# Esponi la porta su cui l'applicazione Spring Boot ascolta (default 8080)
EXPOSE 8080
# Comando per eseguire l'applicazione quando il container parte
ENTRYPOINT ["java", "-jar", "app.jar"]