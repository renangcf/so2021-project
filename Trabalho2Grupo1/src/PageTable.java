import java.util.ArrayList;
import java.util.Iterator;
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

    public int getIdProcess(){
        return idProcess;
    }

    public String toString(){
        String lista = "";
        Iterator iterator = listPages.listIterator();

        Page page = (Page) iterator.next();
        lista = lista + page.toString();
        while(iterator.hasNext()){
            page = (Page) iterator.next();
            lista = lista + "\n" + page.toString();
        }

        return lista;
    }
}
