package a6;

public class ROIObserverDecoratorImpl implements ROIObserverDecorator{
	private ROIObserver observer;
	private Region region;
	
	public ROIObserverDecoratorImpl (ROIObserver observer, Region region) {
		this.observer = observer;
		this.region = region;
	}
	
	@Override
	public void notify(ObservablePicture picture, Region changed_region) {
		observer.notify(picture, changed_region);	
	}

	@Override
	public Region getRegion() {
		return region;
	}

	@Override
	public ROIObserver getObserver() {
		return observer;
	}
}
