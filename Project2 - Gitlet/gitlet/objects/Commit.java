package gitlet.objects;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static gitlet.Repository.SVUID;
import static gitlet.Utils.sha1;

public class Commit implements Serializable{
    @Serial
    private static final long serialVersionUID = SVUID;
    private String UID;
    private Date date;
    private Commit parent;
    private String message;
    private String branch;
    private int depth;
    private Map<File, Blob> contents;

    public Commit(Commit parent, String branch, String message) {
        this.parent = parent;
        this.branch = branch;
        this.message = message;

        if(parent != null){
            contents = parent.getContents();
            depth = parent.depth +1;
            date = new Date();
        }else {
            contents = new HashMap<>();
            date = new Date(0);
            depth = 0;
        }
        this.UID = sha1(branch + depth);
    }

    public Map<File, Blob> getContents() {
        return contents;
    }
    public String getMessage() {
        return message;
    }
    public String getUID() {
        return UID;
    }
    public String getBranch() {
        return branch;
    }
    public Commit getParent() {
        return parent;
    }
    public int getDepth() {
        return depth;
    }

    public void setContents(Map<File, Blob> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        String out = "Commit" + " name=" + branch + depth +
                ", UID='" + UID + '\'' +
                ", date=" + date +
                ", message='" + message + '\'' +
                ", branch='" + branch + '\'';
        if(this.parent != null){
            String p =", parent=" + parent.branch + parent.depth;
            return out+p;
        }else {
            return out;
        }

    }
}
