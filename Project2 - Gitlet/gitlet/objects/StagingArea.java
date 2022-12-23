package gitlet.objects;

import gitlet.Utils;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class StagingArea implements Serializable {
    @Serial
    private static final long serialVersionUID = SVUID;
    private Map<File, Blob> additionStage;
    private Set<File> removalStage;

    public StagingArea() {
        additionStage = new HashMap<>();
        removalStage = new HashSet<>();
    }
    public void clear(){
        additionStage = new HashMap<>();
        removalStage = new HashSet<>();
        writeObjects(STAGING_AREA_DIR,this);
    }

    public Map<File, Blob> getAdditionStage() {
        return additionStage;
    }
    public Set<File> getRemovalStage() {
        return removalStage;
    }

}
