
package myDataBaseCE.backend;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class XMLStoreList {
    public XMLStoreList() {
        head = null;
    }

    XMLStore head = new XMLStore();

    void createNewXML(String name, String[] attributes) {
        XMLStore nXML = new XMLStore();
        if (head == null) {
            head = nXML;
        } else {
            nXML.setNext(head);
            head = nXML;
        }
        nXML.createXML(name, attributes);
    }
    public List<String> findFoldersInDirectory() {
        File directory = new File("/home/dadu/IdeaProjects/AdelantoProyecto/XMLFolder/");

        FileFilter directoryFileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };

        File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
        List<String> foldersInDirectory = new ArrayList<String>(directoryListAsFile.length);
        for (File directoryAsFile : directoryListAsFile) {
            foldersInDirectory.add(directoryAsFile.getName());
        }

        return foldersInDirectory;
    }

}
