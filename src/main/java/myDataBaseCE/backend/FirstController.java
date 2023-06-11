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

    @RequestMapping("/get-user")
    public User testOne() {
        User example = new User();
        example.setUserName("Juan");
        example.setEmail("juansexy@gmail.com");
        example.setPassword("pipiribao");
        return example;
    }

    @PostMapping("/create-user")
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
    public List<String> getTables() {
        XMLStore xmlStore = new XMLStore();
        return xmlStore.findFoldersInDirectory();
    }

    @PostMapping("/sendCommit")

    public void sendCommit(@RequestBody String arroz){
        System.out.println(arroz);
    }

    @GetMapping("/get_xml_Data")
    public ArrayList<ArrayList<String>>testing(@RequestParam String xmlName) {
        XMLStore xmlStore = new XMLStore();
        return xmlStore.sendTable(xmlName);

    }

    @PostMapping("/strip")
    public void Strip (@RequestBody Container container) {
        XMLStore xmlStore = new XMLStore();
        String[] functions = container.getData().split("\n");
        String[] jeje = {"HOla"};
        for (int i = 0; i < functions.length; i++) {
            String[] function = functions[i].split("/");
            if (Objects.equals(function[0], "CreateTable")) {
                String[] atributos = function[2].split(",");
                xmlStore.createXML(function[1], atributos);
            } else if (Objects.equals(function[0], "Insert")) {
                String[] values = function[2].split(",");
                xmlStore.countRows(function[1]);
                xmlStore.insert(function[1], values);
            } else if (Objects.equals(function[0], "Update")) {
                String[] newAttributes = function[4].split(",");
                String[] newRows = function[5].split(",");
                xmlStore.update(function[1], function[2], function[3], newAttributes, newRows);
            } else if (Objects.equals(function[0], "InnerJoin")) {
                String[] newRows = function[5].split(",");
                String[] attribute1 = function[3].split(",");
                String[] attribute2 = function[4].split(",");
                String[] conditionals = function[5].split(",");
                xmlStore.innerJoin(function[1], function[2], attribute1, attribute2, newRows, conditionals);
            } else if (Objects.equals(function[0], "Select")) {
                String[] newAttributes = function[2].split(",");
                xmlStore.select(function[1], newAttributes, function[3], function[4], jeje);
            } else if(Objects.equals(function[0], "Delete"));
                if (function.length==2){
                    xmlStore.deleteSupreme(function[1]);
                }
                else{
                    xmlStore.delete(function[1],function[2],function[3]);
                }
        }
    }

    @PostMapping("/testFour")
    public void testing2() {
        XMLStore xmlStore = new XMLStore();
        String[] attribute1 = {"Hola","Nou"};
        String[] attribute2 = {"Hola","Siu"};
        String[] attributes = {"LeoGodness.Hola","LeoGod.Siu"};
        String[] conditionals= {"||"};

        xmlStore.innerJoin("LeoGodness","LeoGod", attribute1, attribute2,attributes,conditionals);

    }

}
