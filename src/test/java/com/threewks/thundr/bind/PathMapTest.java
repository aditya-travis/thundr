package com.threewks.thundr.bind;
import static com.atomicleopard.expressive.Expressive.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

import com.threewks.thundr.action.method.bind.http.HttpPostDataMap;

public class PathMapTest {
	@Test
	public void shouldNotSplitWhenSimple() {
		Map<String, String[]> map = map("key", new String[] { "value" });
		HttpPostDataMap pathMap = new HttpPostDataMap(map);
		assertThat(pathMap.get(list("key")), is(array("value")));
	}

	@Test
	public void shouldSplitForNestedPath() {
		Map<String, String[]> map = map("one.two.three", new String[] { "value" });
		HttpPostDataMap pathMap = new HttpPostDataMap(map);
		assertThat(pathMap.get(list("one", "two", "three")), is(array("value")));
	}

	@Test
	public void shouldSplitForNestedListPath() {
		Map<String, String[]> map = map("one[two].three", new String[] { "value" });
		HttpPostDataMap pathMap = new HttpPostDataMap(map);
		assertThat(pathMap.get(list("one", "[two]", "three")), is(array("value")));
	}

	@Test
	public void shouldCreateANewPathMapForNestedPath() {
		Map<String, String[]> map = map("one[two].three", new String[] { "value" });
		HttpPostDataMap pathMap = new HttpPostDataMap(map);
		HttpPostDataMap newPathMap = pathMap.pathMapFor("one");
		assertThat(newPathMap.get(list("[two]", "three")), is(array("value")));
	}

	@Test
	public void shouldCreateANewPathMapForNestedPathRemovingUnrelatedPaths() {
		Map<String, String[]> map = mapKeys("one[two].three", "one[one].two", "other.thing").to(new String[] { "value" }, new String[] { "value2" }, new String[] { "value3" });
		HttpPostDataMap pathMap = new HttpPostDataMap(map);
		HttpPostDataMap newPathMap = pathMap.pathMapFor("one");
		assertThat(newPathMap.size(), is(2));
		assertThat(newPathMap.get(list("[two]", "three")), is(array("value")));
		assertThat(newPathMap.get(list("[one]", "two")), is(array("value2")));
	}
}