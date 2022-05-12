package student;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/*
 * AddendumList.java
 * Trevor Mayo
 * 
 * Initial starting code by Prof. Boothe Aug. 2017
 *
 * To an external user an AddendumList appears as a single sorted list ordered by the comparator.
 * Duplicates are allowed, and new items with duplicate keys are added after any matching items.
 * 
 * Internally, at its simplest, an AddendumList is one big sorted array, but additions are added to a 
 * small secondary (addendum) array. Searching first checks the big array, and if a match is not found
 * it then checks the addendum array. Searching is fast because it can use binary search, and adding
 * is fast because adds are added into the small addendum array.
 * 
 * In fact there can be multiple levels of addendum arrays of exponentially decreasing sizes.
 * Searching works it's way through all of them. 
 * 
 * All additions are to the last array. When the last array becomes full, it is merged with the preceding
 * array if it is of equal or greater size. This merging might then continue up the chain to the top.
 * 
 * Invariant: The last array is never allowed to become full. When add causes it to become full, it is
 * possibly merged, and then a new empty minimum size array is added for future additions.
 *  
 * The implementation the AddendumList is stored internally as an array of arrays.
 * 
 * The top level array (called level 1) contains references to the 2nd level arrays.
 * 
 *
 * NOTE: normally fields, internal nested classes and non API methods should all be private,
 *       however some have been made public so that the tester code can set them
 */
@SuppressWarnings("unchecked")  // added to suppress warnings about all the type casting of Object arrays
public class AddendumList<E> implements Iterable<E> {
	private static final int L1_STARTING_SIZE = 4;
	private static final int L2_MINIMUM_SIZE = 4;   
	public int size;             // total number of elements stored
	public Object[] l1array;     // really is an array of L2Array, but compiler won't let me cast to that
	public int l1numUsed;
	private Comparator<E> comp;
	public int currentSize = L1_STARTING_SIZE;

	// create an empty list
	// always have at least 1 second level array even if empty, makes code easier 
	// (DONE)
	public AddendumList(Comparator<E> c){
		size = 0;
		l1array = new Object[L1_STARTING_SIZE];        // you can't create an array of a generic type
		l1array[0] = new L2Array(L2_MINIMUM_SIZE);    // first 2nd level array
		l1numUsed = 1;
		comp = c;
	}

	// nested class for 2nd level arrays
	// (DONE)
	public class L2Array {
		public E[] items;  
		public int numUsed;

		public L2Array(int capacity) {
			items = (E[])new Object[capacity];  // you can't create an array of a generic type
			numUsed = 0;
		}
	}

	//total size (number of entries) in the entire data structure
	// (DONE)
	public int size(){
		return size;
	}

	// null out all references so garbage collector can grab them
	// but keep the now empty l1array 
	// (DONE)
	public void clear(){
		size = 0;
		Arrays.fill(l1array, null);  // clear l1 array
		l1array[0] = new L2Array(L2_MINIMUM_SIZE);    // first 2nd level array
		l1numUsed = 1;
	}


	// Find the index of the 1st matching entry in the specified array.
	// If the item is NOT found, it returns a negative value (-(insertion point) - 1). 
	//   The insertion point is defined as the point at which the key would be inserted into the array,
	//   (which may be the index after the last item)
	public int findFirstInArray(E item, L2Array a){
		
		//binary search for the 'item'
		int binarySearch = Arrays.binarySearch(a.items, 0, a.numUsed, item, comp);
		int index = binarySearch;
		
		//if the return of binary search is greater than 0 loop to make sure it returns the first occurrence 
		if (index > 0)
		{
			for (int i = binarySearch-1; i >= 0; i--)
			{
				int compare = comp.compare(a.items[i], item); //a.items[i].equals(item); 
					if (compare == 0) 
					{
						index = i;
					}
					else
					{
						break;
					}
			}
		}

		return index;
	}
	
	public int findLastInArray(E item, L2Array a){
		
		//binary search for the 'item'
		int binarySearch = Arrays.binarySearch(a.items, 0, a.numUsed, item, comp);
		int index = binarySearch;
		
		//if the return of binary search is greater than 0 loop to make sure it returns the last occurrence 
		if (index > 0 && index < a.numUsed)
		{
			for (int i = binarySearch+1; i < a.numUsed; i++)
			{
				int compare = comp.compare(a.items[i], item); //a.items[i].equals(item); 
					if (compare == 0) 
					{
						index = i;
					}
					else
					{
						break;
					}
			}
		}

		return index;
	}


	/**
	 * check if list contains a match
	 * use the findFirstInArray() helper method
	 */
	public boolean contains(E item){
		
		//get the size of the L1Array
		int total = l1numUsed;
		
		//loop through the L1Array calling the findFirstInArray function
		for (int i = 0; i < total; i++)
		{
			L2Array arr = (L2Array)l1array[i];
			int index = findFirstInArray(item, arr);
			
			//if the return of findFirstInArray is greater than -1 then the it was found
			if (index > -1)
			{
				return true;
			}
		}
		
		return false;  // never found a match
	}

	
	// find the index of the insertion point of this item, which is
	// the index after any smaller or matching entries
	// this might be an unused slot at the end of the array
	// note: this will only be used on the last (very small) addendum array
	public int findIndexAfter(E item, L2Array a){
		
		//use the find last method to find the last occurrence of item. 
		int last = findLastInArray(item, a);
		
		//if the find function returns negative then flip the sign and subtract one
		if (last < 0)
		{
			last = (last * -1) - 1;
		}
		else //else add one to the find
		{
			last++;
		}
		
		return last;

	}

	/** 
	 * add object after any other matching values
	 * 
	 * adds to the last addendum array
	 * findIndexAfter() will give the insertion position
	 * if that array becomes full, you may need to invoke merging
	 * and then create a new empty final addendum array
	 * 
	 * remember to increment numUsed for the L2Array inserted into, and increment size for the whole data structure
	 */
	public boolean add(E item){
		
		//get the array to add to
		L2Array last = (L2Array) l1array[l1numUsed-1];
		int len = last.numUsed;
		int l2Size = 0;
		
		//get the length of the new temp l2array
		if (len < 4)
		{
			l2Size = 4;
		}
		else
		{
			l2Size = len + 1;
		}
		
		//find the right index of the item
		int find = findIndexAfter(item, last);
		
		//create the temp array
		L2Array temp = new L2Array(l2Size);
		
		//add everything from the first array to temp array
		for (int x = 0; x < len; x++)
		{
			temp.items[x] = last.items[x];
			temp.numUsed++;
		}
		
		//reset the counters and numUsed
		int counter = 0;
		temp.numUsed = 0;
		
		//add the new item and update the old items to the new location
		for (int i = 0; i <= len; i++)
		{
			if (i == find)
			{
				temp.items[i] = item;
				
			}
			else
			{
				temp.items[i] = last.items[counter];
				counter++;
			}
			temp.numUsed++;
		}		
		
		//replace the arrays and increment the size
		l1array[l1numUsed-1] = temp;
		size++;
		
		//if the array is full then merge the full arrays if they are the same size
		if (temp.numUsed >= 4 && l1numUsed >= 2)
		{
			int merged = 0;
			int used = temp.numUsed;
			int l1 = l1numUsed - 2;
			for (int i = l1; i >= 0; i--)
			{
				L2Array t = (L2Array) l1array[i];
				if (used >= t.numUsed)
				{
					merge1Level();
					merged = 1;
					used = used + t.numUsed;
				}
				else
					break;
			}
			
			//if the array is merged then create a new blank array at the end
			if (merged == 1 || temp.numUsed == 4)
			{
				if (l1numUsed >= currentSize && temp.numUsed == 4 && merged == 0)
				{
					l1numUsed++;
					currentSize++;
					Object[] l1Temp = new Object[currentSize];
					System.arraycopy(l1array, 0, l1Temp, 0, currentSize - 1);
					l1array = new Object[currentSize];
					System.arraycopy(l1Temp, 0, l1array, 0, currentSize);
					l1array[l1numUsed-1] = new L2Array(L2_MINIMUM_SIZE);
				}
				else
				{
					l1numUsed++;
					l1array[l1numUsed-1] = new L2Array(L2_MINIMUM_SIZE);
				}
			}
			
		}
		//if that array is full then create a new array blank array
		else if (temp.numUsed == 4)
		{
			l1numUsed++;
			l1array[l1numUsed-1] = new L2Array(L2_MINIMUM_SIZE);
		}
		
		//return
		return true;
	} 
	
	// merge the last two levels
		// if there are matching items, those from the earlier array go first in the merged array
		// note: this method does not add a new empty addendum array to the end, that will need to be done elsewhere 
		public void merge1Level() {
			
			if (l1numUsed > 1)
			{
				L2Array firstL2 = (L2Array) l1array[l1numUsed-2];
				int firstLen = firstL2.numUsed;
			
				L2Array secondL2 = (L2Array) l1array[l1numUsed-1];
				int secondLen = secondL2.numUsed;
			
				L2Array temp = new L2Array(firstLen + secondLen);
				temp.numUsed = 0;
				
				int firstPos = 0;
				int secondPos = 0;
				int newPos = 0;
				
				while (true)
				{
					if (firstPos < firstLen && secondPos < secondLen)
					{
						E firstItem = firstL2.items[firstPos];
						E secondItem = secondL2.items[secondPos];
						
						int compare = comp.compare(firstItem, secondItem);
						
						if (compare == 0)
						{
							temp.items[newPos] = firstItem;
							firstPos++;
							newPos++;
						}
						else if (compare < 0)
						{
							temp.items[newPos] = firstItem;
							firstPos++;
							newPos++;
						}
						else
						{
							temp.items[newPos] = secondItem;
							secondPos++;
							newPos++;
						}
						
					}
					else if (firstPos < firstLen && secondPos >= secondLen)
					{
						E firstItem = firstL2.items[firstPos];
						temp.items[newPos] = firstItem;
						firstPos++;
						newPos++;
					}
					else if  (firstPos >= firstLen && secondPos < secondLen)
					{
						E secondItem = secondL2.items[secondPos];
						temp.items[newPos] = secondItem;
						secondPos++;
						newPos++;
					}
					else
					
					{
						break;
					}
					temp.numUsed++;
				}
				
				/*if (currentSize > L1_STARTING_SIZE)
				{
					Object[] l1Temp = new Object[currentSize];
					System.arraycopy(l1array, 0, l1Temp, 0, currentSize);
					l1array = new Object[currentSize];
					System.arraycopy(l1Temp, 0, l1array, 0, currentSize);
					currentSize--;
				}*/
				l1array[l1numUsed-2] = temp;
				l1array[l1numUsed-1] = null;
				l1numUsed--;
				
			}
		}
	
	// merge all levels
	// this is used by iterator(), toArray() and subList()
	// this makes these easy to implement. and the O(N) full merge time would likely be required for these operations anyway
	// Note: after merging, all items will be in the first l2array, but there may still be a second empty level2 addendum array
	// at the end.
	// The result of the merging should still be a valid addendum array with room to add in the last addendum array.
	// The merging will likely cause the size of the array to no longer be a power of two.
	public void mergeAllLevels() {
		
		int len = l1numUsed;
		
		if (len > 1)
		{
			for(int i = 0; i < len; i++)
			{
				if (l1numUsed == 1)
				{
					break;
				}
				else
				{
					merge1Level();
				}
			}
		}
	}

	/**
	 * copy the contents of the AddendumList into the specified array
	 * @param a - an array of the actual type and of the correct size
	 * @return the filled in array
	 */
	public E[] toArray(E[] a){
		
		AddendumList<E> newList = new AddendumList<E>(comp);
		System.arraycopy(this.l1array, 0, newList.l1array, 0, currentSize);
		newList.l1numUsed = this.l1numUsed;
		
		newList.mergeAllLevels();
		
		L2Array fullL2 = (L2Array) newList.l1array[0];
		int len = fullL2.numUsed;
		
		for(int i = 0; i < len; i++)
		{
			E temp = fullL2.items[i];
			a[i] = temp;
		}
		
		return a;
	}

	/**
	 * returns a new independent AddendumList 
	 * whose elements range from fromElemnt, inclusive, to toElement, exclusive
	 * The original list is unaffected.
	 * findFirstIndexOf() will be useful.
	 * @param fromElement
	 * @param toElement
	 * @return the sublist
	 */
	public AddendumList<E> subList(E fromElement, E toElement){
		
		AddendumList<E> newList = new AddendumList<E>(comp);
		System.arraycopy(this.l1array, 0, newList.l1array, 0, l1numUsed-1);
		newList.l1numUsed = this.l1numUsed-1;
		
		newList.mergeAllLevels();
		
		L2Array fullL2 = (L2Array) newList.l1array[0];
		int len = fullL2.numUsed;
		
		int start = findFirstInArray(fromElement, fullL2);
		int end = findFirstInArray(toElement, fullL2);
		
		if (start < 0)
		{
			start = start * -1 - 1;
		}
		
		if (end < 0)
		{
			end = end * -1 - 1;
		}
		
		if (start == end)
		{
			end++;
		}
		
		int newCount = end - start;
		L2Array temp = new L2Array(newCount);
		temp.numUsed = 0;
		
		int i = 0;
		for (;;)
		{
			temp.items[i] = fullL2.items[start];
			i++;
			temp.numUsed++;
			start++;
			
			if (start == end)
			{
				break;
			}
				
		}
		
		newList.l1array[0] = temp;
		newList.l1numUsed = 1;
		newList.size = newCount;
		
		return newList;
	}

	/**
	 * returns an iterator for this list
	 * this method just merges the items into a single array and creates an instance of the inner Itr() class
	 * (DONE)   
	 */
	public Iterator<E> iterator() {
		mergeAllLevels();
		return new Itr();
	}

	/**
	 * Iterator 
	 */
	private class Itr implements Iterator<E> {
		int index = 0;

		/*
		 * create iterator at start of list
		 */
		Itr(){
			

		}

		/**
		 * check if more items
		 */
		public boolean hasNext() {
			
			L2Array arr = (L2Array) l1array[0];
			int len = arr.numUsed;
			
			if (index < len)
			{
				return true;
			}
			
			return false;
		}

		/**
		 * return item and move to next
		 * throws NoSuchElementException if off end of list
		 */
		public E next() {

			if(!this.hasNext()) 
			{
				try {
					throw new Exception();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			L2Array arr = (L2Array) l1array[0];
			
			return arr.items[index++];
		}

		/**
		 * Remove is not implemented. Just use this code.
		 * (DONE)
		 */
		public void remove() {
			throw new UnsupportedOperationException();	
		}
	}
}
