import java.util.Arrays;

public class BoxWhisker {
    static boolean DEBUG = true;
    int timeValues[][] ;
    int qArray[] = new int[3];
    static int counter =0;
    LineDrawBanchmark lineTest;

    BoxWhisker(int daten[][]){
        timeValues=new int[daten.length][];
        timeValues=daten;
        for(int i = 0; i<daten.length; i++) {
            timeValues[i] = Arrays.copyOf(daten[i],daten[i].length);
        }
        sort();

    }


    public int getMin(int version){
        if(version>=timeValues.length) return -1;
        return timeValues[version][0];
    }
    public int getMax(int version){
        if(version>=timeValues.length) return -1;
        int length = timeValues[version].length;
        return timeValues[version][length-1];
    }
    public int[] getQues(int version){
        initQes(version);
        return qArray;
    }
    private int getMedian(int version){
        if(version>=timeValues.length) return -1;
        int median = getMedian(timeValues[version]);
        return median;
    }

    private int getMedian(int timeValues[]){
        int anzVersuchen = timeValues.length;
        if(anzVersuchen%2 == 1){
            int midElNum=(anzVersuchen+1)/2;
            int firstMidleNumber = midElNum-1;
            return timeValues[firstMidleNumber];
        }
        else{
            int midElNum = anzVersuchen/2;
            int median = (timeValues[midElNum-1]+timeValues[midElNum])/2;

            return median;
        }
    }

    private void getMedian(int timeValues[], int links, int rechts){
            int anzVersuchen = rechts;
            int midElNum;
            int medianIndex=0;
            if (anzVersuchen % 2 == 1) {
                midElNum = (anzVersuchen + 1) / 2;
                medianIndex = midElNum - 1;
                qArray[counter] = timeValues[medianIndex];
                counter++;
                getMedian2(timeValues,0,medianIndex);
                getMedian2(timeValues,medianIndex,rechts);
            } else {
                midElNum = anzVersuchen / 2;
                int median = (timeValues[midElNum - 1] + timeValues[midElNum]) / 2;
                qArray[counter] = median;
                counter++;
                getMedian2(timeValues,0,midElNum);
                getMedian2(timeValues,midElNum,rechts);
            }
    }
    private void getMedian2(int timeValues[], int links, int rechts){
            int anzVersuchen = rechts;
            int midElNum;
            int medianIndex=0;
            if (anzVersuchen % 2 == 1) {
                midElNum = (anzVersuchen+links + 1) / 2;
                medianIndex = midElNum - 1;
                qArray[counter] = timeValues[medianIndex];
                counter++;
            } else {
                midElNum = (anzVersuchen+links) / 2;
                int median = (timeValues[midElNum - 1] + timeValues[midElNum]) / 2;
                qArray[counter] = median;
                counter++;
            }
    }

    private void initQes(int version){
        if(version>=timeValues.length) return;
        getMedian(timeValues[version],0,timeValues[version].length);
        counter=0;
    }
    protected void sort(){

        for(int version = 0 ; version < timeValues.length;version++){
            Arrays.sort(timeValues[version]);
                                                                                                                        if(DEBUG) {
                                                                                                                            System.out.println("\nDrawLine Version: "+ version);
                                                                                                                            System.out.print("Zeit in ms: ");
                                                                                                                            for (int versuch = 0; versuch < timeValues[version].length; versuch++) {
                                                                                                                                System.out.print(" " + timeValues[version][versuch] + " ");
                                                                                                                            }
                                                                                                                        }
        }
    }

    //für Testzwecke
    static public void main(String ...args){
        int werte[][] = new int[][]{
                {1,9,6,3,66,22,111},
                {133,91,62,3,6,2,11},
                {1,3,2,5,4,6,7}
        };
        BoxWhisker box = new BoxWhisker(werte);
        if(DEBUG) System.out.println("MAX: " + box.getMax(1));
        if(DEBUG) System.out.println("Min: " + box.getMin(1));
        box.initQes(1);
        if(DEBUG)
            for(int i : box.qArray){
                System.out.println("Medians: " + i + " , " );
            }

    }
}
