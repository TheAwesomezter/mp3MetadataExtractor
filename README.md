A Java Project made using the Tika Apache API, that would get the metadata from the MP3 files specified by the user and would insert it into a MYSQL database.

Instructions:
  1. Modify the Connection part of the code to link to your database
  2. Make sure these tables are created, or modify the table/column names in the .java file:
      user_accounts (username varchar(50) primary key);
      mp3files (title varchar(50), genre varchar(30), artist varchar(50), username varchar(50), mp3_id int primary key not null);
      genre (title varchar(50), genre varchar(30), artist varchar(50), username varchar(50), mp3_id int);
      artist (title varchar(50), artist varchar(50), username varchar(50), mp3_id int);
      
      A foreign key should be set on genre and artist's mp3_id column with mp3files as the parent table
      A foreign key should be set on mp3files's username column with user_accounts as the parent table
  3. Compile the project with both the tikaAPI.jar and mySQL.jar files added as libraries
