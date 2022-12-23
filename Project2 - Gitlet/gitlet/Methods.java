package gitlet;

import gitlet.objects.Blob;
import gitlet.objects.Branches;
import gitlet.objects.Commit;
import gitlet.objects.StagingArea;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static gitlet.staticMethods.*;

public class Methods {
    StagingArea stagingArea;
    Branches branches;
    Map<File, Blob> additionStage;
    Set removalStage;
    Map<String,Commit> branchesMap;
    public Methods() throws IOException {
        stagingArea = readObject(STAGING_AREA_DIR,StagingArea.class);
        branches = readObject(BRANCHES_DIR,Branches.class);
        additionStage = stagingArea.getAdditionStage();
        removalStage = stagingArea.getRemovalStage();
        branchesMap = branches.getBranchesMap();
    }

    public void add(String fileName) throws IOException {
//        Adds a copy of the file as it currently exists to the staging area.
        File file = Utils.join(CWD, fileName);

        if(file.exists()){
            Blob blob = new Blob(file);
            System.out.println(blob.getDepth());

            if(blob.getInsertions() == null && blob.getRemovals() == null && blob.getDepth() != 0){
//                Document hasn't changed since last add.
                System.out.println("No change to add");
            }
            else {
                additionStage.put(file, blob);
                System.out.println(file + " has been added to the staging area successfully");
            }

        }else {
            Set trackedFiles = branches.getTrackedFiles();
            if(trackedFiles.contains(file)){
//                File to remove
                Set removalStage = stagingArea.getRemovalStage();
                removalStage.add(file);
                System.out.println(file.toString() + " has been removed from the staging area successfully");
            }else {
//                File doesn't exist
                System.out.println("File doesn't exists!");
            }
        }
        save();
    }

    public void commit(String message){
//        Saves a snapshot of tracked files in the current commit and staging area so they can be restored at a later time, creating a new commit.
        if(additionStage.size() == 0 && removalStage.size() == 0){
            System.out.println("No document to commit");
            return;
        }
        Commit HEAD = branches.getHEAD();
        String currentBranch = branches.getCurrentBranch();

        Commit newCommit = new Commit(HEAD,currentBranch,message);

        //        update the files in staging area, and copy others from parent.
        Map contents = newCommit.getContents();

        for(File file : additionStage.keySet()){
            contents.put(file,additionStage.get(file));
        }
        for (Object f : removalStage){
            contents.remove(f);
        }
        newCommit.setContents(contents);
        System.out.println(newCommit.toString());

        branches.setHEAD(newCommit);

        save();
        stagingArea.clear();
        System.out.println(newCommit.getBranch() + newCommit.getDepth()+" has been committed successfully");

    }

    public void rm(String fileName){
//        Unstage the file if it is currently staged for addition.
        File file = join(CWD,fileName);
        if(additionStage.containsKey(file)){
            additionStage.remove(file);
            System.out.println(fileName + " has been removed form staging area successfully");
        }else {
            System.out.println(fileName + " has not been staged");
        }
        writeObjects(STAGING_AREA_DIR,stagingArea);
    }

    public void log(String target){
//        Starting at the current head commit, display information about each commit.
        Commit commit = null;
        if (target.equals("HEAD")){
            commit = branches.getHEAD();
        }else {
            commit = branches.getBranch(target);
        }


    while (commit.getParent() != null && commit.getParent().getBranch().equals(commit.getBranch())){
        System.out.println(commit.toString());
        commit = commit.getParent();
    }
        System.out.println(commit.toString());

    }

    public void globalLog(){
//        Displays information about all commits ever made.
        for (Object s : branchesMap.keySet()){
            System.out.println("Branch: " + s);
            log((String) s);
            }
        }

    public void find(String msg){
//        Prints out the ids of all commits that have the given commit message, one per line.
        Set<Commit> match = new HashSet<>();
        for (Object s : branchesMap.keySet()){
            Commit pointer = (Commit) branchesMap.get(s);
            do{
                if(pointer.getMessage().equals(msg)){
                    match.add(pointer);
                }
                pointer = pointer.getParent();
            }while (pointer.getParent() != null);
        }
        for (Object o : match){
            Commit commit = (Commit) o;
            System.out.println(commit.getUID());
        }
    }

    public void status() throws IOException {
//        Displays what current branches, staging area and files info
        System.out.println("=== Branches ===");
        for (String s : branches.getBranchesMap().keySet()){
            if(s == branches.getCurrentBranch()){
                System.out.printf("*");
            }
                System.out.println(s);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");

        for (Object f: additionStage.keySet()){
            System.out.println(f.toString());
        }

        System.out.println("=== Removed Files ===");
        for (Object f: removalStage){
            System.out.println(f.toString());
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");

        Set<File> deleted = new HashSet<>();
        Set<File> modified = new HashSet<>();
        Set<File> trackedFiles = branches.getTrackedFiles();
        for(File file : trackedFiles){
            if(! file.exists()){
                deleted.add(file);
            }
            else {
                String content = branches.getHEAD().getContents().get(file).render();
                Blob blob = new Blob(file);
                blob.compare(content,readContentsAsString(file));
                if(blob.getRemovals() != null || blob.getInsertions() != null){
                    modified.add(file);
                }
            }
        }
        System.out.println("Modified: ");
        for(File f : modified){
            System.out.println(f.toString());
        }

        System.out.println("Deleted: ");
        for(File f : deleted){
            System.out.println(f.toString());
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        Set<File> uf = new HashSet<>();
        for(File f : CWD.listFiles()){
            uf.add(f);
        }

        for(Object f : trackedFiles){
            if(uf.contains(f)){
                uf.remove(f);
            }
        }
        uf.remove(GITLET_DIR);
        for (File f : uf){
            System.out.println(f.toString());
        }

    }

    public void checkOut(String name){

        if(branchesMap.containsKey(name)){
//            name is a branch
//            Takes all files in the commit at the head of the given branch, and overwriting existing files;
        Commit commit = branchesMap.get(name);
        for(File file : commit.getContents().keySet()){
            switchTo(commit, file);
        }

        } else if (branches.getTrackedFiles().contains(join(CWD,name))) {
//            name is a file
//            Takes the version of the file as it exits in the head commit
            File file = join(CWD,name);
            switchTo(branches.getHEAD(),file);
            String text = branches.getHEAD().getContents().get(file).render();
            writeObjects(file,text);
        }else {
            System.out.println("No such file nor branch exists");
        }
    }

    public void checkOut(String UID, String name){
//        Takes the version of the file as it exists in the commit with the given id, and overwrite
        Commit commit = getCommitByUID(UID);
        if(commit == null){
            System.out.println("No commit with that id exists");
            return;
        }
        File file = join(CWD,name);
        if(! commit.getContents().containsKey(file)){
            System.out.println("File does not exist in that commit");
            return;
        }
        switchTo(commit,file);
    }

    public void branch(String name){
//        Creates a new branch with the given name, and points it at the current head commit.
        if(branchesMap.keySet().contains(name)){
            Commit commit = (Commit) branchesMap.get(name);
            branches.setHEAD(commit);
        }else {
            branchesMap.put(name,branches.getBranch("HEAD"));
        }
        branches.setCurrentBranch(name);
        writeObjects(BRANCHES_DIR,branches);
        System.out.println("Current branch: " + name);
    }

    public void rmBranch(String name){
//        Deletes the branch with the given name.
        if(name.equals("Master")){
            System.out.println("Master branch can't be removed");
            return;
        }
        if(branchesMap.keySet().contains(name)){
            branchesMap.remove(name);
            System.out.println(name + " has been removed successfully");
        }else {
            System.out.println(name + "doesn't exist");
        }
        writeObjects(BRANCHES_DIR,branches);
    }

    public void reset(String commit){
//         Checks out all the files tracked by the given commit. Removes tracked files that are not present in that commit.
        checkOut(commit);
    }

    public Commit merge(String branch1, String branch2) throws IOException {
//        merge commit2 to commit 1
//        on the same branch: set HEAD to the later version

        Commit commit1 = branches.getBranch(branch1);
        Commit commit2 = branches.getBranch(branch2);

        if(commit1.getBranch().equals(commit2.getBranch())){
            additionStage.clear();
            branches.setHEAD(commit1);
            return commit1;
        }

        else {
            Commit divergentPoint = findDivergent(commit1,commit2);
            Map<File, Blob> newContent = new HashMap<>();
            Commit mergeResult = new Commit(commit1,commit1.getBranch(),"merge"+commit1.getBranch()+commit2.getBranch());
            for (File file : divergentPoint.getContents().keySet()){
                Blob blob1 = commit1.getContents().get(file);
                Blob blob2 = commit2.getContents().get(file);
                Blob blobD = divergentPoint.getContents().get(file);
                Blob mergedBlob = mergeBlob(blob1,blob2,blobD);
                newContent.put(file,mergedBlob);
            }
            mergeResult.setContents(newContent);
            return mergeResult;
        }
    }

    private Commit getCommitByUID(String UID){
        Commit target = null;
        for (Object s : branchesMap.keySet()){
            Commit pointer = (Commit) branchesMap.get(s);
            if(pointer.getUID().equals(UID)){
                target = pointer;
            }
        }
        return target;
    }

    private void save(){
        writeObjects(STAGING_AREA_DIR, stagingArea);
        writeObjects(BRANCHES_DIR, branches);
    }
}
