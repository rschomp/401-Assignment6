package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import a6.*;

public class SingleObservablePictureTest extends AbstractTest{
	
	private ObservablePicture p;
	private ROIObserverImpl a, b, c, d, e;

	@Before
	public void setUp() throws Exception {
		p = new ObservablePictureImpl(new PictureImpl(10,10));
		a = new ROIObserverImpl("A");
		b = new ROIObserverImpl("B");
		c = new ROIObserverImpl("C");
		d = new ROIObserverImpl("D");
		e = new ROIObserverImpl("E");
		
		observerArray = new ROIObserverImpl[]{a, b, c, d, e};
		
		p.registerROIObserver(a, Region(1,1,5,5));
		
		p.registerROIObserver(b, Region(0,0,3,3));
		p.registerROIObserver(b, Region(2,2,7,7));
		
		p.registerROIObserver(c, Region(8,8,9,9));
		p.registerROIObserver(c, Region(8,8,9,9));
		
		p.registerROIObserver(d, Region(0,6,4,9));
		
		p.registerROIObserver(e, Region(0,0,10,10));
	}
	
	
	@Test
	public void testPixel_8_8() {
		p.setPixel(8,8, new GrayPixel(0.2));
		
		assertEquals(0, a.getNotifications().size());
		assertEquals(0, b.getNotifications().size());
		assertEquals(2, c.getNotifications().size());
		assertEquals(0, d.getNotifications().size());
		assertEquals(1, e.getNotifications().size());
		
		assertTrue(equalRegions(Region(8,8,8,8), c.get(0)));
		assertTrue(equalRegions(Region(8,8,8,8), c.get(1)));
		assertTrue(equalRegions(Region(8,8,8,8), e.get(0)));
	}

	@Test
	public void unregisterObserverByObserverTest(){
		p.setPixel(2, 2, new GrayPixel(0.2));
		
		assertEquals(1, a.getNotifications().size());
		assertEquals(2, b.getNotifications().size());
		assertEquals(0, c.getNotifications().size());
		assertEquals(0, d.getNotifications().size());
		assertEquals(1, e.getNotifications().size());
		
		assertTrue(equalRegions(Region(2,2,2,2), a.get(0)));
		assertTrue(equalRegions(Region(2,2,2,2), b.get(0)));
		assertTrue(equalRegions(Region(2,2,2,2), b.get(1)));
		assertTrue(equalRegions(Region(2,2,2,2), e.get(0)));
		
		p.unregisterROIObserver(a);
		p.unregisterROIObserver(e);
		
		p.setPixel(3, 3, new GrayPixel(0.2));
		
		assertEquals(1, a.getNotifications().size());
		assertEquals(4, b.getNotifications().size());
		assertEquals(0, c.getNotifications().size());
		assertEquals(0, d.getNotifications().size());
		assertEquals(1, e.getNotifications().size());
		
		
		assertTrue(equalRegions(Region(3,3,3,3), b.get(2)));
		assertTrue(equalRegions(Region(3,3,3,3), b.get(3)));
	}
	
	@Test
	public void unregisterObserverByRegionTest(){
		p.setPixel(3, 4, new GrayPixel(0.2));
		
		assertEquals(1, a.getNotifications().size());
		assertEquals(1, b.getNotifications().size());
		assertEquals(0, c.getNotifications().size());
		assertEquals(0, d.getNotifications().size());
		assertEquals(1, e.getNotifications().size());
		
		assertTrue(equalRegions(Region(3,4,3,4), a.get(0)));
		assertTrue(equalRegions(Region(3,4,3,4), b.get(0)));
		assertTrue(equalRegions(Region(3,4,3,4), e.get(0)));
		
		p.unregisterROIObservers(Region(3,3,3,3));
		
		p.suspendObservable();
		
		p.setPixel(0, 3, new GrayPixel(0.2));
		p.setPixel(1, 7, new GrayPixel(0.2));
		p.setPixel(7, 4, new GrayPixel(0.2));
		p.setPixel(8, 8, new GrayPixel(0.2));
		
		p.resumeObservable();

		assertEquals(1, a.getNotifications().size());
		assertEquals(1, b.getNotifications().size());
		assertEquals(2, c.getNotifications().size());
		assertEquals(1, d.getNotifications().size());
		assertEquals(1, e.getNotifications().size());
		
		assertTrue(equalRegions(Region(8,8,8,8), c.get(0)));
		assertTrue(equalRegions(Region(8,8,8,8), c.get(1)));
		assertTrue(equalRegions(Region(0,6,4,8), d.get(0)));
		
	}
	
	@Test
	public void registerMultiplePixelsTest(){
		p.resumeObservable();
		p.setPixel(4, 4, new GrayPixel(0.2));
		p.setPixel(5, 3, new GrayPixel(0.2));
		p.setPixel(1, 2, new GrayPixel(0.2));
		p.setPixel(7, 4, new GrayPixel(0.2));
		p.setPixel(9, 8, new GrayPixel(0.2));

		assertEquals(3, a.getNotifications().size());
		assertEquals(4, b.getNotifications().size());
		assertEquals(2, c.getNotifications().size());
		assertEquals(0, d.getNotifications().size());
		assertEquals(5, e.getNotifications().size());
		
		assertTrue(equalRegions(Region(4,4,4,4), a.get(0)));
		assertTrue(equalRegions(Region(5,3,5,3), a.get(1)));
		assertTrue(equalRegions(Region(1,2,1,2), a.get(2)));
		assertTrue(equalRegions(Region(4,4,4,4), b.get(0)));
		assertTrue(equalRegions(Region(5,3,5,3), b.get(1)));
		assertTrue(equalRegions(Region(1,2,1,2), b.get(2)));
		assertTrue(equalRegions(Region(7,4,7,4), b.get(3)));
		assertTrue(equalRegions(Region(9,8,9,8), c.get(0)));
		assertTrue(equalRegions(Region(9,8,9,8), c.get(1)));
		assertTrue(equalRegions(Region(4,4,4,4), e.get(0)));
		assertTrue(equalRegions(Region(5,3,5,3), e.get(1)));
		assertTrue(equalRegions(Region(1,2,1,2), e.get(2)));
		assertTrue(equalRegions(Region(7,4,7,4), e.get(3)));
		assertTrue(equalRegions(Region(9,8,9,8), e.get(4)));
	}
	
}
