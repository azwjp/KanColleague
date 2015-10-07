package jp.azw.kancolleague.data;


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import jp.azw.kancolleague.data.ConstrDockValue.State;

public class ConstrDockValueTest {

	@Test
	public void testState() {
		assertThat(State.getState(-1), is(State.UNRELEASED));
		assertThat(State.getState(0), is(State.EMPTY));
		assertThat(State.getState(2), is(State.CONSTRUTING));
		assertThat(State.getState(3), is(State.FINISHED));
		assertThat(State.getState(114514), is(State.UNKNOWN));
		assertThat(State.getState(-100000), is(State.UNKNOWN));
	}

}
