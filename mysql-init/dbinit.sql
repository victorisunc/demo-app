DROP DATABASE IF EXISTS demo;
CREATE DATABASE demo;
GRANT ALL PRIVILEGES ON demo.* TO 'username'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON demo.* TO 'username'@'localhost' IDENTIFIED BY 'password';