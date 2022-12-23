package gitlet.test;

import gitlet.objects.Blob;
import gitlet.objects.Branches;
import gitlet.objects.Commit;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static gitlet.Repository.BRANCHES_DIR;
import static gitlet.Repository.CWD;
import static gitlet.Utils.join;
import static gitlet.Utils.readObject;
import static gitlet.staticMethods.*;

public class helperMethodsTests {
    @Test
    public void compareTest() throws IOException {
        Blob blob1 = new Blob(null);
        blob1.compare("abcde","00abcde");
        System.out.println(blob1.getRemovals());
        System.out.println(blob1.getInsertions());
    }

    @Test
    public void matchMapTest() throws IOException {
        Map matchMap = getMatchMap("012345Hello","Hello");
        Map deduct = deduct((Set<Integer>) matchMap.values(), 5);
        System.out.println(deduct);
    }

    @Test
    public void removeTest() throws IOException {
        String original = "abcde";
        System.out.println(remove(original,null));
    }

    @Test
    public void insertTest() throws IOException {
        String original = "12345";
        Map<Integer,String> insertions= new HashMap<>();
        insertions.put(1,"ab");//在指定位置之前插入
        System.out.println(insert(original,insertions));
    }

    @Test
    public void modifyTest() throws IOException {
        Blob blob1 = new Blob(null);
        blob1.compare("1234567","123aaaa");
        System.out.println(blob1.getRemovals());
        String result = modify("1234567", blob1);
        System.out.println(result);
    }

    @Test
    public void renderTest() throws IOException {
        Branches branch = readObject(BRANCHES_DIR,Branches.class);
        Blob currentBlob = branch.getHEAD().getContents().get(join(CWD,"target.txt"));
        System.out.println(currentBlob.render());
    }

    @Test
    public void compressTest() {
        Set lst = new TreeSet();
        lst.add(1);
        lst.add(2);
        lst.add(3);
        lst.add(8);
        lst.add(9);
        lst.add(10);
        Map temp = compress(lst);
        System.out.println(deCompress(temp).toString());
    }

    @Test
    public void decompressTest() {
        Map<Integer,Integer> input = new HashMap<>();
        input.put(0,3);
        input.put(5,7);
        System.out.println(deCompress(input).toString());
    }
}
