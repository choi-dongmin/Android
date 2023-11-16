
import com.google.gson.annotations.SerializedName;
public class AppFileData {

    private int version;

    @SerializedName("versionName")
    private String versionName;

    @SerializedName("versionCode")
    private int versionCode;

    @SerializedName("elements")
    private Element[] elements;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public Element[] getElements() {
        return elements;
    }

    public void setElements(Element[] elements) {
        this.elements = elements;
    }

    public static class Element {
        @SerializedName("versionCode")
        private int elementVersionCode;
        @SerializedName("versionName")
        private String elementVersionName;

        public int getElementVersionCode() {
            return elementVersionCode;
        }

        public void setElementVersionCode(int version) {
            this.elementVersionCode = version;
        }

        public String getElementVersionName() {
            return elementVersionName;
        }

        public void setElementVersionName(String versionName) {
            this.elementVersionName = versionName;
        }
    }
}
