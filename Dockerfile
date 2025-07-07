FROM tomcat:9.0-jdk17

# Varsayılan Tomcat ROOT dizinini temizle
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Maven ile build edilen WAR dosyasını Tomcat'e kopyala
COPY target/RenartProject.war /usr/local/tomcat/webapps/ROOT.war

# Tomcat 8080 portunu aç
EXPOSE 8080

# Tomcat'i başlat
CMD ["catalina.sh", "run"] 