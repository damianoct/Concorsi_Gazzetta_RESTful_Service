# Concorsi Gazzetta RESTful Service

A RESTFul service developed with Spring.io that manages public contests of Gazzetta Ufficiale.

===

##Install and run

- ####Maven

 ```
 git clone https://github.com/damianoct/Concorsi_Gazzetta_RESTful_Service.git
 cd Concorsi_Gazzetta_RESTful_Service
 mvn package
 #run jar
 java -jar /target/gs-rest-service-0.1.0.jar
 ```

<img src="http://2.bp.blogspot.com/-7mObhiF1oQU/Vesm1knXbkI/AAAAAAAADzo/ka_mfLsOBDw/s1600/docker.png" width="200">
- ####Docker 

 You can build and run it on armv7 device with Docker!
 
 ```
 sudo docker run -p 8080:8080 damianodds/armhf-concorsi_restful`
 ```

 Feel free do modify the source and rebuild the docker images. 
 
 ```
 mvn package
 sudo docker build -f target/Dockerfile -t "yourrepository/yournameimage" .
 ```
 

===

##API Overview

- #####List available Gazzette.
 
  `$ curl -i -H "Accept: application/json" "yourserver.com:8080/gazzette"`
  
- #####List available Gazzette with summary constes.

  `$ curl -i -H "Accept: application/json" "yourserver.com:8080/gazzetteWithContests"`

- #####List available Gazzette with summary constes.

  `$ curl -i -H "Accept: application/json" "yourserver.com:8080/concorso?giorno=05&mese=07&anno=2016&codiceRedazionale=16E03156" `

