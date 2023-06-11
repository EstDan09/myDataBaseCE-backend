package myDataBaseCE.backend;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;


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

        User[] list;
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(exampleBoy);
            out.append(jsonString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        User test = gson.fromJson(new FileReader(path), User.class);
        System.out.println(test.getUserName());

    }
    @RequestMapping("/testThree")
    public void testing() {
        /*
        XMLStore xmlStore = new XMLStore();
        String[] jeje = {"Esteban","88888888","AAA"};
        xmlStore.countRows("LeoGod");
        xmlStore.insert("LeoGod",jeje);

         */
        String ay = "Hola"+"\n"+"pepe";
        System.out.println(ay);
        String[] siu = ay.split("\n");
        System.out.println(siu[0]);
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
