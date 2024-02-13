# LA CARTE AU TRESOR

## Pour lancer le projet :

1) ### Directement depuis un IDE
   (**Intellij**, Eclipse, NetBeans, VS Code, ...)
2) ### Installer l'application
   (prérequis : **Maven 3+** et **Java 17** installer et path correctement configurer pour avoir accès aux éxécutable
   dans le terminal)

    - Package l'application
      `mvn clean package`

    - Lancer l'application
      `java -jar ${jarPath} ${inputFilePath} ${outputFilePath}`

    - exemple (sous Windows, dans le root directory, à partir du fichier .\input, génère le fichier .\output)
      `java -jar .\target\CarteAuTresor-1.0-SNAPSHOT.jar input output`

Le fichier input reprend l'exemple de la consigne, le fichier output a été généré par l'application