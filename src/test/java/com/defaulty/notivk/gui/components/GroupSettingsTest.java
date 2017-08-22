package com.defaulty.notivk.gui.components;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GroupSettingsTest {
    @Test
    public void parseToList() throws Exception {
        GroupSettings groupSettings = new GroupSettings("1", "Real Name");
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        assertEquals(groupSettings.parseToList("one,two,three", ","), list);
        assertEquals(groupSettings.parseToList("one ,two ,three ", ","), list);
        assertEquals(groupSettings.parseToList("one |two |three |", "|"), list);
    }

    @Test
    public void parseToString() throws Exception {
        GroupSettings groupSettings = new GroupSettings("1", "Real Name");
        String s = "one,two,three,";
        String s2 = "one|two|three|";
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        assertEquals(groupSettings.parseToString(list, ","), s);
        assertEquals(groupSettings.parseToString(list, "|"), s2);
    }

}