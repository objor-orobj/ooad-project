package cn.edu.xmu.goods.util;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;

public class SqlScript {
    private static String url = "jdbc:mysql://localhost:3306/oomall?serverTimezone=GMT%2B8";
    private static String username = "dbuser";
    private static String password = "123456";

    public static void run(String file) {
        try {
            Connection connection =
                    DriverManager.getConnection(url, username, password);
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setLogWriter(null);
            String path = SqlScript.class.getResource(file).getPath();
            Reader reader = new BufferedReader(new FileReader(path));
            runner.runScript(reader);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
