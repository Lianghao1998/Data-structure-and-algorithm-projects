# Gitlet Design Document
#### Project2 of Berkeley online course CS 61B "Data Structures and Algorithms" Spring 2021, instructed by Josh Hug

**Name**: Lianghao

## Supported Commands:
* init: Creates a new Gitlet version-control system in the current directory.
* add: Adds a copy of the file as it currently exists to the staging area.
* commit: Saves a snapshot of tracked files in the current commit and staging area so they can be restored at a later time, creating a new commit.
* rm: Unstage the file if it is currently staged for addition.
* log: Starting at the current head commit, display information about each commit.
* global-log: Displays information about all commits ever made.
* find: Prints out the ids of all commits that have the given commit message, one per line.
* status: Displays what branches currently exist, and marks the current branch with a *.
* checkout: Checkout is a kind of general command that can do a few different things depending on what its arguments are.
* branch: Creates a new branch with the given name, and points it at the current head commit.
* rm-branch: Deletes the branch with the given name.
* reset: Checks out all the files tracked by the given commit. Removes tracked files that are not present in that commit.
* merge: Merges files from the given branch into the current branch.
*
#### As required on https://sp21.datastructur.es/materials/proj/proj2/proj2


## Classes and Data Structure

### Branches
* Various Pairs of (Branch name --> The Latest Commit of that branch)
* Name of current branch

### StagingArea
* additionStage: Various Pairs of (Tracked modified File name --> Latest blob)
* removalStage: Files that is deleted but tracked in the latest commit;

## Instant Variables

### Commit
* Basic information: Creation time, parent, message, branch name, depth...
* Contents map: Various Pairs of (Tracked modified File name --> Latest blob).


### Blobs
* Basic information: Corresponding file, Creation time, parent, message, branch name, depth...
* Removal map: Compare to previous version, characters on [a,b] position are removed;
* Insertion map: Compare to previous version, these string are inserted on those positions;


## Algorithms
* compress
* deCompress
* compare and insert

## Persistence
* Staging area
* Commits

