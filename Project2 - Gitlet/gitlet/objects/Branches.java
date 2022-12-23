package gitlet.objects;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static gitlet.Repository.SVUID;


public class Branches implements Serializable {
    @Serial
    private static final long serialVersionUID = SVUID;
    private Map<String,Commit> branchesMap;
    private Commit HEAD;
    private String currentBranch;

    public Branches(){
        Commit initCommit = new Commit(null,"Master","initial commit");
        branchesMap = new HashMap<>();
        HEAD = initCommit;
        branchesMap.put("Master",initCommit);
        currentBranch = "Master";
    }

    public Set getTrackedFiles() {
        return HEAD.getContents().keySet();
    }
    public Blob getLatestBLob(String branch,File file){
        Commit commit = branchesMap.get(branch);
        return commit.getContents().get(file);
    }
    public String getCurrentBranch(){
        return currentBranch;
    }
    public Map<String, Commit> getBranchesMap() {
        return branchesMap;
    }
    public Commit getBranch(String s){
        if(s.equals("HEAD")){
            return HEAD;
        }
        return branchesMap.get(s);
    }
    public Commit getHEAD() {
        return HEAD;
    }

    public void setHEAD(Commit target){
        HEAD = target;
        currentBranch = target.getBranch();
        branchesMap.put(currentBranch,target);
    }
    public void setCurrentBranch(String currentBranch) {
        this.currentBranch = currentBranch;
    }
}
