package oop;

public class Shingle {
	//Variables
	private int docId;
	private int hashCode;
	
	//Constructors
	public Shingle() {}
	
	public Shingle(int docId, int hashCode) {
		super();
		this.docId = docId;
		this.hashCode = hashCode;
	}
	
	//Get and set for docId
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	
	//Get and set for hashCode
	public int getHashCode() {
		return hashCode;
	}
	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}
	
}//Shingle

