package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import a6.*;

public class InstructionsTest extends AbstractTest{
	
	ObservablePicture p;
	ROIObserverImpl a, b;

	@Before
	public void setUp() throws Exception {
		p = new ObservablePictureImpl(new PictureImpl(10,10));
		a = new ROIObserverImpl("A");
		b = new ROIObserverImpl("B");
		
		observerArray = new ROIObserverImpl[]{a,b};
		
		p.registerROIObserver(a, Region(1,1,5,5));
		
		p.registerROIObserver(b, Region(0,0,3,3));
		p.registerROIObserver(b, Region(2,2,7,7));
	}
	
	
	@Test
	public void testPixel_1_1() {
		p.setPixel(1, 1, new GrayPixel(0.2));

		assertEquals(1, a.getNotifications().size());
		assertEquals(1, b.getNotifications().size());
		
		assertTrue(equalRegions(Region(1,1,1,1), a.get(0)));
		assertTrue(equalRegions(Region(1,1,1,1), b.get(0)));
	}
	
	@Test
	public void testPixel_2_2() {
		p.setPixel(2, 2, new GrayPixel(0.2));
		
		assertEquals(1, a.getNotifications().size());
		assertEquals(2, b.getNotifications().size());
		
		assertTrue(equalRegions(Region(2,2,2,2), a.get(0)));
		assertTrue(equalRegions(Region(2,2,2,2), b.get(0)));
		assertTrue(equalRegions(Region(2,2,2,2), b.get(1)));
	}
	
	@Test
	public void testPixel_8_8() {
		p.setPixel(8, 8, new GrayPixel(0.2));
		
		assertEquals(0, a.getNotifications().size());
		assertEquals(0, b.getNotifications().size());
	}
	
	@Test
	public void testPixel_suspendAndResume() {
		p.suspendObservable();
		
		p.setPixel(2, 1, new GrayPixel(0.2));
		p.setPixel(3, 2, new GrayPixel(0.2));
		p.setPixel(4, 4, new GrayPixel(0.2));
		
		p.resumeObservable();
		
		assertEquals(1, a.getNotifications().size());
		assertEquals(2, b.getNotifications().size());
		
		assertTrue(equalRegions(Region(2, 1, 4, 4), a.get(0)));
		assertTrue(equalRegions(Region(2, 1, 3, 3), b.get(0)));
		assertTrue(equalRegions(Region(2, 2, 4, 4), b.get(1)));
	}

}
