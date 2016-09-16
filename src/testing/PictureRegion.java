package testing;

import a6.*;

public class PictureRegion {
	ObservablePicture p;
	Region r;
	
	public PictureRegion(ObservablePicture p, Region r){
		this.p = p;
		this.r = r;
	}
	
	public ObservablePicture getObservablePicture(){
		return p;
	}
	
	public Region getRegion(){
		return r;
	}
}
