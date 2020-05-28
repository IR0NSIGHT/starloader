package api.mod;

public class ModInfo {

    public String name;
    public String version;
    public String downloadURL;
    public ModInfo(String name, String version){
        this.name = name;
        this.version = version;
    }
    public void fetchDownloadURL(){
        MainModManifest manifest = MainModManifest.getManifest();
        this.downloadURL = manifest.getURL(this);
    }

    public static ModInfo fromString(String s){
        String[] l = s.split(",,,");
        return new ModInfo(l[0], l[1]);
    }
    public String serialize(){
        return name + ",,," + version;
    }

    @Override
    public String toString() {
        return "ModInfo{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", downloadURL='" + downloadURL + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModInfo modInfo = (ModInfo) o;
        return name.equals(modInfo.name) &&
                version.equals(modInfo.version);
    }
}
