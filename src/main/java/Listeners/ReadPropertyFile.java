package Listeners;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadPropertyFile {
    public static Properties fetch() throws IOException {

        File src=new File(System.getProperty("user.dir")+"\\src\\main\\resources\\TestData.properties");
        FileInputStream objfile=new FileInputStream(src);
        Properties obj=new Properties();
        obj.load(objfile);
        System.out.println("TestData.properties file loaded with path as:"+" "+System.getProperty("user.dir")+"\\src\\main\\resources\\TestData.properties");

        return obj;
    }
}
