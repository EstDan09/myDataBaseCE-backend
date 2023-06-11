package myDataBaseCE.backend;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    public String[] getTables() {
        String[] wordsList = {"hay", "pene"};
        return wordsList;
    }

    @PostMapping("/send-commit")
    public void sendCommit(@RequestBody Container arroz) throws IOException{
        System.out.println(arroz.getData());
    }

    @RequestMapping("/get-xml")
    public String[][] getXmls() {
        String[][] arn = {{"hen", "hon", "wer"}, {"hol", "art", "erw"}, {"dad", "ddd", "wep"} };

        return arn;

    }

}
