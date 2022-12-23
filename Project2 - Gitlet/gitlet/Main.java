package gitlet;

import gitlet.objects.Blob;
import gitlet.objects.Branches;
import gitlet.objects.Commit;

import gitlet.objects.StagingArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


import static gitlet.Repository.*;
import static gitlet.Utils.validateNumArgs;




/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {


    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0){
            throw (new RuntimeException("args is empty"));
        }

        String firstArg = args[0];

        if(firstArg == "init"){
            if(! GITLET_DIR.exists()){
                staticMethods.initialize();
                System.out.printf(CWD.toString());
                System.out.printf(" has been initialized by Gitlet successfully");
            }else {
                System.out.println("Current directory has already been initialized");
            }
            return;
        }

        Methods methods = new Methods();
        switch(firstArg) {
            case "add":
                String fileName = args[1];
                methods.add(fileName);
                break;

            case "commit":
                String message = args[1];
                methods.commit(message);
                break;

            case "rm":
                String target = args[1];
                methods.rm(target);
                break;

            case "log":
                methods.log("HEAD");
                break;

            case "global-log":
                methods.globalLog();
                break;

            case "find":
                String msg = args[1];
                methods.find(msg);
                break;

            case "status":
                methods.status();
                break;

            case "checkout":
                if(args.length == 2){
                    methods.checkOut(args[1]);
                }
                if(args.length == 3){
                    methods.checkOut(args[1],args[2]);
                }
                break;

            case "branch":
                String branchName = args[1];
                methods.branch(branchName);
                break;

            case "rm-branch":
                String branchToRemove = args[1];
                methods.rmBranch(branchToRemove);
                break;

            case "reset":
                methods.reset(args[1]);
                break;

            case "merge":
                String branch1 = args[1];
                String branch2 = args[2];
                methods.merge(branch1,branch2);
                break;
        }
    }
}
