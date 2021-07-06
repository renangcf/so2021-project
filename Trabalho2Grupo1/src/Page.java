public class Page {
    int validationBit;
    int baseByte;

    public Page(int validationBit,int firstBitOfFrame){
        this.validationBit = validationBit;
        this.baseByte = firstBitOfFrame;
    }
}
