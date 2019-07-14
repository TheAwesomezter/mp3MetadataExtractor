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
        File dir = new File("/home/viresh/IdeaProjects/javaProject/");
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

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        List<String> mp3Files = getFilesAndFilter();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "root", "password");
            Statement s = con.createStatement();
        } catch (Exception e) {
            System.out.println("" + e);
        }

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
                System.out.println("Title: " + metadata.get("title"));
                String title = metadata.get("title");
                title = title.replace("'", "");
                System.out.println("Artist: " + metadata.get("xmpDM:artist"));
                String artist = metadata.get("xmpDM:artist");
                System.out.println("Genre: " + metadata.get("xmpDM:genre"));
                String genre = metadata.get("xmpDM:genre");
                System.out.println("Album: " + metadata.get("xmpDM:album"));
                String album = metadata.get("xmpDM:album");

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "root", "password");
                Statement s = con.createStatement();

                s.executeUpdate("insert into mp3files (title, genre, artist, username) values ('" + title + "', '" + genre + "', '" + artist + "','" + username + "');");
                s.executeUpdate("insert into mp3files (title, genre, artist, username) values (" + "'" + title + "'" + "," + "'" + genre + "'" + "," + "'" + artist + "'" + "," + "'" + username + "'" + ");");
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
}
