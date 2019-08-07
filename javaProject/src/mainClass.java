import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.util.*;
import java.sql.*;

public class mainClass {

    public static List<String> getFilesAndFilter() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the file path: ");
        String filePath = sc.nextLine();
        File dir = new File(filePath);
        boolean pathExists = dir.exists();
        if(!pathExists) {
            System.out.println("Path doesn't exist!");
            System.exit(0);
        }
        String files[] = dir.list();

        List<String> MP3Files = new ArrayList<String>();

        if (files == null) {
            System.out.println("No files exist");
        } else {
            for (int i = 0; i < files.length; i++) {
                if (files[i].contains(".mp3")) {
                    MP3Files.add(files[i]);
                    System.out.println("" + files[i]);
                }
            }
        }
        return MP3Files;
    }

    public static void insertValues(List<String> mp3Files, String username) {
        Scanner sc = new Scanner(System.in);

        for (String i : mp3Files) {
            // System.out.println(i);
            String specifiedFile = i;

            try {
                InputStream input = new FileInputStream(new File(specifiedFile));
                ContentHandler handler = new DefaultHandler();
                Metadata metadata = new Metadata();
                Parser parser = new Mp3Parser();
                ParseContext parseCtx = new ParseContext();

                parser.parse(input, handler, metadata, parseCtx);
                input.close();

                String metadataNames[] = metadata.names();

                System.out.println("---------------------------------------------------");
                String title = metadata.get("title");
                try {
                    title = title.replace("'", "");
                } catch (Exception e){
                    continue;
                }
                System.out.println("Title: " + metadata.get("title"));
                System.out.println("Artist: " + metadata.get("xmpDM:artist"));
                String artist = metadata.get("xmpDM:artist");
                System.out.println("Genre: " + metadata.get("xmpDM:genre"));
                String genre = metadata.get("xmpDM:genre");

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "root", "password");
                Statement s = con.createStatement();

                s.executeUpdate("insert into mp3files (title, genre, artist, username) values ('" + title + "', '" + genre + "', '" + artist + "','" + username + "');");
                s.executeUpdate("insert into genre (title, genre, artist, username) values (" + "'" + title + "'" + "," + "'" + genre + "'" + "," + "'" + artist + "'" + "," + "'" + username + "'" + ");");
                s.executeUpdate("insert into artist (title, artist, username) values (" + "'" + title + "'" + "," + "'" + artist + "'" + "," + "'" + username + "'" + ");");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (TikaException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("" + e);
            }
        }
    }

    public static void view() {
        Scanner sc = new Scanner(System.in);

        System.out.println("What do you want to view?: ");
        System.out.println("1. MP3Files");
        System.out.println("2. Genre");
        System.out.println("3. Artist");
        System.out.println("4. None. I want to exit out");
        int choice = sc.nextInt();

        switch(choice) {
            case 1:
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "root", "password");
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery("select * from mp3files;");
                    while (rs.next()) {
                        String title = rs.getString("title");
                        String genre = rs.getString("genre");
                        String artist = rs.getString("artist");
                        String username = rs.getString("username");
                        String mp3id = rs.getString("mp3_id");

                        System.out.println("Title is: " + title);
                        System.out.println("Genre is: " + genre);
                        System.out.println("Artist is: " + artist);
                        System.out.println("Username is: " + username);
                        System.out.println("MP3ID is: " + mp3id);
                    }
                } catch (Exception e) {
                    System.out.println("" + e);
                }
            break;
            case 2:
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "root", "password");
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery("select * from genre;");
                    while (rs.next()) {
                        String title = rs.getString("title");
                        String genre = rs.getString("genre");
                        String artist = rs.getString("artist");
                        String username = rs.getString("username");
                        String mp3id = rs.getString("mp3_id");

                        System.out.println("Title is: " + title);
                        System.out.println("Genre is: " + genre);
                        System.out.println("Artist is: " + artist);
                        System.out.println("Username is: " + username);
                        System.out.println("MP3ID is: " + mp3id);
                    }
                } catch (Exception e) {
                    System.out.println("" + e);
                }
            break;
            case 3:
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "root", "password");
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery("select * from artist;");
                    while (rs.next()) {
                        String title = rs.getString("title");
                        String artist = rs.getString("artist");
                        String username = rs.getString("username");
                        String mp3id = rs.getString("mp3_id");

                        System.out.println("Title is: " + title);
                        System.out.println("Artist is: " + artist);
                        System.out.println("Username is: " + username);
                        System.out.println("MP3ID is: " + mp3id);
                    }
                } catch (Exception e) {
                    System.out.println("" + e);
                }
            break;
            default: System.out.println("Enter correct option");
        }
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        List<String> mp3Files = getFilesAndFilter();

        System.out.println("Have you already used our application?: ");
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.println("Enter the option: ");
        int choice = sc.nextInt();

        sc.nextLine();
        System.out.println("Enter your username: ");
        String username = sc.nextLine();

        if (choice == 2) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "root", "password");
                Statement s = con.createStatement();
                s.executeUpdate("insert into user_accounts values (" + "'" + username + "'" + ");");
            } catch (Exception e) {
                System.out.println("Sorry! This username already exists. Please enter another!");
                System.out.println("" + e);
                System.exit(0);
            }
        }

        while(true) {
            System.out.println("1. Insert");
            System.out.println("2. View");
            System.out.println("3. Exit");
            choice = sc.nextInt();

            switch(choice) {
                case 1: insertValues(mp3Files, username);
                break;
                case 2: view();
                break;
                case 3: System.exit(0);
                break;
                default: System.out.println("Enter correct option");
            }
        }
    }
}
