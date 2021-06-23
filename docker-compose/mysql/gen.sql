GRANT all privileges on *.* to 'test@%' IDENTIFIED BY 'test';
flush privileges;
SET PASSWORD FOR 'root'@'localhost' = PASSWORD('root'); 
exit