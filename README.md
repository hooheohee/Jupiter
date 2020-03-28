## Jupiter
A job recommendation app for software developers that use user's geolocation and favorite jobs to recommend similar jobs  
Try it here: http://ec2-13-58-121-16.us-east-2.compute.amazonaws.com/jupiter

### Get Started Locally
1. Set up tomcat 9.0.31
2. Set up MySQL connection at `/src/main/java/db/MySQLDBUtil.java`
3. Run `/src/main/java/db/MySQLTableCreation.java` to create the tables  
4. Start tomcat server
5. Navigate to `https://localhost:8080/jupiter`  

### Tech Used
Tomcat  
MonkeyLearn keyword extractor API  
GitHub Job API  
AWS RDS MySQL  
AWS EC2  
