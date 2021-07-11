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
        String stringPage = String.format("% 3d", idPage) + "   " + String.format("%04d", firstBitOfFrame) + "         " + String.format("%01d",validationBit);
        return stringPage;
    }
    public int getFirstBitOfFrame(){return firstBitOfFrame;}
    public int getIdPage(){return idPage;}
    public boolean getIsLastPageOfStaticData(){return isLastPageOfStaticData;}
    public boolean getIsLastPageOfHeap(){return isLastPageOfHeapData;}
    public int getAllocatedSpaceOnFrame(){return allocatedSpaceOnFrame;}

    public void setAllocatedSpaceOnFrame(int allocatedSpaceOnFrame){this.allocatedSpaceOnFrame = allocatedSpaceOnFrame;}
    public void setValidationBit(int newValidationBit){this.validationBit = newValidationBit;}
    public void setIsLastPageOfHeapData(boolean newIsLastPageOfHeapData){this.isLastPageOfHeapData = newIsLastPageOfHeapData;}
}
