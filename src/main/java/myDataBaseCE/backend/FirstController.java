package myDataBaseCE.backend;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;

import java.lang.reflect.Type;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FirstController {

    @GetMapping("/get-user")
    public List<String> testOne(@RequestParam String look) throws IOException {
        String response = "wait";
        String ard = "arduinando";
        if (Objects.equals(look, ard)){
            System.out.println("hay");

            String pass = SerialCommunication.recibirDato();
            System.out.println(pass);

            pass = HuffmanEncoder.compress(pass);

            File file = new File(
                    "C:\\Users\\eseca\\Desktop\\Code\\Angular\\proyecto3datos2\\backend\\Users\\users.txt");

            BufferedReader br
                    = new BufferedReader(new FileReader(file));

            String st;



            while ((st = br.readLine()) != null) {
                System.out.println(st);
                String[] listo = st.split(",");
                System.out.println(listo[0]);

                if (Arrays.stream(listo).toList().contains(pass)){
                    SerialCommunication.enviarDato("1");
                    response = "yes";
                }

                else {
                    SerialCommunication.enviarDato("2");
                    response = "no";
                }

            }



        }

        else{
            File file = new File(
                    "C:\\Users\\eseca\\Desktop\\Code\\Angular\\proyecto3datos2\\backend\\Users\\users.txt");

            BufferedReader br
                    = new BufferedReader(new FileReader(file));

            String st;


            look = HuffmanEncoder.compress(look);

            while ((st = br.readLine()) != null) {
                System.out.println(st);
                String[] listo = st.split(",");
                System.out.println(listo[0]);

                if (Arrays.stream(listo).toList().contains(look)){
                    SerialCommunication.enviarDato("1");
                    response = "yes";
                }

                else {
                    SerialCommunication.enviarDato("2");
                    response = "no";
                }

            }
            System.out.println(response);


            List<String> res = List.of(new String[]{response});
            return res;
        }
        List<String> res = List.of(new String[]{response});
        return res;
    }

    @PostMapping("/create-user")
    public void createUser(@RequestBody User exampleBoy) throws IOException {
        System.out.println(exampleBoy.getPassword());

        String save = exampleBoy.getPassword();
        String saveHuff = HuffmanEncoder.compress(save);


        System.out.println(save);
        try{
            File path = new File("C:\\Users\\eseca\\Desktop\\Code\\Angular\\proyecto3datos2\\backend\\Users\\users.txt");
            FileWriter wr = new FileWriter(path, true);
            wr.append(saveHuff);
            wr.append(',');
            wr.flush();
            wr.close();
        }

        catch (IOException ex) {
            System.out.print("Invalid Path");
        }
    }

    @RequestMapping("/get-tables")
    public List<String> getTables() {
        XMLStore xmlStore = new XMLStore();
        return xmlStore.findFoldersInDirectory();
    }


    @PostMapping("/send-commit")
    public void sendCommit(@RequestBody Container arroz) throws IOException{
        System.out.println(arroz.getData());
    }

    @GetMapping("/get-xml-data")
    public ArrayList<ArrayList<String>>testing(@RequestParam String xmlName) {
        System.out.println(xmlName);
        XMLStore xmlStore = new XMLStore();
        return xmlStore.sendTable(xmlName);

    }

    @PostMapping("/strip")
    public void Strip (@RequestBody Container container){
        System.out.println(container.getData());
        XMLStore xmlStore = new XMLStore();
        String[] functions = container.getData().split("\n");
        String[] jeje = {"HOla"};
        for (int i = 0; i < functions.length; i++) {
            String[] function = functions[i].split("/");
            if (Objects.equals(function[0], "CreateTable")) {
                String[] atributos = function[2].split(",");
                xmlStore.createXML(function[1], atributos);
            }
            else if(Objects.equals(function[0], "Delete")){
                if(function.length==2){
                    xmlStore.deleteSupreme(function[1]);
                    SerialCommunication.enviarDato("3");
                }
                else{
                    xmlStore.delete(function[1],function[2],function[3]);
                }
            }
            else if (Objects.equals(function[0], "Insert")) {
                String[] values = function[2].split(",");
                xmlStore.countRows(function[1]);
                xmlStore.insert(function[1], values);
            }
            else if (Objects.equals(function[0], "Update")) {
                if(function.length==6) {
                    String[] newAttributes = function[4].split(",");
                    String[] newRows = function[5].split(",");
                    xmlStore.update(function[1], function[2], function[3], newAttributes, newRows);
                }
                else{
                    String[] newAttributes = function[2].split(",");
                    String[] newValues = function[3].split(",");
                    xmlStore.updateChafa(function[1],newAttributes,newValues);
                }
            }
            else if (Objects.equals(function[0], "InnerJoin")) {

                String[] attribute1 = function[3].split(",");
                String[] attribute2 = function[4].split(",");
                String[] newRows = function[5].split(",");
                String[] conditionals = function[6].split(",");
                xmlStore.innerJoin(function[1], function[2], attribute1, attribute2, newRows, conditionals);
            }
            else if (Objects.equals(function[0], "Select")) {
                String[] newAttributes = function[2].split(",");
                xmlStore.select(function[1], newAttributes, function[3], function[4], jeje);
            }

        }
    }

    @PostMapping("/testFour")
    public void testing2() {
        XMLStore xmlStore = new XMLStore();
        String[] attributes = {"Hola","Siu"};
        String[] newValues = {"Por", "favor"};

        xmlStore.updateChafa("LeoGod",attributes,newValues);

    }

    @RequestMapping("/get-xml")
    public String[][] getXmls() {
        String[][] arn = {{"hen", "hon", "wer"}, {"hol", "art", "erw"}, {"dad", "ddd", "wep"} };

        return arn;

    }

}
