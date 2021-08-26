package boogle;

public class HashWord {
	
	private boolean elements[];
	private String words[];
	private boolean empty;
	
	//Constructor
	public HashWord() {
		empty = true;
		elements = new boolean[100];
		for(int i = 0; i < 100; i++) elements[i] = false;
		words = new String[100];
	}
	
	//Método para sacar el hash code
	private int hashCode(String word) {
		int code = 0;
		for(int i = 0; i < word.length(); i++) code += ((int) word.charAt(i))*((i+5)*13);
		code %= 100;
		return code;
	}
	
	//Para meter palabras en el hash
	public void put(String word) {
		empty = false;
		int hashCode = hashCode(word);
		while(elements[hashCode]) {
			hashCode++;
			if(hashCode >= 100) hashCode = 0;
		}
		elements[hashCode] = true;
		words[hashCode] = word;
		
	}
	
	//Para ver si ya metí "x" parabra
	public boolean isHere(String word) {
		boolean is = false;
		int hashCode = hashCode(word);
		if(elements[hashCode]) {
			if(words[hashCode].equals(word)) {
				is = true;
			}else {
				//Esta parte es para las colicioens
				for(int i = 0; i < 100; i++) {
					hashCode++;
					if(hashCode >= 100) hashCode = 0;
					if(words[hashCode] != null && words[hashCode].equals(word)) {
						is = true;
						break;
					}
				}
			}
		}else {
			is = false;
		}
		return is;
	}
	
	//Método para vaciar todo lo que hay en el hash
	public void vaciar() {
		empty = true;
		for(int i = 0; i < 100; i++) elements[i] = false;
		for(int i = 0; i < 100; i++) words[i] = null;
	}
	
	//Para saber si está vacío el hash
	public boolean isEmpty() {
		return empty;
	}
}