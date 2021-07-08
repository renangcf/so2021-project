import Exceptions.InvalidProcessException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PageTable {
    List<Page> listPages; //Max size == 32.
    int currentLatestPage; //Ultima página registrada, desconsiderando as páginas que alocam a pilha
    int idProcess;
    String processName;
    int textSize;
    int dataSize;


    public PageTable(int idProcess,String processName,int textSize,int dataSize){
        listPages = new ArrayList<>();
        this.currentLatestPage = 0;
        this.idProcess = idProcess;
        this.processName = processName;
        this.textSize = textSize;
        this.dataSize = dataSize;
    }

    public int getIdProcess(){
        return idProcess;
    }
    public String getProcessName(){return processName;}
    public int getTextSize(){return textSize;}

    public String toString(){
        String lista = "";
        Iterator iterator = listPages.listIterator();

        Page page = (Page) iterator.next();
        lista = lista + page.toString();
        while (iterator.hasNext()) {
            page = (Page) iterator.next();
            lista = lista + "\n" + page.toString();
        }

        return lista;
    }

    public void addNewPage(int firstBitOfFrame,boolean isLastPageOfStaticData,int allocatedSpaceOnFrame){
        Page page = new Page(currentLatestPage,1,firstBitOfFrame,isLastPageOfStaticData,allocatedSpaceOnFrame);
        currentLatestPage++;

        listPages.add(page);
    }

    public int getFirstBitOfFrameOfPage(int pageId){
        int result = -1;

        Iterator iterator = listPages.listIterator();
        while(iterator.hasNext()){
            Page page = (Page) iterator.next();

            if(pageId == page.getIdPage()){
                result = page.getFirstBitOfFrame();
            }

        }

        return result;
    }
}
