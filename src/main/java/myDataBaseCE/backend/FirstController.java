package myDataBaseCE.backend;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


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

}
