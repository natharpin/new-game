package island.datastructures;

public class Recipe {

	Dictionary<String, Integer> components;
	Tuple<String, Integer> result;
	
	public Recipe(String item, Integer amt){
		components = new Dictionary<String, Integer>();
		result = new Tuple<String, Integer>(item, amt);
	}
}
