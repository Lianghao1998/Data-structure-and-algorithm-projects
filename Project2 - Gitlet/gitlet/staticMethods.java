package gitlet;

import gitlet.objects.Blob;
import gitlet.objects.Branches;
import gitlet.objects.Commit;
import gitlet.objects.StagingArea;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.writeObjects;

public class staticMethods {
    public static void initialize() throws IOException {
        GITLET_DIR.mkdir();
        writeObjects(STAGING_AREA_DIR,new StagingArea());
        writeObjects(BRANCHES_DIR, new Branches());
    }
    public static String modify(String originalText, Blob blob){
        String temp =originalText;
        if(blob.getRemovals() != null){
            temp = remove(originalText,blob.getRemovals());
        }
        if(blob.getInsertions() != null){
            Map matchMap = getMatchMap(originalText, temp);
            Map updatedInsertions = update(matchMap,blob.getInsertions());
            temp = insert(temp, updatedInsertions);
        }
        return temp;
    }

    public static Map update(Map<Integer,Integer> matchMap, Map<Integer,String> addition){
        for (Integer prev: addition.keySet()){
            String s = addition.get(prev);
            int updated = -1;
            if(prev != -1){
                try {
                    updated = matchMap.get(prev);
                }catch (Exception e){
                    while (! matchMap.containsKey(prev)){
                        prev--;
                    }
                    updated = matchMap.get(prev);
                }
            }
            addition.remove(prev);
            addition.put(updated,s);
        }
        return addition;
    }

    public static String remove(String originalText, Map<Integer,Integer> removal){
        Set<Integer> removedSet = new TreeSet<>();
        for (int start : removal.keySet()){
            int end = removal.get(start);
            for(int i = start; i<= end; i++){
                removedSet.add(i);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < originalText.length();i++){
            if(! removedSet.contains(i)){
                sb.append(originalText.charAt(i));
            }
        }
        return sb.toString();
    }

    public static String insert(String originalText, Map<Integer,String> insertions){
        StringBuilder sb = new StringBuilder();
        if(insertions.containsKey(-1)){
            sb.append(insertions.get(-1));
        }
        for (int i = 0; i < originalText.length(); i++){
            sb.append(originalText.charAt(i));
            if(insertions.containsKey(i)){
                sb.append(insertions.get(i));}
            }
        return sb.toString();
    }

    public static Map getMatchMap(String before,String after){
        Map<Integer,Integer> matchMap = new HashMap<>();
        Integer bpointer = 0;
        for (int i = 0; i < before.length(); i++){
            char target = before.charAt(i);
            for (int j = bpointer; j < after.length(); j++){
                if(after.charAt(j) == target){
                    matchMap.put(i,j);
                    bpointer = j+1;
                    break;
                }
            }
        }
        return matchMap;
    };

    public static Map findInsertions(Map<Integer,Integer> matchMap, String after){
        Map<Integer, String> insertionMap = new HashMap<>();
        Map<Integer, Integer> reversedMatchMap = new HashMap<>();
//      get index map from after to original text;
        for (int key: matchMap.keySet()) {
            int value = matchMap.get(key);
            reversedMatchMap.put(value,key);
        }
//transfer Hash values to set
        Set<Integer> valueSet = new HashSet<>();
        for(int i : matchMap.values()){
            valueSet.add(i);
        }
//get indexes of inserted characters
        Map<Integer,Integer> insertionIndex = deduct(valueSet,after.length());

        if(insertionIndex == null){
            return null;
        }

        for (int start : insertionIndex.keySet()){
            int end = insertionIndex.get(start);
            String str = after.substring(start,end+1);
            Integer index = reversedMatchMap.get(start-1);
            if(index == null){index = -1;}
            insertionMap.put(index,str);
        }
//        index points to the previous character in original text, if it's the first, index = -1
        return insertionMap;
    }

    public static Map deduct(Set<Integer> input, int length){
        Set<Integer> temp = new TreeSet<>();
        for(int i = 0; i < length;i++){
            if(! input.contains(i)){
                temp.add(i);
            }
        }
        if(temp.size() == 0){
            return null;
        }
        return compress(temp);
    }

    public static Map compress(Set<Integer> input){
        if(input.isEmpty() || input == null){
            return null;
        }

        Map<Integer, Integer> result = null;
        int start = 0;
        int pointer = 0;
        for(int i : input){
            if(result == null){
                result = new HashMap<>();
                start = i;
                pointer = i;
                continue;
            }

            if(i == pointer +1){
                pointer++;
            }
            else {
                result.put(start,pointer);
                start = i;
                pointer = i;
            }
        }
        result.put(start,pointer);
        return result;
    }

    public static Set deCompress(Map<Integer, Integer> input){
        Set<Integer> result = new TreeSet<>();
        for(Integer start : input.keySet()){
            Integer end = input.get(start);
            for(int i = start; i <= end; i++){
                result.add(i);
            }
        }
        return result;
    }

    public static String mergeText(String content1, String content2, String originalText) throws IOException {
        Blob diff1 = new Blob(null);
        Blob diff2 = new Blob(null);
        diff1.compare(originalText,content1);
        diff2.compare(originalText,content2);

        Set rm1 = new HashSet();
        Set rm2 = new HashSet();

        if(diff1.getRemovals() != null) {
             rm1 = deCompress(diff1.getRemovals());
        }
        if(diff2.getRemovals() != null){
            rm2 = deCompress(diff2.getRemovals());
        }

        StringBuilder sb1 = new StringBuilder();

        for(int i = 0; i < originalText.length(); i++){
            if(! rm1.contains(i) && ! rm2.contains(i)){
            sb1.append(originalText.charAt(i));
            }
        }

        String temp = sb1.toString();

        Map matchMap = getMatchMap(originalText,temp);

        if(diff1.getInsertions() != null && diff2.getInsertions() != null){
            Map ad1 = update(matchMap,diff1.getInsertions());

            Map ad2 = update(matchMap,diff2.getInsertions());

            for (int i : diff1.getInsertions().keySet()){
                if(diff2.getInsertions().containsKey(i)){
                    throw new RuntimeException("Merge conflict!");
                }
            }
            StringBuilder sb2 = new StringBuilder();
            if(ad1.containsKey(-1)){
                sb2.append(ad1.get(-1));
            }
            if(ad2.containsKey(-1)){
                sb2.append(ad2.get(-1));
            }

            for (int i = 0; i < temp.length(); i++){
                sb2.append(temp.charAt(i));
                if(ad1.containsKey(i)){
                    sb2.append(ad1.get(i));
                }
                if(ad2.containsKey(i)){
                    sb2.append(ad2.get(i));
                }
            }
            return sb2.toString();
        }

        else if(diff1.getInsertions() == null && diff2.getInsertions() != null){
            Map ad2 = update(matchMap,diff2.getInsertions());
            return insert(temp,ad2);
        }
        else if(diff1.getInsertions() != null && diff2.getInsertions() == null){
            Map ad1 = update(matchMap,diff1.getInsertions());
            return insert(temp,ad1);
        }

        return temp;
    };

    static void switchTo(Commit commit, File file){
        Blob blob = commit.getContents().get(file);
        String content = blob.render();
        writeObjects(file,content);
    }

    static Commit findDivergent(Commit commit1, Commit commit2){
        Stack<Commit> path1 = findPath(commit1);
        Stack<Commit> path2 = findPath(commit2);
        Commit pointer = path1.pop();
        while (path2.pop() == pointer){
            pointer = path1.pop();
        }
        return pointer;
    }

    private static Stack findPath(Commit commit){
        Stack<Commit> stack = new Stack<>();
        Commit pointer = commit;
        while (pointer.getParent() != null){
            stack.push(pointer);
            pointer = pointer.getParent();
        }
        stack.push(pointer);
        return stack;
    }

    public static Blob mergeBlob(Blob blob1, Blob blob2, Blob divergentBlob) throws IOException {
        String content1 = blob1.render();
        String content2 = blob2.render();
        String contentD = divergentBlob.render();
        String result = mergeText(content1,content2,contentD);
        Blob blobResult = new Blob(blob1.getFile());
        blobResult.compare(content1,result);
        return blobResult;
    }
}
