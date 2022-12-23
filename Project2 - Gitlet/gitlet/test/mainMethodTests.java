package gitlet.test;


//import gitlet.Main;

import gitlet.Main;
import gitlet.objects.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static gitlet.staticMethods.*;

public class mainMethodTests {
    @Test
    public void initializerTest() throws Exception {
        String[] arg = new String[1];
        arg[0] = "init";
        Main.main(arg);
    }

    @Test
    public void addTest() throws Exception {
        String[] arg = new String[2];
        arg[0] = "add";
        arg[1] = "target.txt";
        Main.main(arg);
    }

    @Test
    public void commitTest() throws IOException {
        String[] arg = new String[2];
        arg[0] = "commit";
        arg[1] = "sub branch commit";
        Main.main(arg);
    }

    @Test
    public void rmTest() throws IOException {
        String[] arg = new String[2];
        arg[0] = "rm";
        arg[1] = "target3.txt";
        Main.main(arg);
    }

    @Test
    public void logTest() throws IOException {
        String[] arg = new String[1];
        arg[0] = "log";
        Main.main(arg);
    }

    @Test
    public void logGlobalTest() throws IOException {
        String[] arg = new String[1];
        arg[0] = "global-log";
        Main.main(arg);
    }

    @Test
    public void findTest() throws IOException {
        String[] arg = new String[2];
        arg[0] = "find";
        arg[1] = "new branch";
        Main.main(arg);
    }

    @Test
    public void statusTest() throws IOException {
        String[] arg = new String[1];
        arg[0] = "status";
        Main.main(arg);
    }

    @Test
    public void checkOut_branchTest() throws IOException {
        String[] arg = new String[2];
        arg[0] = "checkOut";
        arg[1] = "subbranch1";
        Main.main(arg);
    }
    @Test
    public void checkOut_fileNameTest() throws IOException {
        String[] arg = new String[2];
        arg[0] = "checkOut";
        arg[1] = "target";
        Main.main(arg);
    }
    @Test
    public void checkOut_id_fileTest() throws IOException {
        String[] arg = new String[3];
        arg[0] = "checkOut";
//        arg[1] = "id";
        arg[2] = "target.txt";
        Main.main(arg);
    }

    @Test
    public void branchTest() throws IOException {
        String[] arg = new String[2];
        arg[0] = "branch";
        arg[1] = "sub branch";
        Main.main(arg);
    }

    @Test
    public void rmBranchTest() throws IOException {
        String[] arg = new String[2];
        arg[0] = "rm-branch";
        arg[1] = "new branch";
        Main.main(arg);
    }
    @Test
    public void resetTest() throws IOException {
        String[] arg = new String[2];
        arg[0] = "reset";
//        arg[1] = "id";
        Main.main(arg);
    }

    @Test
    public void mergeTest() throws IOException {
        System.out.println(mergeText("abc","abcde11","abcde"));
    }







}
