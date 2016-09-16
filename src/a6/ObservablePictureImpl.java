package a6;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObservablePictureImpl extends AnyPicture implements ObservablePicture {
	private Picture p;
	private boolean suspend;
	private Region container; //collection of notifications 
	
	List<ROIObserverDecorator> observerList = new ArrayList<> ();
	
	public ObservablePictureImpl(Picture p){ //constructor
		this.p = p;
	}
	
	@Override
	public int getWidth() {
		return p.getWidth();
	}

	@Override
	public int getHeight() {
		return p.getHeight(); 
	}

	@Override
	public Pixel getPixel(int x, int y) {
		return p.getPixel(x,y); 
	}

	@Override
	public Pixel getPixel(Coordinate c) {
		return p.getPixel(c);
	}

	@Override
	public void setPixel(int x, int y, Pixel p) {
		this.p.setPixel(x,y,p);
		Region newRegion = new RegionImpl (new Coordinate(x,y), new Coordinate(x,y)); //setting new region
		
		if (suspend) {
			container = newRegion.union(container); //collecting to put into container if suspended 
		}
		else notifyObservers(newRegion); //if not suspended, resume normal notification 
	}

	public void notifyObservers(Region r) { //method for notifications
		
		for (int x=0; x<observerList.size(); x++){
			try {
				Region regionObserver = observerList.get(x).getRegion();
				Region intersection	= regionObserver.intersect(r);
				observerList.get(x).notify(this, intersection);
				}
			catch (NoIntersectionException e) {
			}
		}
	}
	
	@Override
	public void registerROIObserver(ROIObserver observer, Region r) { //adding observers
		observerList.add(new ROIObserverDecoratorImpl(observer, r));	
	}

	@Override
	public void unregisterROIObservers(Region r) { //removing all observers based off of region
		Iterator<ROIObserverDecorator> iterate = observerList.iterator();
		while (iterate.hasNext()) {
			ROIObserverDecorator obs = iterate.next();
			try {
			obs.getRegion().intersect(r);
			iterate.remove();
			} 
			catch (NoIntersectionException e) {
			}
		}
	}

	@Override
	public void unregisterROIObserver(ROIObserver observer) { //removing all observers
		Iterator<ROIObserverDecorator> iterate = observerList.iterator();
		while (iterate.hasNext()){
			ROIObserverDecorator obs = iterate.next();
			if (obs.getObserver() == observer){
				iterate.remove();
			}
		}
	}

	@Override
	public ROIObserver[] findROIObservers(Region r) { //finding observers 
		List<ROIObserver> intersectList = new ArrayList<ROIObserver>();
		for (int x = 0; x<observerList.size(); x++){
			try {
				observerList.get(x).getRegion().intersect(r);
				intersectList.add(observerList.get(x).getObserver());
			}
			catch (NoIntersectionException b){
				continue;
			}	
		}
		
		ROIObserver[] array = new ROIObserver[intersectList.size()];
		
		for (int i = 0; i<intersectList.size(); i++) {
			array[i] = intersectList.get(i);
		}
		return array;
	}

	@Override
	public void suspendObservable() {
		this.suspend = true; //notifications are turned off
	}

	@Override
	public void resumeObservable() {
		this.suspend = false; //notifications on 
		notifyObservers(container);
		container = null;
	}
}
