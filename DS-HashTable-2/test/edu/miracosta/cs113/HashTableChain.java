package edu.miracosta.cs113;
import java.util.*;

/**
 * HashTable implementation using chaining to tack a pair of key and value pairs.
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class HashTableChain<K, V> implements Map<K, V>  {

    private LinkedList<Entry<K, V>>[] table;
    private  int numKeys;
    private static final int CAPACITY = 101;
    private static final double LOAD_THRESHOLD = 1.5;

    ///////////// ENTRY CLASS ///////////////////////////////////////

    /**
     * Contains key-value pairs for HashTable
     * @param <K> the key
     * @param <V> the value
     */
    private static class Entry<K, V> implements Map.Entry<K, V>{
    	/** The key */
    	private K key;
    	/** The value */
        private V value;

        /**
         * Creates a new key-value pair
         * @param key the key
         * @param value the value
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Returns the key
         * @return the key
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value
         * @return the value
         */
        public V getValue() {
            return value;
        }

        /**
         * Sets the value
         * @param val the new value
         * @return the old value
         */
        public V setValue(V val) {
            V oldVal = value;
            value = val ;
            return oldVal ;
        }
        @Override
        public String toString() {
            return  key + "=" + value  ;
        }



    }

    ////////////// end Entry Class /////////////////////////////////

    ////////////// EntrySet Class //////////////////////////////////

    /**
     * Inner class to implement set view
     */
    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {


        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new SetIterator();
        }

        @Override
        public int size() {
            return numKeys;
        }
        
        @Override
        public String toString()
        {
        	LinkedList<HashTableChain.Entry<K, V>> ess = new LinkedList<>();
        	for(int i = 0; i < table.length; i++)
        	{
    			if(table[i] != null)
    			{
    	    		for(int j = 0; j < table[i].size(); j++)
    	    		{
    	    				ess.add(table[i].get(j));        		
    	    		}
    			}
        	}
        	return ess.toString();
        }
    }

    ////////////// end EntrySet Class //////////////////////////////

    //////////////   SetIterator Class ////////////////////////////

    /**
     * Class that iterates over the table. Index is table location
     * and lastItemReturned is entry
     */
    private class SetIterator implements Iterator<Map.Entry<K, V>> {

        private int index = 0 ;
        private Entry<K,V> lastItemReturned = null;
        private Iterator<Entry<K, V>> iter = null;

        @Override
        public boolean hasNext() {
        	if(iter != null)
        	{
        		return iter.next() != null;
        	}
        	return false;
        }

        @Override
        public Map.Entry<K, V> next() {
        	if(!hasNext())
        	{
        		return null;
        	}
        	return iter.next();
        }

        @Override
        public void remove() {
        	iter.remove();
        }	
        
    }

    ////////////// end SetIterator Class ////////////////////////////

    /**
     * Default constructor, sets the table to initial capacity size
     */
    public HashTableChain() {
        table = new LinkedList[CAPACITY];
    }

    // returns number of keys
    @Override
    public int size() {
        return numKeys;
    }

    // returns boolean if table has no keys
    @Override
    public boolean isEmpty() {
    	return numKeys == 0;
    }

    // returns boolean if table has the searched for key
    @Override
    public boolean containsKey(Object key) {
    	System.out.println("Entry Set toString(debug): " + new EntrySet().toString());
    	// Use a loop to iterate through the table
    	for(int i = 0; i < table.length; i++)
    	{
			if(table[i] != null)
			{
	    		for(Entry<K, V> nextItem : table[i])
	    		{
		        	// In the loop, check whether the key matches the current 
		        	// Check all data sets within the table
		    		if(nextItem != null && nextItem.getKey().equals(key))
		    		{
		    			return true;
		    		}
	    		}
			}
    	}
    	return false;
    }

    // returns boolean if table has the searched for value
    @Override
    public boolean containsValue(Object value) {
    	// Use a loop to iterate through the table
    	for(int i = 0; i < table.length; i++)
    	{
			if(table[i] != null)
			{
	    		for(Entry<K, V> nextItem : table[i])
	    		{
		        	// In the loop, check whether the key matches the current 
		        	// Check all data sets within the table
		    		if(nextItem.getValue() == value)
		    		{
		    			return true;
		    		}
	    		}
			}
    	}
    	return false;
    }

    // returns Value if table has the searched for key
    @Override
    public V get(Object key) {
    	// Set index to key.hashCode() % table.length;
    	int index = key.hashCode() % table.length;
    	// If index is negative
    	if(index < 0)
    	{
    		// Add table.length
    		index += table.length;
    	}
    	// If the table value at the given index is null
    	if(table[index] == null)
    	{
    		// Key is not in table
    		System.out.println("NULL INDEX");
    		return null;
    	}
    	for(Entry<K, V> e: table[index])
    	{
    		if(e.getKey().equals(key))
    		{
    			return e.getValue();
    		}
    	}
    	// assert: key is not in the table.
    	return null;
    }

    // adds the key and value pair to the table using hashing
    @Override
    public V put(K key, V value) {
    	// Set index to key.hashCode() % table.length;
    	int index = key.hashCode() % table.length;
    	// If index is negative
    	if(index < 0)
    	{
    		// Add table.length
    		index += table.length;
    	}
    	// If the table value at the given index is null
    	if(table[index] == null)
    	{
    		// Create a new linked list at table[index].
    		table[index] = new LinkedList<Entry<K, V>>();
    	}
    	
    	// Search the list at table[index] to find the key.
    	for(Entry<K, V> nextItem : table[index])
    	{
    		// If the search is successful, replace the old value.
    		if(nextItem.key.equals(key))
    		{
    			// Replace value for this key.
    			V oldVal = nextItem.value;
    			nextItem.setValue(value);
    			return oldVal;
    		}
    	}
    	// assert: key is not in the table, add new item.
    	table[index].addFirst(new Entry<K, V>(key, value));
    	numKeys++;
    	if(numKeys > (LOAD_THRESHOLD * table.length))
    	{
    		rehash();
    	}
    	return null;
    	
    }


    /**
     * Resizes the table to be 2X +1 bigger than previous
     */
    private void rehash() {
    	
    	// Allocate a new hash table with a capacity equal to twice the 
    	// number of spaces of the original hash table plus one.
    	// Reinsert each old table entry that has not been deleted 
    	// into the new hash table.
    	// Reference the new table instead of the original.
    	
    	LinkedList<Entry<K, V>>[] old = table;
    	int real = (old.length * 2) + 1;
    	table = new LinkedList[real];
    	numKeys = 0;
    	for(int i = 0; i < old.length; i++)
    	{
			if(old[i] != null)
			{
	    		for(int j = 0; j < old[i].size(); j++)
	    		{
	    			put(old[i].get(j).getKey(), old[i].get(j).getValue());
	    		}
			}
    	}    	
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder() ;
        for (int i = 0 ; i < table.length ; i++ ) {
            if (table[i] != null) {
                for (Entry<K, V> nextItem : table[i]) {
                    sb.append(nextItem.toString() + " ") ;
                }
                sb.append(" ");
            }
        }
        return sb.toString();

    }

    // remove an entry at the key location
    // return removed value
    @Override
    public V remove(Object key) {
    	// Set index to key.hashCode() % table.length;
    	int index = key.hashCode() % table.length;
    	// If index is negative
    	if(index < 0)
    	{
    		// Add table.length
    		index += table.length;
    	}
    	// If the table value at the given index is null
    	if(table[index] == null)
    	{
    		// Key is not in the table
    		return null;
    	}
    	// Search the list at table[index] to find the key.
    	for(Entry<K, V> nextItem : table[index])
    	{
    		// If the search is successful
    		if(nextItem.key.equals(key))
    		{
    			// Remove the entry with this key.
    			table[index].remove(nextItem);
    			// Decrement numKeys.
    			numKeys--;
    			// if the list at table[index] is empty
    			if(table[index].isEmpty())
    			{
    				// Set table[index] to null.
    				table[index] = null;    				
    			}
    	    	// Return the value associated with this key.
    		}
	    	// The key is not in the table; return null.
    	}
    	return null;
    }

    // throws UnsupportedOperationException
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException() ;
    }

    // empties the table
    @Override
    public void clear() {
    	table = null;
    	numKeys = 0;
    }

    // returns a view of the keys in set view
    @Override
    public Set<K> keySet() {
    	Set<K> keys = new HashSet<K>();
    	for(int i = 0; i < table.length; i++)
    	{
			if(table[i] != null)
			{
	    		for(int j = 0; j < table[i].size(); j++)
	    		{
	    			K gets = table[i].get(j).getKey();
	    			if(gets != null)
	    			{
	    				keys.add(gets);
	    			}
	    		}
			}
    	}
    	return keys;
    }

    // throws UnsupportedOperationException
    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException() ;
    }


    // returns a set view of the hash table
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
    	EntrySet load = new EntrySet();
    	SetIterator si = new SetIterator();
    	if(si.hasNext())
    	{

    	}
    	return load;
    }

    @Override
    public boolean equals(Object o) {
        // Check if this(super class calling) is an object, object o
    	if (this == o) {
          return true;
        }
        // Check if the entry is an instance of a map
        if (!(o instanceof Map)) {
          return false;
        }
        // Downcast to Map
        Map other = (Map) o;
        // Compare one keyset to the other keyset
        
        return keySet().equals(other.keySet());
      } 


    @Override
    public int hashCode() {
    	return keySet().hashCode() + 1;
    }
}
