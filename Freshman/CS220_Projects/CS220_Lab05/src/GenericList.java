/* Implement list interface
 * Author : Noah Quintero, Kala Arentz
 * Last Modifed : 4.22.15
 */
public class GenericList<E> implements List<E>{

	protected int size=0;
	protected Node<E> head;

	public GenericList(){
		head=null;
	}
	
	@Override
	public void add(E item, int pos) {
		pos-=1;
		Node<E> newNode=new Node<E>(item);
		Node<E> cursor;
		cursor=getNodeAt(pos);
		
		if(head==null){
			head=newNode;
			size++;
			return;
		}
		
		cursor=getNodeAt(pos);
		
		//set next of newNode to be cursor
		newNode.setNext(cursor);
			
		//set previous of newNode's next to be newNode
		try{
			newNode.getNext().setPrev(newNode);
		}
		catch(NullPointerException n){}
		
		//set the previous of newNode to be
		//the node previous of cursor
		try{
			newNode.setPrev(cursor.getPrev());
		}
		catch(NullPointerException n){
			cursor=getNodeAt(pos-1);
			newNode.setPrev(cursor);
		}
		
		//set newNode's previous' next to be newNode
		newNode.getPrev().setNext(newNode);
		
		size++;
	}

	@Override
	public E get(int pos) {
		Node<E> cursor=head;
		for (int i=0; i<pos; i++){
			cursor=cursor.getNext();
		}
		return (E) cursor.getItem();
	}

	@Override
	public E remove(int pos) {		
		pos-=1;
		Node<E> removed=getNodeAt(pos);
		if(removed==head){
			head=head.getNext();
		}
		else{
			//Try to set "next" of the previous node 
			//to the next of the removed node;
			try{
				removed.getPrev().setNext(removed.getNext());
			}
			//This shouldn't happen, but if something should
			//be null, the next of previous will be made null;
			catch(NullPointerException e){
				removed.getPrev().setNext(null);
			}
			
			//Now try to set the previous of next to be
			//the node previous the removed node
			try{
				removed.getNext().setPrev(removed.getPrev());
			}
			catch(NullPointerException e){
			//	
			}
		}
		return removed.getItem();
	}

	@Override
	public int size() {		
		return size;
	}
	
	public Node<E> getHead(){
		return head;
	}
	
	private Node<E> getNodeAt(int pos){
		Node<E> cursor=head;
		pos-=1;
		while(pos>=0){
			cursor=cursor.getNext();
			pos--;
		}
		return cursor;
	}
	
	
	public String toString(){
		String listString="{ ";
		
		Node<E> cursor = head;
		while(cursor!=null){
			listString +=cursor.toString() + ", ";
			cursor=cursor.getNext();
		}
		listString+="}";
		
		return listString;
	}
	
	protected class Node<E>{
		
		protected E item;
		protected Node<E> next;		
		protected Node<E> prev;
		
		public Node(E item){
			this.item=item;
			next=null;
		}
		
		public E getItem(){
			return item;
		}
		
		public Node<E> getNext(){
			return next;
		}
		
		public void setNext(Node<E> n){
			next=n;			
		}
		
		public Node<E> getPrev(){
			return prev;
		}
		
		public void setPrev(Node<E> n){
			prev=n;
		}
		
		public String toString(){
			return item.toString();
		}
			
	}
	//Testing code is commented out.
	
	public static void main(String[] args){
		
		GenericList<Integer> list = new GenericList<>();
		for (int i=1;i<11;i++){
			list.add(i, i);
			System.out.println(list);
		}
		
		System.out.println();
		//Like arrays, the length is n, but the last index is n-1
		//so to go backwards we need to start from the final index
		for (int i=list.size(); i>5; i--){
			list.remove(i);
			System.out.println(list);
			
		}
		
		list.remove(1);
		
		System.out.println();
		System.out.println(list);
		
		System.out.println();
		list.remove(2);
		System.out.println(list);
		
	}
	
	

}
