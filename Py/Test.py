import os
import zipfile
import shutil

ignoreFiles = ["desktop.ini", ".lock", ".DS_Store"]
ignoreDestPaths = ["/Users/jfdai/Dropbox/.dropbox.cache", "/Users/jfdai/Dropbox/Downloads", "/Users/jfdai/Dropbox/eBooks", 
    "/Users/jfdai/Dropbox/Music", "/Users/jfdai/Dropbox/Photos", "/Users/jfdai/Dropbox/Samples", "/Users/jfdai/Dropbox/Videos",
    "/Users/jfdai/Google Drive/Music", "/Users/jfdai/Google Drive/Videos", "/Users/jfdai/Google Drive/Samples", "/Users/jfdai/Google Drive/Downloads", "/Users/jfdai/Google Drive/Photos",
    "/Users/jfdai/Box Sync/Downloads", "/Users/jfdai/Box Sync/eBooks", "/Users/jfdai/Box Sync/Music",
    "/Users/jfdai/Box Sync/Photos", "/Users/jfdai/Box Sync/Samples", "/Users/jfdai/Box Sync/Videos"]
ignoreSrcPaths = []
#/Users/jfdai/Library/Mobile Documents/com~apple~CloudDocs
def SyncAllFolders():
    source = "/Users/jfdai/OneDrive"
    targets = ["/Users/jfdai/Dropbox", "/Users/jfdai/Google Drive", "/Users/jfdai/Box Sync", "/Users/jfdai/Library/Mobile Documents/com~apple~CloudDocs"]
    for i in range(len(targets)):
        SyncFolders(source, targets[i])
        CheckFolders(targets[i], source)

def SyncFolders(srcDir, destDir):
    for f in os.listdir(srcDir):
        srcPath = os.path.join(srcDir, f)
        if srcPath in ignoreSrcPaths:
            print ("Ignore source folder %s" % (srcPath))
            continue
        destPath = os.path.join(destDir, f)
        if destPath in ignoreDestPaths:
            print ("Ignore destination folder %s" % (destPath))
            continue
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
                print ("Copying source folder %s to destination folder %s" % (srcPath, destPath))
                shutil.copytree(srcPath, destPath)

def CheckFolders(destDir, srcDir):
    for f in os.listdir(destDir):
        destPath = os.path.join(destDir, f)
        srcPath = os.path.join(srcDir, f)      
        if destPath in ignoreDestPaths:
            shutil.rmtree(destPath, ignore_errors=True)
            print ("Delete ignored destination folder %s" % (destPath))
            continue
        if os.path.isfile(destPath):
            if f in ignoreFiles:
                print ("Ignore file %s" % (destPath))
                continue
            if (os.path.exists(srcPath) and (srcPath not in ignoreSrcPaths)):
                continue
            else:
                os.remove(destPath)
                print ("Delete file from %s" % (destPath))
        else:
            if os.path.exists(srcPath):
                CheckFolders(destPath, srcPath)
            else:
                shutil.rmtree(destPath, ignore_errors=True)
                print ("Delete folder from %s" % (destPath))

# def RenameAllFiles(path, prefix, width):
#         number = 1
#         for f in os.listdir(path):
#                 oldPath = os.path.join(path, f)
#                 if os.path.isfile(oldPath):
#                         ext = f.split(os.extsep)[-1]
#                         nf = prefix + str(number).rjust(width, '0') + "." + ext
#                         newPath = os.path.join(path, nf)
#                         print ("Renaming %s to %s " % (oldPath, newPath))
#                         os.rename(oldPath, newPath)
#                         number = number + 1
#                 else:
#                         RenameAllFiles(f, prefix, width)

# def RemovePrefix(path, prefix, newPrefix):
#         for f in os.listdir(path):
#                 oldPath = os.path.join(path, f)
#                 if(os.path.isfile(oldPath) and f.startswith(prefix)):
#                         nf = newPrefix + f[len(prefix):]
#                         newPath = os.path.join(path,nf)
#                         print ("Renaming %s to %s " % (oldPath, newPath))
#                         os.rename(oldPath, newPath)
#                 elif os.path.isdir(oldPath):
#                         RemovePrefix(f, prefix, newPrefix)
#                 else:
#                         pass

# def UnZipAllFiles(path):
#         for f in os.listdir(path):
#                 p = os.path.join(path,f)
#                 if(os.path.isfile(p)):
#                         with zipfile.ZipFile(p) as zf:
#                                 zf.extractall(path)
#                 elif os.path.isdir(p):
#                         UnZipAllFiles(p)
#                 else:
#                         pass

# def RemoveConflict(path):
#         for f in os.listdir(path):
#                 oldPath = os.path.join(path, f)
#                 if os.path.isfile(oldPath) and 'from' in f:
#                         #os.remove(oldPath)
#                         print(oldPath);
#                 elif os.path.isdir(oldPath):
#                         RemoveConflict(oldPath)
#                 else:
#                         pass

if __name__ == "__main__":
        SyncAllFolders()
