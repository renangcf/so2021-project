public class Page {
    int idPage;
    int validationBit;
    int firstBitOfFrame;
    boolean isLastPageOfStaticData;
    boolean isLastPageOfHeapData;
    int allocatedSpaceOnFrame;

    public Page(int idPage,int validationBit,int firstBitOfFrame,boolean isLastPageOfStaticData,boolean isLastPageOfHeapData,int allocatedSpaceOnFrame){
        this.idPage =idPage;
        this.validationBit = validationBit;
        this.firstBitOfFrame = firstBitOfFrame;
        this.isLastPageOfStaticData = isLastPageOfStaticData;
        this.isLastPageOfHeapData = isLastPageOfHeapData;
        this.allocatedSpaceOnFrame = allocatedSpaceOnFrame;
    }

    public String toString(){
        String stringPage = Integer.toString(idPage) + " " + Integer.toString(firstBitOfFrame) + " " + Integer.toString(validationBit);
        return stringPage;
    }
    public int getFirstBitOfFrame(){return firstBitOfFrame;}
    public int getIdPage(){return idPage;}
    public boolean getIsLastPageOfStaticData(){return isLastPageOfStaticData;}
    public boolean getIsLastPageOfHeap(){return isLastPageOfHeapData;}
    public int getAllocatedSpaceOnFrame(){return allocatedSpaceOnFrame;}

    public void setAllocatedSpaceOnFrame(int allocatedSpaceOnFrame){this.allocatedSpaceOnFrame = allocatedSpaceOnFrame;}
}
