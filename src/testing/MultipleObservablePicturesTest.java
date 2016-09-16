package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import a6.*;

public class MultipleObservablePicturesTest extends AbstractTest{
	
	private ObservablePicture p, q, r;
	private ROIObserverImpl a, b, c, d, e;

	@Before
	public void setUp() throws Exception {
		p = new ObservablePictureImpl(new PictureImpl(10,10));
		q = new ObservablePictureImpl(new PictureImpl(11,15));
		r = new ObservablePictureImpl(new PictureImpl(14,17));
		
		a = new ROIObserverImpl("A");
		b = new ROIObserverImpl("B");
		c = new ROIObserverImpl("C");
		d = new ROIObserverImpl("D");
		e = new ROIObserverImpl("E");
		
		observerArray = new ROIObserverImpl[]{a, b, c, d, e};

		p.registerROIObserver(a, Region(1,1,5,4));
		p.registerROIObserver(a, Region(3,3,5,5));
		p.registerROIObserver(b, Region(1,5,2,9));
		p.registerROIObserver(c, Region(0,0,10,3));
		p.registerROIObserver(c, Region(8,0,10,10));
		p.registerROIObserver(d, Region(5,5,10,10));
		p.registerROIObserver(e, Region(0,0,10,10));

		q.registerROIObserver(a, Region(2,2,11,15));
		q.registerROIObserver(a, Region(11,3,5,5));
		q.registerROIObserver(b, Region(1,5,12,9));
		q.registerROIObserver(c, Region(1,4,10,3));
		q.registerROIObserver(c, Region(8,0,10,10));
		q.registerROIObserver(d, Region(5,5,11,11));
		q.registerROIObserver(e, Region(0,0,11,15));

		r.registerROIObserver(a, Region(1,2,5,4));
		r.registerROIObserver(b, Region(4,3,5,5));
		r.registerROIObserver(b, Region(1,5,2,9));
		r.registerROIObserver(c, Region(0,0,11,3));
		r.registerROIObserver(c, Region(4,0,10,10));
		r.registerROIObserver(d, Region(6,6,14,14));
		r.registerROIObserver(e, Region(0,0,14,17));
	}
	
	
	@Test
	public void testSinglePixel() {
		p.setPixel(9,9, new GrayPixel(0.2));
		q.setPixel(9,9, new GrayPixel(0.2));
		r.setPixel(9,9, new GrayPixel(0.2));

		assertEquals(1, a.getNotifications().size());
		assertEquals(1, b.getNotifications().size());
		assertEquals(3, c.getNotifications().size());
		assertEquals(3, d.getNotifications().size());
		assertEquals(3, e.getNotifications().size());
		
		assertEquals(q, a.getNotifications().get(0).getObservablePicture());
		assertEquals(p, c.getNotifications().get(0).getObservablePicture());
		assertEquals(r, c.getNotifications().get(2).getObservablePicture());
		
		//The nested for-each loop below verifies every notification is of Region(9,9)->(9,9)
		for(ROIObserverImpl observer : observerArray){
			for(PictureRegion picReg : observer.getNotifications()){
				assertTrue(equalRegions(Region(9,9,9,9), picReg.getRegion()));
			}
		}
	}

	@Test
	public void unregisterObserverByObserverTest(){
		p.setPixel(2, 2, new GrayPixel(0.2));
		
		p.unregisterROIObserver(a);
		p.unregisterROIObserver(c);
		q.unregisterROIObserver(a);
		q.unregisterROIObserver(d);
		q.unregisterROIObserver(e);
		r.unregisterROIObserver(b);
		r.unregisterROIObserver(c);
		r.unregisterROIObserver(d);
		
		p.suspendObservable();

		p.setPixel(1, 3, new GrayPixel(0.2));
		p.setPixel(3, 7, new GrayPixel(0.2));
		p.setPixel(9, 4, new GrayPixel(0.2));
		p.setPixel(8, 5, new GrayPixel(0.2));
		q.setPixel(5, 2, new GrayPixel(0.2));
		q.setPixel(3, 7, new GrayPixel(0.2));
		r.setPixel(4, 2, new GrayPixel(0.2));
		r.setPixel(10, 4, new GrayPixel(0.2));
		
		p.resumeObservable();
		
		assertEquals(2, a.getNotifications().size());
		assertEquals(2, b.getNotifications().size());
		assertEquals(1, c.getNotifications().size());
		assertEquals(1, d.getNotifications().size());
		assertEquals(4, e.getNotifications().size());
		
		assertTrue(equalRegions(Region(2,2,2,2), a.get(0)));
		assertTrue(equalRegions(Region(4,2,4,2), a.get(1)));
		assertTrue(equalRegions(Region(3,7,3,7), b.get(0)));
		assertTrue(equalRegions(Region(1,5,2,7), b.get(1)));
		assertTrue(equalRegions(Region(2,2,2,2), c.get(0)));
		assertTrue(equalRegions(Region(5,5,9,7), d.get(0)));
		assertTrue(equalRegions(Region(2,2,2,2), e.get(0)));
		assertTrue(equalRegions(Region(4,2,4,2), e.get(1)));
		assertTrue(equalRegions(Region(10,4,10,4), e.get(2)));
		assertTrue(equalRegions(Region(1,3,9,7), e.get(3)));

		assertEquals(p,a.getNotifications().get(0).getObservablePicture());
		assertEquals(r,a.getNotifications().get(1).getObservablePicture());
		assertEquals(q,b.getNotifications().get(0).getObservablePicture());
		assertEquals(p,b.getNotifications().get(1).getObservablePicture());
		assertEquals(p,c.getNotifications().get(0).getObservablePicture());
		assertEquals(p,d.getNotifications().get(0).getObservablePicture());
		assertEquals(p,e.getNotifications().get(0).getObservablePicture());
		assertEquals(r,e.getNotifications().get(1).getObservablePicture());
		assertEquals(r,e.getNotifications().get(2).getObservablePicture());
		assertEquals(p,e.getNotifications().get(3).getObservablePicture());
		
	}
	
	@Test
	public void unregisterObserverByRegionTest(){
		
		p.unregisterROIObservers(Region(1,2,6,6));
		q.unregisterROIObservers(Region(1,2,5,5));
		r.unregisterROIObservers(Region(1,2,2,4));
		
		p.suspendObservable();

		p.setPixel(1, 3, new GrayPixel(0.2));
		p.setPixel(3, 7, new GrayPixel(0.2));
		p.setPixel(9, 4, new GrayPixel(0.2));
		p.setPixel(8, 5, new GrayPixel(0.2));
		q.setPixel(5, 2, new GrayPixel(0.2));
		q.setPixel(3, 7, new GrayPixel(0.2));
		r.setPixel(4, 2, new GrayPixel(0.2));
		r.setPixel(10, 4, new GrayPixel(0.2));
		
		p.resumeObservable();
		
		assertEquals(0, a.getNotifications().size());
		assertEquals(0, b.getNotifications().size());
		assertEquals(3, c.getNotifications().size());
		assertEquals(0, d.getNotifications().size());
		assertEquals(0, e.getNotifications().size());
		
		assertTrue(equalRegions(Region(4,2,4,2), c.get(0)));
		assertTrue(equalRegions(Region(10,4,10,4), c.get(1)));
		assertTrue(equalRegions(Region(8,3,9,7), c.get(2)));

		assertEquals(r,c.getNotifications().get(0).getObservablePicture());
		assertEquals(r,c.getNotifications().get(1).getObservablePicture());
		assertEquals(p,c.getNotifications().get(2).getObservablePicture());
		
	}
	
	@Test
	public void registerMultiplePixelsTest(){
		p.setPixel(9, 4, new GrayPixel(0.2));
		p.setPixel(8, 3, new GrayPixel(0.2));
		q.setPixel(10, 2, new GrayPixel(0.2));
		q.setPixel(7, 14, new GrayPixel(0.2));
		r.setPixel(9, 15, new GrayPixel(0.2));
		r.setPixel(11, 13, new GrayPixel(0.2));
		
		assertEquals(2, a.getNotifications().size());
		assertEquals(0, b.getNotifications().size());
		assertEquals(4, c.getNotifications().size());
		assertEquals(1, d.getNotifications().size());
		assertEquals(6, e.getNotifications().size());
		
		assertTrue(equalRegions(Region(10,2,10,2), a.get(0)));
		assertTrue(equalRegions(Region(7,14,7,14), a.get(1)));
		assertTrue(equalRegions(Region(9,4,9,4), c.get(0)));
		assertTrue(equalRegions(Region(8,3,8,3), c.get(1)));
		assertTrue(equalRegions(Region(8,3,8,3), c.get(2)));
		assertTrue(equalRegions(Region(10,2,10,2), c.get(3)));
		assertTrue(equalRegions(Region(11,13,11,13), d.get(0)));
		assertTrue(equalRegions(Region(9,4,9,4), e.get(0)));
		assertTrue(equalRegions(Region(8,3,8,3), e.get(1)));
		assertTrue(equalRegions(Region(10,2,10,2), e.get(2)));
		assertTrue(equalRegions(Region(7,14,7,14), e.get(3)));
		assertTrue(equalRegions(Region(9,15,9,15), e.get(4)));
		assertTrue(equalRegions(Region(11,13,11,13), e.get(5)));
	}
	


}
