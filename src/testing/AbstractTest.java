package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import a6.*;

public abstract class AbstractTest {

	protected ROIObserverImpl[] observerArray;

	protected void printNotifications(){
		int count = 0;
		for(ROIObserverImpl observer : observerArray){
			for(PictureRegion picReg : observer.getNotifications()){
				System.out.println("Observer:                     " + observer.getName());
				System.out.println("ObservablePicture Reference:  " + picReg.getObservablePicture());
				System.out.println("                  Dimensions: " + 
						picReg.getObservablePicture().getWidth() + " by " + 
						picReg.getObservablePicture().getHeight());
				System.out.println("changed_region:               (" + 
						picReg.getRegion().getUpperLeft().getX() + "," + 
						picReg.getRegion().getUpperLeft().getY() + ") to (" +
						picReg.getRegion().getLowerRight().getX() + "," +
						picReg.getRegion().getLowerRight().getY() + ")");
				System.out.println();
				count++;
			}
		}
		System.out.println("Notifications: "+ count);
	}
	
	protected boolean equalRegions(Region a, Region b){
		assertTrue(equalCoordinates(a.getUpperLeft(), b.getUpperLeft()));
		assertTrue(equalCoordinates(a.getLowerRight(), b.getLowerRight()));
		return true;
	}
	
	protected boolean equalCoordinates(Coordinate a, Coordinate b){
		assertEquals(a.getX(), b.getX());
		assertEquals(a.getY(), b.getY());
		return true;
	}
	
	protected Region Region(int a, int b, int c, int d){
		return new RegionImpl(new Coordinate(a,b), new Coordinate(c,d));
	}


}
