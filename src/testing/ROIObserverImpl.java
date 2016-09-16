package testing;

import java.util.ArrayList;

import a6.*;

public class ROIObserverImpl implements ROIObserver {

	private String name;
	private ArrayList<PictureRegion> notifications = new ArrayList<PictureRegion>();
	
	public ROIObserverImpl(String name){
		this.name = name;
	}
	
	@Override
	public void notify(ObservablePicture picture, Region changed_region) {
		notifications.add(new PictureRegion(picture, changed_region));
	}
	
	public ArrayList<PictureRegion> getNotifications(){
		return notifications;
	}
	
	public Region get(int index){
		return notifications.get(index).getRegion();
	}
	
	public String getName(){
		return name;
	}

}
