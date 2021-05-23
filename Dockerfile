FROM tomcat:9.0

COPY /target/*.war /usr/local/tomcat/webapps
COPY ROOT.xml /usr/local/tomcat/conf/Catalina/localhost 

# make the app war the root war so all default requests are directed to it
# RUN mv /usr/local/tomcat/webapps/struts-demo-0.0.1.war /usr/local/tomcat/webapps/ROOT.war


# run tomcat
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
