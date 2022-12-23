package gitlet;

import gitlet.objects.Branches;
import gitlet.objects.StagingArea;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
//    public static final File CWD = new File("/Users/chenglianghao/Lab/project1Test");
//        public static final File CWD = new File("/C:/Users/DELL/Desktop/p2doc/");


    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File STAGING_AREA_DIR = join(GITLET_DIR,"Staging Area");
    public static final File BRANCHES_DIR = join(GITLET_DIR,"Branches");
    public static final long SVUID = -5365630128856068164L;

}
