package myDataBaseCE.backend;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Objects;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;




@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FirstController {

    @RequestMapping("/testOne")
    public User testOne() {
        User example = new User();
        example.setUserName("Juan");
        example.setEmail("juansexy@gmail.com");
        example.setPassword("pipiribao");
        return example;
    }

    @PostMapping("/testTwo")
    public void createUser(@RequestBody User exampleBoy) throws IOException {
        System.out.println(exampleBoy.getPassword());

        String path = "C:\\Users\\eseca\\Desktop\\Code\\Angular\\proyecto3datos2\\backend\\src\\main\\java\\myDataBaseCE\\files\\test.json";

        List<User> userList = new ArrayList<>();

        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            userList.add(exampleBoy);

            Gson gson = new Gson();
            gson.toJson(userList, out);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
//        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
//        List<User> userListTest = new Gson().fromJson(new FileReader(path), listType);
//        System.out.println(userListTest.get(0).getUserName());
    }




    @RequestMapping("/get-tables")
    public String[] getTables() {
        String[] wordsList = {"hay", "pene"};
        return wordsList;
    }

    @PostMapping("/sendCommit")
    public void sendCommit(@RequestBody String arroz){
        System.out.println("arroz");
    }
    @RequestMapping("/testThree")
    public ArrayList<ArrayList<String>>testing() {
        /*
        String[] jeje = {"Esteban","88888888","AAA"};
        xmlStore.countRows("LeoGod");
        xmlStore.insert("LeoGod",jeje);
        String ay = "Hola"+"\n"+"pepe";
        System.out.println(ay);
        String[] siu = ay.split("\n");
        System.out.println(siu[0]);
         */
        XMLStore xmlStore = new XMLStore();
        return xmlStore.sendTable("LeoGod");

    }
    @RequestMapping("/strip")
    public void Strip (String strip) {
        XMLStore xmlStore = new XMLStore();
        String[] functions = strip.split("\n");
        String[] jeje = {"HOla"};
        for (int i = 0; i < functions.length; i++) {
            String[] function = functions[i].split("/");
            if (Objects.equals(function[0], "CreateTable")) {
                String[] atributos = function[2].split(",");
                xmlStore.createXML(function[1], atributos);
            } else if (Objects.equals(function[0], "Insert")) {
                String[] values = function[2].split(",");
                xmlStore.insert(function[1], values);
            } else if (Objects.equals(function[0], "Update")) {
                String[] newAttributes = function[4].split(",");
                String[] newRows = function[5].split(",");
                xmlStore.update(function[1], function[2], function[3], newAttributes, newRows);
            } else if (Objects.equals(function[0], "InnerJoin")) {
                String[] newRows = function[5].split(",");
                xmlStore.innerJoin(function[1], function[2], function[3], function[4], newRows, jeje);
            } else if (Objects.equals(function[0], "Select")) {
                String[] newAttributes = function[2].split(",");
                xmlStore.select(function[1], newAttributes, function[3], function[4], jeje);
            }
        }
    }

}
