package boogle;

public class TrieStandard {
	
	private TrieNodo raiz;
	private int index;
	
	//Constructor vacío
	public TrieStandard(){
		raiz = new TrieNodo();
	}
	
	//Este método agrega una nueva palabra al trie
	public void setPalabra(String palabra) {
		
		TrieNodo nodo = buscar(palabra);
		
		for(int i = index; i < palabra.length(); i++) {
			nodo.setNodo(palabra.charAt(i));
			nodo = nodo.getNodo(palabra.charAt(i));
		}
		
		nodo.setFin(true);
	}
	
	//Método para buscar un nodo y regrsar el último nodo con la letra en común
	private TrieNodo buscar(String palabra) {
		
		index = 0;
		TrieNodo nodo = raiz;
		
		for(int i = 0; i < palabra.length(); i++) {
			if(nodo.getNodo(palabra.charAt(i)) != null) {
				nodo = nodo.getNodo(palabra.charAt(i));
				index++;
			}
			else break;
		}
		
		return nodo;
	}
	
	//Método para ver si una palabra existe
	public boolean existe(String palabra) {
		
		boolean existe = false;
		TrieNodo nodo = raiz;
		
		//Vamos letra por letra de cada nodo
		for(int i = 0; i < palabra.length(); i++) {
			if(nodo.getNodo(palabra.charAt(i)) != null) {
				nodo = nodo.getNodo(palabra.charAt(i));
				if(i == palabra.length()-1 && nodo.isFin())existe = true;
			}
			else {
				break;
			}
		}
		
		return existe;
	}
	
	//Metodo para vaciar el trie (Que el "Garbage Collector" se encarge del resto)
	public void vaciar() {
		raiz = new TrieNodo();
	}
}