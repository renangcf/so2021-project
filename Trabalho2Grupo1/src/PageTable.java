import java.util.ArrayList;
import java.util.List;

public class PageTable {
    List<Page> listPages;
    int idProcess;
    String processName;
    int textSize;
    int dataSize;


    public PageTable(int idProcess,String processName,int textSize,int dataSize){
        listPages = new ArrayList<>();
        this.idProcess = idProcess;
        this.processName = processName;
        this.textSize = textSize;
        this.dataSize = dataSize;
    }
}
