package gitlet.objects;

import gitlet.staticMethods;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static gitlet.staticMethods.*;


public class Blob implements Serializable {
    @Serial
    private static final long serialVersionUID = SVUID;
    public File file;
    private Date date;
    private String UID;
    private Blob parent;
    private Map<Integer, Integer> removals;//In original text, [a,b] is removed
    private Map<Integer, String> insertions;// new texts and its location
    private String branch;
    private int depth;
    String content;
    Branches branches = readObject(BRANCHES_DIR,Branches.class);

    public Blob(File file){
        if(file != null){
            this.file = file;
            date = new Date();
            branch = branches.getCurrentBranch();
            parent = branches.getLatestBLob(branch,file);


            String prev = null;
            if(parent != null){
                depth = parent.depth + 1;
                prev = parent.render();
                compare(prev,readContentsAsString(file));
            }else {
                parent = null;
                depth = 0;
            }
            if(depth % 10 == 0){
                content = readContentsAsString(file);
            }

            UID = sha1(date.toString());
        }
    }

    public String render(){
        Stack<Blob> blobs = new Stack<>();
        String content = null;
        Blob pointer = this;
        while (pointer != null && pointer.content == null){
            blobs.push(pointer);
            pointer = pointer.parent;
        }
        if(pointer.content != null){
            content = pointer.content;
        }
        while (! blobs.isEmpty()){
            content = modify(content,blobs.pop());
        }
        return content;
    }
    public void compare(String before, String after){
        Map matchMap = getMatchMap(before, after);
        removals = deduct(matchMap.keySet(),before.length());
        insertions = findInsertions(matchMap, after);
    }

    public int getDepth() {
        return depth;
    }
    public Map<Integer, Integer> getRemovals() {
        return removals;
    }
    public Map<Integer, String> getInsertions() {
        return insertions;
    }
    public File getFile() {
        return file;
    }
    @Override
    public String toString() {
        return "Blob{" +
                "date=" + date +
                ", UID='" + UID + '\'' +
                ", branch='" + branch + '\'' +
                ", depth=" + depth +
                '}';
    }
}
