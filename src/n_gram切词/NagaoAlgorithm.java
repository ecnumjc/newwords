

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NagaoAlgorithm {
	
	private int N;
	
	private List<String> leftPTable;
	private int[] leftLTable;
	private List<String> rightPTable;
	private int[] rightLTable;
	private double wordNumber;
	
	private Map<String, TFNeighbor> wordTFNeighbor;
	
	private final static String stopwords = "的很了么呢是嘛个都也比还这于不与才上用就好在和对挺去后没说";
	
	private NagaoAlgorithm(){
		//default N = 5
		N = 5;
		leftPTable = new ArrayList<String>();
		rightPTable = new ArrayList<String>();
		wordTFNeighbor = new HashMap<String, TFNeighbor>();
	}
	//reverse phrase
	private String reverse(String phrase) {
		StringBuilder reversePhrase = new StringBuilder();
		for (int i = phrase.length() - 1; i >= 0; i--)
			reversePhrase.append(phrase.charAt(i));
		return reversePhrase.toString();
	}
	//co-prefix length of s1 and s2
	public static int coPrefixLength(String s1, String s2){
		int coPrefixLength = 0;
		for(int i = 0; i < Math.min(s1.length(), s2.length()); i++){
			if(s1.charAt(i) == s2.charAt(i))	coPrefixLength++;
			else break;
		}
		return coPrefixLength;
	}
	//add substring of line to pTable
	private void addToPTable(String line){
		//split line according to consecutive none Chinese character
		String[] phrases = line.split("["+stopwords+"]");//分句，按照stopwords进行分句
		for(String phrase : phrases){//遍历分好的句子
			//System.out.println(phrase);
			//System.out.println(phrase.length());
			for(int i = 0; i < phrase.length(); i++)
			{
				//System.out.println(phrase.substring(i));
				rightPTable.add(phrase.substring(i));//将phrase从头开始到i的位置的字截掉，剩下部分add到rightTable中
			}
				
			String reversePhrase = reverse(phrase);
			for(int i = 0; i < reversePhrase.length(); i++)
				leftPTable.add(reversePhrase.substring(i));
			wordNumber += phrase.length();
		}
	}
	
	//count lTable
	private void countLTable(){
		Collections.sort(rightPTable);
		rightLTable = new int[rightPTable.size()];
		for(int i = 1; i < rightPTable.size(); i++)
			rightLTable[i] = coPrefixLength(rightPTable.get(i-1), rightPTable.get(i));
		
		Collections.sort(leftPTable);
		leftLTable = new int[leftPTable.size()];
		for(int i = 1; i < leftPTable.size(); i++)
			leftLTable[i] = coPrefixLength(leftPTable.get(i-1), leftPTable.get(i));
		
		System.out.println("Info: [Nagao Algorithm Step 2]: having sorted PTable and counted left and right LTable");
	}
	//according to pTable and lTable, count statistical result: TF, neighbor distribution
	private void countTFNeighbor(){
		//get TF and right neighbor
		for(int pIndex = 0; pIndex < rightPTable.size(); pIndex++)
		{
			String phrase = rightPTable.get(pIndex);
			for(int length = 1 + rightLTable[pIndex]; length <= N && length <= phrase.length(); length++){
				String word = phrase.substring(0, length);
				TFNeighbor tfNeighbor = new TFNeighbor();
				tfNeighbor.incrementTF();
				if(phrase.length() > length)
				{
					tfNeighbor.addToRightNeighbor(phrase.charAt(length));
				}
							
				for(int lIndex = pIndex+1; lIndex < rightLTable.length; lIndex++)
				{
					if(rightLTable[lIndex] >= length)
					{
						tfNeighbor.incrementTF();
						String coPhrase = rightPTable.get(lIndex);
						if(coPhrase.length() > length)
							tfNeighbor.addToRightNeighbor(coPhrase.charAt(length));
					}
					else break;
				}
				wordTFNeighbor.put(word, tfNeighbor);
			}
		}
		//get left neighbor
		for(int pIndex = 0; pIndex < leftPTable.size(); pIndex++){
			String phrase = leftPTable.get(pIndex);
			for(int length = 1 + leftLTable[pIndex]; length <= N && length <= phrase.length(); length++){
				String word = reverse(phrase.substring(0, length));
				TFNeighbor tfNeighbor = wordTFNeighbor.get(word);
							
				if(phrase.length() > length)
				{
					tfNeighbor.addToLeftNeighbor(phrase.charAt(length));
				}
				
				for(int lIndex = pIndex + 1; lIndex < leftLTable.length; lIndex++){
					if(leftLTable[lIndex] >= length){
						String coPhrase = leftPTable.get(lIndex);
						if(coPhrase.length() > length)
						{
							tfNeighbor.addToLeftNeighbor(coPhrase.charAt(length));
							
						}
							
					}
					else break;
				}
			}
		}
		System.out.println("Info: [Nagao Algorithm Step 3]: having counted TF and Neighbor");
	}
	//according to wordTFNeighbor, count MI of word
	private double countMI(String word){
		if(word.length() <= 1)	return 0;
		double coProbability = wordTFNeighbor.get(word).getTF()/wordNumber;
		List<Double> mi = new ArrayList<Double>(word.length());
		for(int pos = 1; pos < word.length(); pos++){
			String leftPart = word.substring(0, pos);
			String rightPart = word.substring(pos);
			double leftProbability = wordTFNeighbor.get(leftPart).getTF()/wordNumber;
			double rightProbability = wordTFNeighbor.get(rightPart).getTF()/wordNumber;
			mi.add(coProbability/(leftProbability*rightProbability));
		}
		return Collections.min(mi);
	}
	//save TF, (left and right) neighbor number, neighbor entropy, mutual information
	private void saveTFNeighborInfoMI(String out, String stopList, String[] threshold){
		try {
			//read stop words file
			Set<String> stopWords = new HashSet<String>();
			double sumtf=0;
			double leftsum=0;
			double rightsum=0;
			double leftsum_entropy=0;
			double rightsum_entropy=0;
			double MI_mi=0;
			int line_nums=0;
			BufferedReader br = new BufferedReader(new FileReader(stopList));
			String line;
			while((line = br.readLine()) != null){
				if(line.length() >= 1)
					stopWords.add(line);
			}
			br.close();
			//output words TF, neighbor info, MI
			BufferedWriter bw = new BufferedWriter(new FileWriter(out));
			for(Map.Entry<String, TFNeighbor> entry : wordTFNeighbor.entrySet())
			{
				//System.out.println("nn:"+entry.getKey());
				if( entry.getKey().length() <= 1 || stopWords.contains(entry.getKey()) )	continue;
				int contemp=0;
				String  stopw =entry.getKey().replaceAll("\\s*", "");

				if(stopw.length() == 0 ||  stopw.length() ==1) continue;
				for(int u=0;u<stopw.length();u++)
				{	
					if(stopWords.contains(stopw.substring(u, u+1)))
					{
						contemp =1;
					}		
				}
				if(contemp == 0)
				{
				TFNeighbor tfNeighbor = entry.getValue();
				
				int tf, leftNeighborNumber, rightNeighborNumber;
				double leftNeighborEntropy,rightNeighborEntropy,mi;
				tf = tfNeighbor.getTF();
				leftNeighborNumber = tfNeighbor.getLeftNeighborNumber();
				rightNeighborNumber = tfNeighbor.getRightNeighborNumber();
				leftNeighborEntropy = tfNeighbor.getLeftNeighborEntropy();
				rightNeighborEntropy = tfNeighbor.getRightNeighborEntropy();
				mi = countMI(entry.getKey());			
     
				 sumtf+=tf;
				 leftsum+=leftNeighborNumber;
				 rightsum+=leftNeighborNumber;
				 leftsum_entropy+=leftNeighborEntropy;
				 rightsum_entropy+=rightNeighborEntropy;
				 MI_mi=MI_mi+mi;
				 line_nums++;

				
				if(tf >= Integer.parseInt(threshold[0]) && leftNeighborNumber >= Integer.parseInt(threshold[1]) && 
						rightNeighborNumber >= Integer.parseInt(threshold[2]) && leftNeighborEntropy >= Double.valueOf(threshold[3])&&
						rightNeighborEntropy >= Double.valueOf(threshold[4]) && mi >= Double.valueOf(threshold[5]) ){
					StringBuilder sb = new StringBuilder();
					sb.append(entry.getKey());				
					sb.append("\t").append(tf);
					sb.append("\t").append(leftNeighborNumber);
					sb.append("\t").append(rightNeighborNumber);
					sb.append("\t").append(leftNeighborEntropy);
					sb.append("\t").append(rightNeighborEntropy);
					sb.append("\t").append(mi).append("\n");				
					bw.write(sb.toString());					
				}
			  }
			}
			System.out.println(line_nums);
			System.out.println(sumtf/line_nums);
			System.out.println(leftsum/line_nums);
			System.out.println(rightsum/line_nums);
			System.out.println(leftsum_entropy/line_nums);
			System.out.println(rightsum_entropy/line_nums);
			System.out.println(MI_mi/line_nums);
			
			bw.close();
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("Info: [Nagao Algorithm Step 4]: having saved to file");
	}
	//apply nagao algorithm to input file
	public static void applyNagao(String[] inputs, String out, String stopList,String threshold_six){
		NagaoAlgorithm nagao = new NagaoAlgorithm();
		//step 1: add phrases to PTable
		String line;
		for(String in : inputs){
			try {
				BufferedReader br = new BufferedReader(new FileReader(in));
				while((line = br.readLine()) != null){
					nagao.addToPTable(line);
				}
				br.close();
			} catch (IOException e) {
				throw new RuntimeException();
			}
		}
		System.out.println("Info: [Nagao Algorithm Step 1]: having added all left and right substrings to PTable");
		//step 2: sort PTable and count LTable
		nagao.countLTable();
		//step3: count TF and Neighbor
		nagao.countTFNeighbor();
		//step4: save TF NeighborInfo and MI
		nagao.saveTFNeighborInfoMI(out, stopList, threshold_six.split(","));
	}
	public static void applyNagao(String[] inputs, String out, String stopList, int n, String filter){
		NagaoAlgorithm nagao = new NagaoAlgorithm();
		nagao.setN(n);
		String[] threshold = filter.split(",");
		if(threshold.length != 6){
			System.out.println("ERROR: filter must have 6 numbers, seperated with ',' ");
			return;
		}
		//step 1: add phrases to PTable
		String line;
		for(String in : inputs){
			try {
				BufferedReader br = new BufferedReader(new FileReader(in));
				while((line = br.readLine()) != null){
					nagao.addToPTable(line);
				}
				br.close();
			} catch (IOException e) {
				throw new RuntimeException();
			}
		}
		System.out.println("Info: [Nagao Algorithm Step 1]: having added all left and right substrings to PTable");
		//step 2: sort PTable and count LTable
		nagao.countLTable();
		//step3: count TF and Neighbor
		nagao.countTFNeighbor();
		//step4: save TF NeighborInfo and MI
		nagao.saveTFNeighborInfoMI(out, stopList, threshold);
	}
	private void setN(int n){
		N = n;
	}
	
	public static void main(String[] args) {
		
		String[] ins = {"E://NW//fielddata//fund.txt.clean"};
		//applyNagao(ins, "E://NW//fieldwords//securities.txt", "E://NW//stoplist.txt","7,1,1,0.01,0.01,5");
		applyNagao(ins, "E://NW//fieldwords//fund.txt", "E://NW//stoplist.txt","0,0,0,0,0,0");
	}

}
