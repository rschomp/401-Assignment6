package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import a6.*;

public class RegionImplTest extends AbstractTest{
	Region region1, region2, region3, region4;
	@Before
	public void setUp() throws Exception {
		region1 = Region(10,11,2,3);
		region2 = Region(7,8,12,14);
		region3 = Region(12,14,10,11);
		region4 = null;
	}
	
	@Test
	public void testCorners(){
		assertTrue(equalCoordinates(new Coordinate(2,3), region1.getUpperLeft()));
		assertTrue(equalCoordinates(new Coordinate(10,11), region1.getLowerRight()));
		assertTrue(equalCoordinates(new Coordinate(7,8), region2.getUpperLeft()));
		assertTrue(equalCoordinates(new Coordinate(12,14), region2.getLowerRight()));
		assertTrue(equalCoordinates(new Coordinate(10,11), region3.getUpperLeft()));
		assertTrue(equalCoordinates(new Coordinate(12,14), region3.getLowerRight()));
	}

	@Test
	public void testSides() {
		assertEquals(2, region1.getLeft());
		assertEquals(10, region1.getRight());
		assertEquals(3, region1.getTop());
		assertEquals(11, region1.getBottom());
	}
	
	@Test
	public void intersection1Test() {
		
		Region intersection;
		
		try {
			intersection = region1.intersect(region2);
		} catch (NoIntersectionException e) {
			intersection = null;
			e.printStackTrace();
		}
		
		assertEquals(7, intersection.getUpperLeft().getX());
		assertEquals(8, intersection.getUpperLeft().getY());
		assertEquals(10, intersection.getLowerRight().getX());
		assertEquals(11, intersection.getLowerRight().getY());
	}
	
	@Test
	public void intersection2Test() {
		
		Region intersection;
		
		try {
			intersection = region1.intersect(region3);
		} catch (NoIntersectionException e) {
			intersection = null;
		}

		assertEquals(10, intersection.getUpperLeft().getX());
		assertEquals(11, intersection.getUpperLeft().getY());
		assertEquals(10, intersection.getLowerRight().getX());
		assertEquals(11, intersection.getLowerRight().getY());
	}
	
	@Test
	public void intersection3Test() {
		
		Region intersection;
		
		try {
			intersection = region1.intersect(region4);
		} catch (NoIntersectionException e) {
			intersection = null;
		}

		assertNull(intersection);
	}
	
	@Test
	public void union1Test() {
		Region union = region1.union(region2);
	
		assertEquals(2, union.getUpperLeft().getX());
		assertEquals(3, union.getUpperLeft().getY());
		assertEquals(12, union.getLowerRight().getX());
		assertEquals(14, union.getLowerRight().getY());
	}
	
	@Test
	public void union2Test() {
		Region union = region1.union(region3);
	
		assertEquals(2, union.getUpperLeft().getX());
		assertEquals(3, union.getUpperLeft().getY());
		assertEquals(12, union.getLowerRight().getX());
		assertEquals(14, union.getLowerRight().getY());
	}
	
	@Test
	public void union3Test() {
		Region union = region1.union(region4);
	
		assertEquals(2, union.getUpperLeft().getX());
		assertEquals(3, union.getUpperLeft().getY());
		assertEquals(10, union.getLowerRight().getX());
		assertEquals(11, union.getLowerRight().getY());
	}
}
