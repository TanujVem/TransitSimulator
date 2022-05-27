package transit;

import java.util.ArrayList;

public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	public Transit() { trainZero = null; }

	public Transit(TNode tz) { trainZero = tz; }
	

	public TNode getTrainZero () {
		return trainZero;
	}

	public void makeList(int[] trainStations, int[] busStops, int[] locations) {
		trainZero = new TNode(0, null, null);
		TNode ptr = trainZero;
		for(int i = 0; i<trainStations.length; i++){
			ptr.setNext(new TNode(trainStations[i],null, null)); 
			ptr = ptr.getNext();
			};	
		ptr = trainZero;
		ptr.setDown(new TNode(0,null,null));
		TNode busZero = trainZero.getDown();
		TNode ptr2 = ptr.getDown();
		ptr = ptr.getNext();
		for(int j = 0; j<busStops.length; j++){
			ptr2.setNext(new TNode(busStops[j],null,null));
			ptr2 = ptr2.getNext();
			if(ptr != null && ptr.getLocation() == ptr2.getLocation()){
				ptr.setDown(ptr2);
				ptr = ptr.getNext();
			}
		}
		busZero.setDown(new TNode(0,null,null));
		TNode walkZero = busZero.getDown();
		ptr2 = busZero;
		TNode ptr3 = walkZero;
		ptr2 = ptr2.getNext();
		for(int k = 0; k<locations.length; k++){
			ptr3.setNext(new TNode(locations[k],null,null));
			ptr3 = ptr3.getNext();
			if(ptr2 != null && ptr2.getLocation() == ptr3.getLocation()){
				ptr2.setDown(ptr3);
				ptr2 = ptr2.getNext();
			}
		}

		
		}
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {
		TNode ptr = trainZero;
		while(ptr.getNext() != null){
			if(ptr.getNext().getLocation() == station){
				ptr.setNext(ptr.getNext().getNext());
			}
			if(ptr.getNext()!= null){
			ptr = ptr.getNext();
			}
		}
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
		TNode ptr = trainZero.getDown();
		while(ptr.getNext() != null){
			if(ptr.getLocation() < busStop && ptr.getNext().getLocation()>busStop){
				TNode temp = ptr.getNext();
				ptr.setNext(new TNode(busStop, temp, null));
				TNode location = trainZero.getDown().getDown();
				while(location.getNext() != null){
					if(location.getNext().getLocation() == busStop){
						ptr.getNext().setDown(location.getNext());
					}
					location = location.getNext();
				}
			}
		ptr = ptr.getNext();
		}
		
	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {
		ArrayList<TNode> path = new ArrayList<TNode>();
		TNode ptr = trainZero;
		path.add(ptr);
		while(ptr.getNext() != null && ptr.getNext().getLocation() <= destination){
			ptr = ptr.getNext();
			path.add(ptr);
		}
		ptr = ptr.getDown();
		path.add(ptr);
		while(ptr.getNext() != null && ptr.getNext().getLocation() <= destination){
			ptr = ptr.getNext();
			path.add(ptr);
		}
		ptr = ptr.getDown();
		path.add(ptr);
		while(ptr.getNext() != null){
			if(ptr.getNext().getLocation() <=  destination){
				path.add(ptr.getNext());
			}
			ptr = ptr.getNext();
		}
	    return path;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {

		TNode newZero = new TNode(0, null, null);
		TNode ptr = trainZero;
		TNode newPtr = newZero;
		TNode temp;

		//duplicate train
		while(ptr.getNext() != null){
			temp = new TNode(ptr.getNext().getLocation(), ptr.getNext().getNext(), ptr.getNext().getDown());
			newPtr.setNext(temp);
			ptr = ptr.getNext();
			newPtr = newPtr.getNext();
		}

		//duplicate first bus
		ptr = trainZero.getDown();
		temp = new TNode(ptr.getLocation(), ptr.getNext(), ptr.getDown());
		newPtr = newZero;
		newPtr.setDown(temp);
		newPtr = newPtr.getDown();

		//duplicate bus row
		while(ptr.getNext() != null){
			temp = new TNode(ptr.getNext().getLocation(), ptr.getNext().getNext(), ptr.getNext().getDown());
			newPtr.setNext(temp);
			ptr = ptr.getNext();
			newPtr = newPtr.getNext();
		}

		//duplicate first walk
		ptr = trainZero.getDown().getDown();
		temp = new TNode(ptr.getLocation(), ptr.getNext(), ptr.getDown());
		newPtr = newZero.getDown();
		newPtr.setDown(temp);
		newPtr = newPtr.getDown();

		//duplicate walk row
		while(ptr.getNext() != null){
			temp = new TNode(ptr.getNext().getLocation(), ptr.getNext().getNext(), ptr.getNext().getDown());
			newPtr.setNext(temp);
			ptr = ptr.getNext();
			newPtr = newPtr.getNext();
		}

		newPtr = newZero;
		TNode lowerPtr = newPtr.getDown();
		while(newPtr.getNext() != null){
			while(newPtr.getNext().getLocation() != lowerPtr.getNext().getLocation()){
				lowerPtr = lowerPtr.getNext();
			}
			if(newPtr.getNext().getLocation() == lowerPtr.getNext().getLocation()){
				newPtr.getNext().setDown(lowerPtr.getNext());
			}
			newPtr = newPtr.getNext();
		}

		newPtr = newZero.getDown();
		lowerPtr = newPtr.getDown();
		while(newPtr.getNext() != null){
			while(newPtr.getNext().getLocation() != lowerPtr.getNext().getLocation()){
				lowerPtr = lowerPtr.getNext();
			}
			if(newPtr.getNext().getLocation() == lowerPtr.getNext().getLocation()){
				newPtr.getNext().setDown(lowerPtr.getNext());
			}
			newPtr = newPtr.getNext();
		}

	    return newZero;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {
		TNode scooterStart = new TNode(0);
		TNode scooterPtr = scooterStart;
		for(int i = 0; i<scooterStops.length; i++){
			scooterPtr.setNext(new TNode(scooterStops[i]));
			scooterPtr = scooterPtr.getNext();
		}
		scooterPtr = scooterStart;
		TNode busPtr = trainZero.getDown();
		while(busPtr.getNext() != null){
			while(scooterPtr.getNext() != null){
				if(busPtr.getNext().getLocation() == scooterPtr.getNext().getLocation()){
					busPtr.getNext().setDown(scooterPtr.getNext());
				}
				scooterPtr = scooterPtr.getNext();
			}
		scooterPtr = scooterStart;
		busPtr = busPtr.getNext();
		}
		TNode busStart = trainZero.getDown();
		scooterPtr = scooterStart;
		TNode temp = trainZero.getDown().getDown();
		busStart.setDown(scooterStart);
		scooterStart.setDown(temp);
		TNode walk = temp;
		//connects scooter to walk
		while(scooterPtr.getNext() != null){
			while(walk.getNext() != null){
				if(scooterPtr.getNext().getLocation() == walk.getNext().getLocation()){
					scooterPtr.getNext().setDown(walk.getNext());
				}
				walk = walk.getNext();
			}
		walk = temp;
		scooterPtr = scooterPtr.getNext();
		}
	}

	/**
	 * Used by the driver to display the layered linked list. 
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
