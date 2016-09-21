import os
import zipfile
import shutil

ignoreFiles = ["desktop.ini", ".lock", ".DS_Store"]
ignoreFolders = [".dropbox.cache", "Music", "Others"]
ignoreSrcPaths = ["/Users/jfdai/OneDrive/Music", "/Users/jfdai/OneDrive/Others"]
#/Users/jfdai/Library/Mobile Documents/com~apple~CloudDocs
def SyncAllFolders():
    source = "/Users/jfdai/OneDrive"
    targets = ["/Users/jfdai/Dropbox", "/Users/jfdai/Google Drive", "/Users/jfdai/Box Sync", "/Users/jfdai/Library/Mobile Documents/com~apple~CloudDocs"]
    for i in range(len(targets)):
        SyncFolders(source, targets[i])
        CheckFolders(targets[i], source)

def SyncFolders(srcDir, destDir):
    if srcDir in ignoreFolders:
        print ("Ignore folder %s" % (srcDir))
        return
    for f in os.listdir(srcDir):
        srcPath = os.path.join(srcDir, f)
        if srcPath in ignoreSrcPaths:
            print ("Ignore source folder %s" % (srcPath))
            return
        destPath = os.path.join(destDir, f)
        if os.path.isfile(srcPath):
            if f in ignoreFiles:
                print ("Ignore file %s" % (f))
                continue
            if (not os.path.isfile(destPath)) or (os.path.getsize(srcPath) != os.path.getsize(destPath)):
                print ("Copy file from %s to %s" % (srcPath, destPath))
                try:
                    shutil.copy2(srcPath, destPath)
                except IOError as why:
                    print ("[ERROR]Failed to copy file from %s to %s because : %s" % (srcPath, destPath, str(why)))
                else:
                    pass
            else:
                pass
        else:
            if os.path.exists(destPath):
                SyncFolders(srcPath, destPath)
            else:
                shutil.copytree(srcPath, destPath)

def CheckFolders(srcDir, destDir):
    if srcDir in ignoreFolders:
        print ("Ignore folder %s" % (srcDir))
        return
    for f in os.listdir(srcDir):
        srcPath = os.path.join(srcDir, f)
        destPath = os.path.join(destDir, f)
        if os.path.isfile(srcPath):
            if f in ignoreFiles:
                print ("Ignore file %s" % (f))
                continue
            if os.path.exists(destPath):
                continue
            else:
                os.remove(srcPath)
                print ("Delete file from %s" % (srcPath))
        else:
            if os.path.exists(destPath):
                CheckFolders(srcPath, destPath)
            else:
                shutil.rmtree(srcPath, ignore_errors=True)
                print ("Delete folder from %s" % (srcPath))

def RenameAllFiles(path, prefix, width):
        number = 1
        for f in os.listdir(path):
                oldPath = os.path.join(path, f)
                if os.path.isfile(oldPath):
                        ext = f.split(os.extsep)[-1]
                        nf = prefix + str(number).rjust(width, '0') + "." + ext
                        newPath = os.path.join(path, nf)
                        print ("Renaming %s to %s " % (oldPath, newPath))
                        os.rename(oldPath, newPath)
                        number = number + 1
                else:
                        RenameAllFiles(f, prefix, width)

def RemovePrefix(path, prefix, newPrefix):
        for f in os.listdir(path):
                oldPath = os.path.join(path, f)
                if(os.path.isfile(oldPath) and f.startswith(prefix)):
                        nf = newPrefix + f[len(prefix):]
                        newPath = os.path.join(path,nf)
                        print ("Renaming %s to %s " % (oldPath, newPath))
                        os.rename(oldPath, newPath)
                elif os.path.isdir(oldPath):
                        RemovePrefix(f, prefix, newPrefix)
                else:
                        pass

def UnZipAllFiles(path):
        for f in os.listdir(path):
                p = os.path.join(path,f)
                if(os.path.isfile(p)):
                        with zipfile.ZipFile(p) as zf:
                                zf.extractall(path)
                elif os.path.isdir(p):
                        UnZipAllFiles(p)
                else:
                        pass

def RemoveConflict(path):
        for f in os.listdir(path):
                oldPath = os.path.join(path, f)
                if os.path.isfile(oldPath) and 'from' in f:
                        #os.remove(oldPath)
                        print(oldPath);
                elif os.path.isdir(oldPath):
                        RemoveConflict(oldPath)
                else:
                        pass

if __name__ == "__main__":
        SyncAllFolders()
