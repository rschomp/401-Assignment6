package a6;

public interface ROIObserverDecorator extends ROIObserver{
	public Region getRegion();
	public ROIObserver getObserver();
	
	
}
