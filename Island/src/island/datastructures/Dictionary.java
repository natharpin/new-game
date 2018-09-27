package island.datastructures;

import java.util.ArrayList;

public class Dictionary <T, R>{

	public ArrayList<Tuple<T, R>> dict;
	
	public Dictionary(){
		dict = new ArrayList<Tuple<T,R>>();
	}
	
	public void add(Tuple<T,R> tup){
		dict.add(tup);
	}
}
