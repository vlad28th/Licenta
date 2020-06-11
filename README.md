install mysql server & client
https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-20-04
			
			//mysql server
			
			sudo apt update
			sudo apt install mysql-server
			sudo mysql_secure_installation
			//create user named "root" with password "8629570396"
			sudo mysql
			SELECT user,authentication_string,plugin,host FROM mysql.user;
			ALTER USER 'root'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'password';
			FLUSH PRIVILEGES;
			//verify mysql service 
			 systemctl status mysql.service
			
			//mysql client
			sudo apt-get install mysql-client
			
			//import db from export.sql
			
			
			
install redis
https://redis.io/download

			 wget http://download.redis.io/releases/redis-6.0.5.tar.gz
 			 tar xzf redis-6.0.5.tar.gz
			 cd redis-6.0.5
 			make

			//start
			src/redis-server
			
			
install tomcat 
https://linuxconfig.org/ubuntu-20-04-tomcat-installation

			sudo apt install tomcat9 tomcat9-admin
			//verify installation
			ss -ltn
			//start tomcat server on reboot - YES
			sudo systemctl enable tomcat9
			//open 8080 port
			sudo ufw allow from any to any port 8080 proto tcp
			//acces localhost:8080
			
wen deploy from UI, max size for file is 50MB. if you want to increase that size, follow 

The cause of the issue is the max-file-size limit in WEB-INF/web.xml. It is set for 50MB while the war file is over 50MB.
Open the file below for tomcat9: sudo nano /usr/share/tomcat9-admin/manager/WEB-INF/web.xml
Locate multipart-config tag and turn 50MB to 500MB.


/var/lib/tomcat9/logs$  - location for tomcat logs (catalina.log)




