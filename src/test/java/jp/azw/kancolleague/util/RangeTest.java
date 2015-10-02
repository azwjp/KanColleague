package jp.azw.kancolleague.util;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

public class RangeTest {
	Integer max = 15;
	Integer min = 10;
	
	@Before
	public void setUp() {
		
	}

	@Test
	public void testGetMax() {
		Range<Integer> r1 = Range.of(max, min);
		Range<Integer> r2 = Range.of(min, max);
		assertThat(15, is(allOf(equalTo(r1.getMax()), equalTo(r2.getMax()))));
		max = -100;
		assertThat(15, is(allOf(equalTo(r1.getMax()), equalTo(r2.getMax()))));
	}
	
	@Test
	public void testGetMin() {
		assertThat(10, is(allOf(equalTo(Range.of(10, 15).getMin()), equalTo(Range.of(15, 10).getMin()))));
	}

}
