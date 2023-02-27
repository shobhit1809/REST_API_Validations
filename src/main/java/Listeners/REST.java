package Listeners;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class REST {
    public static Properties setUP() throws IOException {

        File src=new File(System.getProperty("user.dir")+"\\src\\main\\resources\\JsonPath.properties");
        FileInputStream objfile=new FileInputStream(src);
        Properties obj=new Properties();
        obj.load(objfile);
        System.out.println("JsonPath.properties file loaded with path as:"+" "+System.getProperty("user.dir")+"\\\\src\\main\\resources\\JsonPath.properties");

        return obj;
    }

    public static Properties load() throws IOException {

        File source = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\Object_repo.properties");
        FileInputStream objectfile = new FileInputStream(source);
        Properties obj2 = new Properties();
        obj2.load(objectfile);
        System.out.println("Object_repo.properties file loaded with path as:" + " " + System.getProperty("user.dir") + "\\src\\main\\resources\\Object_repo.properties");

        return obj2;
    }
}
