package boogle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class HashScore implements Serializable{
	
	private boolean elements[];
	private String names[];
	private int scores[];
	private boolean empty;
	
	//Constructor
	public HashScore() {
		empty = true;
		elements = new boolean[100];
		for(int i = 0; i < 100; i++) elements[i] = false;
		names = new String[100];
		scores = new int[100];
	}
	
	//Método para sacar el hash code
	private int hashCode(String word) {
		int code = 0;
		for(int i = 0; i < word.length(); i++) code += ((int) word.charAt(i))*((i+5)*13);
		code %= 100;
		return code;
	}
	
	//Para meter palabras en el hash
	public void put(String word,int score) {
		empty = false;
		int hashCode = hashCode(word);
		while(elements[hashCode]) {
			hashCode++;
			if(hashCode >= 100) hashCode = 0;
		}
		elements[hashCode] = true;
		names[hashCode] = word;
		scores[hashCode] = score;
		
	}
	
	//Para ver si ya metí "x" parabra
	public boolean isHere(String word) {
		boolean is = false;
		int hashCode = hashCode(word);
		if(elements[hashCode]) {
			if(names[hashCode].equals(word)) {
				is = true;
			}else {
				//Esta parte es para las colicioens
				for(int i = 0; i < 100; i++) {
					hashCode++;
					if(hashCode >= 100) hashCode = 0;
					if(names[hashCode] != null && names[hashCode].equals(word)) {
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
		for(int i = 0; i < 100; i++) names[i] = null;
	}
	
	//Para saber si está vacío el hash
	public boolean isEmpty() {
		return empty;
	}
	
	//Para 
	public String getNames() {
		
		String info = "";
		
		for(int i = 0; i < 100; i++) {
			if(elements[i]) info += "> "+scores[i]+" - "+names[i]+"\n";
		}
		
		return info;
	}
}